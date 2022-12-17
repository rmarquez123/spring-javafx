package com.rm.springjavafx.nodes.label.object;

import com.rm.springjavafx.nodes.NodeProcessor;
import com.rm.springjavafx.nodes.NodeProcessorFactory;
import com.rm.springjavafx.nodes.TextFormatterPropertyBinder;
import common.bindings.ObjectConverter;
import common.bindings.RmBindings;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
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
public class ObjectLabelProcessor implements NodeProcessor, InitializingBean {

  
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
    this.factory.addProcessor(ObjectLabel.class, this);
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
    if (!(annotation instanceof ObjectLabel)) {
      throw new IllegalArgumentException("Annotation is not an instance of " + ObjectLabel.class);
    }
    Label label = (Label) node;
    ObjectLabel conf = (ObjectLabel) annotation;
    StringConverter<Object> converter = this.getConverter(conf, parentBean);
    TextFormatter<Object> formatter = new TextFormatter<>(converter);
    RmBindings.bind1To2(label.textProperty(), formatter.valueProperty(), new ObjectConverter<String, Object>() {
      @Override
      public Object toObject(String reference) {
        return formatter.getValueConverter().fromString(reference);
      }

      @Override
      public String fromObject(Object reference) {
        return formatter.getValueConverter().toString(reference);
      }
    });
    label.setAlignment(conf.alignment());
    if (conf.beanId().length != 0) {
      Object bean = this.appcontext.getBean(conf.beanId()[0]);
      TextFormatterPropertyBinder<?> propertyBinder //
        = new TextFormatterPropertyBinder<>(formatter, conf.beanId(), bean);
      propertyBinder.bind();
    }
  }
  
  /**
   * 
   * @param conf
   * @param parentBean
   * @return 
   */
  private StringConverter<Object> getConverter(ObjectLabel conf, Object parentBean) { 
    StringConverter<Object> converter;
    try {
      String converterField = conf.converterField();
      Field field = parentBean.getClass().getDeclaredField(converterField);
      field.setAccessible(true);
      converter = (StringConverter<Object>) field.get(parentBean);
    } catch(Exception ex) {
      throw new RuntimeException(ex); 
    }
    return converter;
  }
}
