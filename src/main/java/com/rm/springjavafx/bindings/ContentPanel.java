package com.rm.springjavafx.bindings;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.SpringFxUtils;
import java.util.Objects;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author rmarquez
 */
public class ContentPanel implements InitializingBean {

  @Autowired
  FxmlInitializer fxmlInitializer;

  private String parentFxml;
  private String parentNode;
  private String childFxml;

  /**
   * 
   * @param parentFxml 
   */
  public void setParentFxml(String parentFxml) {
    this.parentFxml = parentFxml;
    this.fxmlInitializer.addFxml(parentFxml); 
  }
  
  /**
   * 
   * @param parentNode 
   */
  public void setParentNode(String parentNode) {
    this.parentNode = parentNode;
  }

  /**
   * 
   * @param childFxml 
   */
  public void setChildFxml(String childFxml) {
    this.childFxml = childFxml;
    this.fxmlInitializer.addFxml(childFxml);
  }

  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    this.fxmlInitializer.addListener((a) -> {
      Platform.runLater(() -> {
        try {
          Pane parent = (Pane) this.fxmlInitializer.getNode(parentFxml, parentNode);
          Objects.requireNonNull("Parent node cannot be null. Check args:{"
              + "parentFxml = " + parentFxml
              + ", parentNode = " + parentNode
              + "}");
          Parent child = this.fxmlInitializer.getRoot(childFxml);
          Objects.requireNonNull(child, "Child node cannot be null.  Check args: {"
            + "childfxml = " + childFxml
            + "}"); 
          SpringFxUtils.setNodeOnRefPane(parent, child);
        } catch (Exception ex) {
          throw new RuntimeException("An exception occurred on initializing child pane.  Check args: {"
            + "childFxml = " + childFxml
            + ",parentFxml = " + parentFxml
            + ",parentNode = " + parentNode
            + "}", ex);
        }
      });
    });

  }

}
