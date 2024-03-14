package com.pstep.kmp.health.records

import com.pstep.kmp.health.HealthDataType
import com.pstep.kmp.health.HealthDataType.Weight
import com.pstep.kmp.health.InstantaneousRecord
import com.pstep.kmp.health.units.Mass
import kotlinx.datetime.Instant

/**
 * Captures the user's weight.
 *
 * See [Mass] for supported units.
 */
data class WeightRecord(
    override val time: Instant,
    val weight: Mass,
) : InstantaneousRecord {

    override val dataType: HealthDataType = Weight
}