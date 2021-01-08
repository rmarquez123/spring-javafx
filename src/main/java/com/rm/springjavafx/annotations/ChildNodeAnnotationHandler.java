package com.rm.springjavafx.annotations;

import com.rm.springjavafx.AnnotationHandler;
import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.SpringFxUtils;
import com.rm.springjavafx.annotations.childnodes.CheckBoxAnnotationHandler;
import com.rm.springjavafx.annotations.childnodes.ChildNode;
import com.rm.springjavafx.annotations.childnodes.ChildNodeArgs;
import com.rm.springjavafx.nodes.NodeProcessor;
import com.rm.springjavafx.nodes.NodeProcessorFactory;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.function.Consumer;
import javafx.scene.Parent;
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
  private void setBeanChildNode(Object bean) {
    FxController fxController = bean.getClass().getDeclaredAnnotation(FxController.class);
    String fxml = fxController.fxml();
    Parent parent = fxmlInitializer.getRoot(fxml);
    try {
      setBeanChildNodes(parent, bean, this::handleField);
    } catch (Exception ex) {
      String message = String.format("Error creating child nodes for bean '%s'", bean.getClass().getName());
      throw new RuntimeException(message, ex);
    }
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
    Class<? extends Object> aClass = bean.getClass();
    Field[] fields = SpringFxUtils.getFields(bean);
    FxController fxController;
    if (Modifier.isAbstract(aClass.getModifiers())) {
      fxController = bean.getClass().getDeclaredAnnotation(FxController.class);
    } else {
      fxController = aClass.getDeclaredAnnotation(FxController.class);
    } 
    if (fxController == null) {
      throw new IllegalArgumentException(
        String.format("Bean '%s' is not annotated with '%s'", bean, FxController.class));
    }
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
            fxml = fxController.fxml();
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
    private NodeProcessHelper(NodeProcessorFactory factory, Object parentBean, Field field, Object child) {
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
