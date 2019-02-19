package com.windsim.wpls.view.series;

import gov.inl.glass3.linesolver.ModelPointAmpacities;
import gov.inl.glass3.weather.WeatherRecords;

/**
 *
 * @author Ricardo Marquez
 */
public class ModelSeriesData {
  
  private final WeatherRecords weatherRecords;
  private final ModelPointAmpacities modelPointAmpacities;
  private final ModelPointAmpacities fcstedModelPointAmpacities;

  /**
   * 
   * @param weatherRecords
   * @param modelPointAmpacities 
   */
  public ModelSeriesData(WeatherRecords weatherRecords, 
    ModelPointAmpacities modelPointAmpacities, ModelPointAmpacities fcstedModelPointAmpacities) {
    this.weatherRecords = weatherRecords;
    this.modelPointAmpacities = modelPointAmpacities;
    this.fcstedModelPointAmpacities = fcstedModelPointAmpacities;
  }

  public WeatherRecords getWeatherRecords() {
    return weatherRecords;
  }

  public ModelPointAmpacities getModelPointAmpacities() {
    return modelPointAmpacities;
  }

  public ModelPointAmpacities getFcstedModelPointAmpacities() {
    return fcstedModelPointAmpacities;
  }
  
  

  @Override
  public String toString() {
    return "ModelSeriesData{" + "weatherRecords=" + weatherRecords + ", modelPointAmpacities=" + modelPointAmpacities + '}';
  }
  
  
}
