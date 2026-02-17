package com.bashar.avalag.src.features.appversion.domain.model

enum class UpdateStatus {
    UP_TO_DATE,
    MANDATORY;

    companion object {
        fun fromApi(value: String?): UpdateStatus? {
            return when (value?.trim()?.lowercase()) {
                "up_to_date" -> UP_TO_DATE
                "mandatory" -> MANDATORY
                else -> null // treat unknown as invalid -> error
            }
        }
    }
}
