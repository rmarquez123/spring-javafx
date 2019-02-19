package com.windsim.wpls.db.entities;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Ricardo Marquez
 */
public class StationEntityTest {
  
  public StationEntityTest() {
  }
  
  @Before
  public void setUp() {
  }

  @Test
  public void testFindAll() {
    EntityManager em = Persistence.createEntityManagerFactory("wpls_transmission_pu")
      .createEntityManager(); 
    List<StationEntity> stations = em.createNamedQuery("StationEntity.findAll", StationEntity.class).getResultList();
    System.out.println(stations);
  }
  
}
