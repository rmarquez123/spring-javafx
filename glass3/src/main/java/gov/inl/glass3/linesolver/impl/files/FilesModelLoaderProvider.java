package gov.inl.glass3.linesolver.impl.files;

import gov.inl.glass3.linesolver.ModelLoadersProvider;
import gov.inl.glass3.linesolver.loaders.ConductorsLoader;
import gov.inl.glass3.linesolver.loaders.LineSectionsLoader;
import gov.inl.glass3.linesolver.loaders.LookupTablesLoader;
import gov.inl.glass3.linesolver.loaders.ModelPointsLoader;
import gov.inl.glass3.linesolver.loaders.WeatherStationsLoader;

/**
 *
 * @author Ricardo Marquez
 */
public class FilesModelLoaderProvider implements ModelLoadersProvider {

  private final InputStreamProvider file;

  /**
   *
   * @param file
   */
  public FilesModelLoaderProvider(InputStreamProvider file) {
    this.file = file;
  }

  @Override
  public ConductorsLoader getConductorsLoader() {
    return new FilesConductorsLoader(file.openStream());
  }

  @Override
  public WeatherStationsLoader getWeatherStationsLoader() {
    return new FilesWeatherStationsLoader(file.openStream());
  }

  @Override
  public LineSectionsLoader getLineSectionsLoader() {
    return new FilesLineSectionsLoader(file.openStream());
  }

  @Override
  public ModelPointsLoader getModelPointsLoader() {
    return new FilesModelPointsLoader(file.openStream(), "ModelPoint");
  }

  @Override
  public LookupTablesLoader getLookupTablesLoader() {
    return new FilesLookupTablesLoader(file.openStream(), "LookUpTable");
  }
}
