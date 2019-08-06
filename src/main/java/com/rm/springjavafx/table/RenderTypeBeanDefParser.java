package com.rm.springjavafx.table;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 *
 * @author Ricardo Marquez
 */
public class RenderTypeBeanDefParser extends AbstractSingleBeanDefinitionParser {

  @Override
  protected void doParse(Element element, BeanDefinitionBuilder builder) {
    builder.addPropertyValue("clazz", element.getAttribute("class"));
    if (element.getAttribute("action") != null && !element.getAttribute("action").isEmpty()) {
      builder.addPropertyReference("action", element.getAttribute("action"));
    }
    builder.addPropertyValue("label", element.getAttribute("label"));
    builder.addPropertyValue("secondaryLabel", element.getAttribute("secondaryLabel"));
  }

  @Override
  protected Class<?> getBeanClass(Element element) {
    return RenderTypeFactory.class;
  }
}
