/*
 * Copyright (C) 2018 Dario Scoppelletti, <http://www.scoppelletti.it/>.
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

import android.text.Editable
import mu.KotlinLogging
import org.xml.sax.XMLReader
import javax.inject.Inject

/**
 * HTML custom tag to integrate the `ContentHandler` object with some extended
 * features used by the custom tags inserted after this.
 *
 * @since 1.0.0
 */
public class ContentHandlerTagHandler @Inject constructor() :
        HtmlTagHandler(TAG) {

    override fun handleTag(
            output: Editable,
            start: Int,
            end: Int,
            xmlReader: XMLReader) {
        logger.debug("Install the custom ContentHandler interface.")
        val delegate = xmlReader.contentHandler
        xmlReader.contentHandler = HtmlContentHandler(delegate)
    }

    public companion object {

        /**
         * Tag.
         */
        public const val TAG: String = "it-scoppelletti-contentHandler"

        private val logger = KotlinLogging.logger {}
    }
}

