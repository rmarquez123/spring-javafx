package com.windsim.wpls.view.loadseries;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.popup.Popup;
import com.windsim.wpls.db.linesolver.pg.impl.PgDbWeatherRecordsLoader;
import gov.inl.glass3.linesolver.loaders.WeatherRecordsLoader;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component 
class DbRequestLoadSeriesAction implements RequestLoadSeriesAction {
  
  @Autowired
  FxmlInitializer fxmlInitalizer;

  @Autowired
  @Qualifier(value = "confirmLoadSeriesPopup")
  private Popup confirmLoadSeriesPopup;
  @Autowired
  ConfirmLoadSeriesController confirmLoadSeriesPopupController;

  @Autowired
  ConfirmLoadSeriesController loadSeriesController;

  /**
   *
   */
  @Override
  public void onAction() {
    Map<String, Object> credentials = new HashMap<>();
    EntityManager em = Persistence
      .createEntityManagerFactory("wpls_idaho_power_pu", credentials)
      .createEntityManager();
    WeatherRecordsLoader loader = new PgDbWeatherRecordsLoader(em);
    this.confirmLoadSeriesPopupController.setRecordsLoader(loader);
    this.confirmLoadSeriesPopup.show();
  }

}
