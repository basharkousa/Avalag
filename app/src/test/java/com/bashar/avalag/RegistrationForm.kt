package com.bashar.avalag

import org.junit.Assert.*
import org.junit.Test


class RegistrationFormTest{

    @Test
    fun valid_registration_form(){
        val form = RegistrationForm(
            fullName = "Bashar",
            password = "0945574056#A",
            confirmPassword = "0945574056#A",
            referralCode = "1234",
            phone = 945574056
        )

        val isValid = validateRegisterForm(form)
        assertEquals(RegisterValidationResult.Valid,isValid)
    }


}
data class RegistrationForm(
    val fullName: String,
    val password: String,
    val confirmPassword: String,
    val referralCode: String?,
    val phone: Int,
    )

sealed interface RegisterValidationResult {
    data object Valid : RegisterValidationResult
    data class InValid(val message: String) : RegisterValidationResult
}

fun validateRegisterForm(form: RegistrationForm): RegisterValidationResult {
    if (form.fullName.isEmpty()) return RegisterValidationResult.InValid(message = "FullName Can't be empty!!")
    if (form.phone.toString().length < 8) return RegisterValidationResult.InValid(message = "Phone Number must be more than 8 digits!!")
    if (form.password != form.confirmPassword) return RegisterValidationResult.InValid(message = "ConfirmPassword must equal password")
    if (form.referralCode != null && form.referralCode.length < 4) {
        return RegisterValidationResult.InValid("Referral code must be at least 4 characters")
    }
    return RegisterValidationResult.Valid
}



