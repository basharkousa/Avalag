package com.bashar.avalag

data class ProfileState(
    val fullName:String = "",
    val phone: String = "",
    val isEditing: Boolean = false,
    val errorMessage : String? = null
)

sealed interface ProfileEvent{
    data class FullNameChanged(val value: String): ProfileEvent
    data class PhoneChanged(val value: String): ProfileEvent
    data object EditClicked: ProfileEvent
    data object SavedClicked : ProfileEvent
}

class ProfileViewModel{
    var state = ProfileState()
        private set
    fun onEvent(event: ProfileEvent){
        when(event){
            is ProfileEvent.FullNameChanged -> state = state.copy(fullName = event.value)
            is ProfileEvent.PhoneChanged -> state = state.copy(phone = event.value)
            ProfileEvent.EditClicked -> onEditClicked()
            ProfileEvent.SavedClicked -> onSaveClicked()
        }
    }

    private fun onEditClicked() {
        state = state.copy(
            isEditing = true,
            errorMessage = null
        )
    }


    private fun onSaveClicked() {
        if (state.fullName.isBlank()) {
            state = state.copy(errorMessage = "Full name is required")
            return
        }

        if (state.phone.length < 8) {
            state = state.copy(errorMessage = "Phone must be at least 8 digits")
            return
        }

        state = state.copy(
            isEditing = false,
            errorMessage = null
        )
    }
}