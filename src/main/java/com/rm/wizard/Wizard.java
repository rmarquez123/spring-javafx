/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.wizard;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.popup.Popup;
import com.rm.springjavafx.popup.PopupContent;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author Ricardo Marquez
 */
@Scope("prototype")
public abstract class Wizard implements Initializable, PopupContent {

  @Autowired
  private FxmlInitializer fxmlInitializer;

  @Autowired
  protected ApplicationContext applicationContext;
  protected Popup popup;
  protected final SortedSet<AbstractSettingPanel> panels = new TreeSet<>((i1, i2) -> {
    int i1Order = i1.getClass().getDeclaredAnnotation(SettingPanel.class).order();
    int i2Order = i2.getClass().getDeclaredAnnotation(SettingPanel.class).order();
    return Integer.compare(i1Order, i2Order);
  });
  protected final Property<AbstractSettingPanel> currentPanel = new SimpleObjectProperty<>();
  @FXML
  protected VBox labelsPanel;
  @FXML
  protected StackPane contentPane;
  @FXML
  protected Button previousBtn;
  @FXML
  protected Button nextBtn;

  private final String wizardId;

  /**
   *
   */
  public Wizard() {
    WizardParams a = this.getClass().getDeclaredAnnotation(WizardParams.class);
    this.wizardId = a.id();
  }

  /**
   * Initializes the controller class.
   *
   * @param url
   * @param rb
   */
  @Override
  public final void initialize(URL url, ResourceBundle rb) {
    Objects.requireNonNull(this.nextBtn, "'nextBtn' should be included in fxml: " + url); 
    Objects.requireNonNull(this.previousBtn, "'previousBtn' should be included in fxml: " + url); 
    Objects.requireNonNull(this.labelsPanel, "'labelsPanel' should be included in fxml: " + url); 
    Objects.requireNonNull(this.contentPane, "'contentPane' should be included in fxml: " + url); 
    
    this.nextBtn.setOnAction((evt) -> {
      AbstractSettingPanel current = this.currentPanel.getValue();
      if (current != null) {
        Iterator<AbstractSettingPanel> iterator = this.panels.tailSet(current).iterator();
        iterator.next();
        if (iterator.hasNext()) {
          AbstractSettingPanel next = iterator.next();
          this.currentPanel.setValue(next);
        } else {
          this.onSubmit();
        }
      }
    });
    this.previousBtn.setOnAction((evt) -> {
      AbstractSettingPanel current = this.currentPanel.getValue();
      if (current != null) {
        SortedSet<AbstractSettingPanel> headSet = this.panels.headSet(current);
        if (!headSet.isEmpty()) {
          AbstractSettingPanel last = headSet.last();
          if (last != null) {
            this.currentPanel.setValue(last);
          }
        }
      }
    });
    this.fxmlInitializer.addListener((i) -> {
      this.initPanels();
      this.currentPanel.addListener((obs, old, change) -> {
        this.updateContent();
      });
      this.updateContent();
    });
  }

  /**
   *
   */
  protected void updateContent() {
    AbstractSettingPanel current = this.currentPanel.getValue();
    this.contentPane.getChildren().clear();
    if (current != null) {
      SettingPanel annotation = current.getClass().getDeclaredAnnotation(SettingPanel.class);
      String fxml = annotation.fxml();
      Parent root = this.fxmlInitializer.getRoot(fxml);
      this.contentPane.getChildren().add(root);
      this.labelsPanel.getChildren().forEach((n) -> {
        if (n.getUserData().equals(current)) {
          ((Label) n).setTextFill(Color.BLACK);
        } else {
          ((Label) n).setTextFill(Color.GRAY);
        }
      });
      this.nextBtn.disableProperty().unbind();
      this.nextBtn.disableProperty().bind(current.nextReadyProperty().not());
      if (current.equals(this.panels.last())) {
        this.nextBtn.setText("Apply");
        this.nextBtn.setUserData(true);
      } else {
        this.nextBtn.setText("Next");
      }
    }

  }

  /**
   *
   * @throws BeansException
   */
  private void initPanels() throws BeansException {
    String[] beanNames = this.applicationContext.getBeanNamesForAnnotation(SettingPanel.class);
    this.labelsPanel.getChildren().clear();
    for (String string : beanNames) {
      AbstractSettingPanel p = this.applicationContext.getBean(string, AbstractSettingPanel.class);
      SettingPanel annotation = p.getClass().getDeclaredAnnotation(SettingPanel.class);
      if (annotation.wizardId().equals(this.wizardId)) {
        panels.add(p);
      }
    }
    int step = 0;
    for (AbstractSettingPanel panel : panels) {
      step++;
      SettingPanel annotation = panel.getClass().getDeclaredAnnotation(SettingPanel.class);
      if (this.fxmlInitializer.getRoot(annotation.fxml()) == null) {
        throw new IllegalStateException("Fxml root is null: '" + annotation.fxml() + "'");
      }
      Label label = new Label(step + "." + annotation.label());
      label.setUserData(panel);
      this.labelsPanel.getChildren().add(label);
    }
    if (this.panels.size() > 0) {
      this.currentPanel.setValue(this.panels.first());
    }
  }

  
  @Override
  public final void setPopupWindow(Popup popup) {
    this.popup = popup;
  }

  @Override
  public void onClose() throws IOException {
  }

  /**
   *
   */
  protected abstract void onSubmit();
  
  /**
   * 
   * @param b 
   */
  public void setDisabled(boolean b) {
    if (b) {
      this.panels.stream().forEach(this::disablePanel);
    } else {
      this.panels.stream().forEach(this::enablePanel);
    }
  }
  
  /**
   * 
   * @param p 
   */
  private void disablePanel(AbstractSettingPanel p) {
//    this.popup.contentNodeProperty().getValue().setDisable(true);
    p.nextReadyProperty().setValue(false);
  }
  
  /**
   * 
   * @param p 
   */
  private void enablePanel(AbstractSettingPanel p) {
//    this.popup.contentNodeProperty().getValue().setDisable(false);
    p.nextReadyProperty().setValue(true);
  }
}
