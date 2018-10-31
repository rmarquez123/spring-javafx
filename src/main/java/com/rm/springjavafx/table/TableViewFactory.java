package com.rm.springjavafx.table;

import com.rm.springjavafx.converters.Converter;
import com.rm.datasources.DataSource;
import com.rm.datasources.RecordValue;
import com.rm.springjavafx.FxmlInitializer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
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
    this.appContext.getBean(this.id);
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
          Object value = param.getValue();
          if (value instanceof RecordValue) {
            this.resultProperty.setValue(((RecordValue) value).get(columnDef.getPropertyName()));
          } else {
            throw new UnsupportedOperationException("not supported");
          }
          return this.resultProperty;
        }
      };
      column.setCellValueFactory(propValFactory);
      tvColumns.add(column);
    }
    result.getColumns().clear();
    result.getColumns().addAll(tvColumns.toArray(new TableColumn[]{}));
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
      if (selected != null) {
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
   * @param result
   * @throws BeansException
   */
  private void initDataSourceBinding(TableView result) throws BeansException {
    DataSource dataSource = (DataSource) this.appContext.getBean(this.datasourceRef);
    dataSource.bind(result.getItems(), this.converter);
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
