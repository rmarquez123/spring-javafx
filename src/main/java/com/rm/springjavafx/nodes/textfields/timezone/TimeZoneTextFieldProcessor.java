package com.rm.springjavafx.nodes.textfields.timezone;

import com.rm.springjavafx.nodes.NodeProcessor;
import com.rm.springjavafx.nodes.NodeProcessorFactory;
import com.rm.springjavafx.nodes.TextFormatterPropertyBinder;
import java.lang.annotation.Annotation;
import java.time.ZoneId;
import javafx.scene.control.TextField;
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
public class TimeZoneTextFieldProcessor implements NodeProcessor, InitializingBean {

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
    this.factory.addProcessor(TimeZoneTextField.class, this);
  }

  /**
   *
   * @param node
   * @param annotation
   */
  @Override
  public void process(Object parentBean, Object node, Annotation annotation) {
    if (!(node instanceof TextField)) {
      throw new IllegalArgumentException("Node is not an instance of " + TextField.class);
    }
    if (!(annotation instanceof TimeZoneTextField)) {
      throw new IllegalArgumentException("Annotation is not an instance of " + TimeZoneTextField.class);
    }
    TextField textfield = (TextField) node;
    TimeZoneTextField conf = (TimeZoneTextField) annotation;
    StringConverter<ZoneId> converter = new StringConverter<java.time.ZoneId>() {
      @Override
      public String toString(ZoneId object) {
        return object == null ? "" : object.getId();
      }

      @Override
      public ZoneId fromString(String string) {
        return string.isEmpty() ? null : ZoneId.of(string);
      }
    };
    TextFormatter<ZoneId> formatter = new TextFormatter<>(converter);
    textfield.setTextFormatter(formatter);
    textfield.setAlignment(conf.alignment());
    if (conf.beanId().length != 0) {
      Object bean = this.appcontext.getBean(conf.beanId()[0]);
      TextFormatterPropertyBinder<ZoneId> propertyBinder //
        = new TextFormatterPropertyBinder<>(formatter, conf.beanId(), bean);
      propertyBinder.bind();
    }
  }

}
