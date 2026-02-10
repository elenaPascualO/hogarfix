package com.hogarfix.ui.screens.search

import com.hogarfix.domain.model.SearchResults

data class SearchState(
    val query: String = "",
    val results: SearchResults = SearchResults(),
    val isSearching: Boolean = false
)
