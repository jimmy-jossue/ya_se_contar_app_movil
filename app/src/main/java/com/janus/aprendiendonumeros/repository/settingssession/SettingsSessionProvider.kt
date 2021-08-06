package com.janus.aprendiendonumeros.repository.settingssession

import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.SettingsSession

interface SettingsSessionProvider {
    suspend fun getSettingsSession(): Resource<SettingsSession>
}