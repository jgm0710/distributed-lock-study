package com.example.distributedlockstudy.infra.settlement

import com.example.distributedlockstudy.domain.SettlementLockManager
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.*
import java.util.concurrent.TimeUnit

@Repository
class SettlementLockManagerRedisImpl(
    private val redisTemplate: RedisTemplate<String, Any>,
) : SettlementLockManager {

    val lockKey = "settlement-store-lock-"

    override fun acquireStoreLock(storeId: UUID): Boolean {

        val ifAbsent = redisTemplate.opsForValue()
            .setIfAbsent("${getLockKey(storeId)}", UUID.randomUUID().toString(), 60, TimeUnit.SECONDS)


        return ifAbsent ?: false
    }

    private fun getLockKey(storeId: UUID): String {
        return "settlement-store-lock-${storeId}"
    }

    override fun releaseStoreLock(storeId: UUID): Boolean {
        return redisTemplate.opsForValue().get(getLockKey(storeId))?.let {
            redisTemplate.delete(getLockKey(storeId))
            true
        } ?: false
    }
}
