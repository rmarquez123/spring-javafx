
package gov.inl.glass3.customunits;

import javax.measure.quantity.Quantity;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

/**
 *
 * @author Ricardo Marquez
 */
public interface LinearHeatCapacity extends Quantity  {

  /**
   *
   */
  public static Unit<?> UNIT = SI.JOULE.divide(SI.METRE).divide(SI.KELVIN).alternate("line_heat_capacity_si_unit");
  
}
