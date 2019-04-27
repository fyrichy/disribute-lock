package com.richy.distribute.lock.redis.test;


import com.richy.distribute.lock.redis.service.RedisDistributeLockService;


/**
 * @descr：采用多线程实现分布式锁的操作
 * @author： Richy
 * @time：下午10:06:56
 */
public class RedisDistributeLockTest {

	public static void main(String[] args) {
		for(int i=1;i<=10;i++) {
			new Thread(new RedisDistributeLockService()).start();
		}
	}
}
