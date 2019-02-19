package gov.inl.glass3.frcstdlr.impl.movingave;

import gov.inl.glass3.calculators.SteadyStateAmpacity;
import gov.inl.glass3.calculators.SteadyStateTemperature;
import gov.inl.glass3.conductors.Conductor;
import gov.inl.glass3.customunits.RmSI;
import gov.inl.glass3.frcstdlr.FcstConfigurations;
import gov.inl.glass3.frcstdlr.FrcstDlrCalculator;
import gov.inl.glass3.frcstdlr.SeriesData;
import gov.inl.glass3.modelpoints.ModelPointGeometry;
import gov.inl.glass3.weather.WeatherRecord;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import javax.measure.Measure;
import javax.measure.quantity.ElectricCurrent;
import javax.measure.quantity.Temperature;
import javax.measure.unit.SI;

/**
 *
 * @author Ricardo Marquez
 */
public class DlrMvngAveFrcstDlrCalculator implements FrcstDlrCalculator {

  private final Conductor conductor;

  private final ModelPointGeometry geometry;

  private final SteadyStateAmpacity.Delegates delegates;
  private final SteadyStateAmpacity ssAmpacityCalculator;

  /**
   *
   * @param conductor
   * @param geometry
   * @param delegates
   */
  public DlrMvngAveFrcstDlrCalculator(Conductor conductor,
    ModelPointGeometry geometry,
    SteadyStateAmpacity.Delegates delegates) {
    this.conductor = conductor;
    this.geometry = geometry;
    this.delegates = delegates;
    this.ssAmpacityCalculator = new SteadyStateAmpacity(this.conductor, this.geometry, this.delegates);
  }

  /**
   *
   * @param configs
   * @param maxConductorTemp
   * @param seriesData
   * @return
   */
  @Override
  public double calculate(FcstConfigurations configs, String modelPointId, 
    Measure<Temperature> maxConductorTemp, SeriesData seriesData) {
    double ssTemp = calcSteadyStateTemperature(configs, modelPointId, maxConductorTemp, seriesData);
    double heatingTerms = calcMovingAverageFlux(configs, modelPointId, maxConductorTemp, seriesData);
    double deltaT = configs.getForecastHorizon().get(ChronoUnit.SECONDS);
    double heatCapacity = conductor.getLinearHeatCapacity().doubleValue(RmSI.JL_PER_METER_PER_KEL);
    double unsteadyTerm = heatCapacity * (maxConductorTemp.doubleValue(SI.CELSIUS) - ssTemp) / deltaT;
    double resistence = this.getResistence(maxConductorTemp, modelPointId, seriesData, configs); 
    double result = Math.sqrt( (unsteadyTerm + heatingTerms)/resistence);
    return result;
  }

  private double getResistence(Measure<Temperature> maxConductorTemp, String modelPointId, SeriesData seriesData, FcstConfigurations configs) {
    return this.ssAmpacityCalculator.getResistence(maxConductorTemp, seriesData.getWeatherRecord(modelPointId, configs.getForecastDate()));
  }

  /**
   *
   * @param configs
   * @param seriesData
   * @param maxConductorTemp
   * @return
   * @throws ArithmeticException
   */
  private double calcSteadyStateTemperature(FcstConfigurations configs, String modelPointId, 
    Measure<Temperature> maxConductorTemp, SeriesData seriesData) {
    
    ZonedDateTime currentTimeStep = configs.getDateTime();
    Measure<ElectricCurrent> loadMeasure = seriesData.getLoad(modelPointId, currentTimeStep);
    Objects.requireNonNull(loadMeasure, "Load for time step is null. Check args : {"
      + " current time step = " + currentTimeStep
      + "}"); 
    WeatherRecord currWeatherRecord = seriesData.getWeatherRecord(modelPointId, currentTimeStep);
    double load = loadMeasure.doubleValue(SI.AMPERE);
    SteadyStateTemperature ssTemperatureCalculator = new SteadyStateTemperature(load, this.ssAmpacityCalculator);
    double ssTemp = ssTemperatureCalculator.calculate(maxConductorTemp, currWeatherRecord);
    return ssTemp;
  }

  /**
   *
   * @param configs
   * @param maxConductorTemp
   * @param seriesData
   * @return
   */
  private double calcMovingAverageFlux(FcstConfigurations configs, String modelPointId, 
    Measure<Temperature> maxConductorTemp, SeriesData seriesData) {
    ZonedDateTime currentTimeStep;
    int numSteps = configs.getForecastHorizonSteps();
    double movingAverage = 0.0;
    for (int i = 0; i < numSteps; i++) {
      currentTimeStep = configs.getDateTimeAtTimeStep(i);
      WeatherRecord weatherRecord = seriesData.getFcstWeatherRecord(modelPointId, currentTimeStep);
      double ampacity = this.ssAmpacityCalculator.getHeatTerms(maxConductorTemp, weatherRecord);
      movingAverage += ampacity;
    }
    movingAverage = movingAverage / numSteps;
    return movingAverage;
  }

}
