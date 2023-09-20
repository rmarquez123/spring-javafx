package com.rm.springjavafx.nodes.textarea;

import com.rm.springjavafx.nodes.NodeProcessor;
import com.rm.springjavafx.nodes.NodeProcessorFactory;
import com.rm.springjavafx.nodes.TextFormatterPropertyBinder;
import java.lang.annotation.Annotation;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
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
public class TextAreaProcessor implements NodeProcessor, InitializingBean {

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
    this.factory.addProcessor(FxTextArea.class, this);
  }

  /**
   *
   * @param node
   * @param annotation
   */
  @Override
  public void process(Object parentBean, Object node, Annotation annotation) {
    if (!(node instanceof TextArea)) {
      throw new IllegalArgumentException("Node is not an instance of " + TextArea.class);
    }
    if (!(annotation instanceof FxTextArea)) {
      throw new IllegalArgumentException("Annotation is not an instance of " + FxTextArea.class);
    }
    TextArea textarea = (TextArea) node;
    FxTextArea conf = (FxTextArea) annotation;
    TextFormatter<String> formatter = new TextFormatter<>(new DefaultStringConverter());
    textarea.setTextFormatter(formatter);
    textarea.setWrapText(true);
    
    if (conf.beanId().length != 0) {
      Object bean = this.appcontext.getBean(conf.beanId()[0]);
      TextFormatterPropertyBinder<String> propertyBinder //
        = new TextFormatterPropertyBinder<>(formatter, conf.beanId(), bean);
      propertyBinder.bind();
    }
    
    
  }
}
