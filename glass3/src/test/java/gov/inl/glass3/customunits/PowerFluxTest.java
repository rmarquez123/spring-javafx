package gov.inl.glass3.customunits;

import javax.measure.Measure;
import javax.measure.unit.SI;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Ricardo Marquez
 */
public class PowerFluxTest {

  @Test
  public void test1() {
    SI.WATT.divide(SI.SQUARE_METRE).toSI();
    ;
    Measure<PowerFlux> powerFlux1 = Measure.valueOf(0.0, RmSI.WATTS_PER_SQUARE_METER);
    Measure<PowerFlux> powerFlux2 = powerFlux1.to(RmSI.WATTS_PER_SQUARE_METER);
    Assert.assertEquals(powerFlux1, powerFlux2);
  }

  @Test
  public void test2() {
    Measure<LinearElectricalResistence> powerFlux1 = Measure.valueOf(0.139, RmSI.OHMS_PER_MILE);
    Measure<LinearElectricalResistence> powerFlux2 = powerFlux1.to(RmSI.OHMS_PER_M);
    System.out.println(powerFlux2);
  }

}
