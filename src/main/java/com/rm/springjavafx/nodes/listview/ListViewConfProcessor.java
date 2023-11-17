package com.rm.springjavafx.nodes.listview;

import com.rm.springjavafx.nodes.NodeProcessor;
import com.rm.springjavafx.nodes.NodeProcessorFactory;
import common.bindings.RmBindings;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Objects;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.scene.control.ListView;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
public class ListViewConfProcessor implements InitializingBean, NodeProcessor {

  @Autowired
  private NodeProcessorFactory factory;

  @Autowired
  private ApplicationContext appcontext;

  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    this.factory.addProcessor(ListViewConf.class, this);
  }

  /**
   *
   * @param node
   * @param annotation
   */
  @Override
  public void process(Object parentBean, Object node, Annotation annotation) {
    if (!(node instanceof ListView<?>)) {
      throw new IllegalArgumentException("Node is not an instance of " + ListView.class);
    }
    if (!(annotation instanceof ListViewConf)) {
      throw new IllegalArgumentException("Annotation is not an instance of " + ListViewConf.class);
    }
    ListView listview = (ListView<?>) node;
    ListViewConf conf = (ListViewConf) annotation;
    Object collections = this.appcontext.getBean(conf.listref()); 
    Objects.requireNonNull(collections, "");
    
    if (!(collections instanceof Collection)) {
      throw new IllegalArgumentException();
    }
    ObservableList items = listview.getItems();
    if (collections instanceof ObservableSet) {
      RmBindings.bindCollections(items, (ObservableSet) collections);
    }
    
    if (collections instanceof ObservableList) {
      RmBindings.bindCollections(items, (ObservableList) collections);
    }
    
    this.bindSelection(conf, listview);
  }
  
  /***
   * 
   * @param conf
   * @param listview
   * @throws BeansException 
   */
  private void bindSelection(ListViewConf conf, ListView listview) throws BeansException {
    if (!conf.selectedref().isEmpty()) {
      Property selectedProperty = this.appcontext.getBean(conf.selectedref(), Property.class);
      listview.getSelectionModel().selectedItemProperty().addListener((obs, old, change)->{
        if (listview.getItems().contains(change) ) {
          selectedProperty.setValue(change);
        }
      });
      
      selectedProperty.addListener((obs, old, change)->{
        listview.getSelectionModel().select(change);
      });
      listview.getSelectionModel().select(selectedProperty.getValue());
    }
  }
}
