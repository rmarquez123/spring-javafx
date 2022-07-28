package com.rm.springjavafx.table;

import java.util.List;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.stereotype.Component;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 *
 * @author rmarquez
 */
@Component
public class TableViewBeanDefParser extends AbstractBeanDefinitionParser {

  /**
   *
   * @param elmnt
   * @param pc
   * @return
   */
  @Override
  protected AbstractBeanDefinition parseInternal(Element elmnt, ParserContext pc) {
    BeanDefinitionBuilder result = this.getNewBeanDefBuilder();
    result.addPropertyValue("id", elmnt.getAttribute(ID_ATTRIBUTE));
    result.addPropertyValue("fxml", elmnt.getAttribute("fxml"));
    result.addPropertyValue("fxmlId", elmnt.getAttribute("fxmlId"));
    this.addDataSourceBean(elmnt, result, pc);
    this.addChildElements(elmnt, result);
    System.out.println("id = " + elmnt.getAttribute(ID_ATTRIBUTE));
    return result.getBeanDefinition();
  } 
  
  /**
   * 
   * @return 
   */
  private BeanDefinitionBuilder getNewBeanDefBuilder() {
    BeanDefinitionBuilder result = BeanDefinitionBuilder.rootBeanDefinition(TableViewFactory.class);
    return result;
  }
  
  /**
   * 
   * @param elmnt
   * @param result
   * @param pc
   * @throws NoSuchBeanDefinitionException
   * @throws BeanDefinitionStoreException 
   */
  private void addDataSourceBean(Element elmnt, BeanDefinitionBuilder result, ParserContext pc) {
    String datasource = elmnt.getAttribute("datasource-ref");
    result.addPropertyValue("datasourceRef", datasource);
    BeanDefinitionRegistry registry = pc.getRegistry();
    BeanDefinition datasourceBeanDef = registry.getBeanDefinition(datasource);
    if (!registry.containsBeanDefinition(datasource))  {
      registry.registerBeanDefinition(datasource, datasourceBeanDef);
    }
  } 
  
  /**
   * 
   * @param elmnt
   * @param result 
   */
  private void addChildElements(Element elmnt, BeanDefinitionBuilder result) {
    List<Element> childEls = DomUtils.getChildElements(elmnt);
    ManagedList<BeanDefinition> colRenderers = new ManagedList<>();
    childEls.stream().forEach((c) -> {
      if (c.getTagName().endsWith("columns")) {
        List<Element> cChildEls = DomUtils.getChildElements(c); 
        for (Element cChildEl : cChildEls) {
          AbstractBeanDefinition bd = this.getColumnDefBean(cChildEl);
          colRenderers.add(bd);
        }
      }
    }); 
    result.addPropertyValue("columns", colRenderers);
  }
  
  /**
   * 
   * @param c
   * @return 
   */
  private AbstractBeanDefinition getColumnDefBean(Element c) {
    BeanDefinitionBuilder beanDefBuilder = //
      BeanDefinitionBuilder.rootBeanDefinition(TableViewColumn.class);
    ColumnDefBeanDefParser beanDefParser = new ColumnDefBeanDefParser();
    beanDefParser.doParse(c, beanDefBuilder);
    AbstractBeanDefinition bd = beanDefBuilder.getBeanDefinition();
    return bd;
  }

}
