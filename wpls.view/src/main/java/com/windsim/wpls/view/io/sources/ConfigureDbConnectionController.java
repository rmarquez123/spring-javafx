package com.windsim.wpls.view.io.sources;

import com.rm.datasources.DbConnection;
import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.popup.Popup;
import com.rm.springjavafx.popup.PopupContent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * FXML Controller class
 *
 * @author Ricardo Marquez
 */
@Component("configureDbConnectionController")
public class ConfigureDbConnectionController implements Initializable, InitializingBean, PopupContent {

  @Autowired
  FxmlInitializer fxmlInitializer;

  @FXML
  private TextField hostTxtField;

  @FXML
  private TextField maintenanceDbTxtField;

  @FXML
  private TextField usernameTxtField;

  @FXML
  private TextField portTxtField;

  @FXML
  private PasswordField pwdTxtField;

  @FXML
  private Button testBtn;

  @FXML
  private Button saveBtn;

  @FXML
  private Label testMessageLabel;

  @FXML
  private Button cancelBtn;
  private Popup popup;

  @Autowired
  @Qualifier("dbConnection")
  private Property<DbConnection> dbConnectionProperty;

  /**
   * Initializes the controller class.
   *
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    this.cancelBtn.setOnAction((evt) -> {
      this.popup.showProperty().setValue(false);
    });

    this.portTxtField.textProperty().addListener((obs, old, change) -> {
      if (change.matches("\\d*")) {
        int value = Integer.parseInt(change);
      } else {
        this.portTxtField.setText(old);
      }
    });

    this.saveBtn.setOnAction((evt) -> {
      DbConnection connection = this.getDbConnection();
      this.dbConnectionProperty.setValue(connection);
      this.popup.showProperty().setValue(Boolean.FALSE);

    });

    Paint originalFillColor = this.testMessageLabel.getTextFill();
    this.testBtn.setOnAction((evt) -> {
      this.testMessageLabel.setText("");
      this.testMessageLabel.setTextFill(originalFillColor);
      DbConnection dbConnection = getDbConnection();
      Exception test = dbConnection.test();
      if (test == null) {
        this.testMessageLabel.setText("Connection successful!");
      } else {
        this.testMessageLabel.setText("Connection failed. " + test.getMessage());
        this.testMessageLabel.setTextFill(Color.RED);
      }
    });

  }

  private DbConnection getDbConnection() throws NumberFormatException {
    String host = this.hostTxtField.getText();
    String database = this.maintenanceDbTxtField.getText();
    String user = this.usernameTxtField.getText();
    String password = this.pwdTxtField.getText();
    int port = Integer.parseInt(this.portTxtField.getText());
    DbConnection dbConnection = new DbConnection.Builder()
      .setUrl(host)
      .setSchema(database)
      .setPassword(password)
      .setPort(port)
      .setUser(user).createDbConnection();
    return dbConnection;
  }

  @Override
  public void afterPropertiesSet() throws Exception {

  }

  @Override
  public void setPopupWindow(Popup popup) {
    this.popup = popup;
    Platform.runLater(() -> {
      this.fxmlInitializer.addListener((i) -> {
        if (this.fxmlInitializer.getMainRoot().getScene() == null) {
          this.fxmlInitializer.getMainRoot().sceneProperty().addListener((s) -> {
            Scene scene = this.fxmlInitializer.getMainRoot().getScene();
            this.popup.windowProperty().bind(scene.windowProperty());
          });
        } else {
          Scene scene = this.fxmlInitializer.getMainRoot().getScene();
          this.popup.windowProperty().setValue(scene.getWindow());
          hostTxtField.requestFocus();
        }
      });
    });
  }

  @Override
  public void onClose() throws IOException {
    this.testMessageLabel.setText("");
  }

}
