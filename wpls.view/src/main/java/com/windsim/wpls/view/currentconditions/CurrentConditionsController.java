package com.windsim.wpls.view.currentconditions;

import com.rm.springjavafx.FxmlInitializer;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
public class CurrentConditionsController implements InitializingBean {
  @Autowired
  FxmlInitializer fxmlInitializer;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.fxmlInitializer.addListener((i)->{
      DemoTilesFx demo = new DemoTilesFx();
      demo.init();
      FlowGridPane p = demo.start();
      AnchorPane anchorPane; 
      try {
        anchorPane = (AnchorPane) this.fxmlInitializer.getNode("fxml/ModelPointsInfoPane.fxml", "currentconditionsPane");
      } catch (IllegalAccessException ex) {
        throw new RuntimeException(ex);
      }
      anchorPane.getChildren().add(p); 
      AnchorPane.setBottomAnchor(p, 0d);
      AnchorPane.setLeftAnchor(p, 0d);
      AnchorPane.setRightAnchor(p, 0d);
      AnchorPane.setTopAnchor(p, 0d);
    });
  }
}
