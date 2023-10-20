package pl.energosystem.energoservice

import android.app.Application
import pl.energosystem.energoservice.model.AppContainer
import pl.energosystem.energoservice.model.AppDataContainer

class EnergoServiceApplication: Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer()
    }
}
