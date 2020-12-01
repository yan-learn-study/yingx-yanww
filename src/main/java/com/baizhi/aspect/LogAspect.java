package com.baizhi.aspect;

import com.baizhi.annocation.LogAnnocation;
import com.baizhi.dao.LogMapper;
import com.baizhi.entity.Admin;
import com.baizhi.entity.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author yww
 * @Description
 * @Date 2020/11/24 17:31
 */
@Configuration
@Aspect
public class LogAspect {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    LogMapper logMapper;

    @Around("@annotation(com.baizhi.annocation.LogAnnocation)")
    public Object aroundLog(ProceedingJoinPoint proceedingJoinPoint) {
        //谁  时间  操作  成功

        //获取用户数据
        Admin admin = (Admin) request.getSession().getAttribute("admin");

        //获取方法名
        String methodName = proceedingJoinPoint.getSignature().getName();

        //获取方法
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();

        //获取注解
        LogAnnocation logAnnocation = method.getAnnotation(LogAnnocation.class);

        //获取注解对应的属性值
        String value = logAnnocation.value();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", "操作成功");
        hashMap.put("status", 200);
        String message;
        Object result = null;
        //放行方法
        try {
            result = proceedingJoinPoint.proceed();
            message = "SUCCESS";
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            hashMap.put("message", throwable.getMessage());
            hashMap.put("status", 201);
            message = "ERROR";
        }
        String name;
        if (admin == null) name = "admin";
        else name = admin.getUsername();
        Log log = new Log(UUID.randomUUID().toString(), name, LocalDateTime.now(), methodName + " (" + value + ")", message);

        logMapper.insert(log);

        if (result == null) return hashMap;
        return result;

    }
}
