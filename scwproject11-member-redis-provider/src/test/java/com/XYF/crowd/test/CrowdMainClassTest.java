package com.XYF.crowd.test;

import io.lettuce.core.TimeoutOptions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;


@SpringBootTest
@RunWith(SpringRunner.class)
@SuppressWarnings("all")
public class CrowdMainClassTest {

	// private Logger logger = LoggerFactory.getLogger(RedisTest.class);
	@Autowired
	private StringRedisTemplate redisTemplate;

	@Test
	public void testSet() {

		ValueOperations<String,String> operations = redisTemplate.opsForValue();
       operations.set("apple","red");

	}

	@Test
	public void testExset(){
		ValueOperations<String,String> operations = redisTemplate.opsForValue();
		operations.set("banana","yellow",5000, TimeUnit.SECONDS);

	}
}
