package gov.inl.glass3.customunits;

import javax.measure.quantity.Quantity;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

/**
 *
 * @author Ricardo Marquez
 */
public interface SpecificHeat extends Quantity {

  /**
   *
   */
  public static Unit<?> UNIT = SI.JOULE.divide(SI.KILOGRAM).divide(SI.KELVIN).alternate("heat_capacity_siunits");

}