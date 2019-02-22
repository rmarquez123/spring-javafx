package com.rm.wpls.powerline;

import java.util.Date;
import java.util.Objects;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 *
 * @author Ricardo Marquez
 */
public class WindRecord {
  
  private final String name;
  private final Date date;
  private final Double windSpeed;
  private final Double windDir;

  public WindRecord(String name, Date date, Double windSpeed, Double windDir) {
    this.name = name;
    this.date = date;
    this.windSpeed = windSpeed;
    this.windDir = windDir;
  }

  public String getName() {
    return name;
  }

  public Date getDate() {
    return date;
  }

  public Double getWindSpeed() {
    return windSpeed;
  }

  public Double getWindDir() {
    return windDir;
  }
  
  /**
   * 
   * @param other
   * @return 
   */
  public double vectorSquaredDifference(WindRecord other) {
    Vector2D v1 = this.toVector(); 
    Vector2D v2 = other.toVector();
    Vector2D diff = v1.subtract(v2); 
    double result = diff.getNormSq(); 
    return result; 
  }

  private Vector2D toVector() {
    return new Vector2D(this.windSpeed*Math.cos(this.windDir), this.windSpeed*Math.sin(this.windDir));
  }
  
  
  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.name);
    hash = 97 * hash + Objects.hashCode(this.date);
    return hash;
  } 
  
  /**
   * 
   * @param obj
   * @return 
   */
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
    final WindRecord other = (WindRecord) obj;
    if (!Objects.equals(this.name, other.name)) {
      return false;
    }
    if (!Objects.equals(this.date, other.date)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "WeatherRecord{" + "name=" + name + ", date=" + date + ", windSpeed=" + windSpeed + ", windDir=" + windDir + '}';
  }

  
  
  
  
}
