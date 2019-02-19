package com.windsim.wpls.db.core.pg.entities;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
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
  @NamedQuery(name = "ModelPointPgEntity.findAll", query = "SELECT s FROM ModelPointPgEntity s"), 
  @NamedQuery(name = "ModelPointPgEntity.findAllWithJoin1", 
    query = "SELECT s, l.linesection_id, w.weatherStationName FROM ModelPointPgEntity s, LinePgEntity le, LineSectionPgEntity l, WeatherStationPgEntity w where s.lineId = le.lineId and le.lineSectionId = l.linesection_id and w.weatherStationId = s.weatherStationId"), 
})
public class ModelPointPgEntity implements Serializable {

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
  @Column(name = "point")
  private Geometry point;

  public ModelPointPgEntity() {
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

  public Geometry getPoint() {
    return point;
  }

  public void setPoint(Point point) {
    this.point = point;
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
    final ModelPointPgEntity other = (ModelPointPgEntity) obj;
    if (!Objects.equals(this.lineId, other.lineId)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "ModelPointEntity{" + "lineId=" + lineId + ", weatherStationId=" + weatherStationId + ", elevation=" + elevation + ", azimuth=" + azimuth + ", point=" + point + '}';
  }
  
}
