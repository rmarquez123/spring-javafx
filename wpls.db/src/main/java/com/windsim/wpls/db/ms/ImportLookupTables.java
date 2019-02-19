package com.windsim.wpls.db.ms;

import com.windsim.wpls.db.core.ms.entities.WindLookupMsEntity;
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
    FilesLookupTablesLoader loader = new FilesLookupTablesLoader(inputStream);
    LookupTables lookupTables = loader.loadLookupTables();
    Map<String, String> credentials = new HashMap<>();
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("wpls_idaho_power_pu_ms", credentials);
    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();
      for (LookupTable lookupTable : lookupTables) {
        WindLookupMsEntity entity = new WindLookupMsEntity();
        entity.setModelPointId(lookupTable.getModelPointId());
        entity.setSectorNum((int) lookupTable.getSectorNum());
        entity.setSpeedUp(lookupTable.getSpeedUp());
        entity.setDirectionShift(lookupTable.getDirectionShift());
        boolean contains;
        try {
          em.createNamedQuery("WindLookupMsEntity.findByModelPointIdAndSectorNum", WindLookupMsEntity.class)
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
