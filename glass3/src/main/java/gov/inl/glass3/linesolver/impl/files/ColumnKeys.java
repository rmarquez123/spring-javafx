package gov.inl.glass3.linesolver.impl.files;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ricardo Marquez
 */
public class ColumnKeys {

  private final HashMap<LineHeader, Integer> headers;

  /**
   *
   * @param headers
   */
  public ColumnKeys(Map<LineHeader, Integer> headers) {
    this.headers = new HashMap<>(headers);
  }

  /**
   *
   * @param header
   * @return
   */
  public int getKey(LineHeader header) {
    Integer result = this.headers.get(header);
    if (result == null) {
      throw new IllegalArgumentException("Invalid header : '" + header + "'");
    }
    return result;
  }

}
