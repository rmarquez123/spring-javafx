package gov.inl.glass3.frcstdlr;

import gov.inl.glass3.linesolver.ModelPointAmpacity;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.measure.Measure;
import javax.measure.quantity.Temperature;
import javax.measure.unit.SI;

/**
 *
 * @author Ricardo Marquez
 */
public class FrcstDlrController {

  private final FcstConfigurations fcstConfigurations;
  private final FrcstDlrCalculator calculator;
  
  /**
   *
   * @param fcstConfigurations
   */
  public FrcstDlrController(FcstConfigurations fcstConfigurations, FrcstDlrCalculator calculator) {
    this.fcstConfigurations = fcstConfigurations;
    this.calculator = calculator;
  }
  
  /**
   * 
   */
  public void runFcst(Map<String, Measure<Temperature>> maxConductorTemps, SeriesData seriesData, 
    FcstScheduler schduler, FcstDlrExporter exporter) {
    Objects.requireNonNull(maxConductorTemps, "Maximum conductor temperature cannot be null"); 
    Objects.requireNonNull(seriesData, "SeriesData cannot be null"); 
    Objects.requireNonNull(schduler, "Executor cannot be null");
    TemporalAmount timeInterval = this.fcstConfigurations.getRepeatHorizonTimeInterval();
    schduler.scheduleRuns(this.fcstConfigurations.getDateTime(), timeInterval, (t)->{
      FcstConfigurations confs = new FcstConfigurations.Builder(this.fcstConfigurations)
        .setDateTime(t)
        .build();
      List<ModelPointAmpacity> ampacities = new ArrayList<>();
      for (String modelPointId : seriesData.getModelPointNames()) {
        double ampacity = this.calculator.calculate(confs, modelPointId, maxConductorTemps.get(modelPointId), seriesData);
        ModelPointAmpacity amp = new ModelPointAmpacity(modelPointId, t, Measure.valueOf(ampacity, SI.AMPERE));
        ampacities.add(amp); 
      }
      exporter.export(t, ampacities); 
    });
  }

}
