package com.richy.distribute.lock.redis.service;

import com.richy.distribute.lock.redis.util.RedisDistributeLock;
import com.richy.distribute.lock.redis.util.RedisUtil;

import redis.clients.jedis.Jedis;

/**
 * @descr：一个redis分布式锁的测试类
 * @author： Richy
 * @time：下午10:09:42
 */
public class RedisDistributeLockService implements Runnable{

	private Jedis jedis = RedisUtil.getJedisConn();
	private final static String lockKey = "REDIS_DISTRIBUTE_LOCK";
	
	@Override
	public void run() {
		//获取锁
		boolean lock = RedisDistributeLock.getRedisDistributeLock(jedis, lockKey,5000,5000);
		if(lock) {
			System.out.println("线程："+Thread.currentThread().getName()+"获取到锁:"+jedis.get(lockKey));
			//执行业务
			try {
				Thread.sleep(300);
				System.out.println(Thread.currentThread().getName()+"开始执行业务.....");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//释放锁
			boolean unLock = RedisDistributeLock.releaseDistributeLock(jedis, lockKey,jedis.get(lockKey));
			if(unLock) {
				System.out.println("线程："+Thread.currentThread().getName()+"释放锁成功");
			}else {
				System.out.println("线程："+Thread.currentThread().getName()+"释放锁失败");
			}
		}else {
			System.out.println("线程："+Thread.currentThread().getName()+"获取锁失败");
		}
	}

}
