package com.rm.springjavafx.annotations;

import com.rm.springjavafx.AnnotationHandler;
import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.popup.Popup;
import com.rm.springjavafx.popup.PopupContent;
import java.util.Map;
import javafx.scene.Parent;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
public class PopupComponentAnnotationHandler implements AnnotationHandler, InitializingBean {

  @Autowired
  private ApplicationContext appContext;
  @Autowired
  private FxmlInitializer fxmlInitializer;

  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    this.fxmlInitializer.addAnnotationHandler(this);
  }

  /**
   *
   */
  @Override
  public void readyFxmls() {
    Map<String, Object> beans = appContext.getBeansWithAnnotation(PopupComponent.class);
    for (Object bean : beans.values()) {
      FxController fxController = bean.getClass().getDeclaredAnnotation(FxController.class);
      String fxml = fxController.fxml();
      if (this.getClass().getClassLoader().getResource(fxml) == null) {
        throw new IllegalStateException("Fxml does not exist: '" + fxml + "'");
      }
      if (!FilenameUtils.getExtension(fxml).endsWith("fxml")) {
        throw new RuntimeException("File does not have .fxml extension: '" + fxml + "'");
      }
      this.fxmlInitializer.addFxml(fxml);
    }
  }

  /**
   *
   */
  @Override
  public void setNodes() {
    Map<String, Object> beans = appContext.getBeansWithAnnotation(PopupComponent.class);
    for (Object bean : beans.values()) {
      FxController fxController = bean.getClass().getDeclaredAnnotation(FxController.class);
      PopupComponent p = bean.getClass().getDeclaredAnnotation(PopupComponent.class);
      String fxml = fxController.fxml();
      Parent node = this.fxmlInitializer.getRoot(fxml);
      BeanDefinitionRegistry registry = (BeanDefinitionRegistry) this.appContext.getAutowireCapableBeanFactory();
      BeanDefinition dynamicBean = BeanDefinitionBuilder
        .rootBeanDefinition(Popup.class)
        .getBeanDefinition();
      dynamicBean.getPropertyValues().add("node", node); 
      dynamicBean.getPropertyValues().add("controller", (PopupContent) bean); 
      registry.registerBeanDefinition(p.id(), dynamicBean);
    }
  }

}
