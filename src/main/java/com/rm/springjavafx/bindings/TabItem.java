package com.rm.springjavafx.bindings;

import com.rm.testrmfxmap.javafx.FxmlInitializer;
import java.util.Objects;
import javafx.scene.Node;
import javafx.scene.control.SingleSelectionModel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author rmarquez
 */
public class TabItem implements InitializingBean, ApplicationContextAware {

  private static final String SELECTED_CLASS = "selected";
  @Autowired
  FxmlInitializer fxmlInitializer;
  private String selectionId;
  private String fxml;
  private String fxmlId;
  private String label;
  private Node node;
  private ApplicationContext context;

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
   * @param handler
   */
  public void setOnAction(ActionHandler handler) {
    node.setOnMouseClicked((e) -> {
      handler.onAction();
    });
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (!this.fxmlInitializer.isInitialized()) {
      this.fxmlInitializer.initializeRoots(context);
    }
    this.node = this.fxmlInitializer.getNode(fxml, fxmlId);
  }

  @Override
  public void setApplicationContext(ApplicationContext ac) throws BeansException {
    this.context = ac;
  }

  void bindToSelectionModel(SingleSelectionModel<TabItem> selection) {
    selection.selectedItemProperty().addListener((e)->{
      onSelected(selection);
    });
    onSelected(selection);
  }

  private void onSelected(SingleSelectionModel<TabItem> selection) {
    boolean selected = Objects.equals(selection.selectedItemProperty().getValue(), this);
    if (selected) {
      if (!this.node.getStyleClass().contains(SELECTED_CLASS)) {
        this.node.getStyleClass().add(SELECTED_CLASS);
      }
    } else {
      this.node.getStyleClass().remove(SELECTED_CLASS);
    }
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {
    return "TabItem{" + "selectionId=" + selectionId + ", fxml=" + fxml + ", fxmlId=" + fxmlId + ", label=" + label + '}';
  }

  public static interface ActionHandler {

    public void onAction();
  }
}
