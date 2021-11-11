package com.XYF.crowd.api;

import com.XYF.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.TimeUnit;

/**
 * @username 熊一飞
 */

@FeignClient("xyf-redis-provider")
public interface RedisRemoteService {


    @RequestMapping("/set/redis/key/value/remote")
    ResultEntity<String> setRedisValueRemote(@RequestParam("key") String key, @RequestParam("value") String value);


    @RequestMapping("/set/redis/key/value/remote/timeout")
    ResultEntity<String> setRedisValueRemoteTimeOut(
            @RequestParam("key") String key,
            @RequestParam("value") String value,
            @RequestParam("time") long time,
            @RequestParam("timeUnit")TimeUnit timeUnit);

    @RequestMapping("/get/redis/string/value/by/key")
    ResultEntity<String> getRedisValueRemoteByKey(
            @RequestParam("key") String key);

    @RequestMapping("/remove/redis/by/key")
    ResultEntity<String> removeByKey(
            @RequestParam("key") String key);


}
