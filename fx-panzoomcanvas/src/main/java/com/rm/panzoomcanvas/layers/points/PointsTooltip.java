package com.rm.panzoomcanvas.layers.points;

/**
 *
 * @author rmarquez
 */
public class PointsTooltip {

  private final int heightOffset;

  private PointsTooltip(Builder builder) {
    this.heightOffset = builder.heightOffset;
  }

  /**
   *
   * @return
   */
  public int getHeightOffset() {
    return heightOffset;
  }

  public static class Builder {

    private int heightOffset = 44;

    /**
     *
     * @param heightOffset
     * @return same but modified instance
     */
    public Builder setHeightOffset(int heightOffset) {
      this.heightOffset = heightOffset;
      return this;
    }

    /**
     *
     * @return
     */
    public PointsTooltip build() {
      return new PointsTooltip(this);
    }

  }

}
