
package com.rm.springjavafx.components;

import com.rm.springjavafx.datasources.ListFromDataSourceFactory;
import java.util.List;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
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
    String datasource = elmnt.getAttribute("datasource-ref"); 
    result.addPropertyValue("dataSourceRef", datasource); 
    result.addPropertyValue("valueRef", elmnt.getAttribute("value-ref"));
    List<Element> childEls = DomUtils.getChildElements(elmnt);
    
    for (Element childEl : childEls) {
      if (childEl.getTagName().endsWith("listitem-converter")) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ObjectInstanceFactory.class); 
        new ObjectBeanDefParser().doParse(childEl, builder);
        AbstractBeanDefinition bd = builder.getBeanDefinition(); 
        result.addPropertyValue("listItemConverter", bd); 
        
      } else if (childEl.getTagName().endsWith("selectionitem-converter")) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(ObjectInstanceFactory.class); 
        new ObjectBeanDefParser().doParse(childEl, builder);
        BeanDefinitionBuilder listBeanDef = BeanDefinitionBuilder.rootBeanDefinition(ListFromDataSourceFactory.class); 
        listBeanDef.addPropertyValue("dataSource", datasource);
        ManagedMap<String, Object> props = (ManagedMap) builder.getBeanDefinition().getPropertyValues().get("properties"); 
        props.put("list", listBeanDef.getBeanDefinition());
        builder.addPropertyValue("properties", props);
        AbstractBeanDefinition bd = builder.getBeanDefinition(); 
        result.addPropertyValue("selectionItemConverter", bd); 
      }
    }
    result.setLazyInit(false); 
    pc.getRegistry().registerBeanDefinition(id, result.getBeanDefinition());
    return result.getBeanDefinition();
  }
  
}
