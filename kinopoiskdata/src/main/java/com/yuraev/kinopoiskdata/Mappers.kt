package com.yuraev.kinopoiskdata

import com.yuraev.kinopoiskapi.model.DocsDTO
import com.yuraev.kinopoiskapi.model.PosterDTO
import com.yuraev.kinopoiskdata.model.DocsData
import com.yuraev.kinopoiskdata.model.PosterData

fun DocsDTO.toDocsData(): DocsData {
    return DocsData(
        id = id ?: 0,
        name = name,
        description = description,
        shortDescription = shortDescription,
        ageRating = ageRating.toString(),
        year = year,
        imdbRating = ratingDTO?.imdb?.toString(),
        posterData = posterDTO.toPosterData()
    )
}

internal fun PosterDTO?.toPosterData(): PosterData? {
    return this?.let { PosterData(previewUrl = it.previewUrl, url = it.url) }
}









