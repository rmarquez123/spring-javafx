package com.rm._springjavafx.ignore.textfields;

/**
 *
 * @author Ricardo Marquez
 */
public class Record {
  private final int id;
  private final double number;
  private final String string;
  
  
  public Record(int id, double number, String string) {
    this.id = id;
    this.number = number;
    this.string = string;
  }

  public double number() {
    return number;
  }

  public String string() {
    return string;
  }

  public int id() {
    return id;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 41 * hash + this.id;
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
    final Record other = (Record) obj;
    if (this.id != other.id) {
      return false;
    }
    return true;
  }
  
  @Override
  public String toString() {
    return "{" + "id=" + id + ", number=" + number + ", string=" + string + '}';
  }
  
  
  
}
