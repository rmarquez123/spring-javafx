package com.rm.springjavafx.tabs;

import com.rm.springjavafx.AnnotationHandler;
import com.rm.springjavafx.FxmlInitializer;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class TabsAnnotationHandler implements InitializingBean, AnnotationHandler {
  @Autowired
  private FxmlInitializer fxmlInitializer;
  @Autowired
  private ApplicationContext appContext;
  
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
    Map<String, Object> tabItemBeans = appContext.getBeansWithAnnotation(TabItem.class);
    for (Object value : tabItemBeans.values()) {
      String fxml = value.getClass().getDeclaredAnnotation(TabItem.class).fxml(); 
      this.addFxml(fxml);
    }
    Map<String, Object> beans = appContext.getBeansWithAnnotation(TabsGroup.class);
    for (Object value : beans.values()) {
      String fxml = value.getClass().getDeclaredAnnotation(TabsGroup.class).fxml(); 
      this.addFxml(fxml);
    }
  }
  
  /**
   * 
   * @param fxml
   * @throws RuntimeException
   * @throws IllegalStateException 
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
  @Override
  public void setNodes() { 
  }
    
}
