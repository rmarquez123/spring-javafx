package com.rm.springjavafx.components;

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
public class ChildNodeBeanDefParser extends AbstractBeanDefinitionParser {

  /**
   *
   * @param elmnt
   * @param pc
   * @return
   */
  @Override
  protected AbstractBeanDefinition parseInternal(Element elmnt, ParserContext pc) {
    BeanDefinitionBuilder result = BeanDefinitionBuilder.rootBeanDefinition(ChildNodeFactory.class);
    String id = elmnt.getAttribute(ID_ATTRIBUTE);
    result.addPropertyValue("fxml", elmnt.getAttribute("fxml"));
    result.addPropertyValue("fxmlId", elmnt.getAttribute("fxmlId"));
    result.setLazyInit(false);
    pc.getRegistry().registerBeanDefinition(id, result.getBeanDefinition());
    return result.getBeanDefinition();
  }

}
