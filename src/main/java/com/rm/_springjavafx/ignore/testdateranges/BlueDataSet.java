package com.rm._springjavafx.ignore.testdateranges;

import com.rm.springjavafx.charts.category.CategoryFxDataSet;
import com.rm.springjavafx.charts.category.CategoryValue;
import com.rm.springjavafx.charts.category.SpringFxCategoryDataSet;
import com.rm.springjavafx.charts.category.datasets.HasRanges;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.collections.SetChangeListener;
import org.apache.commons.lang3.Range;
import org.apache.commons.math3.util.Pair;
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

  private final Map<String, List<Pair<String, String>>> dateRanges;
  private final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

  public BlueDataSet() {
    this.dateRanges = new HashMap<>();
    this.dateRanges.put("category 01", Arrays.asList(
      Pair.create("2017/01/01 00:00", "2017/02/01 00:00"),  
      Pair.create("2017/03/15 00:00", "2017/04/01 00:00")));
    this.dateRanges.put("category 02", Arrays.asList(Pair.create("2017/02/01 00:00", "2017/04/01 00:00")));
    this.dateRanges.put("category 03", Arrays.asList(Pair.create("2017/03/01 00:00", "2017/06/01 00:00")));
    this.dateRanges.put("category 04", Arrays.asList(Pair.create("2017/04/01 00:00", "2017/05/01 00:00")));
    this.dateRanges.put("category 05", Arrays.asList(Pair.create("2017/01/01 00:00", "2017/04/01 00:00")));
    this.dateRanges.put("category 06", Arrays.asList(Pair.create("2017/01/01 00:00", "2017/03/01 00:00")));
  }

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
    List<Range<Double>> ranges = this.dateRanges.get(category).stream()
      .map(this::toDateRange)
      .collect(Collectors.toList());

    double y = ranges.stream()
      .mapToDouble((e)->0.5*(e.getMaximum() + e.getMinimum()))
      .average()
      .orElse(0.0);
    HasRanges<Double> userObj = () -> ranges;
    CategoryValue result = new CategoryValue(category, y, userObj);
    return result;
  }

  /**
   *
   * @param pair
   * @return
   */
  private Range<Double> toDateRange(Pair<String, String> pair) {
    double startdt;
    double enddt;
    try {
      startdt = (double) (this.dateformat.parse(pair.getFirst()).getTime());
      enddt = (double) (this.dateformat.parse(pair.getSecond()).getTime());
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    Range<Double> result = Range.between(startdt, enddt);
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
