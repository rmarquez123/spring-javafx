package gov.inl.glass3.customunits;

import javax.measure.quantity.Quantity;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

/**
 *
 * @author Ricardo Marquez
 */
public interface LinearElectricalResistence extends Quantity {

  /**
   *
   */
  public static Unit<?> UNIT = SI.OHM.divide(SI.METRE).alternate("line_resistence_si_unit");

}
