package com.windsim.wpls.db.ms;

import com.windsim.wpls.db.core.ms.entities.LineMsEntity;
import com.windsim.wpls.db.core.ms.entities.ModelPointMsEntity;
import com.windsim.wpls.db.core.ms.entities.WeatherStationMsEntity;
import gov.inl.glass3.linesolver.impl.files.FilesModelPointsLoader;
import gov.inl.glass3.modelpoints.ModelPoint;
import gov.inl.glass3.modelpoints.ModelPoints;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.measure.unit.NonSI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Ricardo Marquez
 */
public class ImportModelPoints {

  public static void main(String[] args) throws Exception {
    String file = "C:\\Data\\Test\\glass_noaa\\NOAAConfigurationWithCorrectWeatherStations.glass";
    InputStream inputStream = new FileInputStream(file);
    FilesModelPointsLoader pointsLoader = new FilesModelPointsLoader(inputStream, "ModelPoint");
    ModelPoints modelPoints = pointsLoader.loadModelPoints();
    Map<String, String> credentials = new HashMap<>();
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("wpls_idaho_power_pu_ms", credentials);
    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();
      for (ModelPoint modelPoint : modelPoints) {
        LineMsEntity line = new LineMsEntity();
        line.setLineId(modelPoint.getModelPointId());
        line.setLineSectionId(modelPoint.getLineId());
        
        if (em.find(LineMsEntity.class, line.getLineId()) == null) {
          em.persist(line);
        }
      }
      em.getTransaction().commit();
      em.getTransaction().begin();
      for (ModelPoint modelPoint : modelPoints) {
        ModelPointMsEntity entity = new ModelPointMsEntity();
        entity.setLineId(modelPoint.getModelPointId());
        WeatherStationMsEntity singleResult = em.createNamedQuery("WeatherStationMsEntity.findByName", WeatherStationMsEntity.class)
          .setParameter("name", modelPoint.getWeatherStationId()).getSingleResult();
        entity.setWeatherStationId(singleResult.getWeatherStationId());
        entity.setElevation(modelPoint.getGeometry().getElevation().doubleValue(NonSI.FOOT));
        entity.setLatitude(modelPoint.getGeometry().getPoint().getY());
        entity.setLongitude(modelPoint.getGeometry().getPoint().getX());
        entity.setAzimuth(modelPoint.getGeometry().getAzimuth().doubleValue(NonSI.DEGREE_ANGLE));
        if (em.find(ModelPointMsEntity.class, entity.getLineId()) == null) {
          em.persist(entity);
        }
      }
      em.getTransaction().commit();
    } finally {
      em.close();
    }
  }

}
