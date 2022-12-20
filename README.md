# Ex12_ActionBarAndMenus
Lecture 02 - Development of Graphical User Interfaces (GUI)

The user can navigate through levels and levels of fragments, going deeper and deeper. 
- A single ViewModel is shared between the Fragments to access the visibility of the help text.
- A custom ToolBar is set up as the ActionBar of the app.
- The ActionBar displays the action elements provided by the Fragments that implement the MenuProvider interface. 
- The action elements display the current level of the Fragment and can hide/show the help text.
- Beyond the first level, an action view allows the user to set the number of levels to move forward.
- Navigation between Fragments automatically changes the title of the ActionBar.
- Up navigation is properly handled by the NavController in the Activity.
