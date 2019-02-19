package com.windsim.wpls.db.core.ms.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Ricardo Marquez
 */
@Entity
@Table(name = "modelpoint")
@NamedQueries({
  @NamedQuery(name = "ModelPointMsEntity.findAll", query = "SELECT s FROM ModelPointMsEntity s"), 
  @NamedQuery(name = "ModelPointMsEntity.findAllWithJoin1", 
    query = "SELECT s, l.linesection_id, w.weatherStationName FROM ModelPointMsEntity s, LineMsEntity le, LineSectionMsEntity l, WeatherStationMsEntity w where s.lineId = le.lineId and le.lineSectionId = l.linesection_id and w.weatherStationId = s.weatherStationId"), 
})
public class ModelPointMsEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "line_id")
  private String lineId;
  @Column(name = "weatherstation_id")
  private Integer weatherStationId;
  @Column(name = "elevation")
  private Double elevation;
  @Column(name = "azimuth")
  private Double azimuth;
  @Column(name = "latitude")
  private Double latitude;
  @Column(name = "longitude")
  private Double longitude;

  public ModelPointMsEntity() {
  }

  public String getLineId() {
    return lineId;
  }

  public void setLineId(String lineId) {
    this.lineId = lineId;
  }

  public Integer getWeatherStationId() {
    return weatherStationId;
  }

  public void setWeatherStationId(Integer weatherStationId) {
    this.weatherStationId = weatherStationId;
  }

  public Double getElevation() {
    return elevation;
  }

  public void setElevation(Double elevation) {
    this.elevation = elevation;
  }

  public Double getAzimuth() {
    return azimuth;
  }

  public void setAzimuth(Double azimuth) {
    this.azimuth = azimuth;
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

  
  
  @Override
  public int hashCode() {
    int hash = 5;
    hash = 17 * hash + Objects.hashCode(this.lineId);
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
    final ModelPointMsEntity other = (ModelPointMsEntity) obj;
    if (!Objects.equals(this.lineId, other.lineId)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "ModelPointEntity{" + "lineId=" + lineId + ", weatherStationId=" + weatherStationId + ", elevation=" + elevation + ", azimuth=" + azimuth + ", point=" + latitude + '}';
  }
  
}
