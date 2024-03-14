package com.pstep.kmp.health

expect class HealthManagerFactory() {

    fun createManager(): HealthManager
}