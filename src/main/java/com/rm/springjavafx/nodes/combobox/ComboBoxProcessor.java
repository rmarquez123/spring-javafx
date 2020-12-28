package com.rm.springjavafx.nodes.combobox;

import com.rm.springjavafx.nodes.NodeProcessor;
import com.rm.springjavafx.nodes.NodeProcessorFactory;
import common.bindings.RmBindings;
import java.lang.annotation.Annotation;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.scene.control.ComboBox;
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
public class ComboBoxProcessor implements InitializingBean, NodeProcessor {

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
    this.factory.addProcessor(ComboBoxConf.class, this);
  }

  /**
   *
   * @param parentBean
   * @param node
   * @param annotation
   */
  @Override
  public void process(Object parentBean, Object node, Annotation annotation) {
    if (!(node instanceof ComboBox)) {
      throw new IllegalArgumentException("Node is not an instance of " + ComboBox.class);
    }
    if (!(annotation instanceof ComboBoxConf)) {
      throw new IllegalArgumentException("Annotation is not an instance of " + ComboBoxConf.class);
    }
    ComboBox combobox = (ComboBox) node;
    ComboBoxConf conf = (ComboBoxConf) annotation;

    Object bean = this.appcontext.getBean(conf.beanId()[0]);
    String[] beanId = conf.beanId();
    ComboBoxPropertyBinder binder = new ComboBoxPropertyBinder(combobox, beanId, bean);
    binder.bind();
    String[] listref = conf.listRef();
    Object collection = this.appcontext.getBean(listref[0]);
    if (collection instanceof ObservableList) {
      ObservableList list = (ObservableList) collection;
      RmBindings.bindCollections(combobox.getItems(), list); 
    } else if (collection instanceof ObservableSet) {
      RmBindings.bindCollections(combobox.getItems(), (ObservableSet) collection);
    } else {
      throw new RuntimeException("collection is not an observable list or observable set.");
    }
  }
}
