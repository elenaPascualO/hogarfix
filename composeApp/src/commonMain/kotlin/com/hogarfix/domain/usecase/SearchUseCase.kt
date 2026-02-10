package com.hogarfix.domain.usecase

import com.hogarfix.domain.model.SearchResult
import com.hogarfix.domain.model.SearchResults
import com.hogarfix.domain.repository.HomeItemRepository
import com.hogarfix.domain.repository.InterventionRepository
import com.hogarfix.domain.repository.ProfessionalRepository
import com.hogarfix.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class SearchUseCase(
    private val interventionRepository: InterventionRepository,
    private val homeItemRepository: HomeItemRepository,
    private val professionalRepository: ProfessionalRepository,
    private val reminderRepository: ReminderRepository
) {
    operator fun invoke(query: String): Flow<SearchResults> {
        return combine(
            interventionRepository.searchByText(query),
            homeItemRepository.searchByText(query),
            professionalRepository.searchByText(query),
            reminderRepository.searchByText(query)
        ) { interventions, homeItems, professionals, reminders ->
            SearchResults(
                interventions = interventions.map { intervention ->
                    SearchResult.InterventionResult(
                        id = intervention.id,
                        title = intervention.title,
                        subtitle = intervention.description,
                        category = intervention.category
                    )
                },
                homeItems = homeItems.map { item ->
                    SearchResult.HomeItemResult(
                        id = item.id,
                        title = item.name,
                        subtitle = listOfNotNull(item.brand, item.model).joinToString(" ").ifEmpty { null },
                        category = item.category
                    )
                },
                professionals = professionals.map { pro ->
                    SearchResult.ProfessionalResult(
                        id = pro.id,
                        title = pro.name,
                        subtitle = pro.phone,
                        category = pro.specialty
                    )
                },
                reminders = reminders.map { reminder ->
                    SearchResult.ReminderResult(
                        id = reminder.id,
                        title = reminder.title,
                        subtitle = reminder.description,
                        category = reminder.category
                    )
                }
            )
        }
    }
}
