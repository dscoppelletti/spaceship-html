package it.scoppelletti.spaceship.html.sample.inject

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import it.scoppelletti.spaceship.html.inject.HtmlModule
import it.scoppelletti.spaceship.inject.AppComponent
import it.scoppelletti.spaceship.inject.StdlibComponent
import javax.inject.Singleton

@Singleton
@Component(modules = [ HtmlModule::class ])
interface SampleComponent : StdlibComponent, AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
                @BindsInstance
                application: Application
        ): SampleComponent
    }
}

