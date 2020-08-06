package com.rm.springjavafx.nodes.label;

import com.rm.springjavafx.nodes.NodeProcessor;
import com.rm.springjavafx.nodes.NodeProcessorFactory;
import common.bindings.RmBindings;
import java.lang.annotation.Annotation;
import javafx.beans.property.Property;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
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
public class StringLabelProcessor implements NodeProcessor, InitializingBean {

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
    this.factory.addProcessor(StringLabel.class, this);
  }

  /**
   *
   * @param node
   * @param annotation
   */
  @Override
  public void process(Object parentBean, Object node, Annotation annotation) {
    if (!(node instanceof Label)) {
      throw new IllegalArgumentException("Node is not an instance of " + Label.class);
    }
    if (!(annotation instanceof StringLabel)) {
      throw new IllegalArgumentException("Annotation is not an instance of " + StringLabel.class);
    }
    Label label = (Label) node;
    StringLabel conf = (StringLabel) annotation;
    label.setAlignment(conf.alignment());
    if (conf.beanId().length != 0) {
      Object bean = this.appcontext.getBean(conf.beanId()[0]);
      if (bean instanceof Property) {
        RmBindings.bindToStringProperty(label.textProperty(), (Property<Object>) bean, new StringConverter<Object>() {
          @Override
          public String toString(Object object) {
            return String.valueOf(object);
          }

          @Override
          public Object fromString(String string) {
            return string;
          }
        });
      } else {
        throw new RuntimeException(String.format("Bean 'conf.beanId()[0]' is not a type of Property", bean));
      }
    }
    
    
  }
}
