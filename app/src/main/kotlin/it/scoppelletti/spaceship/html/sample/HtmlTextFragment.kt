package it.scoppelletti.spaceship.html.sample

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import it.scoppelletti.spaceship.app.appComponent
import it.scoppelletti.spaceship.app.showExceptionDialog
import it.scoppelletti.spaceship.html.sample.databinding.HtmltextFragmentBinding
import it.scoppelletti.spaceship.html.sample.lifecycle.HtmlTextState
import it.scoppelletti.spaceship.html.sample.lifecycle.HtmlTextViewModel

class HtmlTextFragment : Fragment(R.layout.htmltext_fragment) {

    private lateinit var viewModel: HtmlTextViewModel
    private val binding by viewBinding(HtmltextFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtContent.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        val activity = requireActivity()
        val viewModelProvider = activity.appComponent().viewModelProvider()
        viewModel = viewModelProvider.get(this, HtmlTextViewModel::class.java)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (state != null) {
                stateObserver(state)
            }
        }

        viewModel.buildText(getString(R.string.html_text))
    }

    private fun stateObserver(state: HtmlTextState) {
        binding.txtContent.text = state.text

        state.error?.poll()?.let { ex ->
            showExceptionDialog(ex)
        }
    }

    companion object {

        fun newInstance() = HtmlTextFragment()
    }
}
