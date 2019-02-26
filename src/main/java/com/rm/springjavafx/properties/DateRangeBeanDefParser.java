package com.rm.springjavafx.properties;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 *
 * @author rmarquez
 */
public class DateRangeBeanDefParser extends AbstractBeanDefinitionParser {
  
  /**
   * 
   * @param elmnt
   * @param pc
   * @return 
   */
  @Override
  protected AbstractBeanDefinition parseInternal(Element elmnt, ParserContext pc) {
    BeanDefinitionBuilder beanDef = BeanDefinitionBuilder.rootBeanDefinition(DateRangePropertyFactory.class);
    String id = elmnt.getAttribute("id");    
    pc.getRegistry().registerBeanDefinition(id, beanDef.getBeanDefinition());
    return beanDef.getBeanDefinition();
  }

}
