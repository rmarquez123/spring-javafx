package gov.inl.glass3.customunits;

import javax.measure.quantity.Quantity;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

/**
 *
 * @author Ricardo Marquez
 */
public interface LinearMassDensity extends Quantity {

  /**
   *
   */
  public static Unit<?> UNIT = SI.KILOGRAM.divide(SI.METRE).alternate("linear_density_si_unit");

}
