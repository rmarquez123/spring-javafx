package com.rm.springjavafx.project;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Ricardo Marquez
 */
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ProjectSetting {
  String folder();
  
  public static class Utils {

    public static Serializable serialize(Object object) {
      //To change body of generated methods, choose Tools | Templates.
      throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void deserialize(Serializable serializable) {
      //To change body of generated methods, choose Tools | Templates.
      throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
  }
}
