package com.example.distributedlockstudy.domain

import java.math.BigDecimal
import java.util.*

interface SettlementRepository {

    fun save(settlement: Settlement): Settlement

    fun existsByOrderId(orderId: UUID) : Boolean

    fun getStoreTotalBrokerageFeeOfMonth(storeId : UUID) : BigDecimal
}
