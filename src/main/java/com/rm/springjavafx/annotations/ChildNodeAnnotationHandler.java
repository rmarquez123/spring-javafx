package com.rm.springjavafx.annotations;

import com.rm.springjavafx.AnnotationHandler;
import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.SpringFxUtils;
import java.lang.reflect.Field;
import java.util.Map;
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

  @Autowired
  private FxmlInitializer fxmlInitializer;

  @Autowired
  private ApplicationContext appContext;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.fxmlInitializer.addAnnotationHandler(new AnnotationHandler() {
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
    for (Object bean : beans.values()) {
      
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
          } else if (parentFxml.isEmpty()){
            throw new RuntimeException("No fxml file specified for node: '" + field + "'");
          }
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
    this.fxmlInitializer.addFxml(fxml);
  }

  /**
   *
   * @throws BeansException
   * @throws RuntimeException
   */
  private void setBeanChildNodes() {
    Map<String, Object> beans = appContext.getBeansWithAnnotation(FxController.class);
    for (Object bean : beans.values()) {
       FxController fxController = bean.getClass().getDeclaredAnnotation(FxController.class);
      String fxml = fxController.fxml();
      Parent parent = this.fxmlInitializer.getRoot(fxml);
      setBeanChildNodes(parent, bean);
    }
  }
  
  /**
   * 
   * @param parent
   * @param bean 
   */
  public static void setBeanChildNodes(Parent parent, Object bean)  {
    Field[] fields = bean.getClass().getDeclaredFields();
    FxController fxController = bean.getClass().getDeclaredAnnotation(FxController.class);
    if (fxController == null) {
      throw new IllegalArgumentException(
        String.format("Bean '%s' is not annotated with '%s'", bean, FxController.class));
    }
    for (Field field : fields) {
      ChildNode childNode = field.getDeclaredAnnotation(ChildNode.class);
      if (childNode != null) {
        String fxml = childNode.fxml();
        if (fxml.isEmpty()) {
          fxml = fxController.fxml();
        }
        String id = childNode.id();
        Object child = SpringFxUtils.getChildByID(parent, id);
        if (child == null) {
          throw new IllegalStateException("Child node is null.  Check args: {"
            + "fxml = " + fxml
            + ", id = " + id
            + "}");
        }
        try {
          field.setAccessible(true);
          field.set(bean, child);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
          throw new RuntimeException(ex);
        }
      }
      
    }
  }

}
