package gov.inl.glass3.calculators.ieee;

import gov.inl.glass3.conductors.Conductor;
import gov.inl.glass3.modelpoints.ModelPointGeometry;
import gov.inl.glass3.linesections.LineSection;
import gov.inl.glass3.calculators.ConvectionCooling;
import gov.inl.glass3.calculators.LongWaveRadCooling;
import gov.inl.glass3.calculators.ResistanceCalculator;
import gov.inl.glass3.calculators.SolarHeating;
import gov.inl.glass3.calculators.SteadyStateAmpacity;


/**
 *
 */
public class IeeeDelegates implements SteadyStateAmpacity.Delegates {

  private final LineSection line;

  public IeeeDelegates(LineSection line) {
    this.line = line;
  }

  @Override
  public LongWaveRadCooling getLongWaveCalculator(Conductor conductor, ModelPointGeometry geometry) {
    return new IeeeLongWaveHeatCooling(line, conductor);
  }

  @Override
  public ResistanceCalculator getResistanceCalculator(Conductor conductor, ModelPointGeometry geometry) {
    return new IeeeResistanceCalculator(conductor);
  }

  @Override
  public SolarHeating getSolarHeatingCalculator(Conductor conductor, ModelPointGeometry geometry) {
    return new IeeeSolarHeating(line, conductor);
  }

  @Override
  public ConvectionCooling getConvectionCalculator(Conductor conductor, ModelPointGeometry geometry) {
    return new IeeeConvectionCooling(geometry, conductor);
  }
  
}
