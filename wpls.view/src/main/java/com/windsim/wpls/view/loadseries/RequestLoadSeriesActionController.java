package com.windsim.wpls.view.loadseries;

import com.windsim.wpls.view.io.sources.SourceType;
import com.windsim.wpls.view.io.sources.SupportedSourceType;
import javafx.beans.property.Property;
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
public class RequestLoadSeriesActionController implements InitializingBean {

  @Autowired
  FilesRequestLoadSeriesAction filesRequestLoadSeriesAction;
  
  @Autowired
  DbRequestLoadSeriesAction dbRequestLoadSeriesAction;

  @Autowired
  @Qualifier("requestLoadSeriesAction")
  Property<RequestLoadSeriesAction> requestLoadSeriesActionProperty;
  
  @Autowired
  @Qualifier("selectedSourceType")
  Property<SourceType> sourceTypeProperty;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.sourceTypeProperty.addListener((obs, old, change)->{
      SourceType sourceType = this.sourceTypeProperty.getValue();
      if (sourceType == SupportedSourceType.FILE) {
        this.requestLoadSeriesActionProperty.setValue(this.filesRequestLoadSeriesAction);
      } else if (sourceType == SupportedSourceType.DATABASE) {
        this.requestLoadSeriesActionProperty.setValue(this.dbRequestLoadSeriesAction);
      } else {
        this.requestLoadSeriesActionProperty.setValue(null);
      }
    });
  }

}
