package com.pstep.kmp.health.records

import com.pstep.kmp.health.HealthDataType
import com.pstep.kmp.health.IntervalRecord
import kotlinx.datetime.Instant

/**
 * Captures the number of calories burned since the last reading. Each calorie is only reported once so
 * records shouldn't have overlapping time. The start time of each record should represent the start
 * of the interval in which calories were burned.
 *
 * The start time must be equal to or greater than the end time of the previous record. Adding all
 * of the values together for a period of time calculates the total number of calories during that
 * period.
 *
 * @param calories Valid range: 1-1_000_000
 */
data class CaloriesRecord(
    override val startTime: Instant,
    override val endTime: Instant,
    val calories: Int,
) : IntervalRecord {

    override val dataType: HealthDataType = HealthDataType.Calories
}