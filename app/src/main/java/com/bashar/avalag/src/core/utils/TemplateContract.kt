package com.bashar.avalag.src.core.utils


data class TemplateState(
//    val gyms: List<Gym> = emptyList(),
    val name: String = "TemplateScreen",
//    val searchQuery: String = "",
//    val error: String? = null
)

sealed class TemplateEvents {
    data object OnBackPress : TemplateEvents()
    data object OnNavigateToScreen :TemplateEvents()
//    data class ToggleFavorite(val gymId: Int) : TemplateEvents()
//    data class ShowSnackBar(val message: String): TemplateEvents()
//    object Refresh : TemplateEvents()
//    data class Search(val query: String) : TemplateEvents()

}