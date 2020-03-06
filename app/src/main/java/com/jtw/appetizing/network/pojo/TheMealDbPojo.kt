package com.jtw.appetizing.network.pojo

/** All results from TheMealDb are wrapped in a top-level "meals" element */
data class TheMealDbPojo<T>(
        val meals: List<T>
)