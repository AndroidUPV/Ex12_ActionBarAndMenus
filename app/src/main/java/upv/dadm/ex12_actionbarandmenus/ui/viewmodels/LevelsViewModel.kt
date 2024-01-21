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
 * Holds the number of levels to go back.
 */
class LevelsViewModel : ViewModel() {

    // Backing property for the number of levels to go forward
    private val _levelsForward = MutableStateFlow(1)

    // Number of levels to go forward
    val levelsForward = _levelsForward.asStateFlow()

    /**
     * Changes the number of levels to go forward.
     */
    fun setLevelsForward(levels: Int) {
        viewModelScope.launch {
            _levelsForward.update {
                levels
            }
        }
    }

}