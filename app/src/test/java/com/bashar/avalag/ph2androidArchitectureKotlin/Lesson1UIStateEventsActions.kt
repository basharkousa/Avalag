package com.bashar.avalag.ph2androidArchitectureKotlin

import org.junit.Assert
import org.junit.Test


data class TransferUiState(
    val amount: String = "",
    val receiverId: Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val transferCompleted: Boolean = false
)

sealed interface TransferUiEvents {
    data class AmountChanged(val amount: String) : TransferUiEvents
    data class ReceiverSelected(val receiverId: Int) : TransferUiEvents
    data object SubmitTransfer : TransferUiEvents
}

class TransferViewModel {
    fun onEvent(event: TransferUiEvents) {
        when (event) {
            is TransferUiEvents.AmountChanged -> {
//                update amount
            }

            is TransferUiEvents.ReceiverSelected -> {
//                update receiver
            }

            TransferUiEvents.SubmitTransfer -> {
//                submit Transfer
            }
        }
    }
}

//Exercise
data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSaved: Boolean = false
)

sealed interface ProfileUiEvent {
    data class NameChanged(val name: String) : ProfileUiEvent
    data class EmailChanged(val email: String) : ProfileUiEvent
    data object SaveProfile : ProfileUiEvent
}

class ProfileViewModel {
    var state: ProfileUiState = ProfileUiState()
        private set

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }

            is ProfileUiEvent.NameChanged -> {
                state = state.copy(name = event.name)
            }

            ProfileUiEvent.SaveProfile -> {
                when {
                    state.name.isBlank() -> {
                        state = state.copy(
                            errorMessage = "Name is required",
                            isSaved = false
                        )
                    }

                    state.email.isBlank() -> {
                        state = state.copy(
                            errorMessage = "Email is required",
                            isSaved = false
                        )
                    }

                    else -> {
                        state = state.copy(
                            isSaved = true,
                            errorMessage = null
                        )
                    }
                }
            }
        }

    }

    class ProfilePageTests {

        @Test
        fun `Name Updated Successfully`() {
            val viewModel = ProfileViewModel()
            viewModel.onEvent(ProfileUiEvent.NameChanged("Bashar"))
            Assert.assertEquals("Bashar", viewModel.state.name)
        }

        @Test
        fun `Email Updated Successfully`() {
            val viewModel = ProfileViewModel()
            viewModel.onEvent(ProfileUiEvent.EmailChanged("bashar@gmail.com"))
            Assert.assertEquals("bashar@gmail.com", viewModel.state.email)
        }

        @Test
        fun `Profile Saved Successfully`() {
            val viewModel = ProfileViewModel()

            viewModel.onEvent(ProfileUiEvent.NameChanged("Bashar"))
            viewModel.onEvent(ProfileUiEvent.EmailChanged("bashar@gmail.com"))

            viewModel.onEvent(ProfileUiEvent.SaveProfile)

            Assert.assertTrue(viewModel.state.isSaved)
            Assert.assertNull(viewModel.state.errorMessage)
        }

        @Test
        fun `Email is blank profile is not saved`() {
            val viewModel = ProfileViewModel()

            viewModel.onEvent(ProfileUiEvent.NameChanged("Bashar"))
            viewModel.onEvent(ProfileUiEvent.EmailChanged(""))

            viewModel.onEvent(ProfileUiEvent.SaveProfile)

            Assert.assertEquals(
                "Email is required",
                viewModel.state.errorMessage
            )

            Assert.assertFalse(viewModel.state.isSaved)
        }

        @Test
        fun `Name is blank profile is not saved`() {
            val viewModel = ProfileViewModel()

            viewModel.onEvent(ProfileUiEvent.NameChanged(""))
            viewModel.onEvent(
                ProfileUiEvent.EmailChanged("bashar@gmail.com")
            )

            viewModel.onEvent(ProfileUiEvent.SaveProfile)

            Assert.assertEquals(
                "Name is required",
                viewModel.state.errorMessage
            )

            Assert.assertFalse(viewModel.state.isSaved)
        }

    }
}