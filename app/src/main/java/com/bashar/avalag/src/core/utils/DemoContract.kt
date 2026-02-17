package com.bashar.avalag.src.core.utils


data class DemoState(
//    val gyms: List<Gym> = emptyList(),
    val name: String = "TemplateScreen",
//    val searchQuery: String = "",
//    val error: String? = null
)

sealed class DemoEvents {
    data object OnBackPress : DemoEvents()
    data object OnNavigateToScreen :DemoEvents()
//    data class ToggleFavorite(val gymId: Int) : TemplateEvents()
//    data class ShowSnackBar(val message: String): TemplateEvents()
//    object Refresh : TemplateEvents()
//    data class Search(val query: String) : TemplateEvents()

}