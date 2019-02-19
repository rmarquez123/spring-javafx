package gov.inl.glass3.frcstdlr;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;

/**
 *
 * @author Ricardo Marquez
 */
public class FcstConfigurations {

  private final ZonedDateTime dateTime;
  private final TemporalAmount timeInterval;
  private final int forecastHorizonSteps;
  private final int repeatHorizonSteps;

  /**
   *
   * @param dateTime
   * @param temporalUnit
   * @param forecastHorizonSteps
   * @param repeatHorizonSteps
   */
  public FcstConfigurations(ZonedDateTime dateTime, TemporalAmount temporalUnit, int forecastHorizonSteps, int repeatHorizonSteps) {
    this.dateTime = dateTime;
    this.timeInterval = temporalUnit;
    this.forecastHorizonSteps = forecastHorizonSteps;
    this.repeatHorizonSteps = repeatHorizonSteps;
  }

  public ZonedDateTime getDateTime() {
    return dateTime;
  }

  public TemporalAmount getTimeInterval() {
    return timeInterval;
  }

  public int getForecastHorizonSteps() {
    return forecastHorizonSteps;
  }
  
  
  public int getRepeatHorizonSteps() {
    return repeatHorizonSteps;
  }
  

  @Override
  public String toString() {
    return "FcstConfigurations{" + "dateTime=" + dateTime + ", temporalUnit=" + timeInterval + ", forecastHorizonSteps=" + forecastHorizonSteps + '}';
  }

  /**
   *
   * @return
   */
  public TemporalAmount getForecastHorizon() {
    TemporalUnit unit = this.timeInterval.getUnits().get(0);
    long unitAmount = this.timeInterval.get(unit);
    long multipleAmount = this.forecastHorizonSteps * unitAmount;
    TemporalAmount result = Duration.of(multipleAmount, unit);
    return result;
  }

  /**
   * 
   * @param timeStep
   * @return 
   */
  public ZonedDateTime getDateTimeAtTimeStep(int timeStep) {
    TemporalAmount interval = this.getTimeInterval();
    TemporalUnit unit = interval.getUnits().get(0);
    long newTimesteps = timeStep * interval.get(unit);
    ZonedDateTime currentTimeStep = this.getDateTime().plus(newTimesteps, unit);
    return currentTimeStep;
  }

  /**
   *
   * @return
   */
  public ZonedDateTime getForecastDate() {
    TemporalAmount forecastHorizon = this.getForecastHorizon();
    ZonedDateTime forecastDate = ZonedDateTime.from(forecastHorizon.addTo(this.dateTime));
    return forecastDate;
  }
  
  /**
   * 
   * @return 
   */
  public TemporalAmount getRepeatHorizonTimeInterval() {
    TemporalUnit unit = this.timeInterval.getUnits().get(0);
    long unitAmount = this.timeInterval.get(unit);
    long multipleAmount = this.repeatHorizonSteps * unitAmount;
    TemporalAmount result = Duration.of(multipleAmount, unit);
    return result;
  }

  public static class Builder {

    private ZonedDateTime dateTime;
    private TemporalAmount temporalAmount;
    private int forecastHorizonSteps;
    private int repeatHorizonSteps;

    public Builder() {
    }
    
    public Builder(FcstConfigurations other) {
      this.dateTime = other.dateTime;
      this.forecastHorizonSteps = other.forecastHorizonSteps;
      this.repeatHorizonSteps = other.repeatHorizonSteps;
      this.temporalAmount = other.timeInterval;
    }

    public Builder setDateTime(ZonedDateTime dateTime) {
      this.dateTime = dateTime;
      return this;
    }

    public Builder setTimeInterval(TemporalAmount temporalUnit) {
      this.temporalAmount = temporalUnit;
      return this;
    }

    public Builder setForecastHorizonSteps(int forecastHorizonSteps) {
      this.forecastHorizonSteps = forecastHorizonSteps;
      return this;
    }
    
    
    public Builder setRepeatHorizonSteps(int repeatHorizonSteps) {
      this.repeatHorizonSteps = repeatHorizonSteps;
      return this;
    }

    public FcstConfigurations build() {
      FcstConfigurations result = new FcstConfigurations(dateTime, temporalAmount, forecastHorizonSteps, repeatHorizonSteps);
      return result;
    }
  }
}
