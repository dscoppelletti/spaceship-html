@file:Suppress("unused")

package it.scoppelletti.spaceship.html.sample.inject

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import it.scoppelletti.spaceship.html.inject.HtmlModule
import it.scoppelletti.spaceship.html.sample.lifecycle.HtmlTextViewModel
import it.scoppelletti.spaceship.html.sample.lifecycle.HtmlTextViewModelFactory
import it.scoppelletti.spaceship.inject.AppModule
import it.scoppelletti.spaceship.inject.ViewModelKey
import it.scoppelletti.spaceship.lifecycle.ViewModelProviderEx

@Module(includes = [ AppModule::class, HtmlModule::class ])
abstract class SampleModule {

    @Binds
    @IntoMap
    @ViewModelKey(HtmlTextViewModel::class)
    abstract fun bindHtmlTextViewModelFactory(
            obj: HtmlTextViewModelFactory
    ): ViewModelProviderEx.Factory
}
