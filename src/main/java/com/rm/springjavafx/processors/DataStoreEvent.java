package com.rm.springjavafx.processors;

import org.springframework.context.ApplicationEvent;

/**
 *
 * @author Ricardo Marquez
 */
public class DataStoreEvent extends ApplicationEvent {
  
  public DataStoreEvent(Object source) {
    super(source);
  }
  
}
