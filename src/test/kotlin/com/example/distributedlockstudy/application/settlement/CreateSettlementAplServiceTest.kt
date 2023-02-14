package com.example.distributedlockstudy.application.settlement

import com.example.distributedlockstudy.application.settlement.command.CreateOrderSettlementCommand
import com.example.distributedlockstudy.domain.Settlement
import com.example.distributedlockstudy.domain.SettlementRepository
import com.example.distributedlockstudy.infra.settlement.SettlementJpaRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.CountDownLatch

@SpringBootTest
class CreateSettlementAplServiceTest {

    @Autowired
    lateinit var createSettlementAplService: CreateSettlementAplService

    @Autowired
    lateinit var settlementJpaRepository: SettlementJpaRepository

    @Autowired
    lateinit var settlementRepository : SettlementRepository

    @Test
    @DisplayName("sam")
    fun sam() {
        //given
        val storeId = UUID.randomUUID()

        val threadCount = 100
        val latch = CountDownLatch(threadCount)
        val results = mutableListOf<Settlement>()

        //when

        repeat(threadCount) { i ->
            Thread {
                val command = CreateOrderSettlementCommand(
                    storeId = storeId,
                    orderId = UUID.randomUUID(),
                    settlementPrice = BigDecimal(10000)
                )

                val settlement = createSettlementAplService.createOrderSettlementV1(command)
                synchronized(settlement) {
                    results.add(settlement)
                }
                latch.countDown()
            }.start()
        }

        latch.await()

        assertEquals(threadCount, results.size)
        assertTrue(results.distinct().size > 1)

        //then

        val storeTotalBrokerageFeeOfMonth = settlementRepository.getStoreTotalBrokerageFeeOfMonth(storeId)

        val compareTo = BigDecimal(60000).compareTo(storeTotalBrokerageFeeOfMonth)

        println("storeTotalBrokerageFeeOfMonth = ${storeTotalBrokerageFeeOfMonth}")


        assertTrue(compareTo==0)
    }
}
