package com.jtw.appetizing.network.pojo

data class MealDetails(
        val dateModified: Any,
        val idMeal: String,
        val strArea: String,
        val strCategory: String,
        val strDrinkAlternate: Any,
        val strInstructions: String,
        val strMeal: String,
        val strMealThumb: String,
        val strSource: Any,
        val strTags: String?,
        val strYoutube: String?,

        val strIngredient1: String?,
        val strIngredient2: String?,
        val strIngredient3: String?,
        val strIngredient4: String?,
        val strIngredient5: String?,
        val strIngredient6: String?,
        val strIngredient7: String?,
        val strIngredient8: String?,
        val strIngredient9: String?,
        val strIngredient10: String?,
        val strIngredient11: String?,
        val strIngredient12: String?,
        val strIngredient13: String?,
        val strIngredient14: String?,
        val strIngredient15: String?,
        val strIngredient16: String?,
        val strIngredient17: String?,
        val strIngredient18: String?,
        val strIngredient19: String?,
        val strIngredient20: String?,

        val strMeasure1: String?,
        val strMeasure2: String?,
        val strMeasure3: String?,
        val strMeasure4: String?,
        val strMeasure5: String?,
        val strMeasure6: String?,
        val strMeasure7: String?,
        val strMeasure8: String?,
        val strMeasure9: String?,
        val strMeasure10: String?,
        val strMeasure11: String?,
        val strMeasure12: String?,
        val strMeasure13: String?,
        val strMeasure14: String?,
        val strMeasure15: String?,
        val strMeasure16: String?,
        val strMeasure17: String?,
        val strMeasure18: String?,
        val strMeasure19: String?,
        val strMeasure20: String?

)

/** ingredient -> measure */
fun MealDetails.ingredients(): Map<String, String?> {
    // TODO JTW probably a more elegant way to do this
    return listOfNotNull(
            strIngredient1?.takeIf { it.isNotBlank() }?.let { it to strMeasure1 },
            strIngredient2?.takeIf { it.isNotBlank() }?.let { it to strMeasure2 },
            strIngredient3?.takeIf { it.isNotBlank() }?.let { it to strMeasure3 },
            strIngredient4?.takeIf { it.isNotBlank() }?.let { it to strMeasure4 },
            strIngredient5?.takeIf { it.isNotBlank() }?.let { it to strMeasure5 },
            strIngredient6?.takeIf { it.isNotBlank() }?.let { it to strMeasure6 },
            strIngredient7?.takeIf { it.isNotBlank() }?.let { it to strMeasure7 },
            strIngredient8?.takeIf { it.isNotBlank() }?.let { it to strMeasure8 },
            strIngredient9?.takeIf { it.isNotBlank() }?.let { it to strMeasure9 },
            strIngredient11?.takeIf { it.isNotBlank() }?.let { it to strMeasure11 },
            strIngredient12?.takeIf { it.isNotBlank() }?.let { it to strMeasure12 },
            strIngredient13?.takeIf { it.isNotBlank() }?.let { it to strMeasure13 },
            strIngredient14?.takeIf { it.isNotBlank() }?.let { it to strMeasure14 },
            strIngredient15?.takeIf { it.isNotBlank() }?.let { it to strMeasure15 },
            strIngredient16?.takeIf { it.isNotBlank() }?.let { it to strMeasure16 },
            strIngredient17?.takeIf { it.isNotBlank() }?.let { it to strMeasure17 },
            strIngredient18?.takeIf { it.isNotBlank() }?.let { it to strMeasure18 },
            strIngredient19?.takeIf { it.isNotBlank() }?.let { it to strMeasure19 },
            strIngredient20?.takeIf { it.isNotBlank() }?.let { it to strMeasure20 }
    ).toMap()
}

fun MealDetails.tags(): List<String> {
    return strTags?.split(",") ?: emptyList()
}