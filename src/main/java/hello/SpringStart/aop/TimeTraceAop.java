package hello.SpringStart.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class TimeTraceAop {

  @Around("execution(* hello.SpringStart.service..*(..)) && !target(hello.SpringStart.SpringConfig)")
  public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();

    System.out.println("START: " + joinPoint.toString());

    try {
      return joinPoint.proceed();
    } finally {
      long end = System.currentTimeMillis();
      long timeMs = end - start;

      System.out.println("END: " + joinPoint.toString() + " " + timeMs + "ms");
    }
  }
}
