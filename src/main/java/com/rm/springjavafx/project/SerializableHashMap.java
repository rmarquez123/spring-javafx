package com.rm.springjavafx.project;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Ricardo Marquez
 * @param <K>
 * @param <V>
 */
@XmlRootElement(name = "root")
@XmlSeeAlso({ChronoUnit.class, java.util.Properties.class, ZoneId.class})
public class SerializableHashMap<K, V> implements Serializable {

  private Map<K, V> mapProperty;

  public SerializableHashMap() {
    mapProperty = new HashMap<>();
  }

  @XmlJavaTypeAdapter(MapAdapter.class)
  public Map<K, V> getMapProperty() {
    return mapProperty;
  }

  public void setMapProperty(Map<K, V> map) {
    this.mapProperty = map;
  }
  
  
  public void put(K name, V serializable) {
    this.mapProperty.put(name, serializable);
  }
  
  /**
   * 
   * @return 
   */
  public Iterable<Map.Entry<K, V>> entrySet() {
    return this.mapProperty.entrySet();
  }

  /**
   * 
   * @return 
   */
  boolean isEmpty() {
    return this.mapProperty.isEmpty();
  }

  static class MapAdapter<K,V> extends XmlAdapter<MapElements[], Map<K, V>> {
    /**
     * 
     * @param arg0
     * @return
     * @throws Exception 
     */
    public MapElements[] marshal(Map<K, V> arg0) throws Exception {
      MapElements[] mapElements = new MapElements[arg0.size()];
      int i = 0;
      for (Map.Entry<K, V> entry : arg0.entrySet()) {
        mapElements[i++] = new MapElements(entry.getKey(), entry.getValue());
      }

      return mapElements;
    }
    
    /**
     * 
     * @param arg0
     * @return
     * @throws Exception 
     */
    public Map<K, V> unmarshal(MapElements[] arg0) throws Exception {
      Map<K, V> r = new HashMap<>();
      for (MapElements mapelement : arg0) {
        r.put((K) mapelement.key, (V) mapelement.value);
      }
      return r;
    }
  }

  static class MapElements<K,V> {

    @XmlElement
    public K key;
    @XmlElement
    public V value;

    private MapElements() {
    } //Required by JAXB

    public MapElements(K key, V value) {
      this.key = key;
      this.value = value;
    }
  }
}
