package com.windsim.wpls.plsetup.impl.pg;

import com.rm.datasources.DbConnection;
import com.rm.springjavafx.properties.DateRange;
import com.rm.wpls.powerline.WindRecord;
import com.rm.wpls.powerline.WindRecords;
import common.types.DateTimeRange;
import gov.inl.glass3.weather.WeatherStation;
import gov.inl.glass3.weather.WeatherStations;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import org.apache.commons.lang3.mutable.MutableObject;

/**
 *
 * @author Ricardo Marquez
 */
public class PgWeatherRecords extends WindRecords {

  private final DbConnection connection;
  private final DateTimeRange dateTimeRange;

  /**
   *
   * @param connection
   * @param stations
   */
  public PgWeatherRecords(DbConnection connection, WeatherStations stations, DateTimeRange dateTimeRange) {
    super(stations);
    this.connection = connection;
    this.dateTimeRange = dateTimeRange;
  }

  @Override
  public synchronized void forEachRecord(WeatherStation station, Consumer<WindRecord> consumer) {
    List<WindRecord> records = this.loadRecords(station);
    for (WindRecord record : records) {
      consumer.accept(record);
    }
  }

  @Override
  public DateRange getDateRange(WeatherStation station) {
    MutableObject<DateRange> result = new MutableObject<>(DateRange.Empty());
    String format = "YYYY/MM/DD HH24:MI";
    DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    String sql = " select\n"
      + " 	st.station_name as name,\n"
      + "    min(rec.local_timestamp) as start_dt, \n"
      + "    max(rec.local_timestamp) as end_dt \n"
      + " from station_record rec\n"
      + " join station st\n"
      + " on st.station_id = rec.stn_id\n"
      + " where st.station_name = '" + station.getName() + "'\n"
      + " and rec.local_timestamp between "
      + "   to_timestamp('" + df.format(Date.from(this.dateTimeRange.getStartDate().toInstant())) + "', '" + format + "') "
      + "   and to_timestamp('" + df.format(Date.from(this.dateTimeRange.getEndDate().toInstant())) + "', '" + format + "') "
      + " group by st.station_name";
    this.connection.executeQuery(sql, (rs) -> {
      try {
        String name = rs.getString("name");
        Date startDt = rs.getDate("start_dt");
        Date endDt = rs.getDate("end_dt");
        result.setValue(new DateRange(startDt, endDt));
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    });
    return result.getValue();
  }

  /**
   *
   * @param station
   * @return
   */
  private List<WindRecord> loadRecords(WeatherStation station) {
    List<WindRecord> result = new ArrayList<>();
    String format = "YYYY/MM/DD HH24:MI";
    DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    String sql = " select\n"
      + " 	st.station_name as name,\n"
      + "   date_trunc('hour', rec.local_timestamp) \n"
      + "    + (date_part('minute', rec.local_timestamp)::int + 15) / 15 * interval '15 min' as timestamp, \n"
      + "    avg(rec.wind_speed) as wind_speed,\n"
      + "    avg(rec.wind_dir) as wind_dir\n"
      + " from station_record rec\n"
      + " join station st\n"
      + " on st.station_id = rec.stn_id\n"
      + " where st.station_name = '" + station.getName() + "'\n"
      + " and rec.local_timestamp between "
      + "   to_timestamp('" + df.format(Date.from(this.dateTimeRange.getStartDate().toInstant())) + "', '" + format + "') "
      + "   and to_timestamp('" + df.format(Date.from(this.dateTimeRange.getEndDate().toInstant())) + "', '" + format + "') \n"
      + " group by timestamp, name\n"
      + " order by timestamp";
    
   this.connection.executeQuery(sql, (rs) -> {
      try {
        String name = rs.getString("name");
        Date date = rs.getTimestamp("timestamp");
        Double windSpeed = rs.getDouble("wind_speed");
        Double windDir = rs.getDouble("wind_dir");
        WindRecord record = new WindRecord(name, date, windSpeed, windDir);
        result.add(record);
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    });
    return result;
  }

}
