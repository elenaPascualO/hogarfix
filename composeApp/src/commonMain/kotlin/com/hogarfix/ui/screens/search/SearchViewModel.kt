package com.hogarfix.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hogarfix.domain.model.SearchResults
import com.hogarfix.domain.usecase.SearchUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val queryFlow = MutableStateFlow("")

    init {
        viewModelScope.launch {
            queryFlow
                .debounce(300)
                .flatMapLatest { query ->
                    if (query.length < 2) {
                        flowOf(SearchResults())
                    } else {
                        _state.update { it.copy(isSearching = true) }
                        searchUseCase(query)
                    }
                }
                .collect { results ->
                    _state.update {
                        it.copy(results = results, isSearching = false)
                    }
                }
        }
    }

    fun onQueryChanged(query: String) {
        _state.update { it.copy(query = query) }
        queryFlow.value = query
    }
}
