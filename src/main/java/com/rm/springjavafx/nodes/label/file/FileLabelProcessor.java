package com.rm.springjavafx.nodes.label.file;

import com.rm.springjavafx.nodes.NodeProcessor;
import com.rm.springjavafx.nodes.NodeProcessorFactory;
import com.rm.springjavafx.nodes.TextFormatterPropertyBinder;
import common.bindings.ObjectConverter;
import common.bindings.RmBindings;
import java.io.File;
import java.lang.annotation.Annotation;
import javafx.scene.control.Label;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
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
public class FileLabelProcessor implements NodeProcessor, InitializingBean {

  @Autowired
  private NodeProcessorFactory factory;

  @Autowired
  private ApplicationContext appcontext;

  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    this.factory.addProcessor(FileLabel.class, this);
  }

  /**
   *
   * @param node
   * @param annotation
   */
  @Override
  public void process(Object parentBean, Object node, Annotation annotation) {
    if (!(node instanceof Label)) {
      throw new IllegalArgumentException("Node is not an instance of " + Label.class);
    }
    if (!(annotation instanceof FileLabel)) {
      throw new IllegalArgumentException("Annotation is not an instance of " + FileLabel.class);
    }
    Label label = (Label) node;
    FileLabel conf = (FileLabel) annotation;
    TextFormatter<File> formatter = new TextFormatter<>(new StringConverter<File>() {
      @Override
      public String toString(File object) {
        return object == null ? null: object.getAbsolutePath();
      }

      @Override
      public File fromString(String string) {
        return (string == null || string.isEmpty()) ? null : new File(string); 
      }
    });
    RmBindings.bind1To2(label.textProperty(), formatter.valueProperty(), new ObjectConverter<String, File>() {
      @Override
      public File toObject(String reference) {
        return formatter.getValueConverter().fromString(reference);
      }

      @Override
      public String fromObject(File reference) {
        return formatter.getValueConverter().toString(reference); 
      }
    });
    label.setAlignment(conf.alignment());
    if (conf.beanId().length != 0) {
      Object bean = this.appcontext.getBean(conf.beanId()[0]);
      TextFormatterPropertyBinder<File> propertyBinder //
        = new TextFormatterPropertyBinder<>(formatter, conf.beanId(), bean);
      propertyBinder.bind();
    }
  }
}
