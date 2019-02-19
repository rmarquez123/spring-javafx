
import javax.measure.Measure;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Velocity;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import org.junit.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ricardo Marquez
 */
public class NonSIUnitsTest {
  
    
  @Test
  public void test_units() {
    Measure<Velocity> speed1 = Measure.valueOf(0.0, SI.METRES_PER_SECOND); 
    Angle.class.getClasses();
    Measure<Velocity> result = speed1.to(NonSI.MILES_PER_HOUR);    
  }
}
