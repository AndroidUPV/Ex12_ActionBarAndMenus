/*
 * Copyright (c) 2022-2024 Universitat Politècnica de València
 * Authors: David de Andrés and Juan Carlos Ruiz
 *          Fault-Tolerant Systems
 *          Instituto ITACA
 *          Universitat Politècnica de València
 *
 * Distributed under MIT license
 * (See accompanying file LICENSE.txt)
 */

package upv.dadm.ex12_actionbarandmenus.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import upv.dadm.ex12_actionbarandmenus.R
import upv.dadm.ex12_actionbarandmenus.databinding.FragmentFirstLevelBinding
import upv.dadm.ex12_actionbarandmenus.ui.viewmodels.HelpViewModel

/**
 * Displays a screen with a message identifying it as the first level of Fragments.
 * Action elements can hide/show the help text and display a message with the current level.
 * The user can proceed into deeper levels.
 */
class FirstLevelFragment : Fragment(R.layout.fragment_first_level), MenuProvider {

    // Reference to a ViewModel shared between Fragments
    private val viewModel: HelpViewModel by activityViewModels()

    // Backing property to resource binding
    private var _binding: FragmentFirstLevelBinding? = null

    // Property valid between onCreateView() and onDestroyView()
    private val binding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get the automatically generated view binding for the layout resource
        _binding = FragmentFirstLevelBinding.bind(view)
        // Navigates to the DeeperLevelsFragment
        binding.bNextFirstLevel.setOnClickListener {
            navigateToNextLevel()
        }

        // Add this Fragment as MenuProvider to the MainActivity
        requireActivity().addMenuProvider(
            this@FirstLevelFragment,
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Update the action elements and the text help according to the selected visibility
                viewModel.visible.collect { visible ->
                    (requireActivity() as MenuHost).invalidateMenu()
                    binding.tvSharedHelpFirstLevels.isVisible = visible
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear resources to make them eligible for garbage collection
        _binding = null
    }

    // Populates the ActionBar with action elements
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
        menuInflater.inflate(R.menu.menu_first_level, menu)

    // Allows the modification of elements of the already created menu before showing it
    override fun onPrepareMenu(menu: Menu) {
        menu.findItem(R.id.mHideHelp).isVisible = viewModel.visible.value == true
        menu.findItem(R.id.mShowHelp).isVisible = viewModel.visible.value == false
        super.onPrepareMenu(menu)
    }

    // Reacts to the selection of action elements
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        // Determine the action to take place according to its Id
        return when (menuItem.itemId) {

            // Show a message displaying the current level (1)
            R.id.mLevelInfo -> {
                displayMessage()
                true
            }

            // Change the visibility of the help text
            R.id.mHideHelp, R.id.mShowHelp -> {
                viewModel.switchVisibility()
                true
            }

            // If none of the custom action elements was selected, let the system deal with it
            else -> false
        }
    }

    /**
     * Navigates to the second level screen.
     */
    private fun navigateToNextLevel() =
        findNavController().navigate(FirstLevelFragmentDirections.actionToSecondLevel(2))

    /**
     * Displays a message.
     */
    private fun displayMessage() =
        Toast.makeText(
            requireContext(),
            getString(R.string.level_info_message, 1),
            Toast.LENGTH_SHORT
        ).show()
}