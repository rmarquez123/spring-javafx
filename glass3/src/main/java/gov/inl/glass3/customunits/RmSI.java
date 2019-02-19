package gov.inl.glass3.customunits;

import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

/**
 *
 * @author Ricardo Marquez
 */
public class RmSI {

  /**
   *
   */
  public static Unit<PowerFlux> WATTS_PER_SQUARE_METER = SI.WATT.divide(SI.SQUARE_METRE).asType(PowerFlux.class);

  /**
   *
   */
  public static Unit<LinearElectricalResistence> OHMS_PER_KM = SI.OHM.divide(SI.KILOMETRE).asType(LinearElectricalResistence.class);

  /**
   *
   */
  public static Unit<LinearElectricalResistence> OHMS_PER_MILE = SI.OHM.divide(NonSI.MILE).asType(LinearElectricalResistence.class);

  /**
   *
   */
  public static Unit<LinearElectricalResistence> OHMS_PER_M = SI.OHM.divide(SI.METRE).asType(LinearElectricalResistence.class);
  
  /**
   *
   */
  public static Unit<LinearMassDensity> KG_PER_METER = SI.KILOGRAM.divide(SI.METRE).asType(LinearMassDensity.class);
  
  /**
   *
   */
  public static Unit<LinearMassDensity> LB_PER_FOOT = NonSI.POUND.divide(NonSI.FOOT).asType(LinearMassDensity.class);
  
  /**
   *
   */
  public static Unit<SpecificHeat> JL_PER_KG_PER_KEL = SI.JOULE.divide(SI.KILOGRAM).divide(SI.KELVIN).asType(SpecificHeat.class);
  
  /**
   *
   */
  public static Unit<LinearHeatCapacity> JL_PER_METER_PER_KEL = SI.JOULE.divide(SI.METRE).divide(SI.KELVIN).asType(LinearHeatCapacity.class);
  
}
