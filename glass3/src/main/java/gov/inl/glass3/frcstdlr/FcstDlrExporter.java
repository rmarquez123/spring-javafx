package gov.inl.glass3.frcstdlr;

import gov.inl.glass3.linesolver.ModelPointAmpacity;
import java.time.ZonedDateTime;
import java.util.List;

/**
 *
 * @author Ricardo Marquez
 */

public interface FcstDlrExporter {


  /**
   *
   * @param t
   * @param ampacities
   */
  void export(ZonedDateTime t, List<ModelPointAmpacity> ampacities);

}
