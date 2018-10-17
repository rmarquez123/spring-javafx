package com.rm.datasources;

import com.rm.datasources.QueryParameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rmarquez
 */
public class QueryParameters {

  private final Map<String, QueryParameter> queryParam = new HashMap<>();
  
  /**
   * 
   * @param queryParam
   */
  public QueryParameters(List<QueryParameter> queryParam) {
    for (QueryParameter queryParameter : queryParam) {
      String name = queryParameter.getName();
      this.queryParam.put(name, queryParameter); 
    }
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

  
}
