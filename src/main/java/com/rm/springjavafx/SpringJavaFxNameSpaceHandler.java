package com.rm.springjavafx;

import com.rm.springjavafx.components.ComboBoxBeanDefParser;
import com.rm.springjavafx.datasources.DbConnectionBeanDefParser;
import com.rm.springjavafx.datasources.QueryParameterBeanDefParser;
import com.rm.springjavafx.datasources.QueryParametersBeanDefParser;
import com.rm.springjavafx.datasources.SqlDataSourceBeanDefParser;
import com.rm.springjavafx.properties.DateRangeBeanDefParser;
import com.rm.springjavafx.properties.IntegerPropertyBeanDefParser;
import com.rm.springjavafx.table.TableViewBeanDefParser;
import com.rm.springjavafx.components.TextFieldBeanDefParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 *
 * @author rmarquez
 */
public class SpringJavaFxNameSpaceHandler extends NamespaceHandlerSupport{
  
  
  @Override
  public void init() {
    
    this.registerBeanDefinitionParser("int-property", new IntegerPropertyBeanDefParser());
    this.registerBeanDefinitionParser("daterange-property", new DateRangeBeanDefParser());
    this.registerBeanDefinitionParser("db-connection", new DbConnectionBeanDefParser());
    this.registerBeanDefinitionParser("sql-datasource", new SqlDataSourceBeanDefParser());    
     
    this.registerBeanDefinitionParser("query-params", new QueryParametersBeanDefParser()); 
    this.registerBeanDefinitionParser("query-param", new QueryParameterBeanDefParser()); 
    
    this.registerBeanDefinitionParser("tableview", new TableViewBeanDefParser());
    this.registerBeanDefinitionParser("textfield", new TextFieldBeanDefParser());
    this.registerBeanDefinitionParser("combobox", new ComboBoxBeanDefParser());
  }
}
