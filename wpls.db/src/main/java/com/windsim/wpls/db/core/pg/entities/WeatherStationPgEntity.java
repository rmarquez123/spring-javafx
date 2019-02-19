package com.windsim.wpls.db.core.pg.entities;

import com.vividsolutions.jts.geom.Geometry;
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
  @NamedQuery(name = "WeatherStationPgEntity.findAll", query = "SELECT s FROM WeatherStationPgEntity s"),
  @NamedQuery(name = "WeatherStationPgEntity.findByName", query = "SELECT s FROM WeatherStationPgEntity s where s.weatherStationName = :name")
})
public class WeatherStationPgEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(generator = "increment_weatherstation")
  @GenericGenerator(name = "increment_weatherstation", strategy = "increment")
  @Basic(optional = false)
  @Column(name = "weatherstation_id")
  private Integer weatherStationId;
  @Column(name = "weatherstation_name")
  private String weatherStationName;
  @Column(name = "point")
  private Geometry point;
  @Column(name = "elevation")
  private Double elevation;

  public WeatherStationPgEntity() {
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

  public Geometry getPoint() {
    return point;
  }

  public void setPoint(Geometry point) {
    this.point = point;
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
    final WeatherStationPgEntity other = (WeatherStationPgEntity) obj;
    if (!Objects.equals(this.weatherStationId, other.weatherStationId)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "WeatherStationEntity{" + "weatherStationId=" + weatherStationId + ", weatherStationName=" + weatherStationName + ", point=" + point + ", elevation=" + elevation + '}';
  }

}
