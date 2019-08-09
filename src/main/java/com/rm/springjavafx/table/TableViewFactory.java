package com.rm.springjavafx.table;

import com.rm.datasources.DataSource;
import com.rm.datasources.RecordValue;
import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.converters.Converter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author rmarquez
 */
public class TableViewFactory implements FactoryBean<TableView>, ApplicationContextAware, InitializingBean {

  @Autowired
  FxmlInitializer fxmlInitializer;
  private ApplicationContext appContext;
  private String id;
  private String fxml;
  private String fxmlId;
  private String datasourceRef;
  private String selectedRef;
  private List<TableViewColumn> columns;
  private Converter converter = Converter.NONE;

  public void setId(String id) {
    this.id = id;
  }

  public void setFxml(String fxml) {
    this.fxml = fxml;
  }

  public void setFxmlId(String fxmlId) {
    this.fxmlId = fxmlId;
  }

  public void setDatasourceRef(String datasourceRef) {
    this.datasourceRef = datasourceRef;
  }

  public void setColumns(List<TableViewColumn> columns) {
    this.columns = columns;
  }

  public void setConverter(Converter converter) {
    this.converter = converter;
  }

  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    if (this.fxml != null && this.fxmlId == null) {
      throw new IllegalStateException("fxmlId cannot be null if fxml is not null");
    }
    if (this.fxml != null && this.fxmlInitializer == null) {
      throw new IllegalStateException("fxml initializer cannot be null if fxml is not null");
    }
    if (this.datasourceRef == null) {
      throw new NullPointerException("Data source cannot be null");
    }
    this.fxmlInitializer.addListener((i)->{
      this.appContext.getBean(this.id); 
    });
  }

  /**
   *
   * @return @throws Exception
   */
  @Override
  public TableView getObject() throws Exception {
    
    TableView<?> result;
    if (this.fxml != null) {
      if (!this.fxmlInitializer.isInitialized()) {
        this.fxmlInitializer.initializeRoots(this.appContext);
      }
      result = (TableView<?>) this.fxmlInitializer.getNode(this.fxml, this.fxmlId);
      if (result == null) {
        throw new IllegalStateException("table view does not exists.  Check args: {"
          + "fxml = " + fxml
          + ", fxmlId = " + this.fxmlId
          + "}");
      }
    } else {
      result = new TableView<>();
    }
    this.initDataSourceBinding(result);
    this.initSelectionBinding(result);

    List<TableColumn<? extends Object, ?>> tvColumns = new ArrayList<>();
    for (TableViewColumn columnDef : columns) {
      TableColumn<Object, ?> column = new TableColumn<>(columnDef.getLabel());

      PropertyValueFactory propValFactory = new PropertyValueFactory(columnDef.getPropertyName()) {
        private final Property resultProperty = new SimpleObjectProperty();

        @Override
        public ObservableValue<?> call(TableColumn.CellDataFeatures param) {
          
          Object entity = param.getValue();
          if (entity instanceof RecordValue) {
            Object value = ((RecordValue) entity).get(columnDef.getPropertyName());
            if (value instanceof ObservableValue) {
              return (ObservableValue<Object>) value;
            } else {
              this.resultProperty.setValue(value);
            }
          } else {
            throw new UnsupportedOperationException("not supported");
          }
          return this.resultProperty;
        }
      };
      column.setCellValueFactory(propValFactory);
      
      if (columnDef.getWidth() != null) {
        column.setPrefWidth(columnDef.getWidth());
        column.setMaxWidth(columnDef.getWidth());
        column.setMinWidth(columnDef.getWidth());
      }
      RenderType renderer = columnDef.getRenderType();
      if (renderer != null) {
        column.setUserData(columnDef);
        renderer.createCellFactory(result, column);
      }
      tvColumns.add(column);
    }
    result.getColumns().clear();
    result.getColumns().addAll(tvColumns.toArray(new TableColumn[]{}));
    result.setEditable(true);
    return result;
  }

  /**
   *
   * @param result
   * @throws BeansException
   */
  private void initSelectionBinding(TableView result) throws BeansException {

    if (this.selectedRef != null) {
      Property selected = (Property<?>) this.appContext.getBean(this.selectedRef);
      if (selected == null) {
        throw new NullPointerException("bean does not exists : '" + this.selectedRef + "'");
      }
      result.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change c) -> {
        if (c.wasAdded()) {
          List addedList = c.getAddedSubList();
          addedList.stream().forEach((object) -> {
            selected.setValue(object);
          });
        } else if (c.wasRemoved()) {
          selected.setValue(null);
        }
      });
      selected.addListener((obs, old, change) -> {
        if (!Objects.equals(result.getSelectionModel().getSelectedItem(), change)) {
          result.getSelectionModel().select(change);
        }
      });
    }

  }

  /**
   *
   * @param tableView
   * @throws BeansException
   */
  private void initDataSourceBinding(TableView tableView) throws BeansException {
    if (tableView == null) {
      throw new NullPointerException("table view cannot be null.");
    }
    DataSource dataSource = (DataSource) this.appContext.getBean(this.datasourceRef);
    if (dataSource == null) {
      throw new IllegalStateException("Bean does not exists for bean name: '" + this.datasourceRef + "'");
    }
    dataSource.bind(tableView.getItems(), this.converter);
    dataSource.getSingleSelectionProperty().addListener((obs, old, change) -> {
      try {
        if (!tableView.getSelectionModel().getSelectedItems().contains(change)) {
          tableView.getSelectionModel().select(change);
        }
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    });
    tableView.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change c) -> {
      while (c.next()) {
        if (c.wasAdded()) {
          try {
            for (Object object : c.getAddedSubList()) {
              dataSource.getSingleSelectionProperty().setValue(object);
            }
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        } else if (c.wasRemoved()) {
          for (Object object : c.getRemoved()) {
            if (Objects.equals(dataSource.getSingleSelectionProperty().getValue(), object)) {
              dataSource.getSingleSelectionProperty().setValue(null);
            }
          }
        }
      }
    });
  }

  /**
   *
   * @return
   */
  @Override
  public Class<?> getObjectType() {
    return TableView.class;
  }

  /**
   *
   * @param ac
   * @throws BeansException
   */
  @Override
  public void setApplicationContext(ApplicationContext ac) throws BeansException {
    this.appContext = ac;
  }
}
