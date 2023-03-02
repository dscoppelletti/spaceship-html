package it.scoppelletti.spaceship.html.sample.lifecycle

import android.text.Html
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.scoppelletti.spaceship.html.HtmlExt
import it.scoppelletti.spaceship.html.fromHtml
import it.scoppelletti.spaceship.lifecycle.SingleEvent
import it.scoppelletti.spaceship.lifecycle.ViewModelProviderEx
import it.scoppelletti.spaceship.types.StringExt
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class HtmlTextViewModel(
        private val tagHandler: Html.TagHandler,
        private val handle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableLiveData<HtmlTextState>()
    val state: LiveData<HtmlTextState> = _state

    init {
        val text: CharSequence? = handle[PROP_TEXT]
        if (text != null) {
            _state.value = HtmlTextState(text = text)
        }
    }

    fun buildText(source: String) = viewModelScope.launch {
        val text: CharSequence

        try {
            text = fromHtml(source, null, tagHandler)
            handle[PROP_TEXT] = text
            _state.value = HtmlTextState(text = text)
        } catch (ex: CancellationException) {
            throw ex
        } catch (ex: Exception) {
            _state.value = HtmlTextState(error = SingleEvent(ex))
        }
    }

    private companion object {
        const val PROP_TEXT = HtmlExt.PROP_TEXT
    }
}

data class HtmlTextState (
        val text: CharSequence = StringExt.EMPTY,
        val error: SingleEvent<Throwable>? = null
)

class HtmlTextViewModelFactory @Inject constructor(

        @Named(HtmlExt.DEP_TAGHANDLER)
        private val tagHandler: Html.TagHandler
) : ViewModelProviderEx.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(handle: SavedStateHandle): T =
            HtmlTextViewModel(tagHandler, handle) as T
}
