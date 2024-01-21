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

package upv.dadm.ex12_actionbarandmenus.ui.fragments

import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import android.view.View
import android.widget.EditText
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
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import upv.dadm.ex12_actionbarandmenus.R
import upv.dadm.ex12_actionbarandmenus.databinding.FragmentDeeperLevelsBinding
import upv.dadm.ex12_actionbarandmenus.ui.viewmodels.HelpViewModel
import upv.dadm.ex12_actionbarandmenus.ui.viewmodels.LevelsViewModel

/**
 * Displays a screen with a message identifying it as a deeper level (more than 1) of Fragments.
 * Action elements can hide/show the help text, display a message with the current level, and
 * set the number of levels to move forward.
 * The user can proceed into deeper levels.
 */
class DeeperLevelsFragment : Fragment(R.layout.fragment_deeper_levels), MenuProvider {

    // Instance of Fragment's arguments
    private val args: DeeperLevelsFragmentArgs by navArgs()

    // Reference to a ViewModel shared between Fragments
    private val helpViewModel: HelpViewModel by activityViewModels()

    // Reference to a ViewModel shared with the Activity
    private val levelsViewModel: LevelsViewModel by activityViewModels()

    // Backing property to resource binding
    private var _binding: FragmentDeeperLevelsBinding? = null

    // Property valid between onCreateView() and onDestroyView()
    private val binding
        get() = _binding!!

    // Listener to react when the action view is expanded/collapsed
    private val expandListener = object : OnActionExpandListener {
        // Update the EditText with the current value stored in the ViewModel
        override fun onMenuItemActionExpand(item: MenuItem): Boolean {
            (item.actionView as EditText).setText(levelsViewModel.levelsForward.value.toString())
            return true
        }

        // Update the ViewModel with the value entered by the user
        override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
            levelsViewModel.setLevelsForward(
                (item.actionView as EditText).text.toString().toInt()
            )
            return true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get the automatically generated view binding for the layout resource
        _binding = FragmentDeeperLevelsBinding.bind(view)
        // Navigates to the DeeperLevelsFragment
        binding.bNextDeeperLevels.setOnClickListener {
            navigateToDeeperLevels()
        }

        // Add this Fragment as MenuProvider to the MainActivity
        requireActivity().addMenuProvider(
            this@DeeperLevelsFragment,
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Update the action elements and the text help according to the selected visibility
                helpViewModel.visible.collect { visible ->
                    (requireActivity() as MenuHost).invalidateMenu()
                    binding.tvSharedHelpDeeperLevels.isVisible = visible
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
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_deeper_levels, menu)
        // Sets the properties of the actionview (EditText)
        val itemForward = menu.findItem(R.id.mForward)
        (itemForward.actionView as EditText).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            setHint(R.string.forward_hint)
        }
        // Set the listener to react when the action view is expanded/collapsed
        itemForward.setOnActionExpandListener(expandListener)
    }

    // Allows the modification of elements of the already created menu before showing it
    override fun onPrepareMenu(menu: Menu) {
        menu.findItem(R.id.mHideHelp).isVisible = helpViewModel.visible.value == true
        menu.findItem(R.id.mShowHelp).isVisible = helpViewModel.visible.value == false
        super.onPrepareMenu(menu)
    }

    // Reacts to the selection of action elements
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        // Determine the action to take place according to its Id
        return when (menuItem.itemId) {

            // Show a message displaying the current level (> 1)
            R.id.mLevelInfo -> {
                displayMessage()
                true
            }

            // Change the visibility of the help text
            R.id.mHideHelp, R.id.mShowHelp -> {
                helpViewModel.switchVisibility()
                true
            }

            // If none of the custom action elements was selected, let the system deal with it
            else -> false
        }
    }

    /**
     * Navigates to the next level screen.
     */
    private fun navigateToDeeperLevels() =
        findNavController().navigate(
            DeeperLevelsFragmentDirections.actionToNextLevel(
                levelsViewModel.levelsForward.value.plus(args.level)
            )
        )

    /**
     * Displays a message.
     */
    private fun displayMessage() =
        Toast.makeText(
            requireContext(),
            getString(R.string.level_info_message, args.level),
            Toast.LENGTH_SHORT
        ).show()
}