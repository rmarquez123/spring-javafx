package com.windsim.wpls.view.projectopen;

import gov.inl.glass3.linesolver.Model;
import java.util.function.Consumer;

/**
 *
 * @author Ricardo Marquez
 */
public interface RequestProjectOpenAction {
  
  
  /**
   * 
   * @param onDone 
   */
  public void onAction(Consumer<Model> onDone); 
  
}
