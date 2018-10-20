package com.rm.springjavafx.bindings;

import com.rm.testrmfxmap.javafx.FxmlInitializer;
import javafx.scene.Node;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author rmarquez
 */
public class TabItem implements InitializingBean {

  @Autowired
  FxmlInitializer fxmlInitializer;
  private String selectionId;
  private String fxml;
  private String fxmlId;
  private String label;
  private Node node;

  /**
   *
   * @param selectionId
   */
  public void setSelectionId(String selectionId) {
    this.selectionId = selectionId;
  }

  /**
   *
   * @param fxml
   */
  public void setFxml(String fxml) {
    this.fxml = fxml;
  }

  /**
   *
   * @param fxmlId
   */
  public void setFxmlId(String fxmlId) {
    this.fxmlId = fxmlId;
  }

  /**
   *
   * @param label
   */
  public void setLabel(String label) {
    this.label = label;
  }

  public String getSelectionId() {
    return selectionId;
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {
    return "TabItem{" + "selectionId=" + selectionId + ", fxml=" + fxml + ", fxmlId=" + fxmlId + ", label=" + label + '}';
  }
  
  /**
   * 
   * @param handler 
   */
  public void setOnAction(ActionHandler handler) {
    node.setOnMouseClicked((e) -> {
      handler.onAction();
    });
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.node = this.fxmlInitializer.getNode(fxml, fxmlId);

  }
  public static interface ActionHandler {
    
    public void onAction();
  }
}
