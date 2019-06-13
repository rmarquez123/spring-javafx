package com.rm.springjavafx.menu;

import javafx.scene.input.KeyEvent;

/**
 *
 * @author Ricardo Marquez
 */
public enum KeyModifier {
  NONE {
    @Override
    boolean test(KeyEvent evt) {
      return !evt.isAltDown() 
        && !evt.isControlDown() 
        && !evt.isShiftDown() 
        && !evt.isShortcutDown() 
        && !evt.isMetaDown(); 
    }
  }, CONTROL {
    @Override
    boolean test(KeyEvent evt) {
      return evt.isControlDown();
    }
  }, ALT {
    @Override
    boolean test(KeyEvent evt) {
      return evt.isAltDown();
    }
  }, SHIFT {
    @Override
    boolean test(KeyEvent evt) {
      return evt.isShiftDown();
    }
  };

  abstract boolean test(KeyEvent evt); 
}
