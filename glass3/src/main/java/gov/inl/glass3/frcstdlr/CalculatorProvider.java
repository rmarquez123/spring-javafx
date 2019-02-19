package gov.inl.glass3.frcstdlr;

/**
 *
 * @author Ricardo Marquez
 */
@FunctionalInterface
public interface CalculatorProvider {

  FrcstDlrCalculator getCalculator(String modelPointId);
  
}
