package com.rm.testcustomspring;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javafx.embed.swing.JFXPanel;
import javax.swing.SwingUtilities;
import org.junit.BeforeClass;

/**
 *
 * @author rmarquez
 */
public class JavaFxTest {

  @BeforeClass
  public static void initToolkit() throws InterruptedException {
    final CountDownLatch latch = new CountDownLatch(1);
    SwingUtilities.invokeLater(() -> {
      JFXPanel jfxPanel = new JFXPanel(); 
      latch.countDown();
    });
    if (!latch.await(5L, TimeUnit.SECONDS)) {
      throw new ExceptionInInitializerError();
    }
  }
}
