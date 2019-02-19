package com.windsim.wpls.db.entities;

import com.windsim.wpls.db.store.ImportStationsToDb;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Ricardo Marquez
 */
public class ImportStationsToDbTest {
  
  
  @Test
  @Ignore
  public void insertStations() throws Exception  {
    ImportStationsToDb instance = new ImportStationsToDb();
    instance.importAllStations(); 
  }
}
