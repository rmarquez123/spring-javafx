package com.windsim.wpls.db.core.pg.entities;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Ricardo Marquez
 */
@Embeddable
public class WeatherRecordPgEntityId  implements Serializable{
  
  private static final long serialVersionUID = 1L;
  
  @Basic(optional = false)
  @Column(name = "weatherstation_id")
  private Integer stnId;

  @Basic(optional = false)
  @Column(name = "zoneddatetime")
  private OffsetDateTime date;

  public WeatherRecordPgEntityId() {
  }

  public Integer getStnId() {
    return stnId;
  }

  public void setStnId(Integer stnId) {
    this.stnId = stnId;
  }

  public OffsetDateTime getDate() {
    return date;
  }

  public void setDate(OffsetDateTime date) {
    this.date = date;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 11 * hash + Objects.hashCode(this.stnId);
    hash = 11 * hash + Objects.hashCode(this.date);
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
    final WeatherRecordPgEntityId other = (WeatherRecordPgEntityId) obj;
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
