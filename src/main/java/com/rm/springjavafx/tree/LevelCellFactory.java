package com.rm.springjavafx.tree;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author rmarquez
 */
public class LevelCellFactory implements InitializingBean {
  
  private Integer level;
  private String textField; 
  
  @Required
  public void setLevel(Integer level) {
    this.level = level;
  }
    
  @Required
  public void setTextField(String textField) {
    this.textField = textField;
  }

  public Integer getLevel() {
    return level;
  }

  public String getTextField() {
    return textField;
  }
  
  
  /**
   * 
   * @throws Exception 
   */
  @Override
  public void afterPropertiesSet() throws Exception {
  
  }
  
}
