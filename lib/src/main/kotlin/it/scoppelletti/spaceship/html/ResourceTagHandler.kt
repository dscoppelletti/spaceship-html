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

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.text.Editable
import mu.KotlinLogging
import org.xml.sax.XMLReader
import javax.inject.Inject

/**
 * HTML custom tag for inserting the value of a resource.
 *
 * > This tag properly works only if you insert the
 * > `<it-scoppelletti-contentHandler/>` tag before.
 *
 * @see   it.scoppelletti.spaceship.html.ContentHandlerTagHandler
 * @since 1.0.0
 */
public class ResourceTagHandler @Inject constructor(
        private val context: Context,
        private val resources: Resources
) : HtmlTagHandler(TAG) {

    @SuppressLint("DiscouragedApi")
    override fun handleTag(
            output: Editable,
            start: Int, end: Int,
            xmlReader: XMLReader
    ) {
        val resId: Int
        val name: String?
        val resType: String?

        val attrs = xmlReader.getCurrentAttributes()
        name = attrs.entries.firstOrNull {
            ATTR_NAME.equals(it.key, true)
        } ?.value

        if (name.isNullOrBlank()) {
            logger.error {
                "Attribute $ATTR_NAME not set in tag $tag."
            }

            return
        }

        resType = attrs.entries.firstOrNull {
            ATTR_TYPE.equals(it.key, true)
        } ?.value

        if (resType.isNullOrBlank()) {
            logger.error {
                "Attribute $ATTR_TYPE not set in tag $tag."
            }

            return
        }

        if (!resType.equals(TYPE_STRING, true)) {
            logger.error { "Resource type $resType not supported by tag $tag." }
            return
        }

        val pkgName = context.packageName
        resId = resources.getIdentifier(name, resType, pkgName)
        if (resId == 0) {
            logger.error {
                "Resource $name of type $resType not found in package $pkgName."
            }

            return
        }

        val stringValue = resources.getString(resId)
        output.replace(start, end, stringValue)
    }

    public companion object {

        /**
         * Tag.
         */
        public const val TAG: String = "it-scoppelletti-resource"

        /**
         * Attribute containing the resource name.
         */
        public const val ATTR_NAME: String = "name"

        /**
         * Attribute containing the resource type.
         */
        public const val ATTR_TYPE: String = "type"

        /**
         * Resource type `string`.
         */
        public const val TYPE_STRING: String = "string"

        private val logger = KotlinLogging.logger {}
    }
}
