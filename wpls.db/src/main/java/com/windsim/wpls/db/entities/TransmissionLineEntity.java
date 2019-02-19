/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.windsim.wpls.db.entities;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "electric_power_transmission_lines")
@NamedQueries({
  @NamedQuery(name = "TransmissionLineEntity.findAll", query = "SELECT t FROM TransmissionLineEntity t")})
public class TransmissionLineEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "gid")
  private Integer gid;
  @Column(name = "objectid")
  private BigInteger objectid;
  @Size(max = 80)
  @Column(name = "id")
  private String id;
  @Size(max = 80)
  @Column(name = "type")
  private String type;
  @Size(max = 80)
  @Column(name = "status")
  private String status;
  @Size(max = 80)
  @Column(name = "naics_code")
  private String naicsCode;
  @Size(max = 80)
  @Column(name = "naics_desc")
  private String naicsDesc;
  @Size(max = 237)
  @Column(name = "source")
  private String source;
  @Size(max = 80)
  @Column(name = "sourcedate")
  private String sourcedate;
  @Size(max = 80)
  @Column(name = "val_method")
  private String valMethod;
  @Size(max = 80)
  @Column(name = "val_date")
  private String valDate;
  @Size(max = 80)
  @Column(name = "owner")
  private String owner;
  @Column(name = "voltage")
  private BigInteger voltage;
  @Size(max = 80)
  @Column(name = "volt_class")
  private String voltClass;
  @Size(max = 80)
  @Column(name = "inferred")
  private String inferred;
  @Size(max = 80)
  @Column(name = "sub_1")
  private String sub1;
  @Size(max = 80)
  @Column(name = "sub_2")
  private String sub2;
  @Column(name = "shape__len")
  private BigInteger shapeLen;
  @Lob
  @Column(name = "geom")
  private Object geom;

  public TransmissionLineEntity() {
  }

  public TransmissionLineEntity(Integer gid) {
    this.gid = gid;
  }

  public Integer getGid() {
    return gid;
  }

  public void setGid(Integer gid) {
    this.gid = gid;
  }

  public BigInteger getObjectid() {
    return objectid;
  }

  public void setObjectid(BigInteger objectid) {
    this.objectid = objectid;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getNaicsCode() {
    return naicsCode;
  }

  public void setNaicsCode(String naicsCode) {
    this.naicsCode = naicsCode;
  }

  public String getNaicsDesc() {
    return naicsDesc;
  }

  public void setNaicsDesc(String naicsDesc) {
    this.naicsDesc = naicsDesc;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getSourcedate() {
    return sourcedate;
  }

  public void setSourcedate(String sourcedate) {
    this.sourcedate = sourcedate;
  }

  public String getValMethod() {
    return valMethod;
  }

  public void setValMethod(String valMethod) {
    this.valMethod = valMethod;
  }

  public String getValDate() {
    return valDate;
  }

  public void setValDate(String valDate) {
    this.valDate = valDate;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public BigInteger getVoltage() {
    return voltage;
  }

  public void setVoltage(BigInteger voltage) {
    this.voltage = voltage;
  }

  public String getVoltClass() {
    return voltClass;
  }

  public void setVoltClass(String voltClass) {
    this.voltClass = voltClass;
  }

  public String getInferred() {
    return inferred;
  }

  public void setInferred(String inferred) {
    this.inferred = inferred;
  }

  public String getSub1() {
    return sub1;
  }

  public void setSub1(String sub1) {
    this.sub1 = sub1;
  }

  public String getSub2() {
    return sub2;
  }

  public void setSub2(String sub2) {
    this.sub2 = sub2;
  }

  public BigInteger getShapeLen() {
    return shapeLen;
  }

  public void setShapeLen(BigInteger shapeLen) {
    this.shapeLen = shapeLen;
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
    if (!(object instanceof TransmissionLineEntity)) {
      return false;
    }
    TransmissionLineEntity other = (TransmissionLineEntity) object;
    if ((this.gid == null && other.gid != null) || (this.gid != null && !this.gid.equals(other.gid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.windsim.wpls.db.entities.TransmissionLineEntity[ gid=" + gid + " ]";
  }
  
}
