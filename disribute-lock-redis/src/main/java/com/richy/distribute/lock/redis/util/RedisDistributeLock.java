package com.richy.distribute.lock.redis.util;

import java.util.Collections;
import java.util.UUID;

import redis.clients.jedis.Jedis;

/**
 * @descr：通过redis实现分布式锁
 * @author： Richy
 * @time：下午9:47:30
 */
public class RedisDistributeLock {

	//操作成功返回的结果
	private static final String LOCK_SUCCESS = "OK";
	//这个参数的作用：当key不存在时，我们进行set操作，当key存在时，不做任何操作
    private static final String SET_IF_NOT_EXIST = "NX";
    //这是px表示给key设置一个过期时间，时间的长度由后面的参数决定
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    
    private static final Long RELEASE_SUCCESS = 1L;
	
	/**
	 * @descr：获取分布式锁
	 * @param jedis:一个redis连接
	 * @param lockKey：锁传入的值
	 * @param requestId：请求标识
	 * @param acquireTime：获取锁之前的超时时间
	 * @param expireTime：获取锁之后的超时时间
	 * 
	 * 这里的key用来当做锁，而requestId用于解锁时和当前操作的setKey对应的value，
	 * 这样做的是为了避免A线程删除了B线程的锁
	 * @return
	 * @time：下午9:53:23
	 */
	public static boolean getRedisDistributeLock(Jedis jedis,String lockKey,int acquireTime,int expireTime) {
		String requestId = UUID.randomUUID().toString().replaceAll("-", "");
		//定义在没有获取锁之前,锁的超时时间
		Long endTime = System.currentTimeMillis() + acquireTime;
		while (System.currentTimeMillis() < endTime) {
			String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME , expireTime);
			if(LOCK_SUCCESS.equals(result)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @descr：解锁
	 * 
	 * @param jedis
	 * @param lockKey
	 * @param requestId
	 * @return
	 * @time：下午10:06:08
	 */
	public static boolean releaseDistributeLock(Jedis jedis,String lockKey,String requestId) {
		String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
		Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
		if(RELEASE_SUCCESS.equals(result)) {
			return true;
		}
		return false;
	}
}
