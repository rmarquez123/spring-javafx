package com.rm.springjavafx.table;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.xml.DomUtils;
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
    builder.addPropertyValue("columnIndex", element.getAttribute("columnIndex"));
    builder.addPropertyValue("label", element.getAttribute("label"));
    builder.addPropertyValue("propertyName", element.getAttribute("propertyName"));
    builder.addPropertyValue("rendererType", element.getAttribute("rendererType"));
    builder.addPropertyValue("width", element.getAttribute("width"));

    Element renderTypeEl = DomUtils.getChildElementByTagName(element, "rm:render-type");
    if (renderTypeEl != null) {
      BeanDefinitionBuilder renderTypeBeanDef = BeanDefinitionBuilder.rootBeanDefinition(RenderTypeFactory.class);
      new RenderTypeBeanDefParser().doParse(renderTypeEl, renderTypeBeanDef);
      builder.addPropertyValue("renderType", renderTypeBeanDef.getBeanDefinition());
    }
  }

  @Override
  protected Class<?> getBeanClass(Element element) {
    return TableViewColumn.class;
  }

}
