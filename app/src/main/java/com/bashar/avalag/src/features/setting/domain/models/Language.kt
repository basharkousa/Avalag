package com.bashar.avalag.src.features.setting.domain.models

enum class Language(val tag: String) {
    System(""), English("en"), Arabic("ar");

    companion object {
        fun fromTag(tag: String?): Language =
            entries.firstOrNull { it.tag == (tag ?: "") } ?: System
    }
}
