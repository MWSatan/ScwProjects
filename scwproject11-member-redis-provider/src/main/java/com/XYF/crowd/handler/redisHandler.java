package com.XYF.crowd.handler;

import com.XYF.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @username 熊一飞
 */

@RestController
public class redisHandler {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @RequestMapping("/set/redis/key/value/remote")
    ResultEntity<String> setRedisValueRemote(@RequestParam("key") String key, @RequestParam("value") String value) {

        try {
            ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();
            operations.set(key,value);
            return ResultEntity.successWithoutData();

        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failWithMessage(e.getMessage());
        }

    }


    @RequestMapping("/set/redis/key/value/remote/timeout")
    ResultEntity<String> setRedisValueRemoteTimeOut(
            @RequestParam("key") String key,
            @RequestParam("value") String value,
            @RequestParam("time") long time,
            @RequestParam("timeUnit") TimeUnit timeUnit) {

//        http://localhost:3000/set/redis/key/value/remote/timeout?key=grape&value=purple&time=5000&timeUnix=SECONDS
        try {
            ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();
            operations.set(key,value,time,timeUnit);
            return ResultEntity.successWithoutData();

        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failWithMessage(e.getMessage());
        }


    }

    @RequestMapping("/get/redis/string/value/by/key")
    ResultEntity<String> setRedisValueRemoteByKey(
            @RequestParam("key") String key) {


        try {
            ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();
            String value = operations.get(key);
            return ResultEntity.successWithData(value);

        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failWithMessage(e.getMessage());
        }
    }

    @RequestMapping("/remove/redis/by/key")
    ResultEntity<String> removeByKey(
            @RequestParam("key") String key) {
        try {
            stringRedisTemplate.delete(key);
            return ResultEntity.successWithoutData();

        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failWithMessage(e.getMessage());
        }

    }
}
