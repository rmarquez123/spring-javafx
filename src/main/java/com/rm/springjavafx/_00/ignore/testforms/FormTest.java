package com.rm.springjavafx._00.ignore.testforms;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.form.AbstractForm;
import com.rm.springjavafx.form.FormData;
import com.rm.springjavafx.form.FxForm;
import com.rm.springjavafx.form.FxFormGroupId;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
@FxForm(
  fxml = "fxml/Main.fxml",
  componentId = "theAnchorPane",
  formId = "form", 
  group = {
    @FxFormGroupId(groupId = "personAttributes")
  }
)
public final class FormTest extends AbstractForm implements InitializingBean {
  @Autowired
  private FxmlInitializer fxmlInitializer;
  
  
  /**
   * 
   * @throws Exception 
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    this.fxmlInitializer.addListener((i)->{
      Button nextBtn = new Button("Next");
      Button prevBtn = new Button("Previous");
      nextBtn.setOnAction((evt)->{
        this.selectNext();
      });
      prevBtn.setOnAction((evt)->{
        super.selectPrevious();
      });
      HBox hBox = new HBox(prevBtn, nextBtn);
      hBox.setSpacing(5);
      Parent p = i.getRoot("fxml/Main.fxml");
      try { 
        ((VBox) i.getNode("fxml/Main.fxml", "theAnchorPane")).getChildren().add(hBox);
      } catch (IllegalAccessException ex) {
        throw new RuntimeException(ex);
      }
    });
    
  }
  
  
  /**
   * 
   */
  public void onSave(FormData formData) {
    
  }
}
