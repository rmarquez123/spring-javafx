package com.rm.springjavafx.annotations.childnodes;

import common.bindings.RmBindings;
import java.lang.reflect.Field;
import javafx.beans.property.Property;
import javafx.scene.control.CheckBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class CheckBoxAnnotationHandler {

  @Autowired
  private ApplicationContext appContext;

  /**
   *
   * @param field
   */
  public void handle(ChildNodeArgs wrapper) {
    Field field = wrapper.field;
    NodeBind.CheckBox conf = field.getDeclaredAnnotation(NodeBind.CheckBox.class);
    if (conf != null) {
      String beanId = conf.selectedId();
      Property<Boolean> property = (Property<Boolean>) this.appContext.getBean(beanId);
      CheckBox checkBox = (CheckBox) wrapper.node;
      RmBindings.bind1To2(checkBox.selectedProperty(), property);
    }
  }
}
