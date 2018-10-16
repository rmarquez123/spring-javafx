package com.rm.springjavafx.datasources;

/**
 *
 * @author rmarquez
 * @param <T0> source
 * @param <T1> destination
 */
public interface Converter<T0, T1> {
  
  /**
   * 
   */
  public static Converter NONE = (Converter) (Object source) -> source;

  /**
   *
   * @param source
   * @return
   */
  public T1 convert(T0 source);
}
