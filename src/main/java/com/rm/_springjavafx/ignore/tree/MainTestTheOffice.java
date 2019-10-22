package com.rm._springjavafx.ignore.tree;

import com.rm.springjavafx.FxmlInitializer;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
/**
 * Contains main method but also implements JavaFX {@linkplain Application}. Invokes
 * SpringBoot before getting the scene root node from {@linkplain FxmlInitializer} helper
 * class.
 *
 * @author rmarquez
 */
@SpringBootApplication()
@ImportResource(locations = {"spring/theoffice.xml"})
@ComponentScan(
  basePackages = {
    "com.rm.springjavafx", 
    "com.rm._springjavafx.ignore.tree"
  }
)
public class MainTestTheOffice extends Application {

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
    
    Thread.setDefaultUncaughtExceptionHandler((Thread t, Throwable e) -> {
      Platform.runLater(() -> {
        this.showAlert(e);
      });
      Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
    });
  }

  /**
   *
   * @param e
   */
  private void showAlert(Throwable e) {
    Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
    StackTraceElement[] stackTrace = e.getStackTrace();

    TextFlow textFlow = new TextFlow();
    Text text = new Text(e.getClass() + ":" + e.getMessage() + "\n");
    text.setStyle("-fx-font-weight: bold");
    textFlow.getChildren().add(text);
    for (StackTraceElement stackTraceElement : stackTrace) {
      textFlow.getChildren().add(new Text(stackTraceElement.toString() + "\n"));
    }
    this.appendSuppressed(e, textFlow);
    this.appendCause(e, textFlow);
    alert.getButtonTypes().add(ButtonType.NEXT);
    Scene scene = this.root.getScene();
    Window window = scene.getWindow();
    double xPos = window.getX() + window.getWidth() * 0.4;
    double yPos = window.getY() + window.getHeight() * 0.2;
    alert.setX(xPos);
    alert.setY(yPos);
    alert.initModality(Modality.APPLICATION_MODAL);
    ((Stage) alert.getDialogPane().getScene().getWindow())
      .setAlwaysOnTop(true);
    Optional<ButtonType> r = alert.showAndWait();
    if (r.isPresent() && r.get() == ButtonType.NEXT) {

      alert = new Alert(Alert.AlertType.ERROR, null);
      ScrollPane scrollPane = new ScrollPane(textFlow);
      scrollPane.setPrefWidth(500);
      scrollPane.setPrefHeight(300);
      scrollPane.parentProperty().addListener((obs, old, change) -> {
        StackPane.setMargin(scrollPane, Insets.EMPTY);
        AnchorPane.setLeftAnchor(scrollPane, 0d);
      });
      alert.setResizable(true);
      alert.setGraphic(scrollPane);
      alert.setX(xPos);
      alert.setY(yPos);
      alert.initModality(Modality.APPLICATION_MODAL);
      ((Stage) alert.getDialogPane().getScene().getWindow())
        .setAlwaysOnTop(true);

      alert.showAndWait();
    }
  }

  /**
   *
   * @param e
   * @param textFlow
   */
  private void appendSuppressed(Throwable e, TextFlow textFlow) {
    for (Throwable throwable : e.getSuppressed()) {
      Text text = new Text("Suppressed : " + throwable.getClass() + " : " + throwable.getMessage() + "\n");
      text.setStyle("-fx-font-weight: bold");
      textFlow.getChildren().add(text);
      for (StackTraceElement stackTraceElement : throwable.getStackTrace()) {
        textFlow.getChildren().add(new Text(stackTraceElement.toString() + "\n"));
      }
      appendSuppressed(throwable, textFlow);
    }
  }

  /**
   *
   * @param e
   * @param textFlow
   */
  private void appendCause(Throwable e, TextFlow textFlow) {
    Throwable cause = e.getCause();
    if (cause != null) {
      Text text = new Text("Caused by: " + cause.getClass() + ":" + cause.getMessage() + "\n");
      text.setStyle("-fx-font-weight: bold");
      textFlow.getChildren().add(text);
      for (StackTraceElement stackTraceElement : cause.getStackTrace()) {
        textFlow.getChildren().add(new Text(stackTraceElement.toString() + "\n"));
      }
      appendSuppressed(cause, textFlow);
    }
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
    this.root.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.SPACE, KeyCodeCombination.CONTROL_DOWN), () -> {
      RuntimeException ex = new RuntimeException("Testing error");
      ex.addSuppressed(new RuntimeException("Test suppressed"));
      throw ex;
    });
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
