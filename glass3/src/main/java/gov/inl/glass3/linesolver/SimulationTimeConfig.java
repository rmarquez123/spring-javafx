package gov.inl.glass3.linesolver;

import common.types.DateRange;
import common.types.DateTimeRange;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;

/**
 *
 * @author Ricardo Marquez
 */
public class SimulationTimeConfig {

  private final DateTimeRange dateTimeRange;
  private final TemporalAmount temporalUnit;

  /**
   *
   * @param startDt
   * @param endDt
   * @param temporalUnit
   */
  public SimulationTimeConfig(ZonedDateTime startDt, ZonedDateTime endDt, TemporalAmount temporalAmount) {
    this.dateTimeRange = new DateTimeRange(startDt, endDt);
    if (temporalAmount == null) {
      throw new NullPointerException("time interval cannot be null");
    }
    this.temporalUnit = temporalAmount;
  }

  /**
   *
   * @return 
   */
  public ZonedDateTime getStartDate() {
    return this.dateTimeRange.getStartDate();
  }

  /**
   *
   */
  public ZonedDateTime getEndDate() {
    return this.dateTimeRange.getEndDate();
  }

  /**
   *
   * @param startDt
   * @param endDt
   * @param temporalUnit
   * @return
   */
  static SimulationTimeConfig createFixedDateRangeSimulation(ZonedDateTime startDt, ZonedDateTime endDt, TemporalAmount temporalUnit) {
    return new SimulationTimeConfig(startDt, endDt, temporalUnit);
  }

  /**
   *
   * @return
   */
  public DateRange getListOfDays() {
    return this.dateTimeRange.getDateRange();
  }

  /**
   *
   * @return
   */
  DateTimeRange getDateTimeRange() {
    return this.dateTimeRange;
  }

  /**
   *
   * @return
   */
  TemporalAmount getTimeStep() {
    return this.temporalUnit;
  }

  /**
   *
   * @param dateTime
   * @return
   */
  public boolean contains(ZonedDateTime dateTime) {
    return this.dateTimeRange.contains(dateTime);
  }

  @Override
  public String toString() {
    return "SimulationTimeConfig{" + "dateTimeRange=" + dateTimeRange + ", temporalUnit=" + temporalUnit + '}';
  }

}
