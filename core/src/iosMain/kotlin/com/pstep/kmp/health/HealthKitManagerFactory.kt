package com.pstep.kmp.health

actual class HealthManagerFactory {

    actual fun createManager(): HealthManager =
        HealthKitManager()
}