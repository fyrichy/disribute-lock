package com.richy.distribute.lock.redis.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @descr：redis工具类
 * @author： Richy
 * @time：下午9:36:04
 */
public class RedisUtil {

	private static JedisPool jedisPool = null;
	
	/**
	 * 静态代码块，加载redis连接信息
	 */
	static {
		JedisPoolConfig config = new JedisPoolConfig();
		//设置最大连接数
		config.setMaxTotal(200);
		//设置最大空闲数
		config.setMaxIdle(8);
		//设置最大等待时间
		config.setMaxWaitMillis(1000*100);
		// 在borrow一个jedis实例时，是否需要验证，若为true，则所有jedis实例均是可用的
		config.setTestOnBorrow(true);
		jedisPool = new JedisPool(config,"192.168.65.6",6379,3000,"root");
	}
	
	/**
	 * @descr：获取redis连接池
	 * @return
	 * @time：下午9:44:21
	 */
	public static JedisPool getJedisPool() {
		return jedisPool;
	}
	
	/**
	 * @descr：获取一个redis连接
	 * @return
	 * @time：下午9:46:39
	 */
	public static Jedis getJedisConn() {
		if(null != jedisPool) {
			return jedisPool.getResource();
		}
		return null;
	}
}
