package com.windsim.wpls.db.core.pg.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Ricardo Marquez
 */
@Entity
@Table(name = "weatherrecord")
@NamedQueries({
  @NamedQuery(name = "WeatherRecordPgEntity.findAll", query = "SELECT w FROM WeatherRecordPgEntity w"),
  @NamedQuery(name = "WeatherRecordPgEntity.findByStationAndDateRange", 
    query = "SELECT w, s.weatherStationName FROM WeatherRecordPgEntity w, WeatherStationPgEntity s where s.weatherStationId = w.id.stnId and s.weatherStationName = :name and w.id.date BETWEEN :startDt and :endDt "),
  @NamedQuery(name = "WeatherRecordPgEntity.findByStationsAndDateRange", 
    query = "SELECT w, s.weatherStationName FROM WeatherRecordPgEntity w, WeatherStationPgEntity s where s.weatherStationId = w.id.stnId and s.weatherStationName in :names and w.id.date BETWEEN :startDt and :endDt "),
})
public class WeatherRecordPgEntity implements Serializable{

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  @Basic(optional = false)
  private WeatherRecordPgEntityId id;
  @Column(name = "temperature")
  private Double temperature;
  @Column(name = "windspeed")
  private Double windSpeed;
  @Column(name = "winddir")
  private Double windDir;
  @Column(name = "solar")
  private Double solar;

  public WeatherRecordPgEntity() {
  }

  public void setId(WeatherRecordPgEntityId id) {
    this.id = id;
  }

  public void setTemperature(Double temperature) {
    this.temperature = temperature;
  }

  public void setWindSpeed(Double windSpeed) {
    this.windSpeed = windSpeed;
  }

  public void setWindDir(Double windDir) {
    this.windDir = windDir;
  }

  public void setSolar(Double solar) {
    this.solar = solar;
  }

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  public WeatherRecordPgEntityId getId() {
    return id;
  }

  public Double getTemperature() {
    return temperature;
  }

  public Double getWindSpeed() {
    return windSpeed;
  }

  public Double getWindDir() {
    return windDir;
  }

  public Double getSolar() {
    return solar;
  }

  
  
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.id);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final WeatherRecordPgEntity other = (WeatherRecordPgEntity) obj;
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "WeatherRecordEntity{" + "id=" + id + ", temperature=" + temperature + ", windSpeed=" + windSpeed + ", windDir=" + windDir + ", solar=" + solar + '}';
  }
  
  
}
