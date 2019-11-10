package com.rm._springjavafx.ignore.testdatesbarchart;

import com.rm.springjavafx.charts.category.CategoryFxDataSet;
import com.rm.springjavafx.charts.category.CategoryValue;
import com.rm.springjavafx.charts.category.SpringFxCategoryDataSet;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.SetChangeListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
@Lazy(false)
@CategoryFxDataSet(
  name = "Blue Data",
  chart = "testchart",
  dataset = 0,
  lineColorHex = "#0000aa"
)
public class BlueDataSet extends SpringFxCategoryDataSet implements InitializingBean {

  /** 
   *
   * @throws Exception
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    super.categoriesProperty().addListener(this::onCategoriesChanged);
    this.updateRecords();
  }

  /**
   *
   */
  private void updateRecords() {
    List<CategoryValue> records = super.categoriesProperty().stream()
      .map(this::toRecord)
      .collect(Collectors.toList());
    super.setTimeSeries(records);
  }

  /**
   * 
   * @param category
   * @return 
   */
  public CategoryValue toRecord(String category) {
    double y = Math.random() * 100;
    return new CategoryValue(y, null);
  }

  /**
   *
   * @param c
   */
  private void onCategoriesChanged(SetChangeListener.Change c) {
    this.updateRecords();
  }
}
