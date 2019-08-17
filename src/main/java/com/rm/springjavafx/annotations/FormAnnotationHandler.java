package com.rm.springjavafx.annotations;

import com.rm.springjavafx.AnnotationHandler;
import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.SpringFxUtils;
import com.rm.springjavafx.form.AbstractForm;
import com.rm.springjavafx.form.AbstractFormGroup;
import com.rm.springjavafx.form.Form;
import com.rm.springjavafx.form.FormGroup;
import com.rm.springjavafx.form.FxForm;
import com.rm.springjavafx.form.FxFormGroup;
import com.rm.springjavafx.form.FxFormGroupId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.scene.layout.Pane;
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
public class FormAnnotationHandler implements AnnotationHandler, InitializingBean {

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
    Map<String, Object> formBeans = this.appContext.getBeansWithAnnotation(FxForm.class);
    for (Object formBean : formBeans.values()) {
      FxForm formConf = formBean.getClass().getDeclaredAnnotation(FxForm.class);
      String fxml = formConf.fxml();
      this.fxmlInitializer.addFxml(fxml);
    }
  }

  /**
   *
   */
  @Override
  public void setNodes() {
    Map<String, Object> formBeans = this.appContext.getBeansWithAnnotation(FxForm.class);
    Map<String, Object> formGroupBeans = this.appContext.getBeansWithAnnotation(FxFormGroup.class);
    for (Object value : formBeans.values()) {
      AbstractForm formBean;
      if (value instanceof AbstractForm) {
        formBean = (AbstractForm) value;
      } else {
        throw new IllegalStateException(
          String.format("Form bean '%s' is not an instance of '%s'", 
            value == null? "": value.getClass(), AbstractForm.class.getName())); 
      }
      FxForm formConf = value.getClass().getDeclaredAnnotation(FxForm.class);
      Form form = this.initializeForm(formConf);
      FxFormGroupId[] fxFormGroupIds = formConf.group();
      List<AbstractFormGroup> formGroups = new ArrayList<>();
      for (FxFormGroupId fxFormGroupId : fxFormGroupIds) {
        AbstractFormGroup formGroupBean = this.getFormGroupBean(formGroupBeans, fxFormGroupId);
        this.addFormGroupItems(formGroupBean, fxFormGroupId, form);
        formGroups.add(formGroupBean);
      }
      formBean.setFormGroups(formGroups);
    }
    
  }

  private void addFormGroupItems(AbstractFormGroup formGroupBean, FxFormGroupId fxFormGroupId, Form form){
    FormGroup formGroup = getFormGroup(formGroupBean, fxFormGroupId);
    if (formGroup != null) {
      form.getFormGroups().add(formGroup);
      formGroupBean.bind(formGroup);
    } else {
      throw new NullPointerException(
        String.format("No formgroup beans for group id = '%s'", fxFormGroupId.groupId()));
    }
  }

  private Form initializeForm(FxForm formConf) throws NullPointerException, RuntimeException {
    String fxml = formConf.fxml();
    String id = formConf.componentId();
    Pane pane;
    try {
      pane = (Pane) this.fxmlInitializer.getNode(fxml, id);
    } catch (IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
    if (pane == null) {
      throw new NullPointerException("Node does not exists.  Check args:{"
        + "fxml = " + fxml
        + ", id = " + id
        + "}");
    }
    Form form = new Form();
    SpringFxUtils.setNodeOnRefPane(pane, form);
    return form;
  }

  /**
   *
   * @param formGroupBeans
   * @param fxFormGroupId
   * @return
   */
  private FormGroup getFormGroup(Object formGroupBean, FxFormGroupId fxFormGroupId) {
    FormGroup result;
    FxFormGroup fxFormGroup = formGroupBean.getClass().getDeclaredAnnotation(FxFormGroup.class);
    String groupId = fxFormGroupId.groupId();
    if (groupId.equals(fxFormGroup.groupId())) {
      result = new FormGroup();
      result.textProperty().set(fxFormGroup.label());
    } else {
      throw new RuntimeException("mismatching form group ids");
    }
    return result;
  }

  /**
   *
   * @param formGroupBeans
   * @param fxFormGroupId
   * @return
   */
  private AbstractFormGroup getFormGroupBean(Map<String, Object> formGroupBeans, FxFormGroupId fxFormGroupId) {
    AbstractFormGroup result = null;
    for (Object formGroupBean : formGroupBeans.values()) {
      if (!(formGroupBean instanceof AbstractFormGroup)) {
        throw new IllegalStateException(
          String.format("formgroup bean '%s' does not extend '%s'", 
            fxFormGroupId.groupId(), AbstractFormGroup.class.getName())); 
      }
      FxFormGroup fxFormGroup = formGroupBean.getClass().getDeclaredAnnotation(FxFormGroup.class);
      String groupId = fxFormGroupId.groupId();
      if (groupId.equals(fxFormGroup.groupId())) {
        result = (AbstractFormGroup) formGroupBean;
        break;
      }
    }
    return result;
  }

}
