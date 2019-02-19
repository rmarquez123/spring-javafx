package gov.inl.glass3.linesolver.impl._default;

import gov.inl.glass3.conductors.Conductor;
import gov.inl.glass3.conductors.ConductorCatalogue;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ricardo Marquez
 */
public class DefaultConductorCatalogue implements ConductorCatalogue {

  private final Map<String, Conductor> conductors = new HashMap<>();

  /**
   *
   * @param conductors
   */
  public DefaultConductorCatalogue(List<Conductor> conductors) {
    for (Conductor conductor : conductors) {
      if (!this.conductors.containsKey(conductor.getName())) {
        this.conductors.put(conductor.getName(), conductor);
      } else {
        throw new IllegalStateException("Duplicate conductor keys found : '" + conductor.getName() + "'");
      }
    }
  }

  /**
   *
   * @return
   */
  @Override
  public Iterator<Conductor> iterator() {
    return this.conductors.values().iterator();
  }

  /**
   *
   * @param conductorId
   * @return
   */
  @Override
  public Conductor get(String conductorId) {

    Conductor result = this.conductors.get(conductorId);
    if (result == null) {
      throw new NullPointerException("Invalid conductor Id: '" + conductorId + "'");

    }
    return result;
  }

  /**
   *
   * @return
   */
  @Override
  public int size() {
    return this.conductors.size();
  }

  /**
   *
   * @return
   */
  @Override
  public String toString() {
    return "DefaultConductorCatalogue{" + "conductors=" + conductors.size() + '}';
  }

}
