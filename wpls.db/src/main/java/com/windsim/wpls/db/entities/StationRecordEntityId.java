package com.windsim.wpls.db.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;

/**
 *
 * @author Ricardo Marquez
 */
@Embeddable
public class StationRecordEntityId implements Serializable {

  private static final long serialVersionUID = 1L;
  
  @Basic(optional = false)
  @Column(name = "stn_id")
  private Integer stnId;
  
  @Basic(optional = false)
  @Column(name = "local_timestamp")
  @Temporal(javax.persistence.TemporalType.DATE)
  private Date date;
  
  public StationRecordEntityId() {
  }

  public Integer getStnId() {
    return stnId;
  }

  public void setStnId(Integer stnId) {
    this.stnId = stnId;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Date getDate() {
    return date;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 97 * hash + Objects.hashCode(this.stnId);
    hash = 97 * hash + Objects.hashCode(this.date);
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
    final StationRecordEntityId other = (StationRecordEntityId) obj;
    if (!Objects.equals(this.stnId, other.stnId)) {
      return false;
    }
    if (!Objects.equals(this.date, other.date)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "WeatherRecordEntityId{" + "stnId=" + stnId + ", date=" + date + '}';
  }
  
  
}
