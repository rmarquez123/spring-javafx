package com.rm.springjavafx.processors;

import common.RmTimer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

/**
 *
 * @author Ricardo Marquez
 */
@Component
public class TimerInterceptor implements Interceptor{
  
  /**
   * 
   * @param pjp
   * @return
   * @throws Throwable 
   */
  @Override
  public Object execute(ProceedingJoinPoint pjp) throws Throwable {
    RmTimer s = RmTimer.start();
    Object result = pjp.proceed();
    s.endAndPrint(pjp.getSignature().getName() + ":");
    return result;
  }
  
}
