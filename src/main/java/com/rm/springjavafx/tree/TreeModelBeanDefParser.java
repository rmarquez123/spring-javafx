package com.rm.springjavafx.tree;

import java.util.List;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 *
 * @author rmarquez
 */
public class TreeModelBeanDefParser extends AbstractBeanDefinitionParser {

  @Override
  protected AbstractBeanDefinition parseInternal(Element elmnt, ParserContext pc) {
    BeanDefinitionBuilder bdBuilder = BeanDefinitionBuilder.rootBeanDefinition(TreeModelFactory.class); 
    String id = elmnt.getAttribute(ID_ATTRIBUTE); 
    bdBuilder.addPropertyValue(ID_ATTRIBUTE, id);
    List<Element> els = DomUtils.getChildElements(elmnt);
    ManagedList<BeanDefinition> links = new ManagedList<>(); 
    ManagedList<BeanDefinition> datasources = new ManagedList<>(); 
    for (Element el : els) {
      if (el.getTagName().endsWith("links")) {
        List<Element> linkEls = DomUtils.getChildElements(el); 
        for (Element linkEl : linkEls) {
          BeanDefinitionBuilder linkBuilder = BeanDefinitionBuilder.rootBeanDefinition(Link.class); 
          linkBuilder.addConstructorArgValue(linkEl.getAttribute("parentField")); 
          linkBuilder.addConstructorArgValue(linkEl.getAttribute("childField")); 
          linkBuilder.addPropertyValue("level", linkEl.getAttribute("childLevel")); 
          links.add(linkBuilder.getBeanDefinition()); 
        }
      } else if (el.getTagName().endsWith("datasources")) {
        List<Element> dsEls = DomUtils.getChildElements(el); 
        for (Element dsEl : dsEls) {
          BeanDefinitionBuilder dsBuilder = BeanDefinitionBuilder.rootBeanDefinition(TreeDataSource.class);
          dsBuilder.addPropertyValue("level", dsEl.getAttribute("level"));
          dsBuilder.addPropertyValue("idField", dsEl.getAttribute("idField"));
          dsBuilder.addPropertyReference("datasource", dsEl.getAttribute("ref"));
          datasources.add(dsBuilder.getBeanDefinition()); 
        }
      }
    }
    bdBuilder.addPropertyValue("datasources", datasources); 
    bdBuilder.addPropertyValue("links", links); 
    AbstractBeanDefinition result = bdBuilder.getBeanDefinition();
    pc.getRegistry().registerBeanDefinition(id, result);
    return result;
  }
  
}
