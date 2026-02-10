package com.hogarfix.ui.screens.interventions

import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.HomeItem
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class InterventionFormStateTest {

    @Test
    fun selectedHomeItem_null_whenNoHomeItemId() {
        val state = InterventionFormState(
            homeItems = listOf(
                HomeItem(
                    id = 1,
                    name = "Lavadora",
                    category = Category.APPLIANCES,
                    createdAt = Instant.fromEpochMilliseconds(0),
                    updatedAt = Instant.fromEpochMilliseconds(0)
                )
            )
        )
        assertNull(state.selectedHomeItem)
    }

    @Test
    fun selectedHomeItem_returnsCorrectItem() {
        val lavadora = HomeItem(
            id = 1,
            name = "Lavadora",
            category = Category.APPLIANCES,
            createdAt = Instant.fromEpochMilliseconds(0),
            updatedAt = Instant.fromEpochMilliseconds(0)
        )
        val state = InterventionFormState(
            homeItemId = 1,
            homeItems = listOf(lavadora)
        )
        assertEquals(lavadora, state.selectedHomeItem)
    }

    @Test
    fun selectedHomeItem_null_whenIdNotInList() {
        val state = InterventionFormState(
            homeItemId = 99,
            homeItems = listOf(
                HomeItem(
                    id = 1,
                    name = "Lavadora",
                    category = Category.APPLIANCES,
                    createdAt = Instant.fromEpochMilliseconds(0),
                    updatedAt = Instant.fromEpochMilliseconds(0)
                )
            )
        )
        assertNull(state.selectedHomeItem)
    }
}
