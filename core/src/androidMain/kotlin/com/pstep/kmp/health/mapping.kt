package com.pstep.kmp.health

import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.units.Energy
import com.pstep.kmp.health.records.CaloriesRecord
import com.pstep.kmp.health.records.StepsRecord
import com.pstep.kmp.health.records.WeightRecord
import com.pstep.kmp.health.units.Mass
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toKotlinInstant
import androidx.health.connect.client.records.Record as HCRecord
import androidx.health.connect.client.records.StepsRecord as HCStepsRecord
import androidx.health.connect.client.records.WeightRecord as HCWeightRecord
import androidx.health.connect.client.units.Mass as HCMass

internal fun HealthRecord.toHCRecord(): HCRecord? = when (val record = this) {
    is StepsRecord -> HCStepsRecord(
        startTime = record.startTime.toJavaInstant(),
        endTime = record.endTime.toJavaInstant(),
        startZoneOffset = null,
        endZoneOffset = null,
        count = record.count.toLong(),
    )

    is CaloriesRecord -> ActiveCaloriesBurnedRecord(
        startTime = record.startTime.toJavaInstant(),
        endTime = record.endTime.toJavaInstant(),
        startZoneOffset = null,
        endZoneOffset = null,
        energy = Energy.calories(record.calories.toDouble()),
    )

    is WeightRecord -> HCWeightRecord(
        time = record.time.toJavaInstant(),
        zoneOffset = null,
        weight = record.weight.toHCMass(),
    )

    else -> null
}

internal fun HCRecord.toHealthRecord(): HealthRecord? = when (val record = this) {
    is HCStepsRecord -> StepsRecord(
        startTime = record.startTime.toKotlinInstant(),
        endTime = record.endTime.toKotlinInstant(),
        count = record.count.toInt(),
    )

    is ActiveCaloriesBurnedRecord -> CaloriesRecord(
        startTime = record.startTime.toKotlinInstant(),
        endTime = record.endTime.toKotlinInstant(),
        calories = record.energy.inCalories.toInt(),
    )

    is HCWeightRecord -> WeightRecord(
        time = record.time.toKotlinInstant(),
        weight = record.weight.toMass(),
    )

    else -> null
}

private fun Mass.toHCMass(): HCMass =
    HCMass.kilograms(inKilograms)

private fun HCMass.toMass(): Mass =
    Mass.kilograms(inKilograms)
