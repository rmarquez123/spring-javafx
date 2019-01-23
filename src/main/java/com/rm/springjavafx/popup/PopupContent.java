package com.rm.springjavafx.popup;

import java.io.IOException;

/**
 *
 * @author rmarquez
 */
public interface PopupContent {

  /**
   *
   * @param popup
   */
  public void setPopupWindow(Popup popup);

  void onClose() throws IOException;
}
