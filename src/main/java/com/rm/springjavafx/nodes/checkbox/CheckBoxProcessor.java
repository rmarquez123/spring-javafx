package com.rm.springjavafx.nodes.checkbox;

import com.rm.springjavafx.nodes.NodeProcessor;
import com.rm.springjavafx.nodes.NodeProcessorFactory;
import java.lang.annotation.Annotation;
import javafx.scene.control.CheckBox;
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
public class CheckBoxProcessor implements InitializingBean, NodeProcessor {

  @Autowired
  private NodeProcessorFactory factory;

  @Autowired
  private ApplicationContext appcontext;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.factory.addProcessor(CheckBoxConf.class, this);
  }

  @Override
  public void process(Object parentBean, Object node, Annotation annotation) {
    if (!(node instanceof CheckBox)) {
      throw new IllegalArgumentException("Node is not an instance of " + CheckBox.class);
    }
    if (!(annotation instanceof CheckBoxConf)) {
      throw new IllegalArgumentException("Annotation is not an instance of " + CheckBoxConf.class);
    }
    CheckBox checkbox = (CheckBox) node;
    CheckBoxConf conf = (CheckBoxConf) annotation;
    
    Object bean = this.appcontext.getBean(conf.beanId()[0]);
    String[] beanId = conf.beanId();
    CheckBoxPropertyBinder binder = new CheckBoxPropertyBinder(checkbox, beanId, bean);
    binder.bind();
  }

}
