package com.pstep.kmp.health.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pstep.kmp.health.HealthDataType
import com.pstep.kmp.health.HealthManagerFactory
import com.pstep.kmp.health.HealthRecord
import com.pstep.kmp.health.records.StepsRecord
import com.pstep.kmp.health.records.WeightRecord
import com.pstep.kmp.health.units.Mass
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

@Composable
fun SampleApp() {
    val coroutineScope = rememberCoroutineScope()
    val health = remember { HealthManagerFactory().createManager() }

    val readTypes = remember {
        listOf(
            HealthDataType.Steps,
            HealthDataType.Weight
        )
    }
    val writeTypes = remember {
        listOf(
            HealthDataType.Steps,
            HealthDataType.Weight
        )
    }

    var isAvailableResult by remember { mutableStateOf(Result.success(false)) }
    var isAuthorizedResult by remember { mutableStateOf<Result<Boolean>?>(null) }
    var isRevokeSupported by remember { mutableStateOf(false) }

    val data = remember { mutableStateMapOf<HealthDataType, Result<List<HealthRecord>>>() }

    LaunchedEffect(health) {
        isAvailableResult = health.isAvailable()

        if (isAvailableResult.getOrNull() == false) return@LaunchedEffect
        isAuthorizedResult = health.isAuthorized(
            readTypes = readTypes,
            writeTypes = writeTypes,
        )
        isRevokeSupported = health.isRevokeAuthorizationSupported().getOrNull() ?: false
    }

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Hello, this is HealthKMP for ${getPlatformName()}")

            isAvailableResult
                .onSuccess { isAvailable ->
                    Text("HealthManager isAvailable=$isAvailable")
                }
                .onFailure {
                    Text("HealthManager isAvailable=$it")
                }

            isAuthorizedResult
                ?.onSuccess {
                    Text("HealthManager isAuthorized=$it")
                }
                ?.onFailure {
                    Text("HealthManager isAuthorized=$it")
                }
            if (isAvailableResult.getOrNull() == true && isAuthorizedResult?.getOrNull() != true)
                Button(
                    onClick = {
                        coroutineScope.launch {
                            isAuthorizedResult = health.requestAuthorization(
                                readTypes = readTypes,
                                writeTypes = writeTypes,
                            )
                            println("check authorization $isAuthorizedResult")
                        }
                    },
                ) {
                    Text("Request authorization")
                }

            if (isAvailableResult.getOrNull() == true && isRevokeSupported && isAuthorizedResult?.getOrNull() == true)
                Button(
                    onClick = {
                        coroutineScope.launch {
                            health.revokeAuthorization()
                            isAuthorizedResult = health.isAuthorized(
                                readTypes = readTypes,
                                writeTypes = writeTypes,
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Red,
                        contentColor = Color.White,
                    ),
                ) {
                    Text("Revoke authorization")
                }

            if (isAvailableResult.getOrNull() == true && isAuthorizedResult?.getOrNull() == true) {
                Column {
                    readTypes.forEach { type ->
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    data[type] = health.readData(
                                        startTime = Clock.System.now()
                                            .minus(1.days),
                                        endTime = Clock.System.now(),
                                        type = type,
                                    )
                                }
                            },
                        ) {
                            Text("Read $type")
                        }

                        data[type]
                            ?.onSuccess { records ->
                                Column {
                                    Text("count ${records.size}")

                                    records.forEach { record ->
                                        Text("Record $record")
                                    }
                                }
                            }
                            ?.onFailure {
                                Text("Failed to read records $it")
                            }

                        Divider()
                    }
                }
            }
        }
    }
}

expect fun getPlatformName(): String