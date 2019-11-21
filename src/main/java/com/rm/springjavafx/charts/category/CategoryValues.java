package com.rm.springjavafx.charts.category;

import java.util.Iterator;
import java.util.List;


/**
 *
 * @author Ricardo Marquez
 */
public class CategoryValues implements Iterable<CategoryValue>{

  private final String key;
  private final List<CategoryValue> values;   
  
  /**
   * 
   * @param key
   * @param values 
   */
  public CategoryValues(String key, List<CategoryValue> values) {
    this.key = key;
    this.values = values;
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public Iterator<CategoryValue> iterator() {
    return this.values.iterator();
  }
  
  /**
   * 
   * @return 
   */
  public Comparable getKey() {
    return this.key;
  }

  /**
   * 
   * @return 
   */
  public int size() {
    return this.values.size();
  }

  /**
   * 
   * @param i1
   * @return 
   */
  public Number getY(String category) {
    CategoryValue value = getCategoryValue(category); 
    Number result = value.getY();
    return result;
  }
  
  /**
   * 
   * @param category
   * @return 
   */
  private CategoryValue getCategoryValue(String category) {
    CategoryValue value = this.values.stream()
      .filter((c)->c.getCategory().equals(category))
      .findFirst()
      .orElseThrow(()->new RuntimeException("Invalid category : " + category));
    return value;
  }
  
  /**
   * 
   * @param i
   * @return 
   */
  public Object getUserObj(String category) {
    CategoryValue value = getCategoryValue(category); 
    Object result = value.getUserObj();
    return result;
  }
}
