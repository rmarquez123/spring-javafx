package com.rm.springjavafx.annotations;

import com.rm.springjavafx.AnnotationHandler;
import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.SpringFxUtils;
import com.rm.springjavafx.annotations.childnodes.CheckBoxAnnotationHandler;
import com.rm.springjavafx.annotations.childnodes.ChildNode;
import com.rm.springjavafx.annotations.childnodes.ChildNodeArgs;
import com.rm.springjavafx.nodes.NodeProcessor;
import com.rm.springjavafx.nodes.NodeProcessorFactory;
import common.RmExceptions;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Map;
import java.util.function.Consumer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.Window;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
public class ChildNodeAnnotationHandler implements InitializingBean {

  private static FxmlInitializer fxmlInitializer;

  private static NodeProcessorFactory nodeProcessorFactory;

  @Autowired
  private ApplicationContext appContext;

  @Autowired
  private CheckBoxAnnotationHandler handler;

  @Autowired
  public void setFxmlInitializer(FxmlInitializer fxmlInitializer) {
    ChildNodeAnnotationHandler.fxmlInitializer = fxmlInitializer;
  }

  @Autowired
  public void setNodeProcessorFactory(NodeProcessorFactory factory) {
    ChildNodeAnnotationHandler.nodeProcessorFactory = factory;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    fxmlInitializer.addAnnotationHandler(new AnnotationHandler() {
      @Override
      public void readyFxmls() {
        onReadyFxmls();
      }

      @Override
      public void setNodes() {
        setBeanChildNodes();
      }
    });
  }

  /**
   *
   */
  private void onReadyFxmls() {
    Map<String, Object> beans = appContext.getBeansWithAnnotation(FxController.class);
    beans.entrySet().stream().forEach(this::readyFxmlForBeanEntry);
  }

  /**
   *
   * @param entry
   */
  private void readyFxmlForBeanEntry(Map.Entry<String, Object> entry) {
    try {
      this.readyBeanAndChildFxmls(entry.getValue());
    } catch (Exception ex) {
      throw new RuntimeException(
        String.format("Error on creating bean '%s'", entry.getKey()), ex);
    }
  }

  /**
   *
   * @param bean
   * @throws RuntimeException
   */
  private void readyBeanAndChildFxmls(Object bean) throws RuntimeException {
    FxController fxController = SpringFxUtils.getAnnotation(bean, FxController.class);
    if (fxController == null) {
      throw RmExceptions.create( //
        "Could not find controller annotation for bean '%s'",
        bean.getClass());
    }
    String parentFxml = fxController.fxml();
    if (!parentFxml.isEmpty()) {
      addFxml(parentFxml);
    }
    Field[] fields = SpringFxUtils.getFields(bean);
    for (Field field : fields) {
      ChildNode childNode = field.getDeclaredAnnotation(ChildNode.class);
      if (childNode != null) {
        String fxml = childNode.fxml();
        if (parentFxml.isEmpty() && !fxml.isEmpty()) {
          addFxml(fxml);
        } else if (parentFxml.isEmpty()) {
          throw new RuntimeException("No fxml file specified for node: '" + field + "'");
        }
      }
    }
  }

  /**
   *
   * @param fxml
   */
  private void addFxml(String fxml) {
    if (this.getClass().getClassLoader().getResource(fxml) == null) {
      throw new IllegalStateException("Fxml does not exist: '" + fxml + "'");
    }
    if (!FilenameUtils.getExtension(fxml).endsWith("fxml")) {
      throw new RuntimeException("File does not have .fxml extension: '" + fxml + "'");
    }
    fxmlInitializer.addFxml(fxml);
  }

  /**
   *
   * @throws BeansException
   * @throws RuntimeException
   */
  private void setBeanChildNodes() {
    Map<String, Object> beans = appContext.getBeansWithAnnotation(FxController.class);
    beans.values().forEach(this::setBeanChildNode);
  }

  /**
   *
   * @param bean
   */
  public void setBeanChildNode(Object bean) {
    this.setBeanChildNode(bean, false);
  }

  /**
   *
   * @param controller
   */
  public Node setBeanChildNode(Object controller, boolean newRoot) {
    FxController fxController = SpringFxUtils.getAnnotation(controller, FxController.class);
    String fxml = fxController.fxml();
    Parent parent = this.getParent(fxml, newRoot);
    try {
      setBeanChildNodes(parent, controller, this::handleField);
    } catch (Exception ex) {
      String message = String.format( //
        "Error creating child nodes for bean '%s'", controller.getClass().getName());
      throw new RuntimeException(message, ex);
    }

    return parent;
  }

