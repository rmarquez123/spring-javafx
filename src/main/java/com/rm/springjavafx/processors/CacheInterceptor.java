package com.rm.springjavafx.processors;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class CacheInterceptor implements Interceptor {

  private final Map<String, Map<Object, Object>> cache = new HashMap<>();
    
  /**
   * 
   * @param event 
   */
  @EventListener 
  public void onEvent(DataStoreEvent event) {
    System.out.println("Clearing cache");
    this.cache.clear();
  }
  /**
   * 
   * @param pjp
   * @return
   * @throws Throwable 
   */
  @Override
  public Object execute(ProceedingJoinPoint pjp) throws Throwable {
    Object result;
    if (cacheContainsValueFor(pjp)) {
      result = this.getCachedValueFor(pjp);
    } else {
      result = pjp.proceed();
      this.cacheValue(pjp, result);
    }
    return result;
  }
  
  /**
   * 
   * @param pjp
   * @return 
   */
  private boolean cacheContainsValueFor(ProceedingJoinPoint pjp) {
    String methodName = getPjpName(pjp);
    Object key = getKeyFromArgs(pjp.getArgs());
    return cache.containsKey(methodName) && cache.get(methodName).containsKey(key);
  }
  
  /**
   * 
   * @param pjp
   * @return 
   */
  private Object getCachedValueFor(ProceedingJoinPoint pjp) {
    String methodName = getPjpName(pjp);
    Object key = getKeyFromArgs(pjp.getArgs());
    return cache.get(methodName).get(key);
  }
  
  /**
   * 
   * @param pjp
   * @return 
   */
  private String getPjpName(ProceedingJoinPoint pjp) {
    String className = pjp.getSignature().getDeclaringType().getSimpleName();
    String result = className + "." + methodName(pjp);
    return result;
  }

  private String methodName(ProceedingJoinPoint pjp) {
    return pjp.getSignature().getName();
  }
  
  /**
   * 
   * @param pjp
   * @param result 
   */
  private void cacheValue(ProceedingJoinPoint pjp, Object result) {
    String methodName = getPjpName(pjp);
    Object key = getKeyFromArgs(pjp.getArgs());
    cache.computeIfAbsent(methodName, k -> new HashMap<>()).put(key, result);
  }
  
  /**
   * 
   * @param args
   * @return 
   */
  private Object getKeyFromArgs(Object[] args) {
    return Arrays.stream(args)
      .map(Object::toString)
      .collect(Collectors.joining(":"));
  }

}
