package com.hogarfix.domain.usecase

import com.hogarfix.domain.model.Category
import com.hogarfix.domain.model.HomeItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class HomeItemUseCaseTest {

    private val repo = FakeHomeItemRepository()
    private val getUseCase = GetHomeItemsUseCase(repo)
    private val saveUseCase = SaveHomeItemUseCase(repo)
    private val deleteUseCase = DeleteHomeItemUseCase(repo)

    private fun makeHomeItem(
        id: Long = 0L,
        name: String = "Lavadora",
        category: Category = Category.APPLIANCES
    ) = HomeItem(
        id = id,
        name = name,
        category = category,
        createdAt = Instant.fromEpochMilliseconds(0),
        updatedAt = Instant.fromEpochMilliseconds(0)
    )

    @Test
    fun save_newItem_assignsId() = runBlocking {
        val id = saveUseCase(makeHomeItem())
        assertNotEquals(0L, id)
    }

    @Test
    fun save_thenGetById_returnsItem() = runBlocking {
        val id = saveUseCase(makeHomeItem(name = "Caldera"))
        val result = getUseCase.getById(id)
        assertEquals("Caldera", result?.name)
    }

    @Test
    fun save_existingItem_updates() = runBlocking {
        val id = saveUseCase(makeHomeItem(name = "Original"))
        saveUseCase(makeHomeItem(id = id, name = "Updated"))
        val result = getUseCase.getById(id)
        assertEquals("Updated", result?.name)
    }

    @Test
    fun getAll_returnsAllItems() = runBlocking {
        saveUseCase(makeHomeItem(name = "A"))
        saveUseCase(makeHomeItem(name = "B"))
        val all = getUseCase().first()
        assertEquals(2, all.size)
    }

    @Test
    fun getByCategory_filtersCorrectly() = runBlocking {
        saveUseCase(makeHomeItem(category = Category.APPLIANCES))
        saveUseCase(makeHomeItem(category = Category.HVAC))
        val appliances = getUseCase.getByCategory(Category.APPLIANCES).first()
        assertEquals(1, appliances.size)
        assertEquals(Category.APPLIANCES, appliances[0].category)
    }

    @Test
    fun delete_removesItem() = runBlocking {
        val id = saveUseCase(makeHomeItem())
        deleteUseCase(id)
        assertNull(getUseCase.getById(id))
    }

    @Test
    fun delete_nonExistentId_doesNothing() = runBlocking {
        saveUseCase(makeHomeItem())
        deleteUseCase(999L)
        val all = getUseCase().first()
        assertEquals(1, all.size)
    }

    @Test
    fun getWithExpiringWarranty_returnsItemsWithWarranty() = runBlocking {
        saveUseCase(makeHomeItem(name = "Con garantia").copy(
            warrantyEndDate = LocalDate(2025, 6, 1)
        ))
        saveUseCase(makeHomeItem(name = "Sin garantia"))
        val expiring = getUseCase.getWithExpiringWarranty().first()
        assertEquals(1, expiring.size)
        assertEquals("Con garantia", expiring[0].name)
    }

    @Test
    fun getAll_emptyInitially() = runBlocking {
        val all = getUseCase().first()
        assertTrue(all.isEmpty())
    }
}
