
package com.rm.springjavafx.components;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

/**
 *
 * @author rmarquez
 */

public class ComboBoxBeanDefParser extends AbstractBeanDefinitionParser {

  /**
   * 
   * @param elmnt
   * @param pc
   * @return 
   */
  @Override
  protected AbstractBeanDefinition parseInternal(Element elmnt, ParserContext pc) {
    BeanDefinitionBuilder result = BeanDefinitionBuilder.rootBeanDefinition(ComboBoxFactory.class);
    String id = elmnt.getAttribute(ID_ATTRIBUTE);
    result.addPropertyValue("id", elmnt.getAttribute("id")); 
    result.addPropertyValue("fxml", elmnt.getAttribute("fxml")); 
    result.addPropertyValue("fxmlId", elmnt.getAttribute("fxmlId"));
    result.addPropertyValue("dataSourceRef", elmnt.getAttribute("datasource-ref")); 
    result.addPropertyValue("valueRef", elmnt.getAttribute("value-ref"));
    result.setLazyInit(false); 
    pc.getRegistry().registerBeanDefinition(id, result.getBeanDefinition());
    return result.getBeanDefinition();
  }
  
}
