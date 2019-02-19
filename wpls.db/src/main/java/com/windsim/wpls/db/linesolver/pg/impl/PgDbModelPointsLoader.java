package com.windsim.wpls.db.linesolver.pg.impl;

import com.windsim.wpls.db.core.pg.entities.ModelPointPgEntity;
import gov.inl.glass3.linesolver.impl._default.DefaultModelPoints;
import gov.inl.glass3.linesolver.loaders.ModelPointsLoader;
import gov.inl.glass3.modelpoints.ModelPoint;
import gov.inl.glass3.modelpoints.ModelPointGeometry;
import gov.inl.glass3.modelpoints.ModelPoints;
import java.util.ArrayList;
import java.util.List;
import javax.measure.Measure;
import javax.measure.unit.NonSI;
import javax.persistence.EntityManager;

/**
 *
 * @author Ricardo Marquez
 */
public class PgDbModelPointsLoader implements ModelPointsLoader {

  private final EntityManager em;

  public PgDbModelPointsLoader(EntityManager em) {
    this.em = em;
  }

  @Override
  public ModelPoints loadModelPoints() {
    List<Object[]> queryResultList = this.em
      .createNamedQuery("ModelPointPgEntity.findAllWithJoin1")
      .getResultList();
    List<ModelPoint> modelPoints = new ArrayList<>();
    for (Object[] modelPoint : queryResultList) {
      ModelPointPgEntity entity = (ModelPointPgEntity) modelPoint[0];
      String lineId = (String) modelPoint[1];
      String weatherStationId = (String) modelPoint[2];
      String modelPointId = entity.getLineId();
      ModelPointGeometry geometry = new ModelPointGeometry.Builder()
        .setAzimuth(Measure.valueOf(entity.getAzimuth(), NonSI.DEGREE_ANGLE))
        .setElevation(Measure.valueOf(entity.getElevation(), NonSI.FOOT))
        .setPoint(entity.getPoint().getCentroid())
        .build();
      ModelPoint mp = new ModelPoint(modelPointId, weatherStationId, lineId, geometry);
      modelPoints.add(mp); 
    }
    ModelPoints result = new DefaultModelPoints(modelPoints);
    return result;
  }

}
