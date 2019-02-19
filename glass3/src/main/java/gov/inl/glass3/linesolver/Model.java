package gov.inl.glass3.linesolver;

import gov.inl.glass3.conductors.Conductor;
import gov.inl.glass3.conductors.ConductorCatalogue;
import gov.inl.glass3.linesections.LineSection;
import gov.inl.glass3.linesections.LineSections;
import gov.inl.glass3.modelpoints.ModelPoint;
import gov.inl.glass3.modelpoints.ModelPoints;
import gov.inl.glass3.weather.WeatherStations;
import gov.inl.glass3.windmodel.LookupTables;

/**
 *
 * @author Ricardo Marquez
 */
public class Model {

  private final ConductorCatalogue conductorCatalogue;
  private final LineSections lineSections;
  private final WeatherStations weatherStationsLoader;
  private final ModelPoints modelPoints;
  private final LookupTables lookupTables;

  /**
   *
   * @param conductorCatalogue
   * @param lineSections
   * @param weatherStationsLoader
   * @param modelPoints
   * @param lookupTables
   */
  public Model(ConductorCatalogue conductorCatalogue, LineSections lineSections,
    WeatherStations weatherStationsLoader, ModelPoints modelPoints,
    LookupTables lookupTables) {
    this.conductorCatalogue = conductorCatalogue;
    this.lineSections = lineSections;
    this.weatherStationsLoader = weatherStationsLoader;
    this.modelPoints = modelPoints;
    this.lookupTables = lookupTables;
  }

  /**
   *
   * @return
   */
  public ConductorCatalogue getConductorCatalogue() {
    return conductorCatalogue;
  }

  /**
   *
   * @return
   */
  public LineSections getLineSections() {
    return lineSections;
  }

  /**
   *
   * @return
   */
  public WeatherStations getWeatherStations() {
    return weatherStationsLoader;
  }

  /**
   *
   * @return
   */
  public ModelPoints getModelPoints() {
    return modelPoints;
  }

  /**
   *
   * @return
   */
  public LookupTables getLookupTables() {
    return lookupTables;
  }

  @Override
  public String toString() {
    return "Model{" + "conductorCatalogue=" + conductorCatalogue
      + ", lineSections=" + lineSections
      + ", weatherStationsLoader=" + weatherStationsLoader
      + ", modelPoints=" + modelPoints + '}';
  }

  
  /**
   * 
   * @param modelPointId
   * @return 
   */
  public Conductor getConductorByModelPointId(String modelPointId) {
    LineSection lineSection = this.getLineSectionByModelPointId(modelPointId); 
    String conductorId = lineSection.getConductorId();
    Conductor conductor = this.getConductorCatalogue().get(conductorId);
    return conductor;
  }

  /**
   * 
   * @param modelPointId
   * @return 
   */
  public LineSection getLineSectionByModelPointId(String modelPointId) {
    ModelPoint modelPoint = this.modelPoints.get(modelPointId);
    String lineId = modelPoint.getLineId();
    LineSection lineSection = this.getLineSections().get(lineId);
    return lineSection;
  }
  
  /**
   * 
   * @param modelPointId
   * @return 
   */
  public LookupTables getLookupTableByModelPoint(String modelPointId) {
    return this.lookupTables.getByModelPointId(modelPointId); 
  }

  /**
   *
   */
  public static class Builder {

    private ConductorCatalogue conductorCatalogue;
    private LineSections lineSections;
    private WeatherStations weatherStationsLoader;
    private ModelPoints modelPoints;
    private LookupTables lookupTables;

    public Builder() {
    }
    
    public Builder(Model other) {
      this.conductorCatalogue = other.conductorCatalogue;
      this.lineSections = other.lineSections;
      this.weatherStationsLoader = other.weatherStationsLoader;
      this.modelPoints = other.modelPoints;
      this.lookupTables = other.lookupTables;
    }

    public Builder setConductorCatalogue(ConductorCatalogue conductorCatalogue) {
      this.conductorCatalogue = conductorCatalogue;
      return this;
    }

    public Builder setLineSections(LineSections lineSections) {
      this.lineSections = lineSections;
      return this;
    }

    public Builder setWeatherStationsLoader(WeatherStations weatherStationsLoader) {
      this.weatherStationsLoader = weatherStationsLoader;
      return this;
    }

    public Builder setModelPoints(ModelPoints modelPoints) {
      this.modelPoints = modelPoints;
      return this;
    }

    public Builder setLookupTables(LookupTables lookupTables) {
      this.lookupTables = lookupTables;
      return this;
    }

    /**
     *
     * @return
     */
    public Model build() {
      return new Model(conductorCatalogue, lineSections, weatherStationsLoader, modelPoints, lookupTables);
    }

  }

}
