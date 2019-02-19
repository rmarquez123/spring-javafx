package com.windsim.wpls.db.entities;

import com.vividsolutions.jts.geom.Point;
import java.io.Serializable;
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
@Table(name = "station")
@NamedQueries({
  @NamedQuery(name = "StationEntity.findAll", query = "SELECT s FROM StationEntity s")})
public class StationEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  @Basic(optional = false)
  @Column(name = "station_id")
  private Integer stationId;
  @Column(name = "station_name")
  private String stationName;
  @Column(name = "geom")
  private Point geom;
  @Column(name = "elevation")
  private Double elevation;

  public StationEntity() {
  }

  public StationEntity(Integer stationId) {
    this.stationId = stationId;
  }

  public Integer getStationId() {
    return stationId;
  }

  public void setStationId(Integer stationId) {
    this.stationId = stationId;
  }

  public String getStationName() {
    return stationName;
  }

  public void setStationName(String stationName) {
    this.stationName = stationName;
  }

  public Point getGeom() {
    return geom;
  }

  public void setGeom(Point geom) {
    this.geom = geom;
  }

  public Double getElevation() {
    return elevation;
  }

  public void setElevation(Double elevation) {
    this.elevation = elevation;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (stationId != null ? stationId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof StationEntity)) {
      return false;
    }
    StationEntity other = (StationEntity) object;
    if ((this.stationId == null && other.stationId != null) || (this.stationId != null && !this.stationId.equals(other.stationId))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.windsim.wpls.db.entities.StationEntity[ stationId=" + stationId + " ]";
  }

}
