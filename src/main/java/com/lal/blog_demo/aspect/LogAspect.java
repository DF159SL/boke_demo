package com.lal.blog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.hibernate.mapping.Join;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.lal.blog.web.*.*(..))")
    public void log() {
    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        //获取请求，通过请求拿到各种参数
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //请求的url
        String url = request.getRequestURL().toString();
        //请求的IP
        String ip = request.getRemoteAddr();
        //获取切面方法的DeclaringTypeName类名，getName方法名
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        //切面方法中的参数
        Object[] args = joinPoint.getArgs();
        //自定义内部封装类
        RequestLog requestLog = new RequestLog(url, ip, classMethod, args);
        logger.info("Request : {}", requestLog);
    }

    @After("log()")
    public void doAfter() {
        logger.info("--------doAfter-----------");
    }

    @AfterReturning(returning = "result", pointcut = "log()")
    public void doAfterRuturn(Object result) {
        logger.info("Result : {}", result);
    }

    private class RequestLog {
        private String url;
        private String ip;
        private String classMethed;
        private Object[] args;

        public RequestLog(String url, String ip, String classMethed, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethed = classMethed;
            this.args = args;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getClassMethed() {
            return classMethed;
        }

        public void setClassMethed(String classMethed) {
            this.classMethed = classMethed;
        }

        public Object[] getArgs() {
            return args;
        }

        public void setArgs(Object[] args) {
            this.args = args;
        }

        @Override
        public String toString() {
            return "RequestLog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethed='" + classMethed + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
