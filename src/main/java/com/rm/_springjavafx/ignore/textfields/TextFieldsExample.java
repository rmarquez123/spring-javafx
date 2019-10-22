package com.rm._springjavafx.ignore.textfields;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class TextFieldsExample implements InitializingBean{

  @Override
  public void afterPropertiesSet() throws Exception {
    System.out.println("initialized ...");
  }
  
}