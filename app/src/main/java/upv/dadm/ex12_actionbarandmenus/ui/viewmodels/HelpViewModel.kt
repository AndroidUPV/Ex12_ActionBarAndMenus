/*
 * Copyright (c) 2022
 * David de Andrés and Juan Carlos Ruiz
 * Development of apps for mobile devices
 * Universitat Politècnica de València
 */

package upv.dadm.ex12_actionbarandmenus.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Holds the visibility of the help text.
 */
class HelpViewModel : ViewModel() {

    // Backing property for visibility of the help
    private var _visible = MutableLiveData(true)

    // Visibility of the help
    val visible: LiveData<Boolean>
        get() = _visible

    /**
     * Changes the current visibility of the help.
     */
    fun switchVisibility() {
        _visible.value = _visible.value?.not()
    }
}