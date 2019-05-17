package com.rm.springjavafx.datasources;

import java.util.List;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 *
 * @author rmarquez
 */
public class DbConnectionBeanDefParser extends AbstractBeanDefinitionParser {

  /**
   *
   * @param elmnt
   * @param pc
   * @return
   */
  @Override
  protected AbstractBeanDefinition parseInternal(Element elmnt, ParserContext pc) {
    BeanDefinitionBuilder beanDef = BeanDefinitionBuilder.rootBeanDefinition(DbConnectionFactory.class);
    String id = elmnt.getAttribute("id");
    if (id == null || id.isEmpty()) {
      throw new NullPointerException("id attribute cannot be null");
    }
    List<Element> elements = DomUtils.getChildElements(elmnt);
    for (Element element : elements) {
      String tagName = element.getTagName().split(":")[1].trim();
      switch (tagName) {
        case "db-user":
        case "db-port":
        case "db-password":
        case "db-url":
        case "db-schema":
          String value = element.getAttribute("value");
          String propName = toCamelCase(tagName);
          System.out.println("value " + value);
          beanDef.addPropertyValue(propName, value);          
          break;
        default:
          throw new IllegalStateException("Invalid tag name : '" + tagName + "'");
      }
    }
    AbstractBeanDefinition result = beanDef.getBeanDefinition();
    pc.getRegistry().registerBeanDefinition(id, result);
    return result;
  }

  /**
   *
   * @param str
   * @return
   */
  private static String toCamelCase(String str) {
    int indexOf = str.indexOf("-");
    String result;
    if (indexOf != -1) {
      result = str.substring(0, indexOf)
              + str.substring(indexOf + 1, indexOf + 2).toUpperCase()
              + str.substring(indexOf + 2, str.length());
    } else {
      result = str;
    }
    return result;
  }

}
