package gov.inl.glass3.conductors;

/**
 *
 * @author Ricardo Marquez
 */
public interface ConductorCatalogue extends Iterable<Conductor> {

  public int size();

  public Conductor get(String conductorId);
  
}
