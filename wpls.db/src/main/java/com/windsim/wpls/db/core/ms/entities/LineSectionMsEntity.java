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
@Table(name = "linesection")
@NamedQueries({
  @NamedQuery(name = "LineSectionMsEntity.findAll", query = "SELECT s FROM LineSectionMsEntity s")})
public class LineSectionMsEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "linesection_id")
  private String linesection_id;
  @Column(name = "conductor_id")
  private String conductor_id;
  @Column(name = "airquality")
  private Integer airquality;
  @Column(name = "derating")
  private Integer derating;
  @Column(name = "bundle")
  private Integer bundle;
  @Column(name = "temperature_max")
  private Double temperatureMax;
  @Column(name = "temperature_emergency")
  private Double temperatureEmergency;
  @Column(name = "absorptivity")
  private Double absorptivity;
  @Column(name = "emissivity")
  private Double emissivity;

  public LineSectionMsEntity() {
  }

  public String getLinesection_id() {
    return linesection_id;
  }

  public void setLinesection_id(String linesection_id) {
    this.linesection_id = linesection_id;
  }

  public String getConductor_id() {
    return conductor_id;
  }

  public void setConductor_id(String conductor_id) {
    this.conductor_id = conductor_id;
  }

  public Integer getAirquality() {
    return airquality;
  }

  public void setAirquality(Integer airquality) {
    this.airquality = airquality;
  }

  public Integer getDerating() {
    return derating;
  }

  public void setDerating(Integer derating) {
    this.derating = derating;
  }

  public Integer getBundle() {
    return bundle;
  }

  public void setBundle(Integer bundle) {
    this.bundle = bundle;
  }

  public Double getTemperatureMax() {
    return temperatureMax;
  }

  public void setTemperatureMax(Double temperatureMax) {
    this.temperatureMax = temperatureMax;
  }

  public Double getTemperatureEmergency() {
    return temperatureEmergency;
  }

  public void setTemperatureEmergency(Double temperatureEmergency) {
    this.temperatureEmergency = temperatureEmergency;
  }

  public Double getAbsorptivity() {
    return absorptivity;
  }

  public void setAbsorptivity(Double absorptivity) {
    this.absorptivity = absorptivity;
  }

  public Double getEmissivity() {
    return emissivity;
  }

  public void setEmissivity(Double emissivity) {
    this.emissivity = emissivity;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 89 * hash + Objects.hashCode(this.linesection_id);
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
    final LineSectionMsEntity other = (LineSectionMsEntity) obj;
    if (!Objects.equals(this.linesection_id, other.linesection_id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "LineSectionEntity{" + "linesection_id=" + linesection_id + ", conductor_id=" + conductor_id + ", airquality=" + airquality + ", derating=" + derating + ", bundle=" + bundle + ", temperatureMax=" + temperatureMax + ", temperatureEmergency=" + temperatureEmergency + ", absorptivity=" + absorptivity + ", emissivity=" + emissivity + '}';
  }

  
  
  
  
}
