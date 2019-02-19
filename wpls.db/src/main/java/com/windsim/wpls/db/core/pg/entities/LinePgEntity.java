package com.windsim.wpls.db.core.pg.entities;

import com.vividsolutions.jts.geom.Geometry;
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
  @NamedQuery(name = "LinePgEntity.findAll", query = "SELECT s FROM LinePgEntity s"), 
  
})
public class LinePgEntity implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "line_id")
  private String lineId;
  @Column(name = "linesection_id")
  private String lineSectionId;
  @Column(name = "geom")
  private Geometry geom;

  public LinePgEntity() {
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

  public Geometry getGeom() {
    return geom;
  }

  public void setGeom(Geometry geom) {
    this.geom = geom;
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
    final LinePgEntity other = (LinePgEntity) obj;
    if (!Objects.equals(this.lineId, other.lineId)) {
      return false;
    }
    return true;
  }

  
  
  @Override
  public String toString() {
    return "LineEntity{" + "conductorId=" + lineId + ", type=" + lineSectionId + ", diameter=" + geom + '}';
  }
  
  
  

}
