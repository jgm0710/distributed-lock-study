package com.example.distributedlockstudy.domain

import java.util.*

interface SettlementLockManager {

    fun acquireStoreLock(storeId: UUID) : Boolean

    fun releaseStoreLock(storeId: UUID) : Boolean
}
