package gov.inl.glass3.linesolver.impl._default;

import gov.inl.glass3.linesections.LineSection;
import gov.inl.glass3.linesections.LineSections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ricardo Marquez
 */
public class DefaultLineSections implements LineSections {

  private final Map<String, LineSection> lineSections = new HashMap<>();

  /**
   *
   * @param lineSections
   */
  public DefaultLineSections(List<LineSection> lineSections) {
    for (LineSection lineSection : lineSections) {
      this.lineSections.put(lineSection.getLineSectionId(), lineSection);
    }
  }

  /**
   *
   * @return
   */
  @Override
  public Iterator<LineSection> iterator() {
    return this.lineSections.values().iterator();
  }

  @Override
  public int size() {
    return this.lineSections.size();
  }

  @Override
  public LineSection get(String theNOAARoute) {
    LineSection result = this.lineSections.get(theNOAARoute);
    return result;
  }

  @Override
  public String toString() {
    return "DefaultLineSections{" + "lineSections=" + lineSections.size() + '}';
  }

}
