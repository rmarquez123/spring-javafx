package com.rm.springjavafx.bindings;

import com.rm.datasources.Invoker;
import com.rm.springjavafx.components.*;
import com.rm.springjavafx.FxmlInitializer;
import javafx.scene.control.Button;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author rmarquez
 */
public class ButtonBind implements InitializingBean, ApplicationContextAware {

  @Autowired
  FxmlInitializer fxmlInitializer;
  private ApplicationContext appContext;

  private String fxml;
  private String fxmlId;
  private Invoker invoker;

  public void setFxml(String fxml) {
    this.fxml = fxml;
  }

  public void setFxmlId(String fxmlId) {
    this.fxmlId = fxmlId;
  }

  public void setInvokerRef(Invoker invoker) {
    this.invoker = invoker;
  }
  

  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    if (this.invoker == null) {
      throw new IllegalStateException("Invoker cannot be null");
    }
    if (this.fxml == null || this.fxml.isEmpty()) {
      throw new IllegalStateException("fxml cannot be null or empty");
    }
    if (this.fxmlId == null || this.fxmlId.isEmpty()) {
      throw new IllegalStateException("fxml id cannot be null or empty");
    }
    Button button = (Button) this.fxmlInitializer.getNode(fxml, fxmlId);
    button.setOnAction((e) -> invoker.invoke());
  }

  @Override
  public void setApplicationContext(ApplicationContext ac) throws BeansException {
    this.appContext = ac;
  }

}
