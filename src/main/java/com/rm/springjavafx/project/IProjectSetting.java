package com.rm.springjavafx.project;

import java.io.Serializable;

/**
 *
 * @author Ricardo Marquez
 */
public interface IProjectSetting {

  /**
   *
   * @return
   */
  Serializable serialize();

  /**
   *
   * @param serializable
   */
  void deSerialize(Serializable serializable);
}
