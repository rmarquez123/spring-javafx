package com.rm.springjavafx.processors;

import org.springframework.context.ApplicationEvent;

/**
 *
 * @author Ricardo Marquez
 */
public class DataStoreEvent extends ApplicationEvent {
  
  private final Operation operation;
  
  public DataStoreEvent(Object source, Operation operation) {
    super(source);
    this.operation = operation;
  }
  
  public boolean isUpdated() {
    return operation == Operation.UPDATED;
  }
  
  public Operation getOperation() {
    return operation;
  }

  /**
   *
   * @return
   */
  public boolean isCreatedOrRemoved() {
    boolean result = isCreated() || isRemoved();
    return result;
  }
  
  public boolean isRemoved() {
    return this.operation == Operation.DELETED;
  }

  public boolean isCreated() {
    return this.operation == Operation.CREATED;
  }
  
}
