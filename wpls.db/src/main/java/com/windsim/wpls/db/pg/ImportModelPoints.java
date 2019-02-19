package com.windsim.wpls.db.pg;

import com.windsim.wpls.db.core.pg.entities.LinePgEntity;
import com.windsim.wpls.db.core.pg.entities.ModelPointPgEntity;
import com.windsim.wpls.db.core.pg.entities.WeatherStationPgEntity;
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
    FilesModelPointsLoader pointsLoader = new FilesModelPointsLoader(inputStream);
    ModelPoints modelPoints = pointsLoader.loadModelPoints();
    Map<String, String> credentials = new HashMap<>();
    credentials.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/idaho_power");
    credentials.put("hibernate.connection.username", "postgres");
    credentials.put("hibernate.connection.password", "postgres");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("wpls_transmission_pu", credentials);
    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();
      for (ModelPoint modelPoint : modelPoints) {
        LinePgEntity line = new LinePgEntity();
        line.setLineId(modelPoint.getModelPointId());
        line.setLineSectionId(modelPoint.getLineId());
        line.setGeom(null);
        if (em.find(LinePgEntity.class, line.getLineId()) == null) {
          em.persist(line);
        }
      }
      em.getTransaction().commit();
      em.getTransaction().begin();
      for (ModelPoint modelPoint : modelPoints) {
        ModelPointPgEntity entity = new ModelPointPgEntity();
        entity.setLineId(modelPoint.getModelPointId());
        WeatherStationPgEntity singleResult = em.createNamedQuery("WeatherStationEntity.findByName", WeatherStationPgEntity.class)
          .setParameter("name", modelPoint.getWeatherStationId()).getSingleResult();
        entity.setWeatherStationId(singleResult.getWeatherStationId());
        entity.setElevation(modelPoint.getGeometry().getElevation().doubleValue(NonSI.FOOT));
        entity.setPoint(modelPoint.getGeometry().getPoint());
        entity.setAzimuth(modelPoint.getGeometry().getAzimuth().doubleValue(NonSI.DEGREE_ANGLE));
        if (em.find(ModelPointPgEntity.class, entity.getLineId()) == null) {
          em.persist(entity);
        }
      }
      em.getTransaction().commit();
    } finally {
      em.close();
    }
  }

}
