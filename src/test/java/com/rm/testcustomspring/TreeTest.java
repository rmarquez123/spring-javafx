package com.rm.testcustomspring;

import com.rm.springjavafx.tree.TreeModel;
import com.rm.testrmfxmap.javafx.FxmlInitializer;
import javafx.scene.control.TreeView;
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
@ContextConfiguration({"/spring/tree_example.xml"})
public class TreeTest {

  @Autowired
  FxmlInitializer fxmlInitializer;

  @Autowired
  ApplicationContext appContext;

  @Autowired
  @Qualifier("sampletreemodel")
  TreeModel treeModel;
  
  @Autowired
  @Qualifier("sampletree")
  TreeView tree;

  @BeforeClass
  public static void setUp() throws InterruptedException {
    JavaFxTest.initToolkit();
  }
  
  @Test
  public void test1() {
    this.fxmlInitializer.load(this.appContext);
    Assert.assertNotNull(this.treeModel);
  }

}
