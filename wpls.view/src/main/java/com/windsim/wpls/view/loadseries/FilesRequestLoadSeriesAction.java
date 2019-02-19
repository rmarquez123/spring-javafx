package com.windsim.wpls.view.loadseries;

import com.rm.springjavafx.FxmlInitializer;
import com.rm.springjavafx.popup.Popup;
import gov.inl.glass3.linesolver.impl.files.FilesWeatherRecordsLoader;
import gov.inl.glass3.linesolver.impl.files.SeriesDirectory;
import gov.inl.glass3.linesolver.loaders.WeatherRecordsLoader;
import java.io.File;
import java.time.ZoneId;
import javafx.beans.property.Property;
import javafx.stage.DirectoryChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component 
class FilesRequestLoadSeriesAction implements RequestLoadSeriesAction {

  @Autowired
  @Qualifier("zoneId")
  private Property<ZoneId> zoneIdProperty;
  @Autowired
  FxmlInitializer fxmlInitalizer;

  @Autowired
  @Qualifier(value = "confirmLoadSeriesPopup")
  private Popup confirmLoadSeriesPopup;
  @Autowired
  ConfirmLoadSeriesController confirmLoadSeriesPopupController;

  @Autowired
  ConfirmLoadSeriesController loadSeriesController;

  @Autowired
  @Qualifier("seriesDirectoryHistorical")
  Property<File> seriesDirectoryProperty;

  @Override
  public void onAction() {
    DirectoryChooser chooser = new DirectoryChooser();
    File dir = chooser.showDialog(fxmlInitalizer.getMainRoot().getScene().getWindow());
    if (dir != null) {
      SeriesDirectory seriesDirectory = new SeriesDirectory(dir);
      ZoneId zoneId = zoneIdProperty.getValue();
      WeatherRecordsLoader loader = new FilesWeatherRecordsLoader(seriesDirectory, zoneId);
      this.confirmLoadSeriesPopupController.setRecordsLoader(loader);
      this.confirmLoadSeriesPopup.show();
      this.seriesDirectoryProperty.setValue(dir);
    }
  }

}
