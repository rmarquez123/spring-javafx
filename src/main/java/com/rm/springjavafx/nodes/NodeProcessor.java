package com.rm.springjavafx.nodes;

import java.lang.annotation.Annotation;

/**
 *
 * @author Ricardo Marquez
 */
public interface NodeProcessor {
  
  /**
   * 
   * @param node
   * @param annotation 
   */
  public void process(Object parentBean, Object node, Annotation annotation); 
}
