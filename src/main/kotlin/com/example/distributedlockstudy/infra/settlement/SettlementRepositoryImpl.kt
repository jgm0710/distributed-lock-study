package com.example.distributedlockstudy.infra.settlement

import com.example.distributedlockstudy.domain.Settlement
import com.example.distributedlockstudy.domain.SettlementRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.util.*

@Repository
class SettlementRepositoryImpl(
    private val settlementJpaRepository: SettlementJpaRepository,
) : SettlementRepository {

    override fun save(settlement: Settlement): Settlement {
        return settlementJpaRepository.save(settlement)
    }

    override fun existsByOrderId(orderId: UUID): Boolean {
        return settlementJpaRepository.existsByOrderId(orderId)
    }

    override fun getStoreTotalBrokerageFeeOfMonth(storeId: UUID): BigDecimal {
        return settlementJpaRepository
            .findAllByStoreId(storeId = storeId)
            .sumOf { it.settlementFee }
    }
}
