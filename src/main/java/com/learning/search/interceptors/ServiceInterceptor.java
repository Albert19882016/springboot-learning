package com.learning.search.interceptors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
@Aspect
public class ServiceInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ServiceInterceptor.class);

    /**
     * 前置通知：目标方法执行之前执行以下方法体的内容
     */
    @Before("execution(* com.learning.search.serviceImpl.*.*(..))")
    public void beforeMethod(JoinPoint jp){
        String methodName = jp.getSignature().getName();
        log.info("【调用前】the method 【" + methodName + "】 入参 " + Arrays.asList(jp.getArgs()));
    }

    /**
     * 返回通知：目标方法正常执行完毕时执行以下代码
     */
    @AfterReturning(value="execution(* com.learning.search.serviceImpl.*.*(..))",returning="result")
    public void afterReturningMethod(JoinPoint jp, Object result){
        String methodName = jp.getSignature().getName();
        log.info("【正常】the method 【" + methodName + "】 返回 【" + result + "】");
    }

    /**
     * 后置通知：目标方法执行之后执行以下方法体的内容，不管是否发生异常。
     */
    @After("execution(* com.learning.search.serviceImpl.*.*(..))")
    public void afterMethod(JoinPoint jp){
        log.info("【后置通知】this is a afterMethod advice...");
    }

    /**
     * 异常通知：目标方法发生异常的时候执行以下代码
     */
    @AfterThrowing(value="execution(* com.learning.search.serviceImpl.*.*(..))",throwing="e")
    public void afterThorwingMethod(JoinPoint jp, NullPointerException e){
        String methodName = jp.getSignature().getName();
        log.info("【异常】the method 【" + methodName + "】 occurs exception: " + e);
    }

//  /**
//   * 环绕通知：目标方法执行前后分别执行一些代码，发生异常的时候执行另外一些代码
//   * @return
//   */
//  @Around(value="execution(* com.qcc.beans.aop.*.*(..))")
//  public Object aroundMethod(ProceedingJoinPoint jp){
//      String methodName = jp.getSignature().getName();
//      Object result = null;
//      try {
//          System.out.println("【环绕通知中的--->前置通知】：the method 【" + methodName + "】 begins with " + Arrays.asList(jp.getArgs()));
//          //执行目标方法
//          result = jp.proceed();
//          System.out.println("【环绕通知中的--->返回通知】：the method 【" + methodName + "】 ends with " + result);
//      } catch (Throwable e) {
//          System.out.println("【环绕通知中的--->异常通知】：the method 【" + methodName + "】 occurs exception " + e);
//      }
//
//      System.out.println("【环绕通知中的--->后置通知】：-----------------end.----------------------");
//      return result;
//  }

}
