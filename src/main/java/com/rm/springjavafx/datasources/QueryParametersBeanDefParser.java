
package com.rm.springjavafx.datasources;

import common.db.QueryParameters;
import java.util.List;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

/**
 *
 * @author rmarquez
 */
public class QueryParametersBeanDefParser extends AbstractSingleBeanDefinitionParser {

  @Override
  protected Class<?> getBeanClass(Element element) {
    return QueryParameters.class;   
  }
  
  
  
  @Override
  protected void doParse(Element element, BeanDefinitionBuilder builder) {
    List<Element> elemens = DomUtils.getChildElements(element);
    ManagedList<BeanDefinition> queryParamsList = new ManagedList<>();
    for (Element elemen : elemens) {
      BeanDefinitionBuilder elBuilder = BeanDefinitionBuilder.rootBeanDefinition(QueryParameterFactory.class); 
      new QueryParameterBeanDefParser().doParse(elemen, elBuilder);
      queryParamsList.add(elBuilder.getBeanDefinition());
    }
    String invokerRef = element.getAttribute("invokerRef");
    if (invokerRef != null && !invokerRef.isEmpty()) {
      builder.addPropertyReference("invoker", invokerRef); 
    }
    builder.addConstructorArgValue(queryParamsList);
  }
}
