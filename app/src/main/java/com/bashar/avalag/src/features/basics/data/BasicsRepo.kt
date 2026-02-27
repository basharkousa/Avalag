package com.bashar.avalag.src.features.basics.data

import com.bashar.avalag.src.features.basics.data.remote.BasicsApi
import com.bashar.avalag.src.features.basics.domain.model.AppEnums
import com.bashar.avalag.src.features.basics.domain.model.BasicsInfo
import com.bashar.avalag.src.features.basics.domain.model.Country
import com.bashar.avalag.src.features.basics.domain.model.Promotion
import com.bashar.avalag.src.features.basics.domain.repositories.IBasicsRepo
import javax.inject.Inject

class BasicsRepo @Inject constructor(val api: BasicsApi) : IBasicsRepo {

    override suspend fun getEnums(): AppEnums {
        val response = api.getEnums()
        val dto = response.data ?: error("Missing data in enum response")
        val enums = dto.enums ?: error("Missing enums in enum response")
        return AppEnums(categories = enums)
    }

    override suspend fun getBasicsInfo(): BasicsInfo {
        val response = api.getBasicsInfo()
        val dta = response.data ?: error("Missing data in basics response")
        val promotions = dta.promotions?.map {
            Promotion(
                id = it.id ?: error("Missing id in promotion"),
                title = it.title.orEmpty(),
                description = it.description.orEmpty(),
                image = it.image.orEmpty()
            )
        }

        val countries = dta.countries?.map {
            Country(
                id = it.id ?: error("Missing id in country"),
                name = it.name.orEmpty(),
                currency = it.currency.orEmpty()
            )
        }

        return BasicsInfo(
            promotions = promotions.orEmpty(),
            countries = countries.orEmpty()
        )
    }
}