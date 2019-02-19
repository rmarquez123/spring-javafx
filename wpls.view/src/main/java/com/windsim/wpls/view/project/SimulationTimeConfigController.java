package com.windsim.wpls.view.project;

import gov.inl.glass3.linesolver.SimulationTimeConfig;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
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
@Component()
@Lazy(false)
public class SimulationTimeConfigController implements InitializingBean {

  @Autowired
  @Qualifier("zoneId")
  private Property<ZoneId> zoneIdProperty;
  @Autowired
  @Qualifier("startDtHistorical")
  private Property<ZonedDateTime> startDtProperty;
  @Autowired
  @Qualifier("endDtHistorical")
  private Property<ZonedDateTime> endDtProperty;
  @Autowired
  @Qualifier("timeIntervalHistorical")
  private Property<TemporalAmount> timeIntervalProperty;
  @Autowired
  @Qualifier("simulatimeconfigHistorical")
  private Property<SimulationTimeConfig> simTimeConfigProperty;
  
  @Autowired
  @Qualifier("referenceDate")
  private Property<ZonedDateTime> referenceDate;

  public SimulationTimeConfigController() {
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.zoneIdProperty.addListener((obs, old, change) -> {
      this.updateSimTimeConfig();
    });
    this.startDtProperty.addListener((obs, old, change) -> {
      this.updateSimTimeConfig();
    });
    this.endDtProperty.addListener((obs, old, change) -> {
      this.updateSimTimeConfig();
      this.referenceDate.setValue(change);
    });
    
    this.timeIntervalProperty.addListener((obs, old, change) -> {
      this.updateSimTimeConfig();
    });
  }

  /**
   *
   */
  public void updateSimTimeConfig() {
    ZonedDateTime startDt = this.startDtProperty.getValue();
    ZonedDateTime endDt = endDtProperty.getValue();
    TemporalAmount timeInterval = timeIntervalProperty.getValue();
    if (startDt != null && endDt != null && timeInterval != null) {
      SimulationTimeConfig simulationTimeConfig = new SimulationTimeConfig(startDt, endDt, timeInterval);
      simTimeConfigProperty.setValue(simulationTimeConfig);
    } else {
      simTimeConfigProperty.setValue(null);
    }
  }
}
