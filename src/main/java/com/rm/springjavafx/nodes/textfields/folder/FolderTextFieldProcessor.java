package com.rm.springjavafx.nodes.textfields.folder;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.nodes.NodeProcessor;
import com.rm.springjavafx.nodes.NodeProcessorFactory;
import com.rm.springjavafx.nodes.TextFormatterPropertyBinder;
import common.bindings.FileStringConverter;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.DirectoryChooser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
public class FolderTextFieldProcessor implements InitializingBean, NodeProcessor {

  @Autowired
  private NodeProcessorFactory factory;

  @Autowired
  private ApplicationContext appcontext;

  @Autowired
  private FxmlInitializer fxmlInitializer;

  private final List<Map<String, Object>> textfields = new ArrayList<>();

  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    this.factory.addProcessor(FolderTextField.class, this);
    this.fxmlInitializer.addListener((i) -> {
      this.bindButtonAction();
    });
  }
  
  /**
   * 
   */
  private void bindButtonAction() {
    this.textfields.forEach(this::bindButtonAction);
  }
  
  /**
   * 
   * @param map 
   */
  private void bindButtonAction(Map<String, Object> map) {
    FolderTextField conf = (FolderTextField) map.get("conf");
    String buttonRef = conf.buttonRef();
    if (!buttonRef.isEmpty()) {
      TextField textField = (TextField) map.get("textfield");
      TextFormatter<File> textFormatter = (TextFormatter<File>) textField.getTextFormatter();
      Button button = this.getButton(map, buttonRef);
      DirectoryChooser chooser = new DirectoryChooser();
      button.setOnAction((e) -> {
        this.onButtonAction(textFormatter, chooser, button);
      });
    }
  }

  /**
   * 
   * @param map
   * @param buttonRef
   * @return 
   */
  private Button getButton(Map<String, Object> map, String buttonRef) {
    Button button;
    try {
      Class<? extends Object> parentBeanClass = map.get("parentBean").getClass();
      Field f = ReflectionUtils.findField(parentBeanClass, buttonRef);
      if (f == null) {
        throw new RuntimeException( //
          String.format("button '%s' is not found in parent bean class '%s'", // 
            buttonRef, parentBeanClass));
      }
      f.setAccessible(true);
      button = (Button) f
        .get(map.get("parentBean"));
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    return button;
  }
  
  /**
   * 
   * @param textFormatter
   * @param chooser
   * @param button 
   */
  private void onButtonAction(TextFormatter<File> textFormatter, // 
    DirectoryChooser chooser, Button button) {
    File oldfile = textFormatter.getValue();
    if (oldfile != null) {
      File initialDirectory;
      if (oldfile.isFile()) {
        initialDirectory = oldfile.getParentFile();
      } else {
        initialDirectory = oldfile;
      }
      if (initialDirectory.exists()) {
        chooser.setInitialDirectory(initialDirectory);
      }
    }
    File newfile = chooser.showDialog(button.getScene().getWindow());
    if (newfile != null) {
      textFormatter.setValue(newfile);
    }
  }

  /**
   *
   * @param node
   * @param annotation
   */
  @Override
  public void process(Object parentBean, Object node, Annotation annotation) {
    if (!(node instanceof TextField)) {
      throw new IllegalArgumentException( //
        "Node is not an instance of " + TextField.class);
    }
    if (!(annotation instanceof FolderTextField)) {
      throw new IllegalArgumentException(//
        "Annotation is not an instance of " + FolderTextField.class);
    }
    TextField textfield = (TextField) node;
    FolderTextField conf = (FolderTextField) annotation;
    TextFormatter<File> formatter = new TextFormatter<>(new FileStringConverter());
    textfield.setTextFormatter(formatter);
    textfield.setAlignment(conf.alignment());
    if (conf.beanId().length != 0) {
      Object bean = this.appcontext.getBean(conf.beanId()[0]);
      TextFormatterPropertyBinder<File> propertyBinder //
        = new TextFormatterPropertyBinder<>(formatter, conf.beanId(), bean);
      propertyBinder.bind();
    }
    Map<String, Object> map = new HashMap<>();
    map.put("textfield", textfield);
    map.put("conf", conf);
    map.put("parentBean", parentBean);
    this.textfields.add(map);
  }
}
