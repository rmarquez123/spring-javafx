package com.rm.springjavafx.bindings;

import java.util.List;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import static org.springframework.beans.factory.xml.AbstractBeanDefinitionParser.ID_ATTRIBUTE;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author rmarquez
 */
public class TabsBindingBeanDefParser extends AbstractBeanDefinitionParser {
  
  /**
   * 
   * @param elmnt
   * @param pc
   * @return 
   */
  @Override
  protected AbstractBeanDefinition parseInternal(Element elmnt, ParserContext pc) {
    BeanDefinitionBuilder result = BeanDefinitionBuilder.rootBeanDefinition(TabsBinding.class);
    
    String id = elmnt.getAttribute(ID_ATTRIBUTE);
    if (id != null && !id.isEmpty()) {
      pc.getRegistry().registerBeanDefinition(id, result.getBeanDefinition());
    }
    List<Element> els = DomUtils.getChildElements(elmnt); 
    ManagedList<BeanDefinition> tabBeans = new ManagedList<>();  
    for (Element el : els) {
      if (el.getTagName().endsWith("contentpanel")) {
        result.addPropertyValue("fxml", el.getAttribute("fxml")); 
        result.addPropertyValue("fxmlId", el.getAttribute("fxmlId"));
      } else if (el.getTagName().endsWith("tabs")) {
        List<Element> tabEls = DomUtils.getChildElements(el); 
        for (Element tagEl : tabEls) {
          BeanDefinitionBuilder tagBeanDefBuilder = BeanDefinitionBuilder.rootBeanDefinition(TabItem.class);
          tagBeanDefBuilder.addPropertyValue("selectionId", tagEl.getAttribute("selectionId")); 
          tagBeanDefBuilder.addPropertyValue("fxml", tagEl.getAttribute("fxml")); 
          tagBeanDefBuilder.addPropertyValue("fxmlId", tagEl.getAttribute("fxmlId")); 
          tagBeanDefBuilder.addPropertyValue("label", tagEl.getAttribute("label")); 
        }
      }
    }
    result.addPropertyValue("tabs", tabBeans); 
    result.addPropertyReference("listRef", elmnt.getAttribute("listRef")); 
    return result.getBeanDefinition();
  }
}
