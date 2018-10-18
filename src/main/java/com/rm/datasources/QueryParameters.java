package com.rm.datasources;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

/**
 *
 * @author rmarquez
 */
public final class QueryParameters implements Iterable<QueryParameter> {

  private final MapProperty<String, QueryParameter> queryParam = new SimpleMapProperty<>();

  /**
   *
   * @param queryParam
   */
  public QueryParameters(List<QueryParameter> queryParam) {
    this.setQueryParameters(queryParam);
  }

  /**
   *
   * @param listener
   */
  public void addListener(ChangeListener<? super ObservableMap<String, QueryParameter>> listener) {
    this.queryParam.addListener(listener);
  }

  /**
   *
   * @param queryParam
   */
  public void setQueryParameters(List<QueryParameter> queryParam) {
    Map<String, QueryParameter> map = new HashMap<>();
    for (QueryParameter queryParameter : queryParam) {
      String name = queryParameter.getName();
      map.put(name, queryParameter);
    }
    this.queryParam.setValue(FXCollections.observableMap(map));
  }

  /**
   *
   * @param stnId
   * @return
   */
  public QueryParameter get(String stnId) {
    QueryParameter result = this.queryParam.get(stnId);
    return result;
  }

  /**
   *
   * @return
   */
  public int size() {
    return this.queryParam.size();
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public Iterator<QueryParameter> iterator() {
    return this.queryParam.values().iterator();
  }

  @Override
  public String toString() {
    return "QueryParameters{" + "queryParam=" + queryParam + '}';
  }
  
}
