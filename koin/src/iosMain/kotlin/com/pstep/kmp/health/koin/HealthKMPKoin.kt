@file:Suppress("unused")

package com.pstep.kmp.health.koin

import org.koin.core.context.startKoin

object HealthKMPKoin {

    fun start() {
        startKoin {
            modules(commonModule())
        }
    }
}