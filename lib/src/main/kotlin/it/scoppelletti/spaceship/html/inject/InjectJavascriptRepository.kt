/*
 * Copyright (C) 2020 Dario Scoppelletti, <http://www.scoppelletti.it/>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limit
 */

package it.scoppelletti.spaceship.html.inject

import android.webkit.JavascriptInterface
import it.scoppelletti.spaceship.html.JavascriptModule
import it.scoppelletti.spaceship.html.JavascriptRepository
import mu.KotlinLogging
import javax.inject.Inject
import javax.inject.Provider

/**
 * Module bound as Javascript interface into
 * [it.scoppelletti.spaceship.html.app.HtmlViewFragment] in order to connect
 * to the injected [it.scoppelletti.spaceship.html.JavascriptModule]
 * implementations.
 *
 * @since 1.0.0
 */
public class InjectJavascriptRepository @Inject constructor(
        private val creators: Map<String,
                @JvmSuppressWildcards Provider<JavascriptModule>>
): JavascriptRepository {

    /**
     * Returns a Javascript module.
     *
     * @param  tag Tag of the module.
     * @return     The instance.
     */
    @JavascriptInterface
    override fun getModule(tag: String): Any? {
        val creator = creators.entries
            .firstOrNull { tag.equals(it.key, true) } ?.value
        if (creator == null) {
            logger.warn { "No Javascript module found for tag $tag." }
            return null
        }

        return creator.get()
    }

    private companion object {
        val logger = KotlinLogging.logger {}
    }
}
