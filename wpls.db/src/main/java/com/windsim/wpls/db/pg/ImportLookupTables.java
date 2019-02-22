package com.windsim.wpls.db.pg;

import com.windsim.wpls.db.core.pg.entities.WindLookupPgEntity;
import gov.inl.glass3.linesolver.impl.files.FilesLookupTablesLoader;
import gov.inl.glass3.windmodel.LookupTable;
import gov.inl.glass3.windmodel.LookupTables;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Ricardo Marquez
 */
public class ImportLookupTables {

  public static void main(String[] args) throws Exception {
    String file = "C:\\Data\\Test\\glass_noaa\\NOAAConfigurationWithCorrectWeatherStations.glass";
    InputStream inputStream = new FileInputStream(file);
    FilesLookupTablesLoader loader = new FilesLookupTablesLoader(inputStream, "LookUpTable");
    LookupTables lookupTables = loader.loadLookupTables();
    Map<String, String> credentials = new HashMap<>();
    credentials.put("hibernate.connection.url", "jdbc:postgresql://localhost:5432/idaho_power");
    credentials.put("hibernate.connection.username", "postgres");
    credentials.put("hibernate.connection.password", "postgres");
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("wpls_transmission_pu", credentials);
    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();
      for (LookupTable lookupTable : lookupTables) {
        WindLookupPgEntity entity = new WindLookupPgEntity();
        entity.setModelPointId(lookupTable.getModelPointId());
        entity.setSectorNum((int) lookupTable.getSectorNum());
        entity.setSpeedUp(lookupTable.getSpeedUp());
        entity.setDirectionShift(lookupTable.getDirectionShift());
        boolean contains;
        try {
          em.createNamedQuery("WindLookupEntity.findByModelPointIdAndSectorNum", WindLookupPgEntity.class)
            .setParameter("modelpoint_id", entity.getModelPointId())
            .setParameter("sector_num", entity.getSectorNum())
            .getSingleResult();
          contains = true;
        } catch (javax.persistence.NoResultException ex) {
          contains = false;
        }

        if (!contains) {
          em.persist(entity);
        }
      }
      em.getTransaction().commit();
    } finally {
      em.close();
    }
  }

}
