package com.windsim.wpls.db.linesolver.ws.impl;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.windsim.wpls.db.core.ms.entities.ModelPointMsEntity;
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
public class MsDbModelPointsLoader implements ModelPointsLoader {

  private final EntityManager em;

  public MsDbModelPointsLoader(EntityManager em) {
    this.em = em;
  }

  @Override
  public ModelPoints loadModelPoints() {
    List<Object[]> queryResultList = this.em
      .createNamedQuery("ModelPointMsEntity.findAllWithJoin1")
      .getResultList();
    List<ModelPoint> modelPoints = new ArrayList<>();
    GeometryFactory factory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), 4326);

    for (Object[] modelPoint : queryResultList) {
      ModelPointMsEntity entity = (ModelPointMsEntity) modelPoint[0];
      String lineId = (String) modelPoint[1];
      String weatherStationId = (String) modelPoint[2];
      String modelPointId = entity.getLineId();

      ModelPointGeometry geometry = new ModelPointGeometry.Builder()
        .setAzimuth(Measure.valueOf(entity.getAzimuth(), NonSI.DEGREE_ANGLE))
        .setElevation(Measure.valueOf(entity.getElevation(), NonSI.FOOT))
        .setPoint(factory.createPoint(new Coordinate(entity.getLongitude(), entity.getLatitude())))
        .build();
      ModelPoint mp = new ModelPoint(modelPointId, weatherStationId, lineId, geometry);
      modelPoints.add(mp);
    }
    ModelPoints result = new DefaultModelPoints(modelPoints);
    return result;
  }

}
