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

  @Around("@annotation(intercepted)")
  public Object handleInterceptedMethod(ProceedingJoinPoint pjp, Intercepted intercepted) throws Throwable {
    Class<?> interceptorClass = intercepted.value();
    if (Interceptor.class.isAssignableFrom(interceptorClass)) {
      Interceptor interceptor = (Interceptor) context.getBean(interceptorClass);
      return interceptor.execute(pjp);
    } else {
      throw new IllegalStateException("Provided class does not implement Interceptor interface");
    }
  }
}
