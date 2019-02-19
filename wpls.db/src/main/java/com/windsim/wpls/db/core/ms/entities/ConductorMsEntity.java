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
@Table(name = "conductor")
@NamedQueries({
  @NamedQuery(name = "ConductorMsEntity.findAll", query = "SELECT s FROM ConductorMsEntity s")})
public class ConductorMsEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "conductor_id")
  private String conductorId;
  @Column(name = "type")
  private String type;
  @Column(name = "diameter")
  private Double diameter;
  @Column(name = "temperature_min")
  private Double temperature_min;
  @Column(name = "temperature_max")
  private Double temperature_max;
  @Column(name = "resistance_min")
  private Double resistance_min;
  @Column(name = "resistance_max")
  private Double resistance_max;

  public ConductorMsEntity() {
  }
  
  
  
  public String getConductorId() {
    return conductorId;
  }

  public void setConductorId(String conductorId) {
    this.conductorId = conductorId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Double getDiameter() {
    return diameter;
  }

  public void setDiameter(Double diameter) {
    this.diameter = diameter;
  }

  public Double getTemperature_min() {
    return temperature_min;
  }

  public void setTemperature_min(Double temperature_min) {
    this.temperature_min = temperature_min;
  }

  public Double getTemperature_max() {
    return temperature_max;
  }

  public void setTemperature_max(Double temperature_max) {
    this.temperature_max = temperature_max;
  }

  public Double getResistance_min() {
    return resistance_min;
  }

  public void setResistance_min(Double resistance_min) {
    this.resistance_min = resistance_min;
  }

  public Double getResistance_max() {
    return resistance_max;
  }

  public void setResistance_max(Double resistance_max) {
    this.resistance_max = resistance_max;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 41 * hash + Objects.hashCode(this.conductorId);
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
    final ConductorMsEntity other = (ConductorMsEntity) obj;
    if (!Objects.equals(this.conductorId, other.conductorId)) {
      return false;
    }
    return true;
  }

  
  
  @Override
  public String toString() {
    return "ConductorEntity{" + "conductorId=" + conductorId + ", type=" + type + ", diameter=" + diameter + ", temperature_min=" + temperature_min + ", temperature_max=" + temperature_max + ", resistance_min=" + resistance_min + ", resistance_max=" + resistance_max + '}';
  }
  
  
}
