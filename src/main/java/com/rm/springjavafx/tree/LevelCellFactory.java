package com.rm.springjavafx.tree;

import com.rm.datasources.RecordValue;
import javafx.scene.control.ContextMenu;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author rmarquez
 */
public class LevelCellFactory implements InitializingBean {

  private Integer level;
  private String textField;
  private boolean checkbox = true;
  private ContextMenuProvider contextMenuProvider;
  
  /**
   *
   * @param level
   */
  @Required
  public void setLevel(Integer level) {
    this.level = level;
  }

  /**
   *
   * @param textField
   */
  @Required
  public void setTextField(String textField) {
    this.textField = textField;
  }

  @Required
  public void setContextMenuProvider(ContextMenuProvider contextMenuProvider) {
    this.contextMenuProvider = contextMenuProvider;
  }
  
  

  /**
   *
   * @return
   */
  public Integer getLevel() {
    return level;
  }

  /**
   *
   * @return
   */
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

  boolean isCheckBox() {
    return this.checkbox;
  }

  ContextMenu getContextMenu(RecordValue object) {
    return this.contextMenuProvider.getContextMenu(object); 
  }
  

}
