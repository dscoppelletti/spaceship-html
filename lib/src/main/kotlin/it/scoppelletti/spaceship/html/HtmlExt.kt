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

import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.View
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Operations for HTML.
 *
 * @since 1.0.0
 */
public object HtmlExt {

    /**
     * Name of the `Html.TagHandler` dependency.
     */
    public const val DEP_TAGHANDLER: String = "it.scoppelletti.spaceship.html.1"

    /**
     * Property containing a text.
     */
    public const val PROP_TEXT: String = "it.scoppelletti.spaceship.html.1"
}

private const val SPAN_START = "<span>"
private const val SPAN_END = "</span>"

/**
 * Returns a displayable styled text from the provided HTML string.
 *
 * @param  source      Source HTML string.
 * @param  imageGetter Provides the representation of the image for an
 *                    `<IMG>` tag.
 * @param  tagHandler  Handles an unknown tag.
 * @return             Resulting styled text.
 * @since              1.0.0
 */
public suspend fun fromHtml(
        source: String,
        imageGetter: Html.ImageGetter? = null,
        tagHandler: Html.TagHandler? = null
) : Spanned = withContext(Dispatchers.Default) {
    // - Android 8.1
    // If the source text starts with a custom tag, then the end of that tag is
    // not detected and it is assumed at the end of the source text:
    // Enclose the source text in a span element.
   val html = StringBuilder(SPAN_START)
            .append(source)
            .append(SPAN_END).toString()

    Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY, imageGetter, tagHandler)
}

/**
 * Sets a custom handler for hyperlinks in a styled text.
 *
 * @receiver         Original styled text.
 * @param    onClick Custom handler.
 * @param    filter  Predicate to select the hyperlinks.
 * @return           Resulting styled text.
 * @since            1.0.0
 */
public suspend fun Spanned.replaceHyperlinks(
        onClick: (String) -> Unit,
        filter: ((String) -> Boolean)?
): Spanned = withContext(Dispatchers.Default) {
    SpannableStringBuilder(this@replaceHyperlinks)
            .apply {
                getSpans(0, this.length, URLSpan::class.java)
                        .filter {
                            filter?.invoke(it.url) ?: true
                        }
                        .forEach {
                            replaceHyperlink(it, onClick)
                        }
            }
}

/**
 * Sets a custom handler for an hyperlink in a styled text.
 *
 * @receiver         Styled text.
 * @param    urlSpan Hyperlink.
 * @param    onClick Custom handler.
 */
private fun SpannableStringBuilder.replaceHyperlink(
        urlSpan: URLSpan,
        onClick: (String) -> Unit
) {
    val start = this.getSpanStart(urlSpan)
    val end = this.getSpanEnd(urlSpan)
    val flags = this.getSpanFlags(urlSpan)
    val newSpan = object : ClickableSpan() {

        override fun onClick(widget: View) {
            onClick(urlSpan.url)
        }
    }

    this.setSpan(newSpan, start, end, flags)
    this.removeSpan(urlSpan)
}
