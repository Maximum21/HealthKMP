package com.pstep.kmp.health

import com.pstep.kmp.health.legacy.GoogleFitManager

actual class HealthManagerFactory {

    actual fun createManager(): HealthManager {
        val healthConnectManager = HealthConnectManager(ApplicationContextHolder.applicationContext)

        return if (healthConnectManager.isAvailable().getOrNull() == true) {
            healthConnectManager
        } else {
            GoogleFitManager(ApplicationContextHolder.applicationContext)
        }
    }

}