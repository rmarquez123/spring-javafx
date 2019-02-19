package gov.inl.glass3.calculators;

import gov.inl.glass3.conductors.Conductor;
import gov.inl.glass3.modelpoints.ModelPointGeometry;
import gov.inl.glass3.weather.WeatherRecord;
import javax.measure.Measure;
import javax.measure.quantity.Temperature;

/**
 *
 * @author Ricardo Marquez
 */
public class SteadyStateAmpacity implements Calculator {

  private final Delegates delegates;
  private final ModelPointGeometry geometry;
  private final Conductor conductor;

  /**
   *
   * @param conductor
   * @param geometry
   * @param delegates
   */
  public SteadyStateAmpacity(Conductor conductor, ModelPointGeometry geometry, Delegates delegates) {
    if (conductor == null) {
      throw new NullPointerException("Conductor cannot be null");
    }
    if (geometry == null) {
      throw new NullPointerException("Geometry cannot be null");
    }
    if (delegates == null) {
      throw new NullPointerException("Calculator delegates cannot be null");
    }
    this.conductor = conductor;
    this.geometry = geometry;
    this.delegates = delegates;
  }

  /**
   *
   * @param record
   * @return
   */
  @Override
  public double calculate(Measure<Temperature> conductorTemp, WeatherRecord record) {
    double heatTerms = this.getHeatTerms(conductorTemp, record);
    double r = getResistence(conductorTemp, record);
    double result = Math.max(10.00, Math.sqrt((heatTerms) / r));
    return result;
  }

  public double getResistence(Measure<Temperature> conductorTemp, WeatherRecord record) {
    ResistanceCalculator resistance = this.delegates.getResistanceCalculator(this.conductor, this.geometry);
    double r = resistance.calculate(conductorTemp, record);
    return r;
  }

  /**
   *
   * @param conductorTemp
   * @param record
   * @return
   */
  public double getHeatTerms(Measure<Temperature> conductorTemp, WeatherRecord record) {
    LongWaveRadCooling longwave = this.delegates.getLongWaveCalculator(this.conductor, this.geometry);
    SolarHeating solar = this.delegates.getSolarHeatingCalculator(this.conductor, this.geometry);
    ConvectionCooling convection = this.delegates.getConvectionCalculator(this.conductor, this.geometry);
    double qc = convection.calculate(conductorTemp, record);
    double qr = longwave.calculate(conductorTemp, record);
    double qs = solar.calculate(conductorTemp, record);
    double heatTerms = qc + qr - qs;
    return heatTerms;
  }

  /**
   *
   */
  public static interface Delegates {

    public LongWaveRadCooling getLongWaveCalculator(Conductor conductor, ModelPointGeometry geometry);

    public ResistanceCalculator getResistanceCalculator(Conductor conductor, ModelPointGeometry geometry);

    public SolarHeating getSolarHeatingCalculator(Conductor conductor, ModelPointGeometry geometry);

    public ConvectionCooling getConvectionCalculator(Conductor conductor, ModelPointGeometry geometry);

  }

}
