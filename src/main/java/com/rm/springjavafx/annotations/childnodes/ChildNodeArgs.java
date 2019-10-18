package com.rm.springjavafx.annotations.childnodes;

import java.lang.reflect.Field;

/**
 *
 * @author Ricardo Marquez
 */
public class ChildNodeArgs {
  
  final Object bean;
  final Field field;
  final Object node;

  public ChildNodeArgs(Object bean, Field field, Object node) {
    this.bean = bean;
    this.field = field;
    this.node = node;
  }
  
}
