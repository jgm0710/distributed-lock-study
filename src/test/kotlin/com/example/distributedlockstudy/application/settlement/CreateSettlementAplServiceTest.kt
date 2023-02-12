package com.example.distributedlockstudy.application.settlement

import com.example.distributedlockstudy.application.settlement.command.CreateOrderSettlementCommand
import com.example.distributedlockstudy.infra.settlement.SettlementJpaRepository
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.util.*

@SpringBootTest
class CreateSettlementAplServiceTest {

    @Autowired
    lateinit var createSettlementAplService: CreateSettlementAplService

    @Autowired
    lateinit var settlementJpaRepository: SettlementJpaRepository

    @Test
    @DisplayName("sam")
    fun sam() {
        //given
        val command = CreateOrderSettlementCommand(
            UUID.randomUUID(),
            UUID.randomUUID(),
            BigDecimal(1000)
        )

        //when
        val settlement = createSettlementAplService.createOrderSettlement(command)

        //then

        val jacksonObjectMapper = jacksonObjectMapper()
        jacksonObjectMapper.registerModule(JavaTimeModule())

        println(
            "jacksonObjectMapper().writeValueAsString(settlement) = ${
                jacksonObjectMapper.writeValueAsString(
                    settlement
                )
            }"
        )

        val findAllByStoreId = settlementJpaRepository.findAllByStoreId(command.storeId)

        for (settlement1 in findAllByStoreId) {
            println("settlement1 = ${settlement1}")
        }
    }
}
