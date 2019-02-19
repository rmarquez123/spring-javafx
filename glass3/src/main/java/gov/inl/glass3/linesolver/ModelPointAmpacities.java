package gov.inl.glass3.linesolver;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Ricardo Marquez
 */
public class ModelPointAmpacities {
  
  private final Map<String, Set<ModelPointAmpacity>> values = new HashMap<>();
  
  /**
   * 
   * @param values 
   */
  public ModelPointAmpacities(Map<String, Set<ModelPointAmpacity>> values) {
    if (values == null) {
      throw new NullPointerException("Values cannot be null"); 
    }
    this.values.putAll(values);
  }

  /**
   * 
   * @param modelPointId
   * @return 
   */
  public Set<ModelPointAmpacity> get(String modelPointId) {
    Set<ModelPointAmpacity> result = new TreeSet<>(this.values.get(modelPointId)) ;
    return result; 
  }

  @Override
  public String toString() {
    return "ModelPointAmpacities{" + "values=" + values.size() + '}';
  }
  
}
