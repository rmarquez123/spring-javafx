package com.windsim.wpls.view.io.sources;

/**
 *
 * @author Ricardo Marquez
 */
public enum SupportedSourceType implements SourceType {
  
  FILE()
  , DATABASE()
  , PI()
  ;
  SupportedSourceType() {
    
  }
  
}
