package gov.inl.glass3.linesections;

/**
 *
 * @author Ricardo Marquez
 */
public interface LineSections extends Iterable<LineSection> {

  /**
   * 
   * @param theNOAARoute
   * @return 
   */
  public LineSection get(String theNOAARoute);
  
  /**
   *
   * @return
   */
  public int size();
}
