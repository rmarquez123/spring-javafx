package com.rm.testcustomspring;

import com.rm.springjavafx.bindings.TabsBinding;
import com.rm.springjavafx.properties.ElementSelectableListProperty;
import com.rm.testrmfxmap.javafx.FxmlInitializer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author rmarquez
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring/tabsbinding.xml"})
public class TabsBindingTest {

  @Autowired
  FxmlInitializer fxmlInitializer;

  @Autowired
  ApplicationContext appContext;

  @Autowired
  private TabsBinding tabsBinding;
  @Autowired
  @Qualifier("windowsgroup")
  private ElementSelectableListProperty<?> windowsgroup;

  @BeforeClass
  public static void setUp() throws InterruptedException {
    JavaFxTest.initToolkit();
  }

  /**
   *
   * @throws java.lang.Exception
   */
  @Test
  public void test() throws Exception {
    this.fxmlInitializer.load(this.appContext);
    Assert.assertNotNull(this.tabsBinding);
    Assert.assertNotNull(this.windowsgroup);
  }
}
