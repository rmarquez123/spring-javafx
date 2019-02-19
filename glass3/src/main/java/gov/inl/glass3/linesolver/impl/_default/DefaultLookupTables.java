package gov.inl.glass3.linesolver.impl._default;

import gov.inl.glass3.weather.WeatherRecord;
import gov.inl.glass3.windmodel.LookupTable;
import gov.inl.glass3.windmodel.LookupTables;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.measure.Measure;
import javax.measure.quantity.Velocity;
import static javax.measure.unit.NonSI.DEGREE_ANGLE;

/**
 *
 * @author Ricardo Marquez
 */
public class DefaultLookupTables implements LookupTables {

  private final Map<String, List<LookupTable>> lookUpTables = new HashMap<>();

  /**
   *
   * @param lookUpTables
   */
  public DefaultLookupTables(List<LookupTable> lookUpTables) {
    lookUpTables.sort((c1, c2) -> Double.compare(c1.getSectorNum(), c2.getSectorNum()));
    for (LookupTable lookUpTable : lookUpTables) {
      if (!this.lookUpTables.containsKey(lookUpTable.getModelPointId())) {
        this.lookUpTables.put(lookUpTable.getModelPointId(), new ArrayList<>());
      }
      this.lookUpTables.get(lookUpTable.getModelPointId()).add(lookUpTable);
    }
  }

  @Override
  public LookupTables getByModelPointId(String modelPointId) {
    List<LookupTable> lookupTables = this.lookUpTables.get(modelPointId); 
    DefaultLookupTables result = new DefaultLookupTables(lookupTables);
    return result;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public int numModelPoints() {
    return this.lookUpTables.size();
  }
  

  /**
   *
   * @return
   */
  @Override
  public Iterator<LookupTable> iterator() {
    List<LookupTable> result = new ArrayList<>();
    for (List<LookupTable> value : this.lookUpTables.values()) {
      result.addAll(value);
    }
    return result.iterator();
  }

  @Override
  public WeatherRecord applyAdjustments(String modelPointId, WeatherRecord record) {
    Interpolator interpolator = getLookupTable(modelPointId, record);
    WeatherRecord result = this.applyAdjustments(interpolator, record);
    return result;
  }

  /**
   *
   * @param modelPointId
   * @param record
   * @return
   */
  public Interpolator getLookupTable(String modelPointId, WeatherRecord record) {
    List<LookupTable> candidates = this.lookUpTables.get(modelPointId);
    LookupTable lookUpTableMin = null;
    LookupTable lookUpTableMax = null;
    for (int i = 0; i < candidates.size(); i++) {
      if (i != candidates.size() - 1) {
        if (candidates.get(i).getSectorNum() <= record.getWindAngle()
          && record.getWindAngle() < candidates.get(i + 1).getSectorNum()) {
          lookUpTableMin = candidates.get(i);
          lookUpTableMax = candidates.get(i + 1);
          break;
        }
      } else {
        lookUpTableMin = candidates.get(i);
        lookUpTableMax = candidates.get(0);
        break;
      }
    }
    return new Interpolator(lookUpTableMin, lookUpTableMax);
  }

  /**
   *
   * @param lookUpTable
   * @param record
   * @return
   */
  private WeatherRecord applyAdjustments(Interpolator interpolator, WeatherRecord record) {
    LookupTable lookUpTable = interpolator.interpolate(record);
    double directionShift = lookUpTable.getDirectionShift();
    double speedUp = lookUpTable.getSpeedUp();
    double angleAdjusted = record.getWindAngle() + directionShift;
    Measure<Velocity> windSpeed = record.getWindSpeed();
    double speedAdjusted = windSpeed.getValue().doubleValue() * speedUp;
    WeatherRecord result = new WeatherRecord.Builder(record)
      .setWindSpeed(Measure.valueOf(speedAdjusted, windSpeed.getUnit()))
      .setWindAngle(Measure.valueOf(angleAdjusted, DEGREE_ANGLE))
      .build();
    return result;
  }

  @Override
  public String toString() {
    return "DefaultLookupTables{" + "lookUpTables=" + lookUpTables.size() + '}';
  }

  /**
   *
   */
  public static class Interpolator {

    private LookupTable low;
    private LookupTable high;

    public Interpolator(LookupTable low, LookupTable high) {
      this.low = low;
      this.high = high;
    }

    /**
     *
     * @param record
     * @return
     */
    public LookupTable interpolate(WeatherRecord record) {
      double minX = this.low.getSectorNum();
      double maxX = (Math.abs(this.high.getSectorNum()) < 1.0e-6) ? 360.0 : this.high.getSectorNum();
      double minY1 = this.low.getSpeedUp();
      double maxY1 = this.high.getSpeedUp();
      double minY2 = this.low.getDirectionShift();
      double maxY2 = this.high.getDirectionShift();
      double y1 = (maxY1 - minY1) / (maxX - minX) * (record.getWindAngle() - minX) + minY1;
      double y2 = (maxY2 - minY2) / (maxX - minX) * (record.getWindAngle() - minX) + minY2;
      LookupTable result = new LookupTable(this.low.getModelPointId(), record.getWindAngle(), y1, y2);
      return result;
    }
  }

}
