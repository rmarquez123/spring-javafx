package gov.inl.glass3.customunits;

import javax.measure.quantity.Quantity;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

/**
 *
 * @author Ricardo Marquez
 */
public interface PowerFlux extends Quantity {

  public static Unit<?> UNIT = SI.WATT.divide(SI.SQUARE_METRE).alternate("power_flux_si_unit");
}
