package com.windsim.wpls.view;

import com.rm.springjavafx.FxmlInitializer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

/**
 * Contains main method but also implements JavaFX {@linkplain Application}. Invokes
 * SpringBoot before getting the scene root node from {@linkplain FxmlInitializer} helper
 * class.
 *
 * @author rmarquez
 */
@SpringBootApplication()
@ImportResource(locations = {"spring/main.xml"})
public class WplsMain extends Application {

  private FxmlInitializer fxmlInitializer;
  private Parent root;
  private ConfigurableApplicationContext context;

  /**
   *
   * @param args
   */
  public static void main(String args[]) {
    launch(args);
  }

  /**
   * {@inheritDoc} OVERRIDE : Invokes SpringBoot then 1) saves the root for use in
   * {@linkplain #start(javafx.stage.Stage)} method and 2) saves the context so that it
   * can be closed when the application terminates.
   *
   * @see SpringApplicationBuilder#build(java.lang.String...)
   * @see Application#start(javafx.stage.Stage)
   * @see Application#stop()
   * @throws Exception if root is null.
   */
  @Override
  public void init() throws Exception {
    SpringApplicationBuilder builder = new SpringApplicationBuilder(this.getClass());
    this.context = builder.run(getParameters().getRaw().toArray(new String[0]));
    this.fxmlInitializer = this.context.getBean(FxmlInitializer.class);
    this.root = this.fxmlInitializer.load(this.context);
    if (this.root == null) {
      throw new NullPointerException("Root cannot be null");
    }
    this.root.getStylesheets().add("styles/window-style.css");
    this.root.getStylesheets().add("styles/chart-style.css");
    this.root.getStylesheets().add("styles/windsim.buttons.css");
    this.root.getStylesheets().add("styles/windsim.table.css");
    this.root.getStylesheets().add("styles/windsim.scroll.css");
    this.root.getStylesheets().add("styles/windsim.textfield.css");
    this.root.getStylesheets().add("styles/windsim.label.css");
    
    Thread.setDefaultUncaughtExceptionHandler((Thread t, Throwable e) -> {
      if (Platform.isFxApplicationThread()) {
        this.showAlert(e);
      } else {
        Platform.runLater(() -> {
          this.showAlert(e);
        });
      }
      Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
    });
  }

  private void showAlert(Throwable e) {
    Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
    Scene scene = this.root.getScene();
    Window window = scene.getWindow();
    double xPos = window.getX() + window.getWidth() * 0.4;
    double yPos = window.getY() + window.getHeight() * 0.2;
    alert.setX(xPos);
    alert.setY(yPos);
    
    alert.initModality(Modality.APPLICATION_MODAL);
    alert.showAndWait();
  }

  /**
   * {@inheritDoc} OVERRIDE : Sets the scene and invokes {@linkplain Stage#show()}.
   *
   * @param primaryStage
   * @throws Exception
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setScene(new Scene(this.root));
    primaryStage.show();
  }

  /**
   * {@inheritDoc} OVERRIDE: Closes the context.
   *
   * @throws Exception
   */
  @Override
  public void stop() throws Exception {
    this.context.close();
  }
}
