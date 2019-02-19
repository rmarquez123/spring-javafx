package com.windsim.wpls.db.core.pg.entities;

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
@Table(name = "windlookup")
@NamedQueries({
  @NamedQuery(name = "WindLookupPgEntity.findAll", query = "SELECT s FROM WindLookupPgEntity s"), 
  @NamedQuery(name = "WindLookupPgEntity.findByModelPointIdAndSectorNum", 
    query = "SELECT s FROM WindLookupPgEntity s where s.modelPointId = :modelpoint_id and s.sectorNum = :sector_num"), 
})
public class WindLookupPgEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(generator = "increment_lookup_id")
  @GenericGenerator(name = "increment_lookup_id", strategy = "increment")
  @Basic(optional = false)
  @Column(name = "lookup_id")
  private Integer lookupId;
  @Column(name = "modelpoint_id")
  private String modelPointId;
  @Column(name = "sector_num")
  private Integer sectorNum;
  @Column(name = "speedup")
  private Double speedUp;
  @Column(name = "direction_shift")
  private Double directionShift;

  public WindLookupPgEntity() {
  }

  public Integer getLookupId() {
    return lookupId;
  }

  public void setLookupId(Integer lookupId) {
    this.lookupId = lookupId;
  }

  public String getModelPointId() {
    return modelPointId;
  }

  public void setModelPointId(String modelPointId) {
    this.modelPointId = modelPointId;
  }

  public Integer getSectorNum() {
    return sectorNum;
  }

  public void setSectorNum(Integer sectorNum) {
    this.sectorNum = sectorNum;
  }

  public Double getSpeedUp() {
    return speedUp;
  }

  public void setSpeedUp(Double speedUp) {
    this.speedUp = speedUp;
  }

  public Double getDirectionShift() {
    return directionShift;
  }

  public void setDirectionShift(Double directionShift) {
    this.directionShift = directionShift;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 47 * hash + Objects.hashCode(this.lookupId);
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
    final WindLookupPgEntity other = (WindLookupPgEntity) obj;
    if (!Objects.equals(this.lookupId, other.lookupId)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "WindLookup{" + "lookupId=" + lookupId + ", modelPointId=" + modelPointId + ", sectorNum=" + sectorNum + ", speedUp=" + speedUp + ", directionShift=" + directionShift + '}';
  }

}
