package com.windsim.wpls.db.entities;

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
@Table(name = "station_record")
@NamedQueries({
  @NamedQuery(name = "StationRecordEntity.findAll", query = "SELECT w FROM StationRecordEntity w"),
  @NamedQuery(name = "StationRecordEntity.findByStnId", query = "SELECT w FROM StationRecordEntity w where w.id.stnId = :stnId"), 
  @NamedQuery(name = "StationRecordEntity.findByStnName", query = "SELECT w FROM StationRecordEntity w, StationEntity s where w.id.stnId = s.stationId and s.stationName = :stnName")
})
public class StationRecordEntity implements Serializable {

  @EmbeddedId
  @Basic(optional = false)
  private StationRecordEntityId id;
  @Column(name = "temperature")
  private Double temperature;
  @Column(name = "wind_speed")
  private Double windSpeed;
  @Column(name = "wind_dir")
  private Double windDir;

  public StationRecordEntity() {
  }

  public StationRecordEntityId getId() {
    return id;
  }

  public void setId(StationRecordEntityId id) {
    this.id = id;
  }
  

  public Double getTemperature() {
    return temperature;
  }

  public void setTemperature(Double temperature) {
    this.temperature = temperature;
  }

  public Double getWindSpeed() {
    return windSpeed;
  }

  public void setWindSpeed(Double windSpeed) {
    this.windSpeed = windSpeed;
  }

  public Double getWindDir() {
    return windDir;
  }

  public void setWindDir(Double windDir) {
    this.windDir = windDir;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 59 * hash + Objects.hashCode(this.id);
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
    final StationRecordEntity other = (StationRecordEntity) obj;
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "WeatherRecordEntity{" + "id=" + id + ", temperature=" + temperature + ", windSpeed=" + windSpeed + ", windDir=" + windDir + '}';
  }

}
