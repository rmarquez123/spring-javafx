package com.rm._springjavafx.ignore.testdateranges;

import com.rm.springjavafx.charts.category.CategoryFxDataSet;
import com.rm.springjavafx.charts.category.CategoryValue;
import com.rm.springjavafx.charts.category.SpringFxCategoryDataSet;
import com.rm.springjavafx.charts.category.datasets.HasRanges;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.SetChangeListener;
import org.apache.commons.lang3.Range;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;

/**
 *
 * @author Ricardo Marquez
 */
//@Component
@Lazy(true)
@CategoryFxDataSet(
  name = "Green Data",
  chart = "testchart",
  dataset = 0,
  lineColorHex = "#00aa00"
)
public class GreenDataSet extends SpringFxCategoryDataSet implements InitializingBean {

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
    HasRanges<Double> userObj = () -> Arrays.asList(Range.between(y - 12, y - 2), Range.between(y, y + 10));
    CategoryValue result = new CategoryValue(category, y, userObj);
    return result;
  }

  /**
   *
   * @param c
   */
  private void onCategoriesChanged(SetChangeListener.Change c) {
    this.updateRecords();
  }
}
