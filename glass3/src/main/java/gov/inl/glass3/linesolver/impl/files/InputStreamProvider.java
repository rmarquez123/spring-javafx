package gov.inl.glass3.linesolver.impl.files;

import java.io.InputStream;

/**
 *
 * @author Ricardo Marquez
 */
public interface InputStreamProvider {

  /**
   * 
   * @return 
   */
  InputStream openStream();
  
}
