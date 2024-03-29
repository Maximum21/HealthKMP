package com.pstep.kmp.health

import com.pstep.kmp.health.records.CaloriesRecord
import com.pstep.kmp.health.records.StepsRecord
import com.pstep.kmp.health.records.WeightRecord
import com.pstep.kmp.health.units.Mass
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toNSDate
import platform.Foundation.NSDate
import platform.HealthKit.HKQuantity
import platform.HealthKit.HKQuantitySample
import platform.HealthKit.HKQuantityType
import platform.HealthKit.HKQuantityTypeIdentifier
import platform.HealthKit.HKQuantityTypeIdentifierActiveEnergyBurned
import platform.HealthKit.HKQuantityTypeIdentifierBodyMass
import platform.HealthKit.HKQuantityTypeIdentifierStepCount
import platform.HealthKit.HKUnit
import platform.HealthKit.countUnit
import platform.HealthKit.poundUnit
import kotlin.math.roundToInt

internal fun HealthRecord.toHKQuantitySample(): HKQuantitySample? {
    val record = this

    val quantityTypeIdentifier: HKQuantityTypeIdentifier
    val quantity: HKQuantity
    val startDate: NSDate
    val endDate: NSDate

    when (record) {
        is StepsRecord -> {
            quantityTypeIdentifier = HKQuantityTypeIdentifierStepCount
            quantity = HKQuantity.quantityWithUnit(
                unit = HKUnit.countUnit(),
                doubleValue = record.count.toDouble(),
            )
            startDate = record.startTime.toNSDate()
            endDate = record.endTime.toNSDate()
        }

        is WeightRecord -> {
            quantityTypeIdentifier = HKQuantityTypeIdentifierBodyMass
            quantity = HKQuantity.quantityWithUnit(
                unit = HKUnit.poundUnit(),
                doubleValue = record.weight.inPounds,
            )
            startDate = record.time.toNSDate()
            endDate = record.time.toNSDate()
        }

        is CaloriesRecord -> {
            quantityTypeIdentifier = HKQuantityTypeIdentifierActiveEnergyBurned
            quantity = HKQuantity.quantityWithUnit(
                unit = HKUnit.poundUnit(),
                doubleValue = record.calories.toDouble(),
            )
            startDate = record.startTime.toNSDate()
            endDate = record.endTime.toNSDate()
        }

        else -> return null
    }

    return HKQuantitySample.Companion.quantitySampleWithType(
        quantityType = HKQuantityType.quantityTypeForIdentifier(quantityTypeIdentifier)
            ?: return null,
        quantity = quantity,
        startDate = startDate,
        endDate = endDate,
    )
}

internal fun HKQuantitySample.toHealthRecord(): HealthRecord? {
    val sample = this

    return when (sample.quantityType.identifier) {
        HKQuantityTypeIdentifierStepCount -> {
            StepsRecord(
                startTime = sample.startDate.toKotlinInstant(),
                endTime = sample.endDate.toKotlinInstant(),
                count = sample.quantity.doubleValueForUnit(HKUnit.countUnit())
                    .roundToInt(),
            )
        }

        HKQuantityTypeIdentifierBodyMass -> {
            WeightRecord(
                time = sample.startDate.toKotlinInstant(),
                weight = Mass.pounds(
                    sample.quantity.doubleValueForUnit(HKUnit.poundUnit()),
                ),
            )
        }

        HKQuantityTypeIdentifierActiveEnergyBurned -> {
            CaloriesRecord(
                startTime = sample.startDate.toKotlinInstant(),
                endTime = sample.endDate.toKotlinInstant(),
                calories = sample.quantity.doubleValueForUnit(HKUnit.countUnit())
                    .roundToInt(),
            )
        }

        else -> null
    }
}