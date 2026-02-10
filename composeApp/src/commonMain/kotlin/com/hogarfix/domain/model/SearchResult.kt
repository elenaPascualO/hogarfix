package com.hogarfix.domain.model

sealed interface SearchResult {
    val id: Long
    val title: String
    val subtitle: String?
    val category: Category

    data class InterventionResult(
        override val id: Long,
        override val title: String,
        override val subtitle: String?,
        override val category: Category
    ) : SearchResult

    data class HomeItemResult(
        override val id: Long,
        override val title: String,
        override val subtitle: String?,
        override val category: Category
    ) : SearchResult

    data class ProfessionalResult(
        override val id: Long,
        override val title: String,
        override val subtitle: String?,
        override val category: Category
    ) : SearchResult

    data class ReminderResult(
        override val id: Long,
        override val title: String,
        override val subtitle: String?,
        override val category: Category
    ) : SearchResult
}

data class SearchResults(
    val interventions: List<SearchResult.InterventionResult> = emptyList(),
    val homeItems: List<SearchResult.HomeItemResult> = emptyList(),
    val professionals: List<SearchResult.ProfessionalResult> = emptyList(),
    val reminders: List<SearchResult.ReminderResult> = emptyList()
) {
    val isEmpty: Boolean
        get() = interventions.isEmpty() && homeItems.isEmpty() &&
                professionals.isEmpty() && reminders.isEmpty()

    val totalCount: Int
        get() = interventions.size + homeItems.size + professionals.size + reminders.size
}
