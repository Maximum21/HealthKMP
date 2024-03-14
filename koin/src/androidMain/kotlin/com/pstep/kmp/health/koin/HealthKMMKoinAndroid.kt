package com.pstep.kmp.health.koin

import android.app.Application
import com.pstep.kmp.health.ApplicationContextHolder
import org.koin.core.KoinApplication

fun KoinApplication.attachHealthKMP(
    application: Application,
) {
    ApplicationContextHolder.applicationContext = application
    modules(commonModule())
}