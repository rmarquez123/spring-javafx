package com.rm.wpls.powerline.setup;

import com.rm.wpls.powerline.TransmissionLines;

/**
 *
 * @author Ricardo Marquez
 */
public interface TransmissionLineSource {

  
  /**
   * 
   * @param srid
   * @param filter
   * @return 
   */
  TransmissionLines getTransmissionLines(int srid, TransmissionLines.Filter filter); 
  
}
