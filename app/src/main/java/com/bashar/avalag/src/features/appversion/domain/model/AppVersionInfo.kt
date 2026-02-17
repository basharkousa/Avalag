package com.bashar.avalag.src.features.appversion.domain.model

data class AppVersionInfo(
    val updateStatus: UpdateStatus = UpdateStatus.UP_TO_DATE,
    val link: String? = null,
)
