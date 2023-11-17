package com.rm._springjavafx.ignore.textfields;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.annotations.FxController;
import com.rm.springjavafx.annotations.childnodes.ChildNode;
import com.rm.springjavafx.events.ChangeEventListener;
import com.rm.springjavafx.events.OnChangeEvent;
import com.rm.springjavafx.nodes.checkbox.CheckBoxConf;
import com.rm.springjavafx.nodes.listview.ListViewConf;
import com.rm.springjavafx.nodes.textfields.dates.DateTextField;
import com.rm.springjavafx.nodes.textfields.files.FileTextField;
import com.rm.springjavafx.nodes.textfields.numeric.NumericTextField;
import com.rm.springjavafx.nodes.textfields.string.StringTextField;
import common.bindings.RmBindings;
import javafx.beans.property.Property;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.StringConverter;
import javax.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tornadofx.control.DateTimePicker;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@FxController(fxml = "fxml/TextFieldsExample.fxml")
@ChangeEventListener
public class TextFieldsExample implements InitializingBean {

  @Autowired
  private FxmlInitializer fxmlInitializer;

  @ChildNode(id = "numericTextField")
  @NumericTextField(format = "#,###.00", beanId = {"mutable.selected.record", "numberProperty"})
  private TextField numericTextField;

  @ChildNode(id = "stringTextField")
  @StringTextField(beanId = {"mutable.selected.record", "stringProperty"})
  private TextField stringTextField;
  
  @ChildNode(id = "fileTextField")
  @FileTextField(beanId = {"mutable.selected.record", "fileProperty"}, buttonRef = "selectFileBtn")
  private TextField fileTextField;
  
  @ChildNode(id = "datepicker")
  @DateTextField(beanId = {"mutable.selected.record", "dateTimeProperty"}, zoneRef = "zoneIdProperty")
  private DateTimePicker datepicker;
  
  @ChildNode(id = "selectCheckBox")
  @CheckBoxConf(beanId = {"mutable.selected.record", "selectProperty"})
  private CheckBox selectCheckBox;
  
  
  @ChildNode(id="selectFileBtn")
  private Button selectFileBtn;
 
  @ChildNode(id = "listview")
  @ListViewConf(listref = "mutable.records", selectedref = "mutable.selected.record")
  private ListView<RecordObservable> listview;
  
  @ChildNode(id="unselectBtn")
  private Button unselectBtn;
  
  @Resource(name="mutable.selected.record")
  private Property<RecordObservable> selectedProperty;
  
  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    this.fxmlInitializer.addListener((i) -> {
      this.listview.setCellFactory(this::getCell);
      RmBindings.bindActionOnAnyChange(() -> {
        this.listview.refresh();
      }, this.numericTextField.getTextFormatter().valueProperty(), 
      this.stringTextField.getTextFormatter().valueProperty());
      this.unselectBtn.setOnAction((evt)->this.selectedProperty.setValue(null));      
    });
  }
  /**
   * 
   * @param param
   * @return 
   */
  private ListCell<RecordObservable> getCell(Object param) {
    StringConverter<RecordObservable> converter = new StringConverter<RecordObservable>() {
      @Override
      public String toString(RecordObservable object) {
        Record r = object.toRecord();
        return String.format("%d : %f, %s", r.id(), r.number(), r.string());
      }

      @Override
      public RecordObservable fromString(String string) {
        return null;
      }
    };
    ListCell<RecordObservable> cell = new TextFieldListCell<>(converter);
    return cell;
  }
  
  /**
   * 
   * @param obs
   * @param old
   * @param change 
   */
  @OnChangeEvent(bean = "mutable.selected.record")
  public void onStringPropertyChange(Object obs, RecordObservable old, RecordObservable change) {
    
  }
}
