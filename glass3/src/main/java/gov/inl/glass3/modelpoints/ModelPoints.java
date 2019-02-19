package gov.inl.glass3.modelpoints;

import java.util.List;
import java.util.Set;

/**
 *
 * @author Ricardo Marquez
 */
public interface ModelPoints extends Iterable<ModelPoint> {
  
  /**
   * 
   * @param modelPointId
   * @return 
   */
  public ModelPoint get(String modelPointId);

  /**
   * 
   * @return 
   */
  public int size();

  /**
   * 
   * @param modelPoints
   * @return 
   */
  public ModelPoints subset(List<String> modelPoints);
    
  /**
   * 
   * @return 
   */
  public Set<String> asModelPointIds();
  
}
