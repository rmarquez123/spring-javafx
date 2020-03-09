package com.rm.springjavafx.nodes.textfields.dates;

import common.bindings.RmBindings;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import tornadofx.control.DateTimePicker;

/**
 *
 * @author Ricardo Marquez
 */
public class DatePickerPropertyBinder {

  private final DateTimePicker datetimepicker;
  private final String[] beanId;
  private final Property<ZoneId> zoneIdProperty;
  private final Object parentBean;

  public DatePickerPropertyBinder( //
    DateTimePicker datetimepicker, String[] beanId, Property<ZoneId> zoneIdProperty, Object parentBean) {
    this.datetimepicker = datetimepicker;
    this.beanId = beanId;
    this.zoneIdProperty = zoneIdProperty;
    this.parentBean = parentBean;
  }

  /**
   *
   */
  public void bind() {
    if (this.beanId.length == 1) {
      this.doSingleLayerBinding();
    } else if (this.parentBean != null) {
      this.doLayerBinding();
    }
  }

  /**
   *
   * @param binder
   * @throws RuntimeException
   */
  private void doLayerBinding() {
    TextFormatterBinder binder = new TextFormatterBinder(this);
    if (this.parentBean instanceof Property) {
      Property<Object> beanProperty = (Property<Object>) this.parentBean;
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
        } else {
          this.datetimepicker.setValue(null);
        }
      });
      binder.bindToFormatter(beanProperty.getValue());
    } else {
      try {
        Field f = parentBean.getClass().getDeclaredField(beanId[1]);
        Property<ZonedDateTime> property = (Property<ZonedDateTime>) f.get(parentBean);
        RmBindings.bindObject(property, this::mapDatePickerToDateTime, datetimepicker.valueProperty());

      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  /**
   *
   */
  private void doSingleLayerBinding() {
    Property<ZonedDateTime> property = (Property<ZonedDateTime>) parentBean;
    RmBindings.bindObject(property,
      this::mapDatePickerToDateTime, datetimepicker.dateTimeValueProperty());

  }

  /**
   *
   * @return
   */
  private ZonedDateTime mapDatePickerToDateTime() {
    LocalDateTime ld = this.datetimepicker.getDateTimeValue();
    ZoneId zone = this.zoneIdProperty.getValue();
    ZonedDateTime result = (ld == null || zone == null) ? null : ZonedDateTime.of(ld, zone);
    return result;
  }

  /**
   *
   */
  private static class TextFormatterBinder {

    private final DatePickerPropertyBinder host;

    public TextFormatterBinder(DatePickerPropertyBinder host) {
      this.host = host;
    }

    /**
     *
     * @param change
     */
    private void bindToFormatter(Object change) {
      try {
        Field f = change.getClass().getDeclaredField(this.host.beanId[1]);
        f.setAccessible(true);
        Property<ZonedDateTime> property;
        if (this.host.parentBean instanceof Property) {
          property = (Property<ZonedDateTime>) f.get(((Property) this.host.parentBean).getValue());
        } else {
          property = (Property<ZonedDateTime>) f.get(this.host.parentBean);
        }
        property.unbind();
        this.host.datetimepicker.setDateTimeValue(property.getValue() == null
          ? null : property.getValue().toLocalDateTime());

        property.bind(Bindings.createObjectBinding(this.host::mapDatePickerToDateTime,
          this.host.datetimepicker.dateTimeValueProperty()));
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }
  }

}
