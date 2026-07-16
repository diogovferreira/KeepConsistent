package com.dfcoding.keepconsistent.data.repository

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

class OnBoardingRepositoryImpl(private val settings: Settings

) : OnBoardingRepository {
    override fun hasSeenOnboard(): Boolean = settings.getBoolean("has_seen_onboarding", false)

    override fun markOnboardingSeen()  {
        settings["has_seen_onboarding"] = true
    }

}


