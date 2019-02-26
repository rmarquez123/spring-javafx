package com.rm.springjavafx.popup;

import java.io.IOException;

/**
 *
 * @author rmarquez
 */
public interface PopupContent {

  /**
   * Passes the popup to the host class so that the reference window can be set. A typical
   * example would be to get the scene from a node, such as a button.
   *
   * @param popup
   */
  public void setPopupWindow(Popup popup);

  /**
   * Is called when the popup closes for any reason. Alternatively, clients (or hosts) can
   * bind to the {@linkplain Popup#getShowProperty()}. 
   *
   * @throws IOException
   */
  void onClose() throws IOException;
}
