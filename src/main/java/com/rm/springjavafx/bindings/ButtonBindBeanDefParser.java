
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
public class ButtonBindBeanDefParser extends AbstractBeanDefinitionParser {

  /**
   * 
   * @param elmnt
   * @param pc
   * @return 
   */
  @Override
  protected AbstractBeanDefinition parseInternal(Element elmnt, ParserContext pc) {
    BeanDefinitionBuilder result = BeanDefinitionBuilder.rootBeanDefinition(ButtonBind.class);
    String id = elmnt.getAttribute(ID_ATTRIBUTE);
    if (id != null && !id.isEmpty()) {
      pc.getRegistry().registerBeanDefinition(id, result.getBeanDefinition());
    }
    result.addPropertyValue("fxml", elmnt.getAttribute("fxml")); 
    result.addPropertyValue("fxmlId", elmnt.getAttribute("fxmlId")); 
    result.addPropertyReference("invokerRef", elmnt.getAttribute("invokerRef")); 
    return result.getBeanDefinition();
  }
  
}
