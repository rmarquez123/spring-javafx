package com.rm.springjavafx.nodes.textfields.files;

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
import javafx.stage.FileChooser;
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
public class FileTextFieldProcessor implements InitializingBean, NodeProcessor {

  @Autowired
  private NodeProcessorFactory factory;

  @Autowired
  private ApplicationContext appcontext;

  @Autowired
  private FxmlInitializer fxmlInitializer;

  private List<Map<String, Object>> textfields = new ArrayList<>();

  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    this.factory.addProcessor(FileTextField.class, this);
    this.fxmlInitializer.addListener((i) -> {
      this.bindButtonAction();
    });
  }

  private void bindButtonAction() {
    this.textfields.forEach(this::bindButtonAction);
  }

  private void bindButtonAction(Map<String, Object> map) {

    FileTextField conf = (FileTextField) map.get("conf");
    String buttonRef = conf.buttonRef();
    if (!buttonRef.isEmpty()) {
      TextField textField = (TextField) map.get("textfield");
      TextFormatter<File> textFormatter = (TextFormatter<File>) textField.getTextFormatter();
      Button button;
      try {
        Field f = map.get("parentBean").getClass()
          .getDeclaredField(buttonRef);
        f.setAccessible(true);
        button = (Button) f
          .get(map.get("parentBean"));
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
      FileChooser chooser = new FileChooser();
      button.setOnAction((e) -> {
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
        File newfile = chooser.showOpenDialog(button.getScene().getWindow());
        if (newfile != null) {
          textFormatter.setValue(newfile);
        }
      });
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
      throw new IllegalArgumentException("Node is not an instance of " + TextField.class);
    }
    if (!(annotation instanceof FileTextField)) {
      throw new IllegalArgumentException("Annotation is not an instance of " + FileTextField.class);
    }
    TextField textfield = (TextField) node;
    FileTextField conf = (FileTextField) annotation;

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
