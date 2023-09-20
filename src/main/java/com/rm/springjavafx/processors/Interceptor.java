package com.rm.springjavafx.processors;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 *
 * @author Ricardo Marquez
 */
public interface Interceptor {
    Object execute(ProceedingJoinPoint pjp) throws Throwable;
}
