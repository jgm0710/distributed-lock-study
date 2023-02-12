package com.example.distributedlockstudy.application.settlement

import com.example.distributedlockstudy.application.settlement.command.CreateOrderSettlementCommand
import com.example.distributedlockstudy.domain.Settlement
import com.example.distributedlockstudy.domain.SettlementLockManager
import com.example.distributedlockstudy.domain.SettlementRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalStateException
import java.math.BigDecimal

@Service
class CreateSettlementAplService(
    val settlementRepository: SettlementRepository,
    val settlementLockManager: SettlementLockManager,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun createOrderSettlement(command: CreateOrderSettlementCommand): Settlement {
        try {
            settlementLockManager.acquireStoreLock(command.storeId)

            if (settlementRepository.existsByOrderId(command.orderId)) {
                log.warn("Order settlement history already exists. OrderId : {}", command.orderId)
                throw IllegalStateException("Order settlement history already exists. OrderId : ${command.orderId}")
            }

            val totalBrokerageFeeOfMonth = settlementRepository.getStoreTotalBrokerageFeeOfMonth(command.storeId)

            val orderSettlement = Settlement.createOrderSettlement(
                command.orderId,
                command.storeId,
                command.settlementPrice,
                totalBrokerageFeeOfMonth
            )

            return settlementRepository.save(orderSettlement)
        } finally {
            println("호출 확인")
            settlementLockManager.releaseStoreLock(command.storeId)
        }
    }
}
