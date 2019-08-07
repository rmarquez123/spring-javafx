package com.rm.springjavafx.menu;

import java.lang.annotation.Annotation;
import java.util.Objects;
import javafx.scene.input.KeyCode;

/**
 *
 * @author Ricardo Marquez
 */
public class FxMenuItemImpl implements FxMenuItem {

  private final String id;
  private final String fxml;
  private final FxMenuItem item;
  
  private FxMenuItemImpl(FxMenuItem item) {
    this.item = item;
    this.id = item.id();
    this.fxml = item.fxml();
  }

  @Override
  public String fxml() {
    return this.item.fxml();
  }

  @Override
  public String id() {
    return this.item.id();
  }

  @Override
  public KeyCode code() {
    return this.item.code();
  }

  @Override
  public KeyModifier modifier() {
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Class<? extends Annotation> annotationType() {
    //To change body of generated methods, choose Tools | Templates.
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 29 * hash + Objects.hashCode(this.id);
    hash = 29 * hash + Objects.hashCode(this.fxml);
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
    final FxMenuItemImpl other = (FxMenuItemImpl) obj;
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    if (!Objects.equals(this.fxml, other.fxml)) {
      return false;
    }
    return true;
  }


  
  
  /**
   * 
   * @param item
   * @return 
   */
  public static FxMenuItemImpl wrap(FxMenuItem item) {
    return new FxMenuItemImpl(item);
  }
}
