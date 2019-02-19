package com.windsim.wpls.db.linesolver.pg.impl;

import com.windsim.wpls.db.core.pg.entities.LineSectionPgEntity;
import gov.inl.glass3.linesections.LineSection;
import gov.inl.glass3.linesections.LineSections;
import gov.inl.glass3.linesections.RadiativeProperties;
import gov.inl.glass3.linesolver.impl._default.DefaultLineSections;
import gov.inl.glass3.linesolver.loaders.LineSectionsLoader;
import java.util.List;
import java.util.stream.Collectors;
import javax.measure.Measure;
import javax.measure.unit.SI;
import javax.persistence.EntityManager;

/**
 *
 * @author Ricardo Marquez
 */
public class PgDbLineSectionsLoader implements LineSectionsLoader {

  private final EntityManager em;
  
  /**
   *
   * @param em
   */
  public PgDbLineSectionsLoader(EntityManager em) {
    this.em = em;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public LineSections loadLineSections() {
    List<LineSection> lineSections = this.em
      .createNamedQuery("LineSectionPgEntity.findAll", LineSectionPgEntity.class)
      .getResultList()
      .stream()
      .map((e)->{
      LineSection lineSection = new LineSection.Builder()
        .setConductorId(e.getConductor_id())
        .setLineSectionId(e.getLinesection_id())
        .setAirQuality(e.getAirquality())
        .setBundle(e.getBundle())
        .setDerating(0)
        .setEmergencyTemperature(Measure.valueOf(e.getTemperatureEmergency(), SI.CELSIUS))
        .setMaxTemperature(Measure.valueOf(e.getTemperatureMax(), SI.CELSIUS))
        .setRadProps(new RadiativeProperties(e.getAbsorptivity(), e.getEmissivity()))
        .build();
      return lineSection;
    }).collect(Collectors.toList());      
    LineSections result = new DefaultLineSections(lineSections);
    return result;
  }
  
}
