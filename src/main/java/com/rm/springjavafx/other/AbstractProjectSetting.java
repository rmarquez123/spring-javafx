package com.rm.springjavafx.other;

import com.rm.springjavafx.project.AttributeConverter;
import com.rm.springjavafx.project.Converter;
import com.rm.springjavafx.project.IProjectSetting;
import com.rm.springjavafx.project.SerializableHashMap;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;
import javafx.beans.property.Property;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class AbstractProjectSetting implements IProjectSetting {

  @Autowired
  private ApplicationContext appContext;

  /**
   *
   * @return
   */
  @Override
  public final Serializable serialize() {
    /**
     * This method will create and return a hashmap containing the serialized field
     * values. The hashmap, which should contain nothing but serializable entries, will
     * therefore be serializable.
     */
    SerializableHashMap<String, Serializable> result = new SerializableHashMap<>();
    Field[] fields = this.getClass().getDeclaredFields();
    for (Field field : fields) {
      /**
       * The object value is obtained by reflection to get the values as a
       * {@linkplain Property} object then {@linkplain Property#getValue()} is called to
       * get the current value.
       */
      Property<Object> fieldProperty = (Property<Object>) this.getFieldValue(field);
      Object fieldValue = fieldProperty.getValue();

      /**
       * If it has a converter annotation, then the converter annotation values are used
       * for creating the serializable. Otherwise, we expect the field value to itself be
       * serializable.
       */
      Serializable serializable;
      Converter conv;
      if ((conv = field.getAnnotation(Converter.class)) != null) {
        serializable = this.serializeFieldValue(conv, fieldValue);
      } else {
        serializable = (Serializable) fieldValue;
      }
      if (serializable != null) {
        result.put(field.getName(), serializable);
      }
    }
    /**
     * The hash map containing field values as entries is returned.
     */
    return result;
  }

  /**
   *
   * @param serializable
   */
  @Override
  public final void deSerialize(Serializable serializable) {
    SerializableHashMap<String, Serializable> map 
      = (SerializableHashMap<String, Serializable>) serializable;
    for (Map.Entry<String, Serializable> entry : map.entrySet()) {
      String fieldName = entry.getKey();
      Serializable serialized = entry.getValue();
      Field field = this.getField(fieldName);
      Property property = (Property) this.getFieldValue(field);
      Object object;
      Converter conv;
      if ((conv = field.getAnnotation(Converter.class)) != null) {
        object = getObject(conv, serialized);
      } else {
        object = serialized;
      }
      property.setValue(object);
    }
  }

  /**
   *
   * @param conv
   * @param serialized
   * @return
   */
  private Object getObject(Converter conv, Serializable serialized) {
    Object result;
    try {
      AttributeConverter converterInstance = this.getConverter(conv);
      result = converterInstance.fromSerializable(serialized);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    return result;
  }

  /**
   *
   * @param fieldName
   * @return
   */
  private Field getField(String fieldName) {
    Field result;
    try {
      result = this.getClass().getDeclaredField(fieldName);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    return result;
  }

  /**
   *
   * @param conv
   * @param fieldValue
   * @return
   * @throws RuntimeException
   */
  private Serializable serializeFieldValue(Converter conv, Object fieldValue) {
    AttributeConverter convInstance;
    try {
      convInstance = this.getConverter(conv);
    } catch (InstantiationException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
    Serializable serializable = convInstance.toSerializable(fieldValue);
    return serializable;
  }

  /**
   *
   * @param field
   * @return
   * @throws RuntimeException
   */
  private Object getFieldValue(Field field) {
    Object get;
    try {
      field.setAccessible(true);
      get = field.get(this);
    } catch (IllegalArgumentException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
    return get;
  }

  /**
   *
   * @param conv
   * @return
   * @throws IllegalStateException
   * @throws IllegalAccessException
   * @throws BeansException
   * @throws InstantiationException
   */
  private AttributeConverter getConverter(Converter conv) throws IllegalStateException, IllegalAccessException, BeansException, InstantiationException {
    Map<String, ? extends AttributeConverter> map = this.appContext.getBeansOfType(conv.converter());
    AttributeConverter converterInstance;
    if (map.isEmpty()) {
      converterInstance = conv.converter().newInstance();
    } else if (map.size() == 1) {
      converterInstance = map.entrySet().iterator().next().getValue();
    } else {
      throw new IllegalStateException("Only one bean is allowed for type : " + conv.converter());
    }
    return converterInstance;
  }

}
