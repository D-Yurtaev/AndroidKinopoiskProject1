package com.yuraev.kinopoiskmain.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.yuraev.kinopoiskdata.RequestResult
import com.yuraev.kinopoiskmain.DocsUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.List

@HiltViewModel
class KinopoiskNerworkViewModel @Inject constructor(

    private val getFromServerSUseCase: GetFromServerSUseCase,

) : ViewModel() {



    private val _docsState = MutableStateFlow(getDocs())

    @OptIn(ExperimentalCoroutinesApi::class)
    val docsState = _docsState.flatMapLatest { it }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = DocsState.Loading()
    )

        init {
            _docsState.value = getDocs()
        }






    fun getDocs(): Flow<DocsState> {
        val ageRating =  "0-17"
        val imdbRating =  "0-10"
        val year =  "1900-2024"
        return getFromServerSUseCase.invoke(
            ageRating = ageRating,
            imdbRating = imdbRating,
            year = year
        ).map { it.toDocsState() }

    }

}


internal fun RequestResult<List<DocsUI>>.toDocsState(): DocsState {
    return when (this) {
        is RequestResult.Error -> DocsState.Error(data)
        is RequestResult.InProgress -> DocsState.Loading(data)
        is RequestResult.Success -> DocsState.Success(data)
    }
}

sealed class DocsState {

    data class Loading(val docs: List<DocsUI>? = null) : DocsState()
    data class Success(val docs: List<DocsUI>) : DocsState()
    data class Error(val docs: List<DocsUI>? = null) : DocsState()
}

