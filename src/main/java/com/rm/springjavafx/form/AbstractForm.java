package com.rm.springjavafx.form;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author Ricardo Marquez
 */
public class AbstractForm {

  private final FxForm fxForm;
  private List<AbstractFormGroup> formGroups;

  /**
   * 
   */
  public AbstractForm() {
    this.fxForm = this.getClass().getDeclaredAnnotation(FxForm.class);  
  }
  
  /**
   * 
   * @param formGroups 
   */
  public void setFormGroups(List<AbstractFormGroup> formGroups) {
    this.formGroups = formGroups;
    String idField = null;
    for (AbstractFormGroup formGroup : this.formGroups) {
      if (idField == null) {
        idField = formGroup.getIdField(); 
      } else {
        if (!Objects.equals(idField, formGroup.getIdField())) {
          throw new IllegalArgumentException(
            String.format("Form groups associated with form: '%s' have inconsistent id fields", this.fxForm.formId())); 
        }
      }
    }
  }

  /**
   *
   */
  public void selectPrevious() {
    for (AbstractFormGroup formGroup : this.formGroups) {
      formGroup.selectPrevious();
    }
  }

  /**
   *
   */
  public void selectNext() {
    for (AbstractFormGroup formGroup : this.formGroups) {
      formGroup.selectNext();
    }
  }
}
