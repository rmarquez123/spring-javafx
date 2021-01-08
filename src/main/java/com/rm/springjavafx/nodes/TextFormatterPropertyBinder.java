package com.rm.springjavafx.nodes;

import common.bindings.RmBindings;
import java.lang.reflect.Field;
import javafx.beans.property.Property;
import javafx.scene.control.TextFormatter;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author Ricardo Marquez
 */
public class TextFormatterPropertyBinder<T> {

  private final TextFormatter<T> formatter;
  private final String[] beanId;
  private final Object parentBean;

  public TextFormatterPropertyBinder( //
    TextFormatter<T> formatter, String[] beanId, Object parentBean) {
    this.formatter = formatter;
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
    TextFormatterBinder binder = new TextFormatterBinder(formatter, parentBean, beanId);
    if (parentBean instanceof Property) {
      Property<Object> beanProperty = (Property<Object>) parentBean;
      beanProperty.addListener((obs, old, change) -> {
        if (old != null) {
          try {
            Field f = ReflectionUtils.findField(old.getClass(), beanId[1]);
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
          this.formatter.setValue(null);
        }
      });
      binder.bindToFormatter(beanProperty.getValue());
    } else {
      try {
        Field f = ReflectionUtils.findField(parentBean.getClass(), beanId[1]);
        Property<T> property = (Property<T>) f.get(parentBean);
        RmBindings.bind1To2(formatter.valueProperty(), property);
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }
  }
  
  /**
   * 
   */
  private void doSingleLayerBinding() {
    Property<T> property = (Property<T>) parentBean;
    RmBindings.bind1To2(formatter.valueProperty(), property);
  }

  /**
   *
   */
  private static class TextFormatterBinder<T> {

    private final TextFormatter<T> formatter;
    private final Object parentbean;
    private final String[] beanId;

    public TextFormatterBinder(TextFormatter<T> formatter, Object parentbean, String[] beanId) {
      this.formatter = formatter;
      this.parentbean = parentbean;
      this.beanId = beanId;
    }

    /**
     *
     * @param change
     */
    private void bindToFormatter(Object change) {
      try {
        Field f = ReflectionUtils.findField(change.getClass(), this.beanId[1]);
        f.setAccessible(true);
        Property<T> property;
        if (this.parentbean instanceof Property) {
          property = (Property<T>) f.get(((Property) this.parentbean).getValue());
        } else {
          property = (Property<T>) f.get(this.parentbean);
        }
        property.unbind();
        this.formatter.setValue(property.getValue());
        property.bind(this.formatter.valueProperty());
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }
  }

}
