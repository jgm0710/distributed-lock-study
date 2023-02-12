package com.example.distributedlockstudy.domain

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "settlement")
open class Settlement(
    @Id
    @Column(name = "id")
    open val id: UUID,
    open val orderId: UUID,
    open val storeId: UUID,
    open val settlementPrice: BigDecimal,
    open val settlementFee: BigDecimal,
    open val createdAt: OffsetDateTime,
) {
    constructor() : this(
        UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), BigDecimal.ZERO, BigDecimal.ZERO,
        OffsetDateTime.now()
    )

    companion object {
        fun createOrderSettlement(
            orderId: UUID,
            storeId: UUID,
            settlementPrice: BigDecimal,
            totalBrokerageFeeOfMonth: BigDecimal,
        ): Settlement {
            return Settlement(
                id = UUID.randomUUID(),
                orderId = orderId,
                storeId = storeId,
                settlementPrice = settlementPrice,
                settlementFee = calculateSettlementFee(totalBrokerageFeeOfMonth, settlementPrice),
                createdAt = OffsetDateTime.now()
            )
        }

        private fun calculateSettlementFee(
            totalBrokerageFeeOfMonth: BigDecimal,
            settlementPrice: BigDecimal,
        ): BigDecimal {
            return if (totalBrokerageFeeOfMonth >= BigDecimal(60000)) {
                BigDecimal.ZERO
            } else {
                settlementPrice.divide(BigDecimal(10), 2, RoundingMode.HALF_UP)
            }
        }
    }
}
