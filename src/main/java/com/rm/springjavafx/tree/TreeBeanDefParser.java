package com.rm.springjavafx.tree;

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

/**
 *
 * @author rmarquez
 */
public class TreeBeanDefParser extends AbstractBeanDefinitionParser {

  @Override
  protected AbstractBeanDefinition parseInternal(Element elmnt, ParserContext pc) {
    BeanDefinitionBuilder result = BeanDefinitionBuilder.rootBeanDefinition(TreeFactory.class);
    result.addPropertyValue("id", elmnt.getAttribute(ID_ATTRIBUTE));
    result.addPropertyValue("fxml", elmnt.getAttribute("fxml"));
    result.addPropertyValue("fxmlId", elmnt.getAttribute("fxmlId"));
    result.addPropertyReference("treeModel", elmnt.getAttribute("treemodelRef"));
    List<Element> els = DomUtils.getChildElements(elmnt);
    ManagedList<BeanDefinition> cellFactories = new ManagedList<>();
    for (Element el : els) {
      if (el.getTagName().endsWith("level-cellfactory")) {
        BeanDefinitionBuilder bd = BeanDefinitionBuilder.rootBeanDefinition(LevelCellFactory.class);
        bd.addPropertyValue("level", el.getAttribute("level"));
        bd.addPropertyValue("textField", el.getAttribute("textField"));
        bd.addPropertyReference("contextMenuProvider", el.getAttribute("contextMenuRef"));
        cellFactories.add(bd.getBeanDefinition());
      }
    }
    result.addPropertyValue("cellFactories", cellFactories);
    return result.getBeanDefinition();
  }

}
