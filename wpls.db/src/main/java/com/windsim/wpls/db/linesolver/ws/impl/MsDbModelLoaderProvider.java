package com.windsim.wpls.db.linesolver.ws.impl;

import gov.inl.glass3.linesolver.ModelLoadersProvider;
import gov.inl.glass3.linesolver.loaders.ConductorsLoader;
import gov.inl.glass3.linesolver.loaders.LineSectionsLoader;
import gov.inl.glass3.linesolver.loaders.LookupTablesLoader;
import gov.inl.glass3.linesolver.loaders.ModelPointsLoader;
import gov.inl.glass3.linesolver.loaders.WeatherStationsLoader;
import javax.persistence.EntityManager;

/**
 *
 * @author Ricardo Marquez
 */
public class MsDbModelLoaderProvider implements ModelLoadersProvider{

  private final EntityManager em;
  
  public MsDbModelLoaderProvider(EntityManager em) {
    this.em = em;
  }
  
  @Override
  public ConductorsLoader getConductorsLoader() {
    return new MsDbConductorsLoader(em); 
  }

  @Override
  public WeatherStationsLoader getWeatherStationsLoader() {
    return new MsDbWeatherStationsLoader(em); 
  }

  @Override
  public LineSectionsLoader getLineSectionsLoader() {
    return new MsDbLineSectionsLoader(em); 
  }

  @Override
  public ModelPointsLoader getModelPointsLoader() {
    return new MsDbModelPointsLoader(this.em);
  }

  @Override
  public LookupTablesLoader getLookupTablesLoader() {
    return new MsDbLookupTablesLoader(em);
  }
  
}
