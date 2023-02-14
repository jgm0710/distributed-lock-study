package com.example.distributedlockstudy.application.settlement

import com.example.distributedlockstudy.application.settlement.command.CreateOrderSettlementCommand
import com.example.distributedlockstudy.domain.Settlement
import com.example.distributedlockstudy.domain.SettlementLockManager
import com.example.distributedlockstudy.domain.SettlementRepository
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.repository.Lock
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalStateException
import java.util.concurrent.TimeUnit

@Service
class CreateSettlementAplService(
    val settlementRepository: SettlementRepository,
    val settlementLockManager: SettlementLockManager,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun createOrderSettlementV1(command: CreateOrderSettlementCommand): Settlement {
        settlementLockManager.acquireStoreLock(command.storeId)

        try {
            if (settlementRepository.existsByOrderId(command.orderId)) {
                log.warn("Order settlement history already exists. OrderId : {}", command.orderId)
                throw IllegalStateException("Order settlement history already exists. OrderId : ${command.orderId}")
            }

            val totalBrokerageFeeOfMonth = settlementRepository.getStoreTotalBrokerageFeeOfMonth(command.storeId)

            log.info("totalBrokerageFeeOfMonth = ${totalBrokerageFeeOfMonth}")

            val orderSettlement = Settlement.createOrderSettlement(
                command.orderId,
                command.storeId,
                command.settlementPrice,
                totalBrokerageFeeOfMonth
            )

            return settlementRepository.save(orderSettlement)
        } finally {
            settlementLockManager.releaseStoreLock(command.storeId)
        }
    }
}
