package com.rm.springjavafx.processors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Aspect
@Component
public class CustomInterceptAspect {

  @Autowired
  private ApplicationContext context;
  
  /**
   * 
   * @param pjp
   * @param intercepted
   * @return
   * @throws Throwable 
   */
  @Around("@annotation(intercepted)")
  public Object handleInterceptedMethod(ProceedingJoinPoint pjp, Intercepted intercepted) throws Throwable {
    Class<? extends Interceptor> interceptorClass = intercepted.value();
    Interceptor interceptor = context.getBean(interceptorClass);
    return interceptor.execute(pjp);
  }
}
