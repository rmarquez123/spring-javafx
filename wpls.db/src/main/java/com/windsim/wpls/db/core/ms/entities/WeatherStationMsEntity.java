package com.windsim.wpls.db.core.ms.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Ricardo Marquez
 */
@Entity
@Table(name = "weatherstation")
@NamedQueries({
  @NamedQuery(name = "WeatherStationMsEntity.findAll", query = "SELECT s FROM WeatherStationMsEntity s"),
  @NamedQuery(name = "WeatherStationMsEntity.findByName", query = "SELECT s FROM WeatherStationMsEntity s where s.weatherStationName = :name")
})
public class WeatherStationMsEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(generator = "increment_weatherstation")
  @GenericGenerator(name = "increment_weatherstation", strategy = "increment")
  @Basic(optional = false)
  @Column(name = "weatherstation_id")
  private Integer weatherStationId;
  @Column(name = "weatherstation_name")
  private String weatherStationName;
  @Column(name = "latitude")
  private Double latitude;
  @Column(name = "longitude")
  private Double longitude;
  @Column(name = "elevation")
  private Double elevation;

  public WeatherStationMsEntity() {
  }

  public Integer getWeatherStationId() {
    return weatherStationId;
  }

  public void setWeatherStationId(Integer weatherStationId) {
    this.weatherStationId = weatherStationId;
  }

  public String getWeatherStationName() {
    return weatherStationName;
  }

  public void setWeatherStationName(String weatherStationName) {
    this.weatherStationName = weatherStationName;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }
  
  
  public Double getElevation() {
    return elevation;
  }

  public void setElevation(Double elevation) {
    this.elevation = elevation;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 37 * hash + Objects.hashCode(this.weatherStationId);
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
    final WeatherStationMsEntity other = (WeatherStationMsEntity) obj;
    if (!Objects.equals(this.weatherStationId, other.weatherStationId)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "WeatherStationEntity{" + "weatherStationId=" + weatherStationId + ", weatherStationName=" + weatherStationName + ", elevation=" + elevation + '}';
  }

}
