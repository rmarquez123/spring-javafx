package com.rm.testrmfxmap.javafx;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

/**
 * Contains main method but also implements JavaFX {@linkplain Application}.
 * Invokes SpringBoot before getting the scene root node from
 * {@linkplain FxmlInitializer} helper class.
 *
 * @author rmarquez
 */
@SpringBootApplication()
@ImportResource(locations = {"spring/tabsbinding.xml"})
public class Main extends Application {

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
   * {@inheritDoc} OVERRIDE : Invokes SpringBoot then 1) saves the root for use
   * in {@linkplain #start(javafx.stage.Stage)} method and 2) saves the context
   * so that it can be closed when the application terminates.
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
  }

  /**
   * {@inheritDoc} OVERRIDE : Sets the scene and invokes
   * {@linkplain Stage#show()}.
   *
   * @param primaryStage
   * @throws Exception
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setScene(new Scene(this.root, 500, 500));
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
