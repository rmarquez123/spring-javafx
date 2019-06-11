package com.rm.springjavafx.annotations;

import com.rm.springjavafx.AnnotationHandler;
import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.SpringFxUtils;
import java.util.Map;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
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
public class FxAttachAnnotationHandler implements InitializingBean  {

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
        setChildNodeToBean();
      }
    });
  }

  /**
   *
   */
  private void onReadyFxmls() {
    Map<String, Object> beans = appContext.getBeansWithAnnotation(FxAttach.class);
    for (Object bean : beans.values()) {
      FxController fxController = bean.getClass().getDeclaredAnnotation(FxController.class);
      if (fxController == null) {
        throw new IllegalStateException("Beans with FxAttach annotation must also have FxController annotation");
      }
      String parentFxml = fxController.fxml();
      addFxml(parentFxml);
      FxAttach fxAttach = bean.getClass().getDeclaredAnnotation(FxAttach.class);
      String fxml = fxAttach.fxml();
      addFxml(fxml);
    }
  }

  /**
   *
   * @throws BeansException
   * @throws RuntimeException
   */
  private void setChildNodeToBean() {
    Map<String, Object> beans = appContext.getBeansWithAnnotation(FxAttach.class);
    for (Object bean : beans.values()) {
      FxController fxController = bean.getClass().getDeclaredAnnotation(FxController.class);
      Parent root = this.fxmlInitializer.getRoot(fxController.fxml()); 
      FxAttach fxAttach = bean.getClass().getDeclaredAnnotation(FxAttach.class);
      Pane refPane; 
      try {
        refPane = (Pane) this.fxmlInitializer.getNode(fxAttach.fxml(), fxAttach.id());
      } catch (IllegalAccessException ex) {
        throw new RuntimeException(ex); 
      }
      SpringFxUtils.setNodeOnRefPane(refPane, root);
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
}
