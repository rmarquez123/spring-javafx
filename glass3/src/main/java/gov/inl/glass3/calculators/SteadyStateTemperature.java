package gov.inl.glass3.calculators;


import gov.inl.glass3.weather.WeatherRecord;
import javax.measure.Measure;
import javax.measure.quantity.Temperature;
import javax.measure.unit.SI;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;

/**
 *
 * @author Ricardo Marquez
 */
public class SteadyStateTemperature implements Calculator {

  private final SteadyStateAmpacity ampacityCalc;
  private final double load;

  /**
   *
   * @param load
   * @param ampacity
   */
  public SteadyStateTemperature(double load, SteadyStateAmpacity ampacity) {
    this.ampacityCalc = ampacity;
    this.load = load;
  }
  
  /**
   *
   * @param conductorTemp
   * @param record
   * @return
   */
  @Override
  public double calculate(Measure<Temperature> conductorTemp, WeatherRecord record) {
    BisectionSolver solver = new BisectionSolver();
    UnivariateFunction f = (t) -> {
      double current = this.load;
      Measure<Temperature> temp = Measure.valueOf(t, SI.CELSIUS); 
      double resistence = this.ampacityCalc.getResistence(temp, record);
      double heatElectric = Math.pow(current, 2.0) * resistence;
      double heatTerms = this.ampacityCalc.getHeatTerms(temp, record);
      double sumHeat = (heatElectric - heatTerms) * 0.233;
      return sumHeat;
    };
    double nextConductorTemp = solver.solve(1000, f, -20, 100);
    return nextConductorTemp;
  }
}
