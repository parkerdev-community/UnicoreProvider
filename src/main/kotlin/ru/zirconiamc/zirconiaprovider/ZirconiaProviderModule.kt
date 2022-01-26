package ru.zirconiamc.zirconiaprovider

import pro.gravit.launcher.modules.LauncherInitContext
import pro.gravit.launcher.modules.LauncherModule
import pro.gravit.launcher.modules.LauncherModuleInfo
import pro.gravit.launcher.modules.events.PreConfigPhase
import pro.gravit.launchserver.auth.core.AuthCoreProvider
import pro.gravit.utils.Version

class ZirconiaProviderModule : LauncherModule(LauncherModuleInfo("ZirconiaProvider", version, arrayOf("LaunchServerCore"))) {
    companion object {
        val version = Version(1, 0, 0, 0, Version.Type.BETA)
        private var registred = false
    }

    fun preInit(preConfigPhase: PreConfigPhase?) {
        if (!registred) {
            AuthCoreProvider.providers.register("zirconia", ZirconiaProvider::class.java)
            registred = true
        }
    }

    override fun init(initContext: LauncherInitContext?) {
        registerEvent(
            { preConfigPhase: PreConfigPhase? -> this.preInit(preConfigPhase) },
            PreConfigPhase::class.java
        )
    }
}

