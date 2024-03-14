package com.pstep.kmp.health

sealed interface HealthDataType {
    data object Steps : HealthDataType

    data object Weight : HealthDataType

    data object Calories : HealthDataType
}