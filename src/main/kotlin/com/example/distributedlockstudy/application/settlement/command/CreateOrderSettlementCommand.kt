package com.example.distributedlockstudy.application.settlement.command

import java.math.BigDecimal
import java.util.*

data class CreateOrderSettlementCommand(val storeId: UUID, val orderId: UUID, val settlementPrice : BigDecimal) {
}
