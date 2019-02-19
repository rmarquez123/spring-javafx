package com.rm.wpls.powerline;

import com.vividsolutions.jts.geom.Envelope;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ricardo Marquez
 */
public class TransmissionLines {

  private final List<TransmissionLine> lines;
    
  /**
   * 
   * @param lines 
   */
  public TransmissionLines(List<TransmissionLine> lines) {
    this.lines = new ArrayList<>(lines);
  }
  
  /**
   * 
   * @return 
   */
  public int size() {
    return this.lines.size();
  }

  public Envelope getEnvelope() {
    Envelope result;
    if (!this.lines.isEmpty()) {
      result = null;
      for (TransmissionLine line : this.lines) {
        Envelope env = line.getEnvelope();
        if (result == null) {
          result = new Envelope(env); 
        } else {
          result.expandToInclude(env);
        }
      }
    } else {
      result = null;
    }    
    return result;
  }

  
  /**
   * 
   * @return 
   */
  public List<TransmissionLine> asList() {
    return new ArrayList<>(this.lines);
  }

  /**
   *
   */
  public static class Filter {

    private final Double minRatedVoltage;
    private final String filterName;

    /**
     *
     * @param minRatedVoltage
     * @param filterName
     */
    public Filter(Double minRatedVoltage, String filterName) {
      this.minRatedVoltage = minRatedVoltage;
      this.filterName = filterName;
    }
    
    /**
     * 
     * @return 
     */
    public String getFilterName() {
      return filterName;
    }
    
    /**
     * 
     * @return 
     */
    public Double getMinRatedVoltage() {
      return minRatedVoltage;
    }
    

    /**
     *
     */
    public static class Builder {

      private Double minRatedVoltage = null;
      private String filterName = null;

      public Builder() {

      }

      /**
       *
       * @param filterName
       * @return
       */
      public Builder filterByName(String filterName) {
        this.filterName = filterName;
        return this;
      }

      /**
       *
       * @param minRatedVoltage
       * @return
       */
      public Builder filterByMinRatedVoltage(Double minRatedVoltage) {
        this.minRatedVoltage = minRatedVoltage;
        return this;
      }

      /**
       *
       * @return
       */
      public Filter build() {
        return new Filter(minRatedVoltage, filterName);
      }
    }

  }

}
