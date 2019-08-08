package com.rm.springjavafx.form;

import com.rm.datasources.RecordValue;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.util.StringConverter;

/**
 *
 * @author Ricardo Marquez
 */
public abstract class AbstractFormGroup {

  private final String idField;
  private final Property<RecordValue> selectedProperty = new SimpleObjectProperty<>();
  private final Property<List<RecordValue>> recordsProperty = new SimpleObjectProperty<>();
  private final FormGroup formGroup = new FormGroup();
  private final Map<Object, List<FormItem>> formItems = new HashMap<>();

  /**
   *
   * @param idField
   */
  public AbstractFormGroup() {
    FxFormGroup fxFormItem = this.getClass().getAnnotation(FxFormGroup.class);
    if (fxFormItem == null) {
      throw new IllegalStateException(
        String.format("Annotation '%s' is not defined in field '%s'", FxFormItem.class.getName(), this.getClass().getName()));
    }
    this.idField = fxFormItem.recordId();
    this.recordsProperty.addListener((obs, old, change) -> {
      this.updateSelected();
    });
    this.updateSelected();
    this.selectedProperty.addListener((obs, old, change) -> {
      System.out.println("selected = " + change);
      this.updateFormItems();
    });
  }
  
  /**
   * 
   * @param idValue
   * @return 
   */
  public boolean containsKey(Object idValue) {
    List<RecordValue> records = this.recordsProperty.getValue();
    boolean result;
    if (records != null) {
      result = records.stream().anyMatch((r)->Objects.equals(r.getIdValue(), idValue));
    } else {
      result = false;
    }
    return result;
  }

  /**
   *
   * @return
   */
  public String getIdField() {
    return idField;
  }

  /**
   *
   * @param aFormGroup
   */
  public void bind(FormGroup aFormGroup) {
    this.formGroup.getItems().addListener((ListChangeListener.Change<? extends FormItem> c) -> {
      aFormGroup.getItems().clear();
      if (c.next()) {
        if (c.wasAdded()) {
          aFormGroup.getItems().addAll(c.getAddedSubList());
        } else if (c.wasRemoved()) {
          aFormGroup.getItems().removeAll(c.getRemoved());
        }
      }
    });
    aFormGroup.getItems().clear();
    aFormGroup.getItems().addAll(this.formGroup.getItems());
  }

  /**
   *
   * @param id
   * @param consumer
   */
  public void forRecord(Object id, Consumer<RecordValue> consumer) {
    List<RecordValue> records = this.recordsProperty.getValue();
    Optional<RecordValue> record = records.stream()
      .filter((r) -> Objects.equals(id, r.get(idField)))
      .findAny();

    if (record.isPresent()) {
      consumer.accept(record.get());
    }
  }

  /**
   *
   * @return
   */
  public void selected(Object id) {
    this.forRecord(id, (r) -> this.selectedProperty.setValue(r));
  }

  /**
   *
   * @return
   */
  public ReadOnlyProperty<RecordValue> selectedRecordProperty() {
    return this.selectedProperty;
  }

  /**
   *
   * @param records
   */
  public void setRecords(List<RecordValue> records) {
    this.recordsProperty.setValue(records);
  }

  /**
   *
   * @return
   */
  public ReadOnlyProperty<List<RecordValue>> recordsProperty() {
    return this.recordsProperty;
  }

  /**
   *
   */
  public void saveSelected() {
    this.onSave(this.selectedProperty.getValue());
  }

  /**
   *
   * @param id
   */
  public void saveRecord(Object id) {
    this.forRecord(id, (r) -> this.onSave(r));
  }

  /**
   *
   */
  public void saveAll() {
    this.onSaveAll(this.recordsProperty.getValue());
  }

  /**
   *
   * @param records
   */
  public abstract void onSaveAll(List<RecordValue> records);

  /**
   *
   * @param records
   */
  public abstract void onSave(RecordValue records);

  /**
   *
   */
  private void updateSelected() {
    List<RecordValue> records = this.recordsProperty.getValue();
    if (records != null) {
      RecordValue first = records.stream().findFirst().orElse(null);
      this.selectedProperty.setValue(first);
    }
  }

  /**
   *
   */
  private void updateFormItems() {
    this.formGroup.getItems().clear();
    RecordValue selected = this.selectedProperty.getValue();
    if (selected != null) {
      Object idValue = selected.get(this.idField);
      if (!this.formItems.containsKey(idValue)) {
        List<FormItem> newFormItems = this.createFormItems(selected);
        this.formItems.put(idValue, new ArrayList<>(newFormItems));
      }
      List<FormItem> items = this.formItems.get(idValue);
      this.formGroup.getItems().addAll(items);
    }
  }

  /**
   *
   * @param r
   * @return
   */
  private List<FormItem> createFormItems(RecordValue r) {
    Field[] fields = this.getClass().getDeclaredFields();
    List<FormItem> items = new ArrayList<>();
    for (Field field : fields) {
      FxFormItem fxFormItem = field.getDeclaredAnnotation(FxFormItem.class);
      if (fxFormItem != null) {
        String id = fxFormItem.id();
        ObjectProperty valueProperty = new SimpleObjectProperty();

        Object value = r.get(id);
        valueProperty.set(value);
        StringConverter converter;
        try {
          converter = fxFormItem.converter().newInstance();
        } catch (Exception ex) {
          throw new RuntimeException(ex);
        }
        String label = fxFormItem.label();
        FormItem formItem = new FormItem(label, valueProperty, converter);
        items.add(formItem);
      }
    }
    return items;
  }

  /**
   *
   */
  void selectNext() {
    List<RecordValue> records = this.recordsProperty.getValue();
    if (records != null && !records.isEmpty()) {
      RecordValue selected = this.selectedProperty.getValue();
      if (selected != null) {
        int indexOf = records.indexOf(selected);
        int nextIndex = indexOf + 1;
        if (0 <= nextIndex && nextIndex < records.size()) {
          RecordValue newSelected = records.get(nextIndex);
          this.selected(newSelected.getIdValue());
        }
      } else {
        RecordValue record = records.stream().findFirst().get();
        this.selected(record.getIdValue());
      }
    }
  }

  /**
   *
   */
  void selectPrevious() {
    List<RecordValue> records = this.recordsProperty.getValue();
    if (records != null && !records.isEmpty()) {
      RecordValue selected = this.selectedProperty.getValue();
      if (selected != null) {
        int indexOf = records.indexOf(selected);
        int nextIndex = indexOf - 1;
        if (0 <= nextIndex && nextIndex < records.size()) {
          RecordValue newSelected = records.get(nextIndex);
          this.selected(newSelected.getIdValue());
        }
      }
    }
  }
}
