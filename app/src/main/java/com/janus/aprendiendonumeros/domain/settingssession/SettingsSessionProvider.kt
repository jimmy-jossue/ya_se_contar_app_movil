package com.janus.aprendiendonumeros.domain.settingssession

import com.janus.aprendiendonumeros.data.model.SettingsSession

interface SettingsSessionProvider {
    suspend fun getSettingsSession(): SettingsSession
}