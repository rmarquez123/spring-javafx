package com.rm._springjavafx.ignore.textfields;

import com.rm.springjavafx.annotations.FxController;
import com.rm.springjavafx.textfields.NumericTextField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@FxController(fxml = "fxml/TextFieldsExample.fxml")
public class TextFieldsExample implements InitializingBean {

  @NumericTextField(nodeId = "numericTextField", format = "##.##", beanId="numberProperty")
  private TextField numericTextField;

  @Override
  public void afterPropertiesSet() throws Exception {
    System.out.println("initialized ...");
  }

}
