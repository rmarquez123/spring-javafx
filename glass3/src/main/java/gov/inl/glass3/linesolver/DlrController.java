package gov.inl.glass3.linesolver;

import common.types.DateTimeRange;
import gov.inl.glass3.calculators.SteadyStateAmpacity;
import gov.inl.glass3.calculators.ieee.IeeeDelegates;
import gov.inl.glass3.conductors.Conductor;
import gov.inl.glass3.linesections.LineSection;
import gov.inl.glass3.modelpoints.ModelPoint;
import gov.inl.glass3.modelpoints.ModelPointGeometry;
import gov.inl.glass3.weather.WeatherRecord;
import gov.inl.glass3.weather.WeatherRecords;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javax.measure.Measure;
import javax.measure.quantity.ElectricCurrent;
import javax.measure.quantity.Temperature;
import static javax.measure.unit.SI.AMPERE;

/**
 *
 * @author Ricardo Marquez
 */
public class DlrController {

  /**
   *
   */
  private final DoubleProperty progressProperty = new SimpleDoubleProperty(0.0);

  private final BooleanProperty calculatingProperty = new SimpleBooleanProperty(false);

  private final BooleanProperty cancelledProperty = new SimpleBooleanProperty(false);

  /**
   *
   */
  public DlrController() {
  }

  /**
   * Progress property which will range between 0.0 and 1.0. The progress is updated after
   * execution of all dlr calculations in a day. For example, if there are 30 days in a
   * calculation then the progress property will be updated 30 times.
   *
   * @return
   */
  public DoubleProperty progressProperty() {
    return this.progressProperty;
  }

  public BooleanProperty calculatingProperty() {
    return this.calculatingProperty;
  }

  /**
   *
   * @param model
   * @param config
   * @param weatherRecords
   * @return
   */
  public ModelPointAmpacities computeSteadyStateAmpacity(Model model,
    SimulationTimeConfig config, WeatherRecords weatherRecords) {

    DateTimeRange dateTimeRange = config.getDateTimeRange();
    TemporalAmount timeStep = config.getTimeStep();
    Map<String, Set<ModelPointAmpacity>> map = new HashMap<>();
    double total = dateTimeRange.getNumberOfSteps(timeStep);
    double count = 0;
    this.progressProperty.setValue(0.0);
    this.calculatingProperty.setValue(true);
    this.cancelledProperty.setValue(false);
    try {
      for (ZonedDateTime dateTime : dateTimeRange.iterator(timeStep)) {
        count++;
        for (ModelPoint modelPoint : model.getModelPoints()) {
          if (this.cancelledProperty.getValue()) {
            break;
          }
          this.computeSteadyStateAmpacity(model, weatherRecords, modelPoint, dateTime, map);
        }
        if (this.cancelledProperty.getValue()) {
          break;
        }
        double progress = count / total;
        this.progressProperty.setValue(progress);
      }
    } finally {
      this.calculatingProperty.set(false);
    }
    ModelPointAmpacities result;
    if (!this.cancelledProperty.getValue()) {
      result = new ModelPointAmpacities(map);      
    } else {
      result = null;
    }
    return result;
  }

  /**
   *
   * @param model
   * @param weatherRecords
   * @param modelPoint
   * @param dateTime
   * @param map
   */
  private void computeSteadyStateAmpacity(Model model, WeatherRecords weatherRecords,
    ModelPoint modelPoint, ZonedDateTime dateTime, Map<String, Set<ModelPointAmpacity>> map) {
    String modelPointId = modelPoint.getModelPointId();

    //  Create ampacity calculator.
    SteadyStateAmpacity ampacityCalculator = this.getAmpacityCalculator(model, modelPointId);
    //  Invoking ampacity calculator
    LineSection lineSection = model.getLineSectionByModelPointId(modelPointId);
    Measure<Temperature> maxTemperature = lineSection.getMaxTemperature();
    String stationId = modelPoint.getWeatherStationId();
    WeatherRecord record = weatherRecords.get(stationId, dateTime);
    WeatherRecord adjustedRecord = model.getLookupTables().applyAdjustments(modelPointId, record);
    double ampacity = ampacityCalculator.calculate(maxTemperature, adjustedRecord);

    //  Adding result to map.
    if (!map.containsKey(modelPointId)) {
      map.put(modelPointId, new HashSet<>());
    }
    Measure<ElectricCurrent> quanity = Measure.valueOf(ampacity, AMPERE);
    ModelPointAmpacity modelPointAmpacity = new ModelPointAmpacity(modelPointId, dateTime, quanity);
    map.get(modelPointId).add(modelPointAmpacity);
  }

  /**
   *
   * @param model
   * @param modelPointId
   * @return
   */
  private SteadyStateAmpacity getAmpacityCalculator(Model model, String modelPointId) {
    LineSection lineSection = model.getLineSectionByModelPointId(modelPointId);
    // Creatinng delegates for ampacity calculator
    SteadyStateAmpacity.Delegates delegates = new IeeeDelegates(lineSection);
    // Instantiating ampacity calculator.
    Conductor conductor = model.getConductorByModelPointId(modelPointId);
    ModelPoint modelPoint = model.getModelPoints().get(modelPointId);
    ModelPointGeometry geometry = modelPoint.getGeometry();
    SteadyStateAmpacity ampacityCalculator = new SteadyStateAmpacity(conductor, geometry, delegates);
    return ampacityCalculator;
  }

  /**
   *
   */
  public void cancel() {
    this.cancelledProperty.setValue(true);
  }
}
