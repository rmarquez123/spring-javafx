package com.rm.springjavafx.table;

import com.rm.springjavafx.datasources.Converter;
import com.rm.springjavafx.datasources.DataSource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javafx.beans.property.Property;
import javafx.beans.property.SetProperty;
import javafx.collections.ListChangeListener;
import javafx.scene.control.TableView;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author rmarquez
 */
public class TableViewFactory implements FactoryBean<TableView>, ApplicationContextAware, InitializingBean {

  private ApplicationContext appContext;
  private String datasourceRef;
  private String selectedRef;
  private String text;
  private List<TableViewRenderer> colRenderers;
  private Converter converter = Converter.NONE;

  public void setDatasourceRef(String datasourceRef) {
    this.datasourceRef = datasourceRef;
  }

  public void setColRenderers(List<TableViewRenderer> colRenderers) {
    this.colRenderers = colRenderers;
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
    if (this.datasourceRef == null) {
      throw new NullPointerException("Data source cannot be null"); 
    }
  }
  

  /**
   *
   * @return @throws Exception
   */
  @Override
  public TableView getObject() throws Exception {
    TableView result = new TableView();
    this.initDataSourceBinding(result);
    this.initSelectionBinding(result);
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

  private void setItems(SetProperty<?> dataSource, TableView result) {
    List<?> asList = dataSource.getValue().stream().collect(Collectors.toList());
    result.getItems().addAll(asList);
  }

  @Override
  public Class<?> getObjectType() {
    return TableView.class;
  }

  @Override
  public void setApplicationContext(ApplicationContext ac) throws BeansException {
    this.appContext = ac;
  }
}
