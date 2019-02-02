
package com.rm.springjavafx.properties;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 *
 * @author Ricardo Marquez
 */
public class NullIfNullPropertyBeanDefParser extends AbstractBeanDefinitionParser {

  @Override
  protected AbstractBeanDefinition parseInternal(Element elmnt, ParserContext pc) {
    BeanDefinitionBuilder beanDef = BeanDefinitionBuilder.rootBeanDefinition(NullIfNullPropertyBinding.class);
    String referenceProperty = elmnt.getAttribute("referenceProperty"); 
    String property = elmnt.getAttribute("property"); 
    beanDef.addPropertyReference("referenceProperty", referenceProperty); 
    beanDef.addPropertyReference("property", property);
    String id = elmnt.getAttribute(ID_ATTRIBUTE); 
    pc.getRegistry().registerBeanDefinition(id, beanDef.getBeanDefinition());
    return beanDef.getBeanDefinition();
  }
  
}
