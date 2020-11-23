@file:Suppress("JoinDeclarationAndAssignment")

package it.scoppelletti.spaceship.html.sample

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import it.scoppelletti.spaceship.app.appComponent
import it.scoppelletti.spaceship.app.showExceptionDialog
import it.scoppelletti.spaceship.html.sample.databinding.HtmltextFragmentBinding
import it.scoppelletti.spaceship.html.sample.lifecycle.HtmlTextState
import it.scoppelletti.spaceship.html.sample.lifecycle.HtmlTextViewModel
import it.scoppelletti.spaceship.lifecycle.ViewModelProviderEx

class HtmlTextFragment : Fragment() {

    private lateinit var viewModel: HtmlTextViewModel
    private var binding: HtmltextFragmentBinding? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = HtmltextFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.txtContent?.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val activity: FragmentActivity
        val viewModelProvider: ViewModelProviderEx

        super.onActivityCreated(savedInstanceState)

        activity = requireActivity()
        viewModelProvider = activity.appComponent().viewModelProvider()
        viewModel = viewModelProvider.get(this, HtmlTextViewModel::class.java)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (state != null) {
                stateObserver(state)
            }
        }

        viewModel.buildText(getString(R.string.html_text))
    }

    private fun stateObserver(state: HtmlTextState) {
        binding?.txtContent?.text = state.text

        state.error?.poll()?.let { ex ->
            showExceptionDialog(ex)
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    companion object {

        fun newInstance() = HtmlTextFragment()
    }
}
