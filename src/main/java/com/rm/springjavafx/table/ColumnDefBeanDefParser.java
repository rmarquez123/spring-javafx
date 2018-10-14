
package com.rm.springjavafx.table;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 *
 * @author rmarquez
 */
public class ColumnDefBeanDefParser extends AbstractSingleBeanDefinitionParser {

  public ColumnDefBeanDefParser() {
  }

  @Override
  protected void doParse(Element element, BeanDefinitionBuilder builder) {
    builder.addPropertyValue("column", element.getAttribute("column"));
    builder.addPropertyValue("name", element.getAttribute("name"));
    builder.addPropertyValue("rendererType", element.getAttribute("rendererType"));
  }

  @Override
  protected Class<?> getBeanClass(Element element) {
    return TableViewRenderer.class;
  }

}
