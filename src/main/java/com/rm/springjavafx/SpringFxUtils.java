package com.rm.springjavafx;

import java.lang.reflect.Field;
import javafx.beans.property.Property;
import javafx.scene.*;
import javafx.scene.control.*;
import org.springframework.context.ApplicationContext;

public final class SpringFxUtils {
  
  private SpringFxUtils() {
  }

  /**
   *
   * @param appContext
   * @param beanId
   * @return 
   */
  @SuppressWarnings("UseSpecificCatch")
  public static Property<?> getValueProperty(ApplicationContext appContext, String beanId) {
    Property<?> valueProperty;
    if (!beanId.contains("#")) {
      valueProperty = (Property<?>) appContext.getBean(beanId);
    } else {
      try {
        String[] parts = beanId.split("#");
        Object parent = appContext.getBean(parts[0]);
        Field field = parent.getClass().getDeclaredField(parts[1]);
        field.setAccessible(true);
        valueProperty = (Property<?>) field.get(parent);
      } catch(Exception ex) {
        throw new RuntimeException("Error parsing property from beanId : '" + beanId + "'", ex); 
      }
    }
    return valueProperty;
  }

  /**
   * Find a {@link Node} within a {@link Parent} by it's ID.
   * <p>
   * This might not cover all possible {@link Parent} implementations but it's a
   * decent crack. {@link Control} implementations all seem to have their own
   * method of storing children along side the usual
   * {@link Parent#getChildrenUnmodifiable()} method.
   *
   * @param parent The parent of the node you're looking for.
   * @param id The ID of node you're looking for.
   * @return The {@link Node} with a matching ID or {@code null}.
   */
  @SuppressWarnings("unchecked")
  public static <T> T getChildByID(Parent parent, String id) {

    String nodeId = null;

    if (parent instanceof TitledPane) {
      TitledPane titledPane = (TitledPane) parent;
      Node content = titledPane.getContent();
      nodeId = content.idProperty().get();

      if (nodeId != null && nodeId.equals(id)) {
        return (T) content;
      }

      if (content instanceof Parent) {
        T child = getChildByID((Parent) content, id);

        if (child != null) {
          return child;
        }
      }
    }

    for (Node node : parent.getChildrenUnmodifiable()) {
      nodeId = node.idProperty().get();
      if (nodeId != null && nodeId.equals(id)) {
        return (T) node;
      }

      if (node instanceof SplitPane) {
        SplitPane splitPane = (SplitPane) node;
        for (Node itemNode : splitPane.getItems()) {
          nodeId = itemNode.idProperty().get();

          if (nodeId != null && nodeId.equals(id)) {
            return (T) itemNode;
          }

          if (itemNode instanceof Parent) {
            T child = getChildByID((Parent) itemNode, id);

            if (child != null) {
              return child;
            }
          }
        }
      } else if (node instanceof Accordion) {
        Accordion accordion = (Accordion) node;
        for (TitledPane titledPane : accordion.getPanes()) {
          nodeId = titledPane.idProperty().get();

          if (nodeId != null && nodeId.equals(id)) {
            return (T) titledPane;
          }

          T child = getChildByID(titledPane, id);

          if (child != null) {
            return child;
          }
        }
      } else if (node instanceof Parent) {
        T child = getChildByID((Parent) node, id);

        if (child != null) {
          return child;
        }
      }
    }
    return null;
  }
}
