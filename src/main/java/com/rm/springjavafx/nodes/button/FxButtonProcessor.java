package com.rm.springjavafx.nodes.button;

import com.rm.springjavafx.nodes.NodeProcessor;
import com.rm.springjavafx.nodes.NodeProcessorFactory;
import com.rm.springjavafx.popup.Popup;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Consumer;
import javafx.scene.control.Button;
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
public class FxButtonProcessor implements InitializingBean, NodeProcessor {

  @Autowired
  private NodeProcessorFactory factory;

  @Autowired
  private ApplicationContext appcontext;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.factory.addProcessor(FxButton.class, this);
  }

  @Override
  public void process(Object parentBean, Object node, Annotation annotation) {
    if (!(node instanceof Button)) {
      throw new IllegalArgumentException("Node is not an instance of " + Button.class);
    }
    if (!(annotation instanceof FxButton)) {
      throw new IllegalArgumentException("Annotation is not an instance of " + FxButton.class);
    }
    Button button = (Button) node;
    FxButton conf = (FxButton) annotation;
    Consumer<Object> handler;
    if (!conf.onActionBtn().isEmpty()) {
      handler = this.onMethodActionHandler(parentBean, conf);
    } else if (!conf.onActionPopup().isEmpty()) {
      handler = this.onPopupActionHandler(button, conf);
    } else {
      throw new RuntimeException(
        String.format("Error on configuring bean '%s' with '%s' annotation. "
        + "Please define either '%s' or '%s' configuration", 
          button.getId(), FxButton.class, "action method", "popup"));
    }
    button.setOnAction(handler::accept);
  }

  /**
   *
   * @param parentBean
   * @param conf
   * @return
   */
  private Consumer<Object> onMethodActionHandler(Object parentBean, FxButton conf) {
    Consumer<Object> handler;
    Method onAction;
    try {
      onAction = Objects //
        .requireNonNull(ReflectionUtils.findMethod(parentBean.getClass(), conf.onActionBtn(), Object.class), //
          String.format("method '%s' not found for bean '%s'", conf.onActionBtn(), parentBean));
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }

    handler = (evt) -> {
      try {
        onAction.setAccessible(true);
        onAction.invoke(parentBean, evt);
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    };
    return handler;
  }

  /**
   *
   * @param parentBean
   * @param button
   * @param conf
   * @return
   */
  private Consumer<Object> onPopupActionHandler(Button button, FxButton conf) {
    Consumer<Object> handler;
    handler = (evt) -> {
      try {
        String popupId = conf.onActionPopup();
        Popup popup = (Popup) this.appcontext.getBean(popupId);
        popup.windowProperty().setValue(button.getScene().getWindow());
        popup.show();
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    };
    return handler;
  }

}
