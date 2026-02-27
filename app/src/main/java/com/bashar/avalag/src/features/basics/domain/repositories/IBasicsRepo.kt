package com.bashar.avalag.src.features.basics.domain.repositories


import com.bashar.avalag.src.features.basics.domain.model.AppEnums
import com.bashar.avalag.src.features.basics.domain.model.BasicsInfo

interface IBasicsRepo {

    suspend fun getEnums(): AppEnums
    suspend fun getBasicsInfo(): BasicsInfo

}