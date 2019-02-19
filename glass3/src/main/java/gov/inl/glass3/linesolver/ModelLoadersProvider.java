package gov.inl.glass3.linesolver;

import gov.inl.glass3.linesolver.loaders.ConductorsLoader;
import gov.inl.glass3.linesolver.loaders.LineSectionsLoader;
import gov.inl.glass3.linesolver.loaders.LookupTablesLoader;
import gov.inl.glass3.linesolver.loaders.ModelPointsLoader;
import gov.inl.glass3.linesolver.loaders.WeatherStationsLoader;

/**
 *
 * @author Ricardo Marquez
 */
public interface ModelLoadersProvider {
  
  /**
   * 
   * @return 
   */
  public ConductorsLoader getConductorsLoader();
  
  /**
   * 
   * @return 
   */
  public WeatherStationsLoader getWeatherStationsLoader();

  /**
   * 
   * @return 
   */
  public LineSectionsLoader getLineSectionsLoader();
  
  /**
   * 
   * @return 
   */
  public ModelPointsLoader getModelPointsLoader();
  
  /**
   * 
   * @return 
   */
  public LookupTablesLoader getLookupTablesLoader();
  
}
