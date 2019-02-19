package com.windsim.wpls.view.projectopen;

import com.windsim.wpls.view.io.sources.SourceType;
import com.windsim.wpls.view.io.sources.SupportedSourceType;
import javafx.beans.property.Property;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class RequestProjectOpenActionController implements InitializingBean {

  @Autowired
  DbRequestProjectOpenAction dbRequestProjectOpenAction;
  
  @Autowired
  FilesRequestProjectOpenAction filesRequestProjectOpenAction;
  
  @Autowired
  @Qualifier("requestProjectOpenAction")
  Property<RequestProjectOpenAction> requestProjectOpenActionProperty;
  
  @Autowired
  @Qualifier("selectedSourceType")
  Property<SourceType> sourceTypeProperty;

  /**
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    this.sourceTypeProperty.addListener((obs, old, change)->{
      SourceType sourceType = this.sourceTypeProperty.getValue();
      if (sourceType == SupportedSourceType.FILE) {
        this.requestProjectOpenActionProperty.setValue(this.filesRequestProjectOpenAction);
      } else if (sourceType == SupportedSourceType.DATABASE) {
        this.requestProjectOpenActionProperty.setValue(this.dbRequestProjectOpenAction);
      } else {
        this.requestProjectOpenActionProperty.setValue(null);
      }
    });
  }

}
