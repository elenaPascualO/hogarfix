package com.hogarfix.domain.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class InterventionTest {

    private fun makeIntervention(
        laborCost: Double? = null,
        materialCost: Double? = null
    ) = Intervention(
        id = 1L,
        title = "Test",
        date = LocalDate(2025, 1, 15),
        category = Category.PLUMBING,
        doneBy = DoneBy.MYSELF,
        laborCost = laborCost,
        materialCost = materialCost,
        createdAt = Instant.fromEpochMilliseconds(0),
        updatedAt = Instant.fromEpochMilliseconds(0)
    )

    @Test
    fun totalCost_bothCosts() {
        val intervention = makeIntervention(laborCost = 80.0, materialCost = 40.0)
        assertEquals(120.0, intervention.totalCost)
    }

    @Test
    fun totalCost_onlyLaborCost() {
        val intervention = makeIntervention(laborCost = 80.0)
        assertEquals(80.0, intervention.totalCost)
    }

    @Test
    fun totalCost_onlyMaterialCost() {
        val intervention = makeIntervention(materialCost = 40.0)
        assertEquals(40.0, intervention.totalCost)
    }

    @Test
    fun totalCost_noCosts() {
        val intervention = makeIntervention()
        assertEquals(0.0, intervention.totalCost)
    }

    @Test
    fun totalCost_zeroCosts() {
        val intervention = makeIntervention(laborCost = 0.0, materialCost = 0.0)
        assertEquals(0.0, intervention.totalCost)
    }
}
