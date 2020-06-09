package com.fidato.headytestapp.base

import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    fun updateTitle(title: String?) {
        (activity as MainActivity).supportActionBar?.title = title ?: activity?.actionBar?.title
    }

}
