package com.pstep.kmp.health

import com.pstep.kmp.health.HealthDataType.Steps
import com.pstep.kmp.health.HealthDataType.Weight
import com.pstep.kmp.health.HealthDataType.Calories
import platform.HealthKit.HKQuantityType
import platform.HealthKit.HKQuantityTypeIdentifierActiveEnergyBurned
import platform.HealthKit.HKQuantityTypeIdentifierBodyMass
import platform.HealthKit.HKQuantityTypeIdentifierStepCount
import platform.HealthKit.HKSampleType

internal fun HealthDataType.toHKSampleType(): HKSampleType? = when (this) {
    Steps ->
        HKQuantityType.quantityTypeForIdentifier(HKQuantityTypeIdentifierStepCount)

    Weight ->
        HKQuantityType.quantityTypeForIdentifier(HKQuantityTypeIdentifierBodyMass)

    Calories ->
        HKQuantityType.quantityTypeForIdentifier(HKQuantityTypeIdentifierActiveEnergyBurned)
}