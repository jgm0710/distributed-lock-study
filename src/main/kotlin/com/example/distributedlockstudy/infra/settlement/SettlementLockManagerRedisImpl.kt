package com.example.distributedlockstudy.infra.settlement

import com.example.distributedlockstudy.domain.SettlementLockManager
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.TimeUnit

@Repository
class SettlementLockManagerRedisImpl(
    private val redisTemplate: RedisTemplate<String, Any>,
    val redissonClient: RedissonClient,
) : SettlementLockManager {

    val lockKey = "settlement-store-lock-"

    override fun acquireStoreLock(storeId: UUID) {
        getLock(storeId).lock(30, TimeUnit.SECONDS)

//        val ifAbsent = redisTemplate.opsForValue()
//            .setIfAbsent("${getLock(storeId)}", UUID.randomUUID().toString(), 60, TimeUnit.SECONDS)
//
//        return ifAbsent ?: false
    }

//    private fun getLockKey(storeId: UUID): String {
//        return "settlement-store-lock-${storeId}"
//    }

    private fun getLock(storeId: UUID): RLock {
//        return redissonClient.getFairLock("${lockKey}${storeId}")
        return redissonClient.getLock("${lockKey}${storeId}")
    }

    override fun releaseStoreLock(storeId: UUID) {
        getLock(storeId).unlock()
    }
}
