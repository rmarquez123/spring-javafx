package com.windsim.wpls.db.store;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.windsim.wpls.db.entities.StationEntity;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Ricardo Marquez
 */
public class ImportStationsToDb {
  public static final String PERSISTENCE_UNIT ="wpls_transmission_pu";
  private static final String STATIONS_FILE = "imported_data/SiteList.csv";
  
  /**
   * 
   */
  public ImportStationsToDb() {
  }

  /**
   *
   * @throws java.lang.Exception
   */
  public void importAllStations() throws Exception {
    InputStream resource = this.getClass().getClassLoader().getResourceAsStream(STATIONS_FILE);
    List<String> lines = IOUtils.readLines(resource, Charset.forName("UTF-8"));
    lines.remove(0); 
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
    EntityManager em = emf.createEntityManager();
    em.getTransaction().begin();
    for (String line : lines) {
      String parts[] = line.split(",", -1);
      if (!parts[8].isEmpty()) {
        double elevation = Double.parseDouble(parts[7]);
        double lat = Double.parseDouble(parts[6]);
        double lon = Double.parseDouble(parts[5]);
        int srid = 4326;
        PrecisionModel precisionModel = new PrecisionModel(PrecisionModel.FLOATING);
        Coordinate coordinate = new Coordinate(lat, lon);
        Point point = new GeometryFactory(precisionModel, srid).createPoint(coordinate);
        StationEntity station = new StationEntity();
        station.setStationName(parts[0]);
        station.setGeom(point);
        station.setElevation(elevation);
        em.persist(station);
        
      }
    }
    em.getTransaction().commit();
  }
  
}
