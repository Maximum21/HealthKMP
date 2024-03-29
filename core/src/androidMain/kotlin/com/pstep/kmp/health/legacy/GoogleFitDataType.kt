package com.pstep.kmp.health.legacy

import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType
import com.pstep.kmp.health.HealthDataType
import com.pstep.kmp.health.HealthDataType.Steps
import com.pstep.kmp.health.HealthDataType.Weight
import com.pstep.kmp.health.HealthDataType.Calories

internal fun fitnessOptions(
    readTypes: List<HealthDataType>,
    writeTypes: List<HealthDataType>,
): FitnessOptions {
    val options = FitnessOptions.builder()

    readTypes.map {
        it.toDataType(isRead = true)
    }.forEach { (type, access) ->
        options.addDataType(type, access)
    }
    writeTypes.map {
        it.toDataType(isWrite = true)
    }.forEach { (type, access) ->
        options.addDataType(type, access)
    }

    return options.build()
}

internal fun HealthDataType.toDataType(): DataType = when (this) {
    Steps -> DataType.TYPE_STEP_COUNT_DELTA

    Weight -> DataType.TYPE_WEIGHT

    Calories -> DataType.TYPE_CALORIES_EXPENDED
}

/**
 * second value => access type
 *
 * @see [FitnessOptions.ACCESS_READ], [FitnessOptions.ACCESS_WRITE]
 */
private fun HealthDataType.toDataType(
    isRead: Boolean = false,
    isWrite: Boolean = false,
): Pair<DataType, Int> {
    require(isRead != isWrite)

    return Pair(
        first = toDataType(),
        second = if (isRead) FitnessOptions.ACCESS_READ else FitnessOptions.ACCESS_WRITE,
    )
}