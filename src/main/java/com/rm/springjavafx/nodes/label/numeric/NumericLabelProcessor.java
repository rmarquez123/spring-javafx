package com.rm.springjavafx.nodes.label.numeric;

import com.rm.springjavafx.nodes.NodeProcessor;
import com.rm.springjavafx.nodes.NodeProcessorFactory;
import com.rm.springjavafx.nodes.TextFormatterPropertyBinder;
import common.bindings.ObjectConverter;
import common.bindings.RmBindings;
import java.lang.annotation.Annotation;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.NumberStringConverter;
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
public class NumericLabelProcessor implements NodeProcessor, InitializingBean {

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
    this.factory.addProcessor(NumericLabel.class, this);
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
    if (!(annotation instanceof NumericLabel)) {
      throw new IllegalArgumentException("Annotation is not an instance of " + NumericLabel.class);
    }
    Label label = (Label) node;
    NumericLabel conf = (NumericLabel) annotation;

    NumberFormat numberFormat = new DecimalFormat(conf.format());
    TextFormatter<Number> formatter = new TextFormatter<>(new NumberStringConverter(numberFormat));
    RmBindings.bind1To2(label.textProperty(), formatter.valueProperty(), new ObjectConverter<String, Number>() {
      @Override
      public Number toObject(String reference) {
        return formatter.getValueConverter().fromString(reference);
      }

      @Override
      public String fromObject(Number reference) {
        return formatter.getValueConverter().toString(reference);
      }
    });

    label.setAlignment(conf.alignment());
    if (conf.beanId().length != 0) {
      Object bean = this.appcontext.getBean(conf.beanId()[0]);
      TextFormatterPropertyBinder<Number> propertyBinder //
        = new TextFormatterPropertyBinder<>(formatter, conf.beanId(), bean);
      propertyBinder.bind();
    }
  }
}
