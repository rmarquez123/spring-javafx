/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm.springjavafx.treetable;

import com.rm.springjavafx.tree.LevelCellFactory;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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
public class TreeTableBeanDefParser extends AbstractBeanDefinitionParser {

  @Override
  protected AbstractBeanDefinition parseInternal(Element elmnt, ParserContext pc) {
    BeanDefinitionBuilder result = BeanDefinitionBuilder.rootBeanDefinition(TreeTableFactory.class);
    result.addPropertyValue("id", elmnt.getAttribute(ID_ATTRIBUTE));
    result.addPropertyValue("fxml", elmnt.getAttribute("fxml"));
    result.addPropertyValue("fxmlId", elmnt.getAttribute("fxmlId"));
    result.addPropertyReference("treeModel", elmnt.getAttribute("treemodelRef"));
    List<Element> els = DomUtils.getChildElements(elmnt);
    ManagedList<BeanDefinition> cellFactories = new ManagedList<>();
    for (Element el : els) {
      if (el.getTagName().endsWith("tt-columns")) {
        DomUtils.getChildElements(el).stream()
                .filter((t) -> t.getTagName().endsWith("tt-column"))
                .map((e) -> {
                  List<BeanDefinition> levelCellFactories = DomUtils.getChildElements(e).stream()
                          .filter((f) -> f.getTagName().endsWith("level-cellfactory"))
                          .map((m) -> {
                            BeanDefinitionBuilder bd = BeanDefinitionBuilder.rootBeanDefinition(LevelCellFactory.class);
                            bd.addPropertyValue("level", m.getAttribute("level"));
                            bd.addPropertyValue("textField", m.getAttribute("textField"));
                            return bd.getBeanDefinition();
                          }).collect(Collectors.toList());
                  BeanDefinitionBuilder bd = BeanDefinitionBuilder.rootBeanDefinition(TreeTableColumnDef.class);
                  bd.addPropertyValue("colIndex", e.getAttribute("colindex"));
                  bd.addPropertyValue("label", e.getAttribute("label"));
                  ManagedList<BeanDefinition> list = new ManagedList<>();
                  list.addAll(levelCellFactories); 
                  bd.addPropertyValue("cellFactories",list);
                  return bd.getBeanDefinition();
                }).forEach(bd -> {
          cellFactories.add(bd);
        });
      }
    }
    result.addPropertyValue("cellFactories", cellFactories);
    return result.getBeanDefinition();
  }
}
