package com.janus.aprendiendonumeros.repository.settingssession

import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.datastore.SettingsSessionDataStore
import com.janus.aprendiendonumeros.data.model.SettingsSession
import kotlinx.coroutines.flow.first

class SettingsSessionImpl(private val dataSource: SettingsSessionDataStore) :
    SettingsSessionProvider {
    override suspend fun getSettingsSession(): Resource<SettingsSession> {
        return dataSource.getSettingsSession().first()
    }
}