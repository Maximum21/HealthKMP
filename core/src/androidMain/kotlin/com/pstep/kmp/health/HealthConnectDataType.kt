package com.pstep.kmp.health

import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.Record
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.WeightRecord
import com.pstep.kmp.health.HealthDataType.Steps
import com.pstep.kmp.health.HealthDataType.Weight
import com.pstep.kmp.health.HealthDataType.Calories
import com.pstep.kmp.health.records.CaloriesRecord
import kotlin.reflect.KClass

internal fun HealthDataType.toRecordType(): KClass<out Record> = when (this) {
    Steps -> StepsRecord::class

    Weight -> WeightRecord::class

    Calories -> ActiveCaloriesBurnedRecord::class
}

/**
 * Returns a permission defined in [HealthPermission] to read provided [HealthDataType].
 */
internal fun HealthDataType.toHealthPermission(
    isRead: Boolean = false,
    isWrite: Boolean = false,
): String {
    require(isRead != isWrite)

    return if (isRead) {
        HealthPermission.getReadPermission(recordType = toRecordType())
    } else {
        HealthPermission.getWritePermission(recordType = toRecordType())
    }
}