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
 * Holds the number of levels to go back.
 */
class LevelsViewModel: ViewModel() {

    // Backing property for the number of levels to go forward
    private var _levelsForward = MutableLiveData(1)

    // Number of levels to go forward
    val levelsForward: LiveData<Int>
        get() = _levelsForward

    /**
     * Changes the number of levels to go forward.
     */
    fun setLevelsForward(levels: Int) {
        _levelsForward.value = levels
    }

}