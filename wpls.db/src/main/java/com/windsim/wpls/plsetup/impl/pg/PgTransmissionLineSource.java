package com.windsim.wpls.plsetup.impl.pg;

import com.rm.datasources.DbConnection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.rm.wpls.powerline.TransmissionLine;
import com.rm.wpls.powerline.TransmissionLines;
import com.rm.wpls.powerline.setup.TransmissionLineSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ricardo Marquez
 */
public class PgTransmissionLineSource implements TransmissionLineSource {

  private final DbConnection dbConnection;

  /**
   *
   * @param dbConnection
   */
  public PgTransmissionLineSource(DbConnection dbConnection) {
    this.dbConnection = dbConnection;
  }

  /**
   *
   * @param filter
   * @return
   */
  @Override
  public TransmissionLines getTransmissionLines(int srid, TransmissionLines.Filter filter) {
    Connection connection = this.dbConnection.getConnection();
    String sqlQuery = this.createQueryString(srid, filter);
    TransmissionLines result;
    try {
      PreparedStatement statement = connection.prepareStatement(sqlQuery);
      ResultSet resultSet = statement.executeQuery();
      result = this.extractResultSet(resultSet);
    } catch (SQLException | ParseException ex) {
      throw new RuntimeException(ex);
    } finally {
      try {
        connection.close();
      } catch (SQLException ex) {
        Logger.getLogger(PgTransmissionLineSource.class.getName())
          .log(Level.SEVERE, null, ex);
      }
    }
    return result;
  }

  /**
   *
   * @param resultSet
   * @param wkbReader
   * @return
   * @throws ParseException
   * @throws SQLException
   */
  private TransmissionLines extractResultSet(ResultSet resultSet)
    throws ParseException, SQLException {
    
    TransmissionLines result;
    WKBReader wkbReader = this.createWkbReader();
    List<TransmissionLine> lines = new ArrayList<>();
    while (resultSet.next()) {
      String lineName = "line_" + resultSet.getInt("id");
      byte[] geomResult = resultSet.getBytes("geom");
      LineString geom = (LineString) wkbReader.read(geomResult);
      TransmissionLine line = new TransmissionLine(lineName, geom);
      lines.add(line);
    }
    result = new TransmissionLines(lines);
    return result;
  }

  /**
   *
   * @return
   */
  private WKBReader createWkbReader() {
    int srid = 4326;
    PrecisionModel.Type modelType = PrecisionModel.FLOATING;
    PrecisionModel precisionModel = new PrecisionModel(modelType);
    GeometryFactory geomFactory = new GeometryFactory(precisionModel, srid);
    WKBReader wkbReader = new WKBReader(geomFactory);
    return wkbReader;
  }

  /**
   *
   * @param filter
   * @return
   */
  private String createQueryString(int srid, TransmissionLines.Filter filter) {
    String baseSqlQuery = "with new_york as (\n"
      + "	select \n"
      + "    	state.geom as geom\n"
      + "   	FROM cb_2016_us_state_500k state\n"
      + "    where state.NAME = 'New York'\n"
      + ") \n"
      + "select\n"
      + "	line.gid\n"
      + "    , line.objectid\n"
      + "    , line.id\n"
      + "    , line.owner\n"
      + "    , line.naics_code\n"
      + "    , line.status\n"
      + "    , st_asbinary(st_transform(st_linemerge(line.geom), " + srid + ")) as geom \n"
      + "\n"
      + "from electric_power_transmission_lines line\n"
      + "join new_york ny\n"
      + "	on st_contains(ny.geom, st_transform(line.geom, st_srid(ny.geom))) \n";
    String whereClause = "where";
    if (filter.getMinRatedVoltage() != null) {
      String toAppend = " line.voltage > " + filter.getMinRatedVoltage();
      whereClause = addToWhereClause(whereClause, toAppend);
    }
    whereClause = addToWhereClause(whereClause, " st_numgeometries(line.geom) = 1");
    String sqlQuery = baseSqlQuery + whereClause;
    return sqlQuery;
  }

  /**
   *
   * @param whereClause
   * @param toAppend
   * @return
   */
  private static String addToWhereClause(String whereClause, String toAppend) {
    String result = "where".equals(whereClause)
      ? whereClause + toAppend
      : whereClause + " and " + toAppend;
    return result;
  }
}
