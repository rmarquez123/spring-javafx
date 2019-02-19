package gov.inl.glass3.weather;

import gov.inl.glass3.customunits.PowerFlux;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.measure.Measure;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Temperature;
import javax.measure.quantity.Velocity;

/**
 *
 * @author Ricardo Marquez
 */
public class WeatherRecord {

  private final String stationId;
  private final ZonedDateTime dateTime;
  private final Measure<Temperature> ambientTemp;
  private final Measure<Velocity> windSpeed;
  private final Measure<Angle> windAngle;
  private final Measure<PowerFlux> solar;
  private final int airQuality;

  /**
   *
   * @param stationId
   * @param dateTime
   * @param ambientTemp
   * @param windSpeed
   * @param windAngle
   * @param airQuality
   * @param solar
   */
  public WeatherRecord(String stationId, ZonedDateTime dateTime,
    Measure<Temperature> ambientTemp,
    Measure<Velocity> windSpeed,
    Measure<Angle> windAngle,
    Measure<PowerFlux> solar, int airQuality) {

    this.stationId = stationId;
    this.dateTime = dateTime;

    this.ambientTemp = ambientTemp;
    this.windSpeed = windSpeed;
    this.windAngle = windAngle;
    this.airQuality = airQuality;
    this.solar = solar;
  }

  public String getStationId() {
    return stationId;
  }

  public ZonedDateTime getDateTime() {
    return dateTime;
  }

  /**
   *
   * @return
   */
  public Measure<Temperature> getAmbientTemp() {
    return ambientTemp;
  }

  /**
   * 
   * @return 
   */
  public Measure<Velocity> getWindSpeed() {
    return windSpeed;
  }

  public double getWindAngle() {
    return windAngle.getValue().doubleValue();
  }

  public int getAirQuality() {
    return airQuality;
  }

  public double getSolar() {
    return solar.getValue().doubleValue();
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 89 * hash + Objects.hashCode(this.stationId);
    hash = 89 * hash + Objects.hashCode(this.dateTime);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final WeatherRecord other = (WeatherRecord) obj;
    if (!Objects.equals(this.stationId, other.stationId)) {
      return false;
    }
    if (!Objects.equals(this.dateTime, other.dateTime)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "{" + "stationId=" + stationId
      + ", dateTime=" + dateTime
      + ", ambientTemp=" + ambientTemp
      + ", windSpeed=" + windSpeed
      + ", windAngle=" + windAngle
      + ", airQuality=" + airQuality
      + ", solar=" + solar + '}';
  }

  /**
   *
   */
  public static class Builder {

    private String stationId;
    private ZonedDateTime dateTime;
    private Measure<Temperature> ambientTemp;
    private Measure<Velocity> windSpeed;
    private Measure<Angle> windAngle;
    private Measure<PowerFlux> solar;
    private int airQuality;

    public Builder() {
    }

    public Builder(WeatherRecord other) {
      this.stationId = other.stationId;
      this.dateTime = other.dateTime;
      this.ambientTemp = other.ambientTemp;
      this.windSpeed = other.windSpeed;
      this.windAngle = other.windAngle;
      this.airQuality = other.airQuality;
      this.solar = other.solar;
    }

    public Builder setStationId(String stationId) {
      this.stationId = stationId;
      return this;
    }

    public Builder setDateTime(ZonedDateTime dateTime) {
      this.dateTime = dateTime;
      return this;
    }

    public Builder setAmbientTemp(Measure<Temperature> ambientTemp) {
      this.ambientTemp = ambientTemp;
      return this;
    }

    public Builder setWindSpeed(Measure<Velocity> windSpeed) {
      this.windSpeed = windSpeed;
      return this;
    }

    public Builder setWindAngle(Measure<Angle> windAngle) {
      this.windAngle = windAngle;
      return this;
    }

    public Builder setAirQuality(int airQuality) {
      this.airQuality = airQuality;
      return this;
    }

    public Builder setSolar(Measure<PowerFlux> solar) {
      this.solar = solar;
      return this;
    }

    /**
     *
     * @return
     */
    public WeatherRecord build() {
      if (stationId == null) {
        throw new NullPointerException("station cannot be null");
      }
      if (dateTime == null) {
        throw new NullPointerException("datetime cannot be null");
      }
      WeatherRecord result = new WeatherRecord(stationId, dateTime, this.ambientTemp, windSpeed, windAngle, solar, airQuality);
      return result;
    }

  }

}
