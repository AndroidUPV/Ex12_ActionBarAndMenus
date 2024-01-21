/*
 * Copyright (c) 2022-2023 Universitat Politècnica de València
 * Authors: David de Andrés and Juan Carlos Ruiz
 *          Fault-Tolerant Systems
 *          Instituto ITACA
 *          Universitat Politècnica de València
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package upv.dadm.ex12_actionbarandmenus.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Holds the visibility of the help text.
 */
class HelpViewModel : ViewModel() {

    // Backing property for visibility of the help
    private val _visible = MutableStateFlow(true)

    // Visibility of the help
    val visible = _visible.asStateFlow()

    /**
     * Changes the current visibility of the help.
     */
    fun switchVisibility() {
        viewModelScope.launch {
            _visible.update { currentValue ->
                currentValue.not()
            }
        }
    }
}