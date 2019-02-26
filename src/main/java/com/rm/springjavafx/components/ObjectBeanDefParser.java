package com.rm.springjavafx.components;

import java.util.List;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 *
 * @author rmarquez
 */
public class ObjectBeanDefParser extends AbstractSingleBeanDefinitionParser {

  public ObjectBeanDefParser() {

  }
  
  /**
   * 
   * @param element
   * @return 
   */
  @Override
  protected Class<?> getBeanClass(Element element) {
    return Object.class;
  }
    
  /**
   * 
   * @param elmnt
   * @param builder 
   */
  @Override
  protected void doParse(Element elmnt, BeanDefinitionBuilder builder) {
    String converterClass = elmnt.getAttribute("class");
    builder.addPropertyValue("className", converterClass);
    List<Element> ces = DomUtils.getChildElements(elmnt);
    ManagedMap<String, String> properties = new ManagedMap<>();
    for (Element ce : ces) {
      if (ce.getTagName().endsWith("property")) {
        String name = ce.getAttribute("name");
        String value = ce.getAttribute("value");
        properties.put(name, value);
      }
    }
    builder.addPropertyValue("properties", properties);
  }

}