  /**
   *
   * @param newRoot
   * @param fxml
   * @return
   * @throws RuntimeException
   */
  private Parent getParent(String fxml, boolean newRoot) {
    Parent parent;
    if (!newRoot) {
      parent = fxmlInitializer.getRoot(fxml);
    } else {
      URL resource = this.getClass().getClassLoader().getResource(fxml);
      if (resource == null) {
        throw new IllegalStateException( //
          String.format("Resource '%s' does not exists", fxml));
      }
      FXMLLoader loader = new FXMLLoader(resource);
      try {
        parent = loader.load();
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
    return parent;
  }

  /**
   *
   * @param f
   */
  private void handleField(ChildNodeArgs f) {
    handler.handle(f);
  }

  public static void setBeanChildNodes(Parent parentArg, Object bean) {
    setBeanChildNodes(parentArg, bean, null);
  }

  /**
   *
   * @param parentArg
   * @param bean
   */
  public static void setBeanChildNodes(Parent parentArg, Object bean, Consumer<ChildNodeArgs> consumer) {
    FieldsProcessor processor = new FieldsProcessor(bean, parentArg, consumer);
    Field[] fields = SpringFxUtils.getFields(bean);
    processor.processFields(fields);
    String contextmenuid = getFxControllerDefinition(bean).contextMenu();
    if (!contextmenuid.isEmpty()) {
      if (parentArg == null) {
        throw new RuntimeException();
      }
      ContextMenu contextmenu = (ContextMenu) fxmlInitializer.getContext().getBean(contextmenuid);
      if (parentArg instanceof Control) {
        ((Control) parentArg).setContextMenu(contextmenu);
      } else {
        parentArg.setOnContextMenuRequested((ContextMenuEvent event) -> {
          double screenX = event.getScreenX();
          double screenY = event.getScreenY();
          Window window = parentArg.getScene().getWindow();
          contextmenu.setUserData(bean);
          contextmenu.show(window, screenX, screenY);
        });
      }
    }
  }

  private static FxController getFxControllerDefinition(Object bean) {
    FxController fxController;
    Class<? extends Object> aClass = bean.getClass();
    if (Modifier.isAbstract(aClass.getModifiers())) {
      fxController = bean.getClass().getDeclaredAnnotation(FxController.class);
    } else {
      fxController = SpringFxUtils.getAnnotation(bean, FxController.class);
    }
    if (fxController == null) {
      throw new IllegalArgumentException(
        String.format("Bean '%s' is not annotated with '%s'", bean, FxController.class));
    }
    return fxController;
  }

  /**
   *
   * @param field
   * @param bean
   * @param child
   */
  private static void setChildNodeToFieldValue(Field field, Object bean, Object child) {
    try {
      field.setAccessible(true);
      field.set(bean, child);
    } catch (IllegalArgumentException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
  }

  /**
   *
   * @param parent
   * @param id
   * @param fxml
   * @return
   */
  private static Object getChildNode(Parent parent, String id, String fxml) {
    Object child;
    try {
      child = SpringFxUtils.getChildByID(parent, id);
    } catch (Exception ex) {
      throw new RuntimeException(
        String.format("Error getting child element.  Check args: {fxml='%s', childId='%s'}", fxml, id), ex);
    }
    return child;
  }

  public static class FieldsProcessor {

    private final Object bean;
    private final Class<? extends Object> aClass;
    private final FxController fxController;
    private Parent parentArg;
    private Consumer<ChildNodeArgs> consumer;

    private FieldsProcessor(Object bean, Parent parentArg, Consumer<ChildNodeArgs> consumer) {
      this.bean = bean;
      this.aClass = bean.getClass();
      this.fxController = getFxControllerDefinition(bean);
      this.parentArg = parentArg;
      this.consumer = consumer;
    }

    private void processFields(Field[] fields) {
      for (Field field : fields) {
        ChildNode childNode = field.getDeclaredAnnotation(ChildNode.class);
        if (childNode != null) {
          String fxml = null;
          Parent parent;
          if (!childNode.fxml().isEmpty()) {
            fxml = childNode.fxml();
            parent = fxmlInitializer.getRoot(fxml);
          } else if (parentArg == null) {
            fxml = childNode.fxml();
            if (fxml.isEmpty()) {
              fxml = this.fxController.fxml();
            }
            parent = fxmlInitializer.getRoot(fxml);
          } else {
            parent = parentArg;
          }
          String id = childNode.id();
          Object child;
          try {
            child = (Object) getChildNode(parent, id, fxml);
          } catch (Exception ex) {
            throw new RuntimeException("Error on getting child node.  Check args: {"
              + "parent = " + parent
              + ", id = " + id
              + ", fxml = " + fxml
              + "}", ex);
          }
          if (child == null) {
            throw new IllegalStateException("Child node is null.  Check args: {"
              + "fxml = " + fxml
              + ", parent = " + parent
              + ", id = " + id
              + ", bean = " + bean
              + "}");
          }
          setChildNodeToFieldValue(field, bean, child);
          NodeProcessHelper nodeProcessor // 
            = new NodeProcessHelper(nodeProcessorFactory, bean, field, child);
          nodeProcessorFactory.getAnnotations().stream()
            .filter((e) -> field.getDeclaredAnnotation(e) != null)
            .forEach(nodeProcessor::processNode);
          if (consumer != null) {
            consumer.accept(new ChildNodeArgs(bean, field, child));
          }
        }
      }
    }

  }

  private static class NodeProcessHelper {

    private final NodeProcessorFactory factory;
    private final Object parentBean;
    private final Field field;
    private final Object child;

    /**
     *
     * @param factory
     * @param bean
     * @param field
     * @param child
     */
    private NodeProcessHelper(NodeProcessorFactory factory,
      Object parentBean, Field field, Object child) {
      this.factory = factory;
      this.parentBean = parentBean;
      this.field = field;
      this.child = child;
    }

    /**
     *
     * @param clazz
     */
    private void processNode(Class<? extends Annotation> clazz) {
      Annotation annotation = this.field.getDeclaredAnnotation(clazz);
      NodeProcessor processor = this.factory.getProcessor(clazz);
      processor.process(this.parentBean, this.child, annotation);
    }
  }

}
