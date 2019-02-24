package com.windsim.wpls.db.entities;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Ricardo Marquez
 */
@RunWith(JUnitParamsRunner.class)
public class NumberFormatTest {
  
  @Test
  @Parameters({
    "0.00001, [         0.00001]", 
    "912.066, [       912.06600]"
  })
  public void test1(double number, String expText){
    NumberFormat formatter = new DecimalFormat("0.00000"); 
    String text = String.format("%16s", formatter.format(number));
    String replace = expText.trim().replace("[", "").replace("]", "");
    Assert.assertEquals("", replace, text);
  }
  
  
  @Test
  @Parameters({
    "0.00001", 
    "912.066"
  })
  public void test2(double number){
    NumberFormat formatter = new DecimalFormat(".00000E00"); 
    String text = String.format("%16s", "0"+formatter.format(number).replace("E0", "E+0"));
    System.out.println(text);
  }
  
  
  
}
