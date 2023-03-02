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
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.webkit.JavascriptInterface
import it.scoppelletti.spaceship.app.AppExt
import mu.KotlinLogging
import javax.inject.Inject

/**
 * HTML Javascript module.
 *
 * @since 1.0.0
 */
public class HtmlJavascriptModule @Inject constructor(
        private val context: Context,
        private val packageManager: PackageManager,
        private val resources: Resources
) : JavascriptModule {

    /**
     * Returns name of this application.
     */
    @JavascriptInterface
    public fun getApplicationLabel(): String {
        val pkgName = context.packageName

        val applInfo = try {
            AppExt.getApplicationInfo(packageManager, pkgName, 0)
        } catch (ex: PackageManager.NameNotFoundException) {
            logger.error(ex) {
                "Failed to get ApplicationInfo for package $pkgName."
            }

            context.applicationInfo
        }

        val applLabel = packageManager.getApplicationLabel(applInfo)
        return applLabel.toString()
    }

    /**
     * Returns the version of this application.
     */
    @JavascriptInterface
    public fun getApplicationVersion(): String? {
        val pkgInfo: PackageInfo

        val pkgName = context.packageName

        try {
            pkgInfo = AppExt.getPackageInfo(packageManager, pkgName, 0)
        } catch (ex: PackageManager.NameNotFoundException) {
            logger.error(ex) {
                "Failed to get PackageInfo for package $pkgName."
            }

            return null
        }

        return pkgInfo.versionName.toString()
    }

    /**
     * Returns the value of a resource.
     */
    @JavascriptInterface
    @SuppressLint("DiscouragedApi")
    public fun getResourceValue(name: String?, resType: String?): String? {
        val resId: Int

        if (name.isNullOrBlank()) {
            logger.error("Argument name is null.")
            return null
        }

        if (resType.isNullOrBlank()) {
            logger.error("Argument resType is null.")
            return null
        }

        if (!resType.equals(TYPE_STRING, true)) {
            logger.error { "Resource type $resType not supported." }
            return null
        }

        val pkgName = context.packageName
        resId = resources.getIdentifier(name, resType, pkgName)
        if (resId == 0) {
            logger.error {
                "Resource $name of type $resType not found in package $pkgName."
            }

            return null
        }

        return resources.getString(resId)
    }

    public companion object {

        /**
         * Tag.
         */
        public const val TAG: String = "it.scoppelletti.html.JavascriptModule"

        /**
         * Resource type `string`.
         */
        public const val TYPE_STRING: String = "string"

        private val logger = KotlinLogging.logger {}
    }
}
