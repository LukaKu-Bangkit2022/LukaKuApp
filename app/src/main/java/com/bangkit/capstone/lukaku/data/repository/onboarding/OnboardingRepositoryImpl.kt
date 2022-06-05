package com.bangkit.capstone.lukaku.data.repository.onboarding

import com.bangkit.capstone.lukaku.data.local.datastore.OnboardingDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val onboardingDataStore: OnboardingDataStore
) : OnboardingRepository {
    override suspend fun saveOnboarding(isFinished: Boolean) =
        onboardingDataStore.saveOnboarding(isFinished)

    override fun getOnboarding(): Flow<Boolean?> = onboardingDataStore.getOnboarding()
}