package com.baizhi.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author yww
 * @Description
 * @Date 2020/11/27 19:23
 */
@Aspect
@Configuration
public class HashCacheAspect {
    private static final Logger log = LoggerFactory.getLogger(HashCacheAspect.class);
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 添加缓存
     *
     * @param proceedingJoinPoint
     * @return
     */
    @Around("@annotation(com.baizhi.annocation.AddCache)")
    public Object addCache(ProceedingJoinPoint proceedingJoinPoint) {
        log.info("===环绕通知-添加缓存===");
        //解决序列化乱码
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        StringBuffer stringBuffer = new StringBuffer();
        //获取类名
        String className = proceedingJoinPoint.getTarget().getClass().getName();

        //获取方法名
        String methodName = proceedingJoinPoint.getSignature().getName();
        stringBuffer.append(methodName + "(");

        //获取参数
        Object[] args = proceedingJoinPoint.getArgs();
        for (Object arg : args) stringBuffer.append(arg + " ");
        stringBuffer.append(")");

        //判断key是否存在
        HashOperations hash = redisTemplate.opsForHash();
        String key = stringBuffer.toString();
        Boolean aBoolean = hash.hasKey(className, key);

        Object result = null;
        if (aBoolean) result = hash.get(className, key);
        else {
            try {
                result = proceedingJoinPoint.proceed();
                hash.put(className, key, result);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return result;
    }

    @After("@annotation(com.baizhi.annocation.ClearCache)")
    public void clearCache(JoinPoint joinPoint) {
        log.info("===后置通知-清除缓存===");
        //获取类名
        String className = joinPoint.getTarget().getClass().getName();
        //清除缓存
        redisTemplate.delete(className);
    }
}
