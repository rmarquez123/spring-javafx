package com.rm.springjavafx.nodes;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
public class NodeProcessorFactory {
  
  private final Map<Class<? extends Annotation>, NodeProcessor> map = new HashMap<>();
  
  /**
   * 
   * @param annotation
   * @param processor 
   */
  public void addProcessor(Class<? extends Annotation> annotation, NodeProcessor processor) {
    if (this.map.containsKey(annotation)) {
      throw new IllegalArgumentException(); 
    }
    this.map.put(annotation, processor);
  }
  
  /**
   * 
   */
  public Set<Class<? extends Annotation>> getAnnotations() {
    return this.map.keySet();
  }
  
  /**
   * 
   */
  public NodeProcessor getProcessor(Class<? extends Annotation> annotation) {
    return this.map.get(annotation);
  }
}
