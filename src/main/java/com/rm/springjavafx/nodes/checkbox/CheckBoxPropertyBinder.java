package com.rm.springjavafx.nodes.checkbox;

import common.bindings.RmBindings;
import java.lang.reflect.Field;
import javafx.beans.property.Property;
import javafx.scene.control.CheckBox;

/**
 *
 * @author Ricardo Marquez
 */
public class CheckBoxPropertyBinder {

  private final CheckBox checkbox;
  private final String[] beanId;
  private final Object parentBean;

  public CheckBoxPropertyBinder( //
    CheckBox checkbox, String[] beanId, Object parentBean) {
    this.checkbox = checkbox;
    this.beanId = beanId;
    this.parentBean = parentBean;
  }

  /**
   *
   */
  public void bind() {
    if (beanId.length == 1) {
      this.doSingleLayerBinding();
    } else if (parentBean != null) {
      this.doLayerBinding();
    }
  }
  
  /**
   * 
   * @param binder
   * @throws RuntimeException 
   */
  private void doLayerBinding()  {
    Binder binder = new Binder(checkbox, parentBean, beanId);
    if (parentBean instanceof Property) {
      Property<Object> beanProperty = (Property<Object>) parentBean;
      beanProperty.addListener((obs, old, change) -> {
        if (old != null) {
          try {
            Field f = old.getClass().getDeclaredField(beanId[1]);
            f.setAccessible(true);
            Property<Number> property = (Property<Number>) f.get(old);
            property.unbind();
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        }
        if (change != null) {
          binder.bindToFormatter(change);
        } else{
          this.checkbox.setIndeterminate(true);
        }
      });
      binder.bindToFormatter(beanProperty.getValue());
    } else {
      try {
        Field f = parentBean.getClass().getDeclaredField(beanId[1]);
        Property<Boolean> property = (Property<Boolean>) f.get(parentBean);
        RmBindings.bind1To2(checkbox.selectedProperty(), property);
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }
  }
  
  /**
   * 
   */
  private void doSingleLayerBinding() {
    Property<Boolean> property = (Property<Boolean>) parentBean;
    RmBindings.bind1To2(checkbox.selectedProperty(), property);
  }

  /**
   *
   */
  private static class Binder<T> {

    private final CheckBox checkbox;
    private final Object parentbean;
    private final String[] beanId;

    public Binder(CheckBox checkbox, Object parentbean, String[] beanId) {
      this.checkbox = checkbox;
      this.parentbean = parentbean;
      this.beanId = beanId;
    }

    /**
     *
     * @param change
     */
    private void bindToFormatter(Object change) {
      try {
        Field f = change.getClass().getDeclaredField(this.beanId[1]);
        f.setAccessible(true);
        Property<Boolean> property;
        if (this.parentbean instanceof Property) {
          property = (Property<Boolean>) f.get(((Property) this.parentbean).getValue());
        } else {
          property = (Property<Boolean>) f.get(this.parentbean);
        }
        property.unbind();
        this.checkbox.setAllowIndeterminate(false);
        this.checkbox.setIndeterminate(property.getValue() == null);
        if (property.getValue() != null) {
          this.checkbox.setSelected(property.getValue());
        }
        property.bind(this.checkbox.selectedProperty());
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }
  }

}
