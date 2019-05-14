package com.rm.springjavafx.bindings;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 *
 * @author Ricardo Marquez
 */
public class ButtonPopupBeanDefParser extends AbstractBeanDefinitionParser {

  @Override
  protected AbstractBeanDefinition parseInternal(Element elmnt, ParserContext pc) {
    BeanDefinitionBuilder result = BeanDefinitionBuilder.rootBeanDefinition(ButtonPopup.class);
    result.addPropertyReference("button", elmnt.getAttribute("button"));
    result.addPropertyReference("popup", elmnt.getAttribute("popup"));
    result.addPropertyReference("readyProperty", elmnt.getAttribute("readyProperty"));
    return result.getBeanDefinition();
  }

}
