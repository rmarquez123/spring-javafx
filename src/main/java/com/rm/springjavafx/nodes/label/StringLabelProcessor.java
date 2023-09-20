package com.rm.springjavafx.nodes.label;

import com.rm.springjavafx.nodes.NodeProcessor;
import com.rm.springjavafx.nodes.NodeProcessorFactory;
import com.rm.springjavafx.nodes.TextFormatterPropertyBinder;
import java.lang.annotation.Annotation;
import java.util.Objects;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
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
    TextFormatter<Object> formatter = (TextFormatter<Object>) getFormatter(conf);
    formatter.valueProperty().addListener((a, b, c) -> {
      this.setText(label, formatter);
    });
    this.setText(label, formatter);
    label.setAlignment(conf.alignment());
    if (conf.beanId().length != 0) {
      Object bean = this.appcontext.getBean(conf.beanId()[0]);
      TextFormatterPropertyBinder<? extends Object> propertyBinder //
        = new TextFormatterPropertyBinder<>(formatter, conf.beanId(), bean);
      propertyBinder.bind();
    }
  }

  private void setText(Label label, TextFormatter<? super Object> formatter) {
    label.setText(formatter.getValueConverter().toString((Object) formatter.getValue()));
  }

  /**
   *
   * @param conf
   * @return
   */
  private TextFormatter<? extends Object> getFormatter(StringLabel conf) {
    String converter = conf.converterBean();
    Class<? extends StringConverter> converterClass = conf.converterClass();
    TextFormatter<? extends Object> formatter;
    if (!converter.isEmpty()) {
      formatter = new TextFormatter<>(this.appcontext.getBean(converter, StringConverter.class));
    } else if (!Objects.equals(converterClass, StringConverter.class)) {
      try {
        formatter = new TextFormatter<>(converterClass.newInstance());
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    } else {
      formatter = new TextFormatter<>(new DefaultStringConverter());
    }
    return formatter;
  }
}
