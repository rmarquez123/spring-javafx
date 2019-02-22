package com.rm.springjavafx.components;

import com.rm.springjavafx.FxmlInitializer;
import javafx.scene.Node;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Ricardo Marquez
 */
public class ChildNodeFactory implements FactoryBean<ChildNodeWrapper>, InitializingBean {

  @Autowired
  FxmlInitializer fxmlInitializer;

  private String fxml;
  private String fxmlId;

  public void setFxml(String fxml) {
    this.fxml = fxml;
  }

  public void setFxmlId(String fxmlId) {
    this.fxmlId = fxmlId;
  }
  

  @Override
  public ChildNodeWrapper getObject() throws Exception {
    ChildNodeWrapper<Node> result = new ChildNodeWrapper<>();
    this.fxmlInitializer.addListener((i) -> {
      Node node;
      try {
        node = i.getNode(this.fxml, this.fxmlId);
      } catch (IllegalAccessException ex) {
        throw new RuntimeException(ex);
      }
      result.setNode(node);

    });
    return result;
  }

  @Override
  public void afterPropertiesSet() throws Exception {

  }

  @Override
  public Class<?> getObjectType() {
    return ChildNodeWrapper.class;
  }

}
