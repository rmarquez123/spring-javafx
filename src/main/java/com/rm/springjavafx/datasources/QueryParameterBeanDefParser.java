package com.rm.springjavafx.datasources;


import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;


/**
 *
 * @author rmarquez
 */
public class QueryParameterBeanDefParser extends AbstractSingleBeanDefinitionParser {
  
  /**
   * 
   * @param element
   * @return 
   */
  @Override
  protected Class<?> getBeanClass(Element element) {
    return QueryParameterFactory.class;
  }
  
  /**
   * 
   * @param element
   * @param builder 
   */
  @Override
  protected void doParse(Element element, BeanDefinitionBuilder builder) {
    builder.addPropertyValue("name", element.getAttribute("name"));
    builder.addPropertyValue("valueRef", element.getAttribute("valueRef"));
  }

  
  
}
