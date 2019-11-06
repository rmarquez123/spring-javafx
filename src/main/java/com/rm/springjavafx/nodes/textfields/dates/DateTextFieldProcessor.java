package com.rm.springjavafx.nodes.textfields.dates;

import com.rm.springjavafx.nodes.NodeProcessor;
import com.rm.springjavafx.nodes.NodeProcessorFactory;
import java.lang.annotation.Annotation;
import java.time.ZoneId;
import javafx.beans.property.Property;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import tornadofx.control.DateTimePicker;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
public class DateTextFieldProcessor implements InitializingBean, NodeProcessor {

  @Autowired
  private NodeProcessorFactory factory;
  
  @Autowired
  private ApplicationContext appcontext;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.factory.addProcessor(DateTextField.class, this);
  }

  @Override
  public void process(Object parentBean, Object node, Annotation annotation) {
    if (!(node instanceof DateTimePicker)) {
      throw new IllegalArgumentException("Node is not an instance of " + DateTimePicker.class);
    }
    if (!(annotation instanceof DateTextField)) {
      throw new IllegalArgumentException("Annotation is not an instance of " + DateTextField.class);
    }
    DateTimePicker textfield = (DateTimePicker) node;
    DateTextField conf = (DateTextField) annotation;
    Property<ZoneId> zoneIdProperty = (Property<ZoneId>) this.appcontext.getBean(conf.zoneRef());
    
    Object bean = this.appcontext.getBean(conf.beanId()[0]);
    DatePickerPropertyBinder binder = new DatePickerPropertyBinder(  //
      textfield, conf.beanId(), zoneIdProperty, bean); 
    binder.bind();
  }

}
