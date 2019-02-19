package gov.inl.glass3.frcstdlr;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

/**
 *
 * @author Ricardo Marquez
 */
public final class RealTimeFcstScheduler implements FcstScheduler {

  private TimerTask timerTask;

  /**
   *
   * @param referenceDate
   * @param timeInterval
   * @param runnable
   */
  @Override
  public void scheduleRuns(ZonedDateTime referenceDate, TemporalAmount timeInterval, Consumer<ZonedDateTime> runnable) {
    if (this.timerTask != null) {
      throw new IllegalArgumentException("The scheduler can only be called once"); 
    }
    Timer timer = new Timer(this.getClass().getName(), false);
    Instant referenceInstant = referenceDate.toInstant();
    TemporalUnit baseUnit = timeInterval.getUnits().get(0);
    long durationInMillis = Duration.of(timeInterval.get(baseUnit), baseUnit).toMillis();
    this.timerTask = new TimerTask() {
      @Override
      public void run() {
        long ticks = timeInterval.get(baseUnit);
        long elapsedTicks = baseUnit.between(referenceInstant, Instant.now());
        long adjustedElapsedTickes = (long) Math.floor(elapsedTicks / ticks) * ticks;
        ZonedDateTime t = referenceDate.plus(adjustedElapsedTickes, baseUnit);
        runnable.accept(t);
      }
    };
    
    if (!referenceInstant.isBefore(Instant.now())) {
      timer.scheduleAtFixedRate(timerTask, Date.from(referenceInstant), durationInMillis);
    } else {
      long ticks = timeInterval.get(baseUnit);
      long elapsedTicks = baseUnit.between(referenceInstant, Instant.now());
      long adjustedElapsedTickes = (long) Math.floor(elapsedTicks / ticks) * ticks;
      ZonedDateTime t = referenceDate.plus(adjustedElapsedTickes, baseUnit);
      timer.scheduleAtFixedRate(timerTask, Date.from(t.toInstant()), durationInMillis);
    }
  }

  @Override
  public void stop() {
    this.timerTask.cancel();
  }
  

}
