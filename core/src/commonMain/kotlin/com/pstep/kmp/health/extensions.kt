@file:Suppress("unused")

package com.pstep.kmp.health

import com.pstep.kmp.health.HealthDataType.Steps
import com.pstep.kmp.health.HealthDataType.Weight
import com.pstep.kmp.health.HealthDataType.Calories
import com.pstep.kmp.health.records.CaloriesRecord
import com.pstep.kmp.health.records.StepsRecord
import com.pstep.kmp.health.records.WeightRecord
import kotlinx.datetime.Instant

suspend fun HealthManager.readSteps(
    startTime: Instant,
    endTime: Instant,
): Result<List<StepsRecord>> =
    readData(
        startTime = startTime,
        endTime = endTime,
        type = Steps,
    ).map { it.filterIsInstance<StepsRecord>() }

suspend fun HealthManager.readWeight(
    startTime: Instant,
    endTime: Instant,
): Result<List<WeightRecord>> =
    readData(
        startTime = startTime,
        endTime = endTime,
        type = Weight,
    ).map { it.filterIsInstance<WeightRecord>() }
suspend fun HealthManager.readCalories(
    startTime: Instant,
    endTime: Instant,
): Result<List<CaloriesRecord>> =
    readData(
        startTime = startTime,
        endTime = endTime,
        type = Calories,
    ).map { it.filterIsInstance<CaloriesRecord>() }
