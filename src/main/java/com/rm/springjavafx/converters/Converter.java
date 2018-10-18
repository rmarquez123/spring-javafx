package com.rm.springjavafx.converters;

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
  public static Converter NONE = new Converter() {
    @Override
    public Object convert(Object source) {
      return source;
    }
    @Override
    public Object deconvert(Object value) {
      return value;
    }
  };

  /**
   *
   * @param source
   * @return
   */
  public T1 convert(T0 source);

  public T0 deconvert(T1 value);
}
