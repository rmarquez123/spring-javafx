package com.rm.springjavafx.nodes.spinner;

import com.rm.springjavafx.nodes.NodeProcessor;
import com.rm.springjavafx.nodes.NodeProcessorFactory;
import java.lang.annotation.Annotation;
import javafx.beans.property.Property;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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
public class SpinnerProcessor implements InitializingBean, NodeProcessor {

  @Autowired
  private NodeProcessorFactory factory;

  @Autowired
  private ApplicationContext appcontext;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.factory.addProcessor(SpinnerConf.class, this);
  }

  @Override
  public void process(Object parentBean, Object node, Annotation annotation) {

    if (!(node instanceof Spinner)) {
      throw new IllegalArgumentException("Node is not an instance of " + Spinner.class);
    }
    if (!(annotation instanceof SpinnerConf)) {
      throw new IllegalArgumentException("Annotation is not an instance of " + SpinnerConf.class);
    }
    Spinner combobox = (Spinner) node;
    SpinnerConf conf = (SpinnerConf) annotation;

    Property bean = (Property) this.appcontext.getBean(conf.beanId()[0]);
    combobox.setEditable(true);
    combobox.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(conf.min(), conf.max()));

    combobox.valueProperty().addListener((obs, old, change) -> {
      Object safeValue = change == null ? Integer.MIN_VALUE : change;
      bean.setValue(safeValue);
    });
    bean.addListener((obs, old, change) -> {
      Object safeValue = change == null ? Integer.MIN_VALUE : change;
      combobox.getValueFactory().setValue(safeValue);
    });
    Object safeValue = bean.getValue() == null ? Integer.MIN_VALUE : bean.getValue();
    combobox.getValueFactory().setValue(safeValue);

  }

}
