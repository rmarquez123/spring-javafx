package com.rm.springjavafx.bindings;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 *
 * @author rmarquez
 */
public class ContentPanelBeanDefParser extends AbstractBeanDefinitionParser{
  
  /**
   * {@inheritDoc}
   * <p>
   * OVERRIDE: </p>
   */
  @Override
  protected AbstractBeanDefinition parseInternal(Element elmnt, ParserContext pc) {
    BeanDefinitionBuilder result = BeanDefinitionBuilder.rootBeanDefinition(ContentPanel.class);
    String parentFxml = elmnt.getAttribute("parentFxml"); 
    String parentNode = elmnt.getAttribute("parentNode"); 
    String childFxml = elmnt.getAttribute("childFxml"); 
    result.addPropertyValue("parentNode", parentNode); 
    result.addPropertyValue("parentFxml", parentFxml); 
    result.addPropertyValue("childFxml", childFxml); 
    return result.getBeanDefinition();
  }
}
