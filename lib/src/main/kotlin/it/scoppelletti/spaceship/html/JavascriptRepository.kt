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

package it.scoppelletti.spaceship.html

import android.webkit.JavascriptInterface

/**
 * Repository of `JavascriptModule` instances.
 *
 * @since 1.0.0
 */
public interface JavascriptRepository {

    /**
     * Returns a Javascript module.
     *
     * @param  tag Tag of the module.
     * @return     The instance.
     */
    @JavascriptInterface
    public fun getModule(tag: String): Any?

    public companion object {

        /**
         * Name of the bound module.
         */
        public const val MODULE: String = "spaceshipRepository"
    }
}
