package com.rm.springjavafx.bindings;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.components.ChildNodeWrapper;
import com.rm.springjavafx.popup.Popup;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Button;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author Ricardo Marquez
 */
public class ButtonPopup implements InitializingBean{
  @Autowired
  private FxmlInitializer fxmlInitializer;
  
  private ChildNodeWrapper<Button> button;
  private Popup popup;
  private ObjectProperty<Boolean> readyProperty;
  
  @Required
  public void setButton(ChildNodeWrapper<Button> button) {
    this.button = button;
  }
  
  @Required
  public void setPopup(Popup popup) {
    this.popup = popup;
  }

  public void setReadyProperty(ObjectProperty<Boolean> readyProperty) {
    this.readyProperty = readyProperty;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.fxmlInitializer.addListener((i)->{
      this.button.getNode().setOnAction((evt)->{
        this.popup.show();
      });
      this.button.getNode().disableProperty().bind(this.readyProperty.isEqualTo(false));
    });
  }
  
}
