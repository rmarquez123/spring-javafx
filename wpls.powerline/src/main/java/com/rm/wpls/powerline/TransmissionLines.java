package com.rm.wpls.powerline;

import com.vividsolutions.jts.geom.Envelope;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

  /**
   *
   * @return
   */
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
   * @return
   */
  @Override
  public String toString() {
    return "TransmissionLines{" + "lines=" + lines.size() + '}';
  }

  /**
   *
   */
  public static class Filter {

    private final Double minRatedVoltage;
    private final String filterName;
    private final String stateName;
    private final Envelope envelope;

    /**
     *
     * @param minRatedVoltage
     * @param filterName
     */
    public Filter(Double minRatedVoltage, String filterName, String stateName, Envelope envelope) {
      this.filterName = filterName;
      this.minRatedVoltage = minRatedVoltage;
      this.stateName = stateName;
      this.envelope = envelope;
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
    public String getStateName() {
      return this.stateName;
    }

    /**
     *
     * @return
     */
    public Envelope getEnvelope() {
      return envelope;
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
      private String stateName = null;
      private Envelope envelope = null;

      public Builder() {

      }

      /**
       *
       * @param filterName
       * @return
       */
      public Builder filterByStateName(String stateName) {
        this.stateName = stateName;
        return this;
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
       * @param envelope
       * @return
       */
      public Builder filterByEnvelope(Envelope envelope) {
        this.envelope = envelope;
        return this;
      }

      /**
       *
       * @return
       */
      public Filter build() {
        Objects.requireNonNull(this.stateName, "State name cannot be null");
        return new Filter(this.minRatedVoltage, this.filterName, this.stateName, this.envelope);
      }

    }

  }

}
