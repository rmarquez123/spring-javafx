package com.windsim.wpls.db.linesolver.pg.impl;

import com.windsim.wpls.db.core.pg.entities.WindLookupPgEntity;
import gov.inl.glass3.linesolver.impl._default.DefaultLookupTables;
import gov.inl.glass3.linesolver.loaders.LookupTablesLoader;
import gov.inl.glass3.windmodel.LookupTable;
import gov.inl.glass3.windmodel.LookupTables;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

/**
 *
 * @author Ricardo Marquez
 */
public class PgDbLookupTablesLoader implements LookupTablesLoader{

  private final EntityManager em;
  
  public PgDbLookupTablesLoader(EntityManager em) {
    this.em = em;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public LookupTables loadLookupTables() {
    List<LookupTable> lookUpTables = this.em.createNamedQuery("WindLookupPgEntity.findAll", WindLookupPgEntity.class)
      .getResultList()
      .stream()
      .map((e)->{
        LookupTable record = new LookupTable.Builder()
          .setModelPointId(e.getModelPointId())
          .setSectorNum(e.getSectorNum())
          .setSpeedUp(e.getSpeedUp())
          .setDirectionShift(e.getDirectionShift())
          .build();
        return record;
      }).collect(Collectors.toList());
    return new DefaultLookupTables(lookUpTables);
  }
  
}
