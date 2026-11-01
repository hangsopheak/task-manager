package kh.edu.rupp.taskmanagement.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

// a key names a screen, and @Serializable lets the back stack save it across a rotation
@Serializable
data object TaskList : NavKey

// the id travels as a field of the key, so there is nothing to parse
@Serializable
data class TaskDetail(val taskId: String) : NavKey

// the two other tabs and Settings carry no data, so each is one object
@Serializable
data object Calendar : NavKey

@Serializable
data object Stats : NavKey

@Serializable
data object Settings : NavKey
