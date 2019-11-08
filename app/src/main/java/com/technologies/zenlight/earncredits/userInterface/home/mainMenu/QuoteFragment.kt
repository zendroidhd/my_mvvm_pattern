package com.technologies.zenlight.earncredits.userInterface.home.mainMenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.technologies.zenlight.earncredits.R
import com.technologies.zenlight.earncredits.databinding.QuoteLayoutBinding

class QuoteFragment: Fragment() {

    private lateinit var binding : QuoteLayoutBinding

    companion object {
        private var menuCallbacks: MainMenuCallbacks? = null
        private var quote = ""
        private var author = ""

        fun newInstance(callbacks: MainMenuCallbacks,title: String, auth: String): QuoteFragment {
            menuCallbacks = callbacks
            quote = title
            author = auth
            return QuoteFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.quote_layout,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvAuthor.text = "-$author"
        binding.tvQuote.text = quote

        binding.layoutBackground.setOnClickListener{
            activity?.onBackPressed()
        }

        binding.btnNewQuote.setOnClickListener{
            activity?.onBackPressed()
            menuCallbacks?.onNewQuotesClicked()
        }
    }
}