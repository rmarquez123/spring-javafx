package com.rm.springjavafx;

import common.RmExceptions;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaView;
import javafx.stage.Window;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ReflectionUtils;

public final class SpringFxUtils {

  private SpringFxUtils() {
  }

  /**
   *
   * @param alert
   * @param window
   */
  public static void setReferenceWind(Alert alert, Window window) {
    double xPos = window.getX() + window.getWidth() * 0.4;
    double yPos = window.getY() + window.getHeight() * 0.2;
    alert.setX(xPos);
    alert.setY(yPos);
  }

  /**
   *
   * @param refPane
   * @param childNode
   */
  public static void setNodeOnRefPane(Pane refPane, Node childNode) {
    if (refPane == null) {
      throw new NullPointerException("Reference pane cannot be null");
    }
    if (childNode == null) {
      throw new NullPointerException("Child node cannot be null");
    }
    refPane.getChildren().clear();
    refPane.getChildren().add(childNode);
    if (refPane instanceof AnchorPane) {
      AnchorPane.setLeftAnchor(childNode, 0d);
      AnchorPane.setTopAnchor(childNode, 0d);
      AnchorPane.setRightAnchor(childNode, 0d);
      AnchorPane.setBottomAnchor(childNode, 0d);
    } else if (refPane instanceof StackPane) {
      StackPane.setAlignment(childNode, Pos.CENTER);
      if (childNode instanceof Canvas) {
        ((Canvas) childNode).widthProperty().bind(refPane.widthProperty());
        ((Canvas) childNode).heightProperty().bind(refPane.heightProperty());
      }
    } else if (refPane instanceof HBox) {
      HBox.setHgrow(childNode, Priority.ALWAYS);
    } else if (refPane instanceof VBox) {
      VBox.setVgrow(childNode, Priority.ALWAYS);
    } else {
      throw new UnsupportedOperationException("Reference pane type is not supported: '"
        + refPane.getClass().getName() + "'");
    }

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
        Field field = ReflectionUtils.findField(parent.getClass(), parts[1]);
        field.setAccessible(true);
        valueProperty = (Property<?>) field.get(parent);
      } catch (Exception ex) {
        throw new RuntimeException("Error parsing property from beanId : '" + beanId + "'", ex);
      }
    }
    return valueProperty;
  }
  
  
  /**
   * 
   * @param appContext
   * @param beanId
   * @return 
   */
  public static ObservableList getValueObservableList(ApplicationContext appContext, String beanId) {
    ObservableList<?> valueProperty;
    if (!beanId.contains("#")) {
      try {
        valueProperty = (ObservableList<?>) appContext.getBean(beanId);
      } catch(Exception ex) {
        throw RmExceptions.create(ex, "Error getting bean '%s' ", beanId);
      }
    } else {
      try {
        String[] parts = beanId.split("#");
        Object parent = appContext.getBean(parts[0]);
        Field field = ReflectionUtils.findField(parent.getClass(), parts[1]);
        field.setAccessible(true);
        valueProperty = (ObservableList<?>) field.get(parent);
      } catch (Exception ex) {
        throw new RuntimeException("Error parsing observablelist from beanId : '" + beanId + "'", ex);
      }
    }
    return valueProperty;
  }

  /**
   * Find a {@link Node} within a {@link Parent} by it's ID.
   * <p>
   * This might not cover all possible {@link Parent} implementations but it's a decent
   * crack. {@link Control} implementations all seem to have their own method of storing
   * children along side the usual {@link Parent#getChildrenUnmodifiable()} method.
   *
   * @param parent The parent of the node you're looking for.
   * @param id The ID of node you're looking for.
   * @return The {@link Node} with a matching ID or {@code null}.
   */
  @SuppressWarnings("unchecked")
  public static <T> T getChildByID(Parent parent, String id) {
    Objects.requireNonNull(parent, "Parent cannot be null");
    String nodeId;
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
    if (parent instanceof MenuBar) {
      if (Objects.equals(((MenuBar) parent).getId(), id)) {
        return (T) parent;
      }
      List<Menu> menus = ((MenuBar) parent).getMenus();
      for (Menu menu : menus) {
        if (Objects.equals(menu.getId(), id)) {
          return (T) menu;
        }
        if (menu.getGraphic() != null && Objects.equals(menu.getGraphic().getId(), id)) {
          return (T) menu.getGraphic();
        }

        List<MenuItem> menuItems = menu.getItems();
        Object a = getChildByIDFromMenuItems(menuItems, id);
        if (a != null) {
          return (T) a;
        }
      }
    }
    if (parent instanceof ScrollPane) {
      Node content = ((ScrollPane) parent).getContent();

      if (content != null) {
        if (id.equals(content.getId())) {
          return (T) content;
        }
        Object result = getChildByID((Parent) content, id);
        if (result != null) {
          return (T) result;
        }
      }
    }

    Set<Node> children = new HashSet<>(parent == null ? Collections.EMPTY_SET : parent.getChildrenUnmodifiable());
    if (parent instanceof Pane) {
      children.addAll(((Pane) parent).getChildren());
    }

    if (parent instanceof SplitPane) {
      children.addAll(((SplitPane) parent).getItems());
    }

    if (parent instanceof TabPane) {
      ObservableList<Tab> tabs = ((TabPane) parent).getTabs();
      List<Node> n = tabs.stream().map((t) -> t.getContent()).collect(Collectors.toList());
      children.addAll(n);
    }

    for (Node node : children) {
      nodeId = node.getId();
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
      } else if (node instanceof TabPane) {
        TabPane accordion = (TabPane) node;
        for (Tab tab : accordion.getTabs()) {
          if (Objects.equals(tab.getId(), id)) {
            return (T) tab;
          }
          Node tabContent = tab.getContent();
          if (tabContent != null) {
            nodeId = tabContent.idProperty().get();
            if (nodeId != null && nodeId.equals(id)) {
              return (T) tabContent;
            }
            if (tabContent instanceof Parent) {
              T child = getChildByID((Parent) tabContent, id);
              if (child != null) {
                return child;
              }
            }
          } else {
            throw new IllegalStateException(
              String.format(
                "The tab with label '%s' must have a "
                + "pane content (i.e. Anchor Pane).", tab.getText()));
          }
        }
      } else if (node instanceof ToolBar) {
        ObservableList<Node> items = ((ToolBar) node).getItems();
        for (Node item : items) {
          nodeId = item.idProperty().get();
          if (nodeId != null && nodeId.equals(id)) {
            return (T) item;
          }
          if (item instanceof Parent) {
            T child = getChildByID((Parent) item, id);
            if (child != null) {
              return child;
            }
          }
        }
      } else if (node instanceof Parent) {
        T child = getChildByID((Parent) node, id);
        if (child != null) {
          return child;
        }
      } else if (node instanceof MediaView) {
        return (T) node;
      }
    }
    return null;
  }

  /**
   *
   */
  private static <T> T getChildByIDFromMenuItems(List<MenuItem> menuItems, String id) {
    for (MenuItem item : menuItems) {
      if (Objects.equals(item.getId(), id)) {
        return (T) item;
      }
      if (item instanceof Menu) {
        T child = getChildByIDFromMenuItems(((Menu) item).getItems(), id);
        if (child != null) {
          return child;
        }
      }
    }
    return null;
  }

  /**
   *
   * @param <T>
   * @param bean
   * @param annotationClass
   * @return
   */
  public static <T extends Annotation> T getAnnotation(Object bean, Class<T> annotationClass) {
    Objects.requireNonNull(bean, "Bean cannot be null");
    T r = bean.getClass().getAnnotation(annotationClass);
    if (r == null) {
      r = bean.getClass().getDeclaredAnnotation(annotationClass);
    }
    return r;
  }

  /**
   *
   * @param bean
   * @return
   */
  public static Field[] getFields(Object bean) {
    List<Field> list = new ArrayList<>();
    ReflectionUtils.doWithFields(bean.getClass(), list::add);
    Field[] result = list.toArray(new Field[]{});
    return result;
  }
}
