package gov.inl.glass3.windmodel;

import gov.inl.glass3.weather.WeatherRecord;

/**
 *
 * @author Ricardo Marquez
 */
public interface LookupTables extends Iterable<LookupTable> {

  
  /**
   * 
   * @param modelPointId
   * @param record
   * @return 
   */
  public WeatherRecord applyAdjustments(String modelPointId, WeatherRecord record);

  
  /**
   * 
   * @param modelPointId
   * @return 
   */
  public LookupTables getByModelPointId(String modelPointId);
  
  
  /**
   * 
   * @return 
   */
  public int numModelPoints(); 
  
}
