/*
 * Copyright (c) 2022
 * David de Andrés and Juan Carlos Ruiz
 * Development of apps for mobile devices
 * Universitat Politècnica de València
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import upv.dadm.ex12_actionbarandmenus.R
import upv.dadm.ex12_actionbarandmenus.databinding.FragmentDeeperLevelsBinding
import upv.dadm.ex12_actionbarandmenus.ui.viewmodels.HelpViewModel

/**
 * Displays a screen with a message identifying it as a deeper level (more than 1) of Fragments.
 * Action elements can hide/show the help text and display a message with the current level.
 * The user can proceed into deeper levels.
 */
class DeeperLevelsFragment : Fragment(R.layout.fragment_deeper_levels), MenuProvider {

    // Instance of Fragment's arguments
    private val args: DeeperLevelsFragmentArgs by navArgs()

    // Reference to a ViewModel shared between Fragments
    private val viewModel: HelpViewModel by activityViewModels()

    // Backing property to resource binding
    private var _binding: FragmentDeeperLevelsBinding? = null
    // Property valid between onCreateView() and onDestroyView()
    private val binding
        get() = _binding!!

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

        // Update the action elements and the text help according to the selected visibility
        viewModel.visible.observe(viewLifecycleOwner) { visible ->
            (requireActivity() as MenuHost).invalidateMenu()
            binding.tvSharedHelpDeeperLevels.isVisible = visible
        }
    }

    // Populates the ActionBar with action elements
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
        menuInflater.inflate(R.menu.menu_shared, menu)

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

            // Show a message displaying the current level (> 1)
            R.id.mLevelInfo -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.level_info_message, args.level),
                    Toast.LENGTH_SHORT
                ).show()
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
     * Navigates to the next level screen.
     */
    private fun navigateToDeeperLevels() =
        findNavController().navigate(DeeperLevelsFragmentDirections.actionToNextLevel(args.level + 1))
}