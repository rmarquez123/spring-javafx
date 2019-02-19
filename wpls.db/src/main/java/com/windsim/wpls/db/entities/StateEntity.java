package com.windsim.wpls.db.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
/**
 *
 * @author Ricardo Marquez
 */
@Entity
@Table(name = "cb_2016_us_state_500k")
@NamedQueries({
  @NamedQuery(name = "StateEntity.findAll", query = "SELECT s FROM StateEntity s")})
public class StateEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "gid")
  private Integer gid;
  @Size(max = 2)
  @Column(name = "statefp")
  private String statefp;
  @Size(max = 8)
  @Column(name = "statens")
  private String statens;
  @Size(max = 11)
  @Column(name = "affgeoid")
  private String affgeoid;
  @Size(max = 2)
  @Column(name = "geoid")
  private String geoid;
  @Size(max = 2)
  @Column(name = "stusps")
  private String stusps;
  @Size(max = 100)
  @Column(name = "name")
  private String name;
  @Size(max = 2)
  @Column(name = "lsad")
  private String lsad;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Column(name = "aland")
  private Double aland;
  @Column(name = "awater")
  private Double awater;
  @Lob
  @Column(name = "geom")
  private Object geom;

  public StateEntity() {
  }

  public StateEntity(Integer gid) {
    this.gid = gid;
  }

  public Integer getGid() {
    return gid;
  }

  public void setGid(Integer gid) {
    this.gid = gid;
  }

  public String getStatefp() {
    return statefp;
  }

  public void setStatefp(String statefp) {
    this.statefp = statefp;
  }

  public String getStatens() {
    return statens;
  }

  public void setStatens(String statens) {
    this.statens = statens;
  }

  public String getAffgeoid() {
    return affgeoid;
  }

  public void setAffgeoid(String affgeoid) {
    this.affgeoid = affgeoid;
  }

  public String getGeoid() {
    return geoid;
  }

  public void setGeoid(String geoid) {
    this.geoid = geoid;
  }

  public String getStusps() {
    return stusps;
  }

  public void setStusps(String stusps) {
    this.stusps = stusps;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLsad() {
    return lsad;
  }

  public void setLsad(String lsad) {
    this.lsad = lsad;
  }

  public Double getAland() {
    return aland;
  }

  public void setAland(Double aland) {
    this.aland = aland;
  }

  public Double getAwater() {
    return awater;
  }

  public void setAwater(Double awater) {
    this.awater = awater;
  }

  public Object getGeom() {
    return geom;
  }

  public void setGeom(Object geom) {
    this.geom = geom;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (gid != null ? gid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof StateEntity)) {
      return false;
    }
    StateEntity other = (StateEntity) object;
    if ((this.gid == null && other.gid != null) || (this.gid != null && !this.gid.equals(other.gid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.windsim.wpls.db.entities.StateEntity[ gid=" + gid + " ]";
  }
  
}
