package com.hogarfix.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OnboardingTemplatesTest {

    @Test
    fun apartment_returns6BaseTemplates() {
        val templates = OnboardingTemplates.getTemplates(HousingType.APARTMENT)
        assertEquals(6, templates.size)
    }

    @Test
    fun houseWithGarden_returns8Templates() {
        val templates = OnboardingTemplates.getTemplates(HousingType.HOUSE_WITH_GARDEN)
        assertEquals(8, templates.size)
    }

    @Test
    fun villaWithPool_returns10Templates() {
        val templates = OnboardingTemplates.getTemplates(HousingType.VILLA_WITH_POOL)
        assertEquals(10, templates.size)
    }

    @Test
    fun gardenTemplates_includeBaseTemplates() {
        val base = OnboardingTemplates.getTemplates(HousingType.APARTMENT)
        val garden = OnboardingTemplates.getTemplates(HousingType.HOUSE_WITH_GARDEN)
        base.forEach { baseTemplate ->
            assertTrue(garden.any { it.title == baseTemplate.title })
        }
    }

    @Test
    fun poolTemplates_includeGardenAndBaseTemplates() {
        val garden = OnboardingTemplates.getTemplates(HousingType.HOUSE_WITH_GARDEN)
        val pool = OnboardingTemplates.getTemplates(HousingType.VILLA_WITH_POOL)
        garden.forEach { gardenTemplate ->
            assertTrue(pool.any { it.title == gardenTemplate.title })
        }
    }

    @Test
    fun allTemplates_havePositiveIntervalDays() {
        HousingType.entries.forEach { type ->
            val templates = OnboardingTemplates.getTemplates(type)
            templates.forEach { template ->
                assertTrue(template.intervalDays > 0, "Template '${template.title}' has invalid intervalDays: ${template.intervalDays}")
            }
        }
    }

    @Test
    fun allTemplates_haveNonBlankTitles() {
        HousingType.entries.forEach { type ->
            val templates = OnboardingTemplates.getTemplates(type)
            templates.forEach { template ->
                assertTrue(template.title.isNotBlank(), "Template has blank title")
            }
        }
    }

    @Test
    fun allTemplates_haveNonBlankDescriptions() {
        HousingType.entries.forEach { type ->
            val templates = OnboardingTemplates.getTemplates(type)
            templates.forEach { template ->
                assertTrue(template.description.isNotBlank(), "Template '${template.title}' has blank description")
            }
        }
    }

    @Test
    fun gardenTemplates_containGardenCategory() {
        val base = OnboardingTemplates.getTemplates(HousingType.APARTMENT)
        val garden = OnboardingTemplates.getTemplates(HousingType.HOUSE_WITH_GARDEN)
        val gardenOnly = garden.filter { g -> base.none { b -> b.title == g.title } }
        assertTrue(gardenOnly.any { it.category == Category.GARDEN })
    }

    @Test
    fun poolTemplates_containCleaningCategory() {
        val garden = OnboardingTemplates.getTemplates(HousingType.HOUSE_WITH_GARDEN)
        val pool = OnboardingTemplates.getTemplates(HousingType.VILLA_WITH_POOL)
        val poolOnly = pool.filter { p -> garden.none { g -> g.title == p.title } }
        assertTrue(poolOnly.any { it.category == Category.CLEANING })
    }
}
