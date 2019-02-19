package gov.inl.glass3.frcstdlr;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.util.function.Consumer;

/**
 *
 * @author Ricardo Marquez
 */
public interface FcstScheduler {
  
  /**
   * 
   * @param timeInterval
   * @param runnable 
   */
  public void scheduleRuns(ZonedDateTime referenceDate, TemporalAmount timeInterval, Consumer<ZonedDateTime> runnable);
  
  /**
   * 
   */
  public void stop(); 
}
