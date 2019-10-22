package com.rm._springjavafx.ignore.tree;

import com.rm.springjavafx.annotations.FxController;
import com.rm.springjavafx.annotations.PopupComponent;
import com.rm.springjavafx.popup.Popup;
import com.rm.springjavafx.popup.PopupContent;
import java.io.IOException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
@FxController(fxml = "fxml/theoffice.editdepartment.fxml")
@PopupComponent(id = "office.editdepartments.popup")
public class EditDepartmentsPopup implements PopupContent {

  private Popup popup;

  /**
   *
   * @param popup
   */
  @Override
  public void setPopupWindow(Popup popup) {
    this.popup = popup;
    this.popup.showProperty().addListener((obs, old, change) -> {
      if (change) {
        Object userData = this.popup.getUserData();
        System.out.println("userdata = " + userData);
      }
    });

  }

  @Override
  public void onClose() throws IOException {
  }

}
