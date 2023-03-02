package it.scoppelletti.spaceship.html.sample

import android.app.Application
import it.scoppelletti.spaceship.html.inject.HtmlComponent
import it.scoppelletti.spaceship.html.inject.HtmlComponentProvider
import it.scoppelletti.spaceship.html.sample.inject.DaggerSampleComponent
import it.scoppelletti.spaceship.html.sample.inject.SampleComponent
import it.scoppelletti.spaceship.inject.AppComponent
import it.scoppelletti.spaceship.inject.StdlibComponent

class MainApp : Application(), HtmlComponentProvider {

    private lateinit var _sampleComponent: SampleComponent

    override fun onCreate() {
        super.onCreate()

        _sampleComponent = DaggerSampleComponent.factory()
                .create(this)
    }

    override fun appComponent(): AppComponent = _sampleComponent

    override fun stdlibComponent(): StdlibComponent = _sampleComponent

    override fun htmlComponent(): HtmlComponent = _sampleComponent
}
