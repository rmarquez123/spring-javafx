package com.rm.springjavafx.properties;

import java.util.List;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 *
 * @author rmarquez
 */
public class ListPropertyBeanDefParser extends AbstractBeanDefinitionParser {
  
  /**
   *
   * @param elmnt
   * @param pc
   * @return
   */
  @Override
  protected AbstractBeanDefinition parseInternal(Element elmnt, ParserContext pc) {
    BeanDefinitionBuilder beanDef = BeanDefinitionBuilder.rootBeanDefinition(ListPropertyFactory.class);
    String id = elmnt.getAttribute("id");
    List<Element> els = DomUtils.getChildElements(elmnt);
    ManagedList<BeanDefinition> items = new ManagedList<>();
    for (Element el : els) {
      if (el.getTagName().endsWith("items")) {
        List<Element> itemEls = DomUtils.getChildElements(el);
        for (Element itemEl : itemEls) {
          BeanDefinitionBuilder itemBeanDefBuilder = BeanDefinitionBuilder.rootBeanDefinition(ListItem.class);
          itemBeanDefBuilder.addPropertyValue("id", itemEl.getAttribute("id"));
          itemBeanDefBuilder.addPropertyValue("label", itemEl.getAttribute("label"));
          List<Element> itemChildEls = DomUtils.getChildElements(itemEl);
          ManagedMap<String, Object> properties = new ManagedMap<>();
          for (Element itemChildEl : itemChildEls) {  
            if (itemChildEl.getTagName().endsWith("property")) {
              BeanDefinition propBeanDef = BeanDefinitionBuilder.rootBeanDefinition(Object.class).getBeanDefinition();
              String propName = itemChildEl.getAttribute("name");
              Object a = pc.getDelegate().parsePropertyValue(itemChildEl, propBeanDef, propName); 
              properties.put(propName, a); 
            }
          }
          itemBeanDefBuilder.addPropertyValue("properties", properties);
          items.add(itemBeanDefBuilder.getBeanDefinition());
        }
      }
    }
    beanDef.addPropertyValue("items", items); 
    pc.getRegistry().registerBeanDefinition(id, beanDef.getBeanDefinition());
    return beanDef.getBeanDefinition();
  }
}
