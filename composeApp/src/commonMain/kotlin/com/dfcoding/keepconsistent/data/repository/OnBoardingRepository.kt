package com.dfcoding.keepconsistent.data.repository

interface OnBoardingRepository {
    fun hasSeenOnboard(): Boolean
    fun markOnboardingSeen()
}