package com.rm.springjavafx.project;

import com.rm.datasources.DbConnection;
import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Ricardo Marquez
 */
public class DbConnectionAttributeConverter implements AttributeConverter {

  /**
   *
   * @param object
   * @return
   */
  @Override
  public Serializable toSerializable(Object object) {
    return (object == null) ? null : getSerializationMap((DbConnection) object);
  }
  
  /**
   * 
   * @param object
   * @return 
   */
  private static SerializableHashMap<String, Object> getSerializationMap(DbConnection object) {
    Properties properties = ((DbConnection) object).properties();
    SerializableHashMap<String, Object> result = new SerializableHashMap<>();
    for (Object object1 : properties.keySet()) {
      result.put((String)object1, properties.get(object1));
    }
    return result;
  }

  /**
   *
   * @param serialized
   * @return
   */
  @Override
  public Object fromSerializable(Serializable serialized) {
    SerializableHashMap<String, Object> map = (SerializableHashMap<String, Object>) serialized;
    return (map.isEmpty() || serialized == null) ? null : getDbConnection(map);
  }
  
    
  /**
   * 
   * @param serialized
   * @return 
   */
  private static DbConnection getDbConnection(SerializableHashMap<String, Object> serialized) {
    Properties props = new Properties();
    for (Map.Entry<String, Object> entry : serialized.entrySet()) {
      props.put(entry.getKey(), entry.getValue());
    }
    return DbConnection.fromProperties(props);
  }

}
