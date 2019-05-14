package com.rm.springjavafx.bindings;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.components.ChildNodeWrapper;
import javafx.beans.property.Property;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author Ricardo Marquez
 */
public class BindLabel implements InitializingBean {

  @Autowired
  private FxmlInitializer fxmlInitializer;
  private StringConverter<Object> converter;
  private ChildNodeWrapper<Label> label;
  private Property<Object> value;

  @Required
  public void setConverter(StringConverter<Object> converter) {
    this.converter = converter;
  }

  @Required
  public void setLabel(ChildNodeWrapper<Label> label) {
    this.label = label;
  }
  
  @Required
  public void setValue(Property<Object> value) {
    this.value = value;
  }

  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    this.fxmlInitializer.addListener((i) -> {
      this.label.getNode().textProperty()
        .bindBidirectional(this.value, this.converter);
    });
  }

}
