package gov.inl.glass3.linesolver;

import gov.inl.glass3.conductors.ConductorCatalogue;
import gov.inl.glass3.modelpoints.ModelPoints;
import gov.inl.glass3.linesections.LineSections;
import gov.inl.glass3.linesolver.loaders.ConductorsLoader;
import gov.inl.glass3.linesolver.loaders.LineSectionsLoader;
import gov.inl.glass3.linesolver.loaders.LookupTablesLoader;
import gov.inl.glass3.linesolver.loaders.ModelPointsLoader;
import gov.inl.glass3.linesolver.loaders.WeatherStationsLoader;
import gov.inl.glass3.weather.WeatherStations;
import gov.inl.glass3.windmodel.LookupTables;

/**
 *
 * @author Ricardo Marquez
 */
public class ModelController {

  private final ModelLoadersProvider provider;

  /**
   *
   * @param provider
   */
  public ModelController(ModelLoadersProvider provider) {
    this.provider = provider;
  }
  
  

  /**
   *
   * @return
   */
  public Model loadModel() {

    // Loading conductors. 
    ConductorsLoader conductorsLoader = this.provider.getConductorsLoader();
    ConductorCatalogue conductorCatalogue = conductorsLoader.loadConductors();

    // Loading line sections
    LineSectionsLoader lineSectionsLoader = this.provider.getLineSectionsLoader();
    LineSections lineSections = lineSectionsLoader.loadLineSections();

    // Loading weather stations
    WeatherStationsLoader weatherStnsLoader = this.provider.getWeatherStationsLoader();
    WeatherStations weatherStationsLoader = weatherStnsLoader.loadWeatherStations();

    // Loading model points
    ModelPointsLoader modelPointsLoader = this.provider.getModelPointsLoader();
    ModelPoints modelPoints = modelPointsLoader.loadModelPoints();

    // Loading model points
    LookupTablesLoader lookupTablesLoader = this.provider.getLookupTablesLoader();
    LookupTables lookupTables = lookupTablesLoader.loadLookupTables();

    //  Create and return model.
    Model model = new Model.Builder()
      .setConductorCatalogue(conductorCatalogue)
      .setLineSections(lineSections)
      .setLookupTables(lookupTables)
      .setModelPoints(modelPoints)
      .setWeatherStationsLoader(weatherStationsLoader)
      .build();

    return model;
  }

}
