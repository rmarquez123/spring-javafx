package com.rm.springjavafx;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Helper class for initializing fxml with controllers instantiated through spring.
 * Collaborates with the main class in a Spring boot environment.
 *
 * @author rmarquez
 */
public class FxmlInitializer implements InitializingBean {

  @Autowired
  ApplicationContext context;

  private final List<String> fxmlList = new ArrayList<>();
  private final Map<String, Parent> rootNodes = new HashMap<>();
  private final Map<String, Object> controllers = new HashMap<>();
  private String sceneRoot;
  private boolean initialized = false;
  private boolean initializing = false;
  private List<Consumer<FxmlInitializer>> listeners = new ArrayList<>();

  /**
   * Public constructor. Set properties using setters.
   */
  public FxmlInitializer() {
  }

  public void addListener(Consumer<FxmlInitializer> listener) {
    if (this.isInitialized()) {
      listener.accept(this);
    } else {
      this.listeners.add(listener);
    }
  }

  /**
   * Fxml List setter. This list does not need to have the root.
   *
   * @param fxmlList
   */
  public void setFxmlList(List<String> fxmlList) {
    this.fxmlList.addAll(fxmlList);
  }

  /**
   * Scene root setter. The scene root must be set otherwise the
   * {@linkplain #load(org.springframework.context.ApplicationContext)} method will fail
   * with a null pointer exception.
   *
   * @param sceneRoot
   * @see #load(org.springframework.context.ApplicationContext)
   */
  public void setSceneRoot(String sceneRoot) {
    this.sceneRoot = sceneRoot;
  }

  /**
   * {@inheritDoc} OVERRIDE : Checks if scene root is null. Adds the scene root to the
   * fxml list if not already present.
   *
   * @throws Exception if scene root has not been set.
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    if (this.sceneRoot == null) {
      throw new NullPointerException("Scene root has not been set");
    }
    if (!this.fxmlList.contains(this.sceneRoot)) {
      this.fxmlList.add(this.sceneRoot);
    }
  }

  /**
   * Sets the controller factories to fxml loaders. Stores the roots for each fxml node
   * graph into a map. This method should be called by the main application to load and
   * get the scene root.
   *
   * @param context The application context which contains beans for the controllers of
   * the fxml list.
   * @return The parent node for the scene root fxml node graph.
   * @see #sceneRoot
   * @see #setFxmlList(java.util.List)
   * @see FXMLLoader
   */
  public Parent load(ApplicationContext context) {
    if (!this.isInitialized()) {
      this.initializeRoots(context);
    }
    Parent result = this.rootNodes.get(this.sceneRoot);
    return result;
  }

  /**
   *
   * @return
   */
  public boolean isInitialized() {
    return this.initialized;
  }

  /**
   *
   * @param context
   */
  public void initializeRoots(ApplicationContext context) {
    if (!this.isInitialized()) {
      if (this.initializing) {
        throw new IllegalStateException("Fxml loaders are initalizing.  Add a listener instead.");
      }
      this.initializing = true;
      ClassLoader classLoader = this.getClass().getClassLoader();
      for (String fxml : this.fxmlList) {
        if (!this.rootNodes.containsKey(fxml)) {
          URL resource = classLoader.getResource(fxml);
          FXMLLoader loader = new FXMLLoader(resource);
          loader.setControllerFactory(context::getBean);
          Parent root;
          try {
            root = loader.load();
          } catch (IOException ex) {
            throw new RuntimeException("Error loading fxml from resource.  Check args : {"
              + "fxml = " + fxml
              + "}", ex);
          }
          if (!this.rootNodes.containsKey(fxml)) {
            this.rootNodes.put(fxml, root);
            Object controller = loader.getController();
            this.controllers.put(fxml, controller);
          }
        }
      }
      this.initializing = false;
      this.initialized = true;
      for (Consumer<FxmlInitializer> listener : this.listeners) {
        listener.accept(this);
      }
    }
  }
  
  /**
   * 
   * @return 
   */
  public Parent getMainRoot() {
    return this.getRoot(this.sceneRoot); 
  }
  
  /**
   *
   * @param fxml
   * @return
   */
  public Parent getRoot(String fxml) {
    if (!this.initialized) {
      this.initializeRoots(this.context);
    }
    return this.rootNodes.get(fxml);
  }
  
  
  
  /**
   *
   * @param fxml
   * @return
   */
  public Object getController(String fxml) {
    if (!this.initialized) {
      this.initializeRoots(this.context);
    }
    return this.controllers.get(fxml);
  }

  /**
   *
   * @param fxml
   * @param fxmlId
   * @return
   * @throws java.lang.IllegalAccessException
   */
  public Node getNode(String fxml, String fxmlId) throws IllegalAccessException {
    if (!this.isInitialized()) {
      throw new IllegalAccessException("Attempting to get node without initializing");
    }
    Parent root = this.rootNodes.get(fxml);
    if (root == null) {
      throw new IllegalArgumentException("Invalid fmxl file : '" + fxml + "'");
    }
    Object result = SpringFxUtils.getChildByID(root, fxmlId);
    if (result == null) {
      throw new IllegalArgumentException("Component not found. Check args : {"
        + "fxml = " + fxml
        + ", fxmlId = " + fxmlId
        + "}");
    }
    return (Node) result;
  }
}
