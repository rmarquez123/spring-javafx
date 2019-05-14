package com.rm.springjavafx.bindings;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import static org.springframework.beans.factory.xml.AbstractBeanDefinitionParser.ID_ATTRIBUTE;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 *
 * @author Ricardo Marquez
 */
public class BindLabelBeanDefParser extends AbstractBeanDefinitionParser {

  /**
   *
   * @param elmnt
   * @param pc
   * @return
   */
  @Override
  protected AbstractBeanDefinition parseInternal(Element elmnt, ParserContext pc) {
    BeanDefinitionBuilder result = BeanDefinitionBuilder.rootBeanDefinition(BindLabel.class);
    String id = elmnt.getAttribute(ID_ATTRIBUTE);
    if (id != null && !id.isEmpty()) {
      pc.getRegistry().registerBeanDefinition(id, result.getBeanDefinition());
    }
    
    result.addPropertyReference("label", elmnt.getAttribute("label"));
    result.addPropertyReference("value", elmnt.getAttribute("value"));
    result.addPropertyReference("converter", elmnt.getAttribute("converter"));
    
    return result.getBeanDefinition();
  }
}
