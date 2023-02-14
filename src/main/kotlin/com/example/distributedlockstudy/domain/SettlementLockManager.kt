package com.example.distributedlockstudy.domain

import java.util.*

interface SettlementLockManager {

    fun acquireStoreLock(storeId: UUID)

    fun releaseStoreLock(storeId: UUID)
}
