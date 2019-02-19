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
@Table(name = "line")
@NamedQueries({
  @NamedQuery(name = "LineMsEntity.findAll", query = "SELECT s FROM LineMsEntity s"),  
})
public class LineMsEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "line_id")
  private String lineId;
  @Column(name = "linesection_id")
  private String lineSectionId;
  
  public LineMsEntity() {
  }

  public String getLineId() {
    return lineId;
  }

  public void setLineId(String lineId) {
    this.lineId = lineId;
  }

  public String getLineSectionId() {
    return lineSectionId;
  }

  public void setLineSectionId(String lineSectionId) {
    this.lineSectionId = lineSectionId;
  }


  @Override
  public int hashCode() {
    int hash = 3;
    hash = 89 * hash + Objects.hashCode(this.lineId);
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
    final LineMsEntity other = (LineMsEntity) obj;
    if (!Objects.equals(this.lineId, other.lineId)) {
      return false;
    }
    return true;
  }

  
  
  @Override
  public String toString() {
    return "LineEntity{" + "conductorId=" + lineId + ", type=" + lineSectionId + '}';
  }
  
  
  

}
