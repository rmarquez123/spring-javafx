package com.windsim.wpls.db.linesolver.impl;

import com.windsim.wpls.db.linesolver.pg.impl.PgDbWeatherStationsLoader;
import com.windsim.wpls.db.linesolver.pg.impl.PgDbLineSectionsLoader;
import com.windsim.wpls.db.linesolver.pg.impl.PgDbModelPointsLoader;
import com.windsim.wpls.db.linesolver.pg.impl.PgDbWeatherRecordsLoader;
import com.windsim.wpls.db.linesolver.pg.impl.PgDbLookupTablesLoader;
import com.windsim.wpls.db.linesolver.pg.impl.PgDbConductorsLoader;
import gov.inl.glass3.conductors.ConductorCatalogue;
import gov.inl.glass3.linesections.LineSections;
import gov.inl.glass3.linesolver.SimulationTimeConfig;
import gov.inl.glass3.modelpoints.ModelPoints;
import gov.inl.glass3.weather.WeatherRecords;
import gov.inl.glass3.weather.WeatherStations;
import gov.inl.glass3.windmodel.LookupTables;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Ricardo Marquez
 */
public class DbLoadersTest {
    
  @Test
  public void testLoadWeatherRecords() {
    EntityManager em = Persistence.createEntityManagerFactory("wpls_idaho_power_pu").createEntityManager(); 
    PgDbWeatherRecordsLoader loader = new PgDbWeatherRecordsLoader(em);
    Set<String> stations = new HashSet<>();
    stations.add("WS07");
    ZonedDateTime startDt = ZonedDateTime.ofInstant(Instant.parse("2012-01-01T07:00:00Z"), ZoneId.of("US/Mountain")); 
    ZonedDateTime endDt = ZonedDateTime.ofInstant(Instant.parse("2012-01-02T07:00:00Z"), ZoneId.of("US/Mountain"));
    TemporalAmount temporalAmount = Duration.of(1, ChronoUnit.HOURS);
    SimulationTimeConfig simulationInputs = new SimulationTimeConfig(startDt, endDt, temporalAmount);
    WeatherRecords records = loader.loadWeatherRecords(stations, simulationInputs); 
    Assert.assertTrue(records.numStations() > 0);  
  }
  
  @Test
  public void testLoadWeatherStations() {
    EntityManager em = Persistence.createEntityManagerFactory("wpls_idaho_power_pu").createEntityManager(); 
    PgDbWeatherStationsLoader loader = new PgDbWeatherStationsLoader(em);
    WeatherStations stations = loader.loadWeatherStations();
    Assert.assertTrue(stations.size() > 0); 
  }
  
  @Test
  public void testLoadConductors() {
    EntityManager em = Persistence.createEntityManagerFactory("wpls_idaho_power_pu").createEntityManager(); 
    PgDbConductorsLoader loader = new PgDbConductorsLoader(em);
    ConductorCatalogue records = loader.loadConductors(); 
    Assert.assertTrue(records.size() > 0); 
  }
  
  @Test
  public void testLoadLookupTables() {
    EntityManager em = Persistence.createEntityManagerFactory("wpls_idaho_power_pu").createEntityManager(); 
    PgDbLookupTablesLoader loader = new PgDbLookupTablesLoader(em);
    LookupTables records = loader.loadLookupTables(); 
    Assert.assertTrue(records.numModelPoints() > 0); 
  }
  
  @Test
  public void testLoadModelPoints() {
    EntityManager em = Persistence.createEntityManagerFactory("wpls_idaho_power_pu").createEntityManager(); 
    PgDbModelPointsLoader loader = new PgDbModelPointsLoader(em);
    ModelPoints records = loader.loadModelPoints();
    Assert.assertTrue(records.size() > 0); 
  }
  
  @Test
  public void testLoadLineSections() {
    EntityManager em = Persistence.createEntityManagerFactory("wpls_idaho_power_pu").createEntityManager(); 
    PgDbLineSectionsLoader loader = new PgDbLineSectionsLoader(em);
    LineSections records = loader.loadLineSections();
    Assert.assertTrue(records.size() > 0); 
  }
  
}
