package com.windsim.wpls.db.linesolver.pg.impl;

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
public class PgDbModelLoaderProvider implements ModelLoadersProvider{

  private final EntityManager em;
  
  public PgDbModelLoaderProvider(EntityManager em) {
    this.em = em;
  }
  
  @Override
  public ConductorsLoader getConductorsLoader() {
    return new PgDbConductorsLoader(em); 
  }

  @Override
  public WeatherStationsLoader getWeatherStationsLoader() {
    return new PgDbWeatherStationsLoader(em); 
  }

  @Override
  public LineSectionsLoader getLineSectionsLoader() {
    return new PgDbLineSectionsLoader(em); 
  }

  @Override
  public ModelPointsLoader getModelPointsLoader() {
    return new PgDbModelPointsLoader(this.em);
  }

  @Override
  public LookupTablesLoader getLookupTablesLoader() {
    return new PgDbLookupTablesLoader(em);
  }
  
}
