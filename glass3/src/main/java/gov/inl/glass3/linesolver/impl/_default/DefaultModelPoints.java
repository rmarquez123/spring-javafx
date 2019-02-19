package gov.inl.glass3.linesolver.impl._default;

import gov.inl.glass3.modelpoints.ModelPoint;
import gov.inl.glass3.modelpoints.ModelPoints;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Ricardo Marquez
 */
public class DefaultModelPoints implements ModelPoints {

  private final Map<String, ModelPoint> modelPoints = new HashMap<>();

  /**
   *
   * @param modelPoints
   */
  public DefaultModelPoints(List<ModelPoint> modelPoints) {
    for (ModelPoint modelPoint : modelPoints) {
      if (!this.modelPoints.containsKey(modelPoint.getModelPointId())) {
        this.modelPoints.put(modelPoint.getModelPointId(), modelPoint);
      } else {
        throw new IllegalStateException("Duplicate model points found : '" + modelPoint.getModelPointId() + "'");
      }
    }

  }

  /**
   *
   * @param modelPointId
   * @return
   */
  @Override
  public ModelPoint get(String modelPointId) {
    return this.modelPoints.get(modelPointId);
  }

  /**
   *
   * @return
   */
  @Override
  public Iterator<ModelPoint> iterator() {
    return this.modelPoints.values().iterator();
  }

  /**
   *
   * @param modelPointsIds
   * @return
   */
  @Override
  public ModelPoints subset(List<String> modelPointsIds) {
    List<ModelPoint> newModelPoints = modelPointsIds.stream()
      .map((id) -> this.modelPoints.get(id))
      .collect(Collectors.toList());
    ModelPoints result = new DefaultModelPoints(newModelPoints);
    return result;
  }

  /**
   *
   * @return
   */
  @Override
  public Set<String> asModelPointIds() {
    return this.modelPoints.keySet();
  }

  /**
   *
   * @return
   */
  @Override
  public int size() {
    return this.modelPoints.size();
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {
    return "DefaultModelPoints{" + "modelPoints=" + modelPoints.size() + '}';
  }

}
