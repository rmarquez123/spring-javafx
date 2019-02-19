package com.windsim.wpls.db.linesolver.pg.impl;

import com.windsim.wpls.db.core.pg.entities.ConductorPgEntity;
import gov.inl.glass3.conductors.Conductor;
import gov.inl.glass3.conductors.ConductorCatalogue;
import gov.inl.glass3.customunits.RmSI;
import gov.inl.glass3.linesolver.impl._default.DefaultConductorCatalogue;
import gov.inl.glass3.linesolver.loaders.ConductorsLoader;
import java.util.List;
import java.util.stream.Collectors;
import javax.measure.Measure;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.persistence.EntityManager;

/**
 *
 * @author Ricardo Marquez
 */
public class PgDbConductorsLoader implements ConductorsLoader{

  private final EntityManager em;
  
  /**
   *
   * @param em
   */
  public PgDbConductorsLoader(EntityManager em) {
    this.em = em;
  }
  
  @Override
  public ConductorCatalogue loadConductors() {
    List<Conductor> conductors = this.em
      .createNamedQuery("ConductorPgEntity.findAll", ConductorPgEntity.class)
      .getResultList()
      .stream()
      .map((e)->{
        Conductor c = new Conductor.Builder()
          .setName(e.getConductorId())
          .setDiameter(Measure.valueOf(e.getDiameter(), NonSI.INCH))
          .setMaxResistence(Measure.valueOf(e.getResistance_max(), RmSI.OHMS_PER_MILE))
          .setMinResistence(Measure.valueOf(e.getResistance_min(), RmSI.OHMS_PER_MILE))
          .setMaxTemperature(Measure.valueOf(e.getTemperature_max(), SI.CELSIUS))
          .setMinTemperature(Measure.valueOf(e.getTemperature_min(), SI.CELSIUS))
          .setType(e.getType())
          .build(); 
        return c;
      })
      .collect(Collectors.toList()); 
    ConductorCatalogue result = new DefaultConductorCatalogue(conductors);
    return result;
  }
  
}
