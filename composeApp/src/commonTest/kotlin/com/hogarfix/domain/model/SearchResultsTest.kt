package com.hogarfix.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SearchResultsTest {

    @Test
    fun isEmpty_whenAllListsAreEmpty() {
        val results = SearchResults()
        assertTrue(results.isEmpty)
    }

    @Test
    fun isEmpty_falseWithInterventions() {
        val results = SearchResults(
            interventions = listOf(
                SearchResult.InterventionResult(1, "Test", null, Category.PLUMBING)
            )
        )
        assertFalse(results.isEmpty)
    }

    @Test
    fun isEmpty_falseWithHomeItems() {
        val results = SearchResults(
            homeItems = listOf(
                SearchResult.HomeItemResult(1, "Test", null, Category.APPLIANCES)
            )
        )
        assertFalse(results.isEmpty)
    }

    @Test
    fun isEmpty_falseWithProfessionals() {
        val results = SearchResults(
            professionals = listOf(
                SearchResult.ProfessionalResult(1, "Test", null, Category.ELECTRICAL)
            )
        )
        assertFalse(results.isEmpty)
    }

    @Test
    fun isEmpty_falseWithReminders() {
        val results = SearchResults(
            reminders = listOf(
                SearchResult.ReminderResult(1, "Test", null, Category.HVAC)
            )
        )
        assertFalse(results.isEmpty)
    }

    @Test
    fun totalCount_sumsAllLists() {
        val results = SearchResults(
            interventions = listOf(
                SearchResult.InterventionResult(1, "A", null, Category.PLUMBING),
                SearchResult.InterventionResult(2, "B", null, Category.PLUMBING)
            ),
            homeItems = listOf(
                SearchResult.HomeItemResult(1, "C", null, Category.APPLIANCES)
            ),
            professionals = listOf(
                SearchResult.ProfessionalResult(1, "D", null, Category.ELECTRICAL),
                SearchResult.ProfessionalResult(2, "E", null, Category.ELECTRICAL),
                SearchResult.ProfessionalResult(3, "F", null, Category.ELECTRICAL)
            ),
            reminders = emptyList()
        )
        assertEquals(6, results.totalCount)
    }

    @Test
    fun totalCount_zeroWhenEmpty() {
        val results = SearchResults()
        assertEquals(0, results.totalCount)
    }
}
