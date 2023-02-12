package com.example.distributedlockstudy.infra.settlement

import com.example.distributedlockstudy.domain.Settlement
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SettlementJpaRepository: JpaRepository<Settlement, UUID> {

    fun existsByOrderId(orderId: UUID) : Boolean

    fun findAllByStoreId(storeId: UUID): List<Settlement>
}
