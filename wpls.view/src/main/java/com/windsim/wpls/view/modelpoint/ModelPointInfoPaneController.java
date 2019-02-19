package com.windsim.wpls.view.modelpoint;

import com.rm.springjavafx.FxmlInitializer;
import gov.inl.glass3.linesections.LineSection;
import gov.inl.glass3.linesolver.Model;
import gov.inl.glass3.modelpoints.ModelPoint;
import gov.inl.glass3.weather.WeatherStation;
import java.util.Objects;
import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
public class ModelPointInfoPaneController implements InitializingBean {

  @Autowired
  private FxmlInitializer initializer;

  @Autowired
  @Qualifier(value = "glassModelProperty")
  private Property<Model> modelProperty;

  @Autowired
  @Qualifier(value = "selectedModelPoint")
  private Property<ModelPoint> modelPointProperty;
  private GridPane lineGridPane;
  private GridPane stationGridPane;
  

  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    this.initializer.addListener((a) -> {
      try {
        this.lineGridPane = (GridPane) this.initializer.getNode("fxml/ModelPointsInfoPane.fxml", "lineInformationGridPane");
        this.stationGridPane = (GridPane) this.initializer.getNode("fxml/ModelPointsInfoPane.fxml", "stationGridPane");
      } catch (IllegalAccessException ex) {
        throw new RuntimeException(ex);
      }
    });
    this.modelPointProperty.addListener((obs, old, change) -> {
      if (change != null) {
        String modelPointId = change.getModelPointId();
        LineSection lineSection = modelProperty.getValue().getLineSectionByModelPointId(modelPointId);
        WeatherStation station = modelProperty.getValue().getWeatherStations().get(change.getWeatherStationId());
        ((Label) getNodeById("lineSection", lineGridPane)).setText(lineSection.getLineSectionId());
        ((Label) getNodeById("lineId", lineGridPane)).setText(modelPointId);
        ((Label) getNodeById("lineCenter", lineGridPane)).setText(change.getGeometry().getPoint().toString());
        ((Label) getNodeById("lineElevation", lineGridPane)).setText(String.valueOf(change.getGeometry().getElevation()));

        ((Label) getNodeById("stationId", stationGridPane)).setText(station.getName());
        ((Label) getNodeById("stationLocation", stationGridPane)).setText(station.getGeometry().toString());
        ((Label) getNodeById("stationElevation", stationGridPane)).setText(String.valueOf(station.getElevation()));
      }
    });

  }
  
  public static Node getNodeById(Object id, GridPane gridPane) {
    Node result = null;
    for (Node object : gridPane.getChildren()) {
      if (Objects.equals(object.getId(), id)) {
        result = object;
        break; 
      }
    }
    return result;
  }
  
}
