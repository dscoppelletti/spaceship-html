/*
 * Copyright (C) 2020-2021 Dario Scoppelletti, <http://www.scoppelletti.it/>.
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
 * limitations under the License.
 */

@file:Suppress("JoinDeclarationAndAssignment", "RedundantVisibilityModifier",
        "RemoveRedundantQualifierName", "unused")

package it.scoppelletti.spaceship.html.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewClientCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import it.scoppelletti.spaceship.html.JavascriptRepository
import it.scoppelletti.spaceship.html.R
import it.scoppelletti.spaceship.html.databinding.HtmlViewFragmentBinding
import mu.KotlinLogging

/**
 * Fragment hosting a `WebView` control.
 *
 * @since 1.0.0
 */
@UiThread
public class HtmlViewFragment :
        Fragment(R.layout.it_scoppelletti_html_htmlview_fragment) {

    private val binding by viewBinding(HtmlViewFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ctx: Context
        val assetLoader: WebViewAssetLoader

        super.onViewCreated(view, savedInstanceState)

        ctx = requireContext()
        assetLoader = WebViewAssetLoader.Builder()
                .addPathHandler(HtmlViewFragment.PATH_ASSET,
                        WebViewAssetLoader.AssetsPathHandler(ctx))
                .addPathHandler(HtmlViewFragment.PATH_RES,
                        WebViewAssetLoader.ResourcesPathHandler(ctx))
                .build()

        binding.webView.let { webView ->
            webView.webViewClient = object : WebViewClientCompat() {
                override fun shouldInterceptRequest(
                        view: WebView?,
                        url: String?
                ): WebResourceResponse? {
                    return url?.let {
                        assetLoader.shouldInterceptRequest(Uri.parse(it))
                    }
                }

                override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        url: String?
                ): Boolean {
                    Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                        startActivity(this)
                    }

                    return true
                }
            }
        }
    }

    @SuppressLint("AddJavascriptInterface", "SetJavaScriptEnabled")
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        val url: String?
        val repo: JavascriptRepository

        super.onViewStateRestored(savedInstanceState)

        binding.webView.let { view ->
            view.settings.javaScriptEnabled = true

            repo = requireActivity().htmlComponent().javascriptRepository()
            view.addJavascriptInterface(repo, JavascriptRepository.MODULE)

            url = requireArguments().getString(HtmlViewFragment.PROP_URL)
            if (url.isNullOrBlank()) {
                logger.error {
                    "Missing argument ${HtmlViewFragment.PROP_URL}."
                }
            } else {
                view.loadUrl(url)
            }
        }
    }

    public companion object {

        /**
         * Base URL of the files embedded as assets.
         */
        public const val URL_ASSET =
                "https://appassets.androidplatform.net/asset/"

        /**
         * Base URL of the files embedded as resources.
         */
        public const val URL_RESOURCE =
                "https://appassets.androidplatform.net/res/"

        private const val PATH_ASSET = "/asset/"
        private const val PATH_RES = "/res/"
        private const val PROP_URL = "1"
        private val logger = KotlinLogging.logger {}

        /**
         * Creates a new fragment.
         *
         * @return The new object.
         */
        public fun newInstance(url: String): HtmlViewFragment {
            val args = Bundle().apply {
                putString(HtmlViewFragment.PROP_URL, url)
            }

            return HtmlViewFragment().apply {
                arguments = args
            }
        }
    }
}
