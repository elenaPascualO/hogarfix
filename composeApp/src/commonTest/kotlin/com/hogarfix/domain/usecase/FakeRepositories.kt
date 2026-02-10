package com.hogarfix.domain.usecase

import com.hogarfix.domain.model.*
import com.hogarfix.domain.repository.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

// --- Fake InterventionRepository ---

class FakeInterventionRepository : InterventionRepository {
    private val items = MutableStateFlow<List<Intervention>>(emptyList())
    private var nextId = 1L

    override fun getAll(): Flow<List<Intervention>> = items

    override fun getRecent(limit: Int): Flow<List<Intervention>> =
        items.map { it.sortedByDescending { i -> i.date }.take(limit) }

    override fun getByCategory(category: Category): Flow<List<Intervention>> =
        items.map { list -> list.filter { it.category == category } }

    override fun getByHomeItem(itemId: Long): Flow<List<Intervention>> =
        items.map { list -> list.filter { it.homeItemId == itemId } }

    override fun getByProfessional(professionalId: Long): Flow<List<Intervention>> =
        items.map { list -> list.filter { it.professionalId == professionalId } }

    override fun searchByText(query: String): Flow<List<Intervention>> =
        items.map { list ->
            list.filter {
                it.title.contains(query, ignoreCase = true) ||
                    (it.description?.contains(query, ignoreCase = true) == true) ||
                    (it.notes?.contains(query, ignoreCase = true) == true)
            }
        }

    override suspend fun getById(id: Long): Intervention? =
        items.value.find { it.id == id }

    override suspend fun save(intervention: Intervention): Long {
        val id = if (intervention.id == 0L) nextId++ else intervention.id
        val saved = intervention.copy(id = id)
        items.value = items.value.filter { it.id != id } + saved
        return id
    }

    override suspend fun delete(id: Long) {
        items.value = items.value.filter { it.id != id }
    }

    override suspend fun savePhoto(interventionId: Long, photoBytes: ByteArray): String =
        "photo_$interventionId.jpg"

    override suspend fun deletePhoto(photoUri: String) {}
}

// --- Fake HomeItemRepository ---

class FakeHomeItemRepository : HomeItemRepository {
    private val items = MutableStateFlow<List<HomeItem>>(emptyList())
    private var nextId = 1L

    override fun getAll(): Flow<List<HomeItem>> = items

    override fun getByCategory(category: Category): Flow<List<HomeItem>> =
        items.map { list -> list.filter { it.category == category } }

    override fun getWithExpiringWarranty(daysAhead: Int): Flow<List<HomeItem>> =
        items.map { list -> list.filter { it.warrantyEndDate != null } }

    override fun searchByText(query: String): Flow<List<HomeItem>> =
        items.map { list ->
            list.filter {
                it.name.contains(query, ignoreCase = true) ||
                    (it.brand?.contains(query, ignoreCase = true) == true) ||
                    (it.model?.contains(query, ignoreCase = true) == true) ||
                    (it.notes?.contains(query, ignoreCase = true) == true)
            }
        }

    override suspend fun getById(id: Long): HomeItem? =
        items.value.find { it.id == id }

    override suspend fun save(homeItem: HomeItem): Long {
        val id = if (homeItem.id == 0L) nextId++ else homeItem.id
        val saved = homeItem.copy(id = id)
        items.value = items.value.filter { it.id != id } + saved
        return id
    }

    override suspend fun delete(id: Long) {
        items.value = items.value.filter { it.id != id }
    }

    override suspend fun savePhoto(homeItemId: Long, photoBytes: ByteArray): String =
        "photo_$homeItemId.jpg"

    override suspend fun deletePhoto(photoUri: String) {}
}

// --- Fake ProfessionalRepository ---

class FakeProfessionalRepository : ProfessionalRepository {
    private val items = MutableStateFlow<List<Professional>>(emptyList())
    private var nextId = 1L

    override fun getAll(): Flow<List<Professional>> = items

    override fun getBySpecialty(category: Category): Flow<List<Professional>> =
        items.map { list -> list.filter { it.specialty == category } }

    override fun searchByText(query: String): Flow<List<Professional>> =
        items.map { list ->
            list.filter {
                it.name.contains(query, ignoreCase = true) ||
                    (it.notes?.contains(query, ignoreCase = true) == true)
            }
        }

    override suspend fun getById(id: Long): Professional? =
        items.value.find { it.id == id }

    override suspend fun save(professional: Professional): Long {
        val id = if (professional.id == 0L) nextId++ else professional.id
        val saved = professional.copy(id = id)
        items.value = items.value.filter { it.id != id } + saved
        return id
    }

    override suspend fun delete(id: Long) {
        items.value = items.value.filter { it.id != id }
    }
}

// --- Fake ReminderRepository ---

class FakeReminderRepository : ReminderRepository {
    private val items = MutableStateFlow<List<Reminder>>(emptyList())
    private var nextId = 1L
    var lastCompletedId: Long? = null
        private set

    override fun getAllActive(): Flow<List<Reminder>> =
        items.map { list -> list.filter { it.isActive } }

    override fun getOverdue(): Flow<List<Reminder>> =
        items.map { list -> list.filter { it.isActive } }

    override fun getUpcoming(daysAhead: Int): Flow<List<Reminder>> =
        items.map { list -> list.filter { it.isActive } }

    override fun getByHomeItem(homeItemId: Long): Flow<List<Reminder>> =
        items.map { list -> list.filter { it.homeItemId == homeItemId } }

    override fun searchByText(query: String): Flow<List<Reminder>> =
        items.map { list ->
            list.filter {
                it.title.contains(query, ignoreCase = true) ||
                    (it.description?.contains(query, ignoreCase = true) == true)
            }
        }

    override suspend fun getById(id: Long): Reminder? =
        items.value.find { it.id == id }

    override suspend fun save(reminder: Reminder): Long {
        val id = if (reminder.id == 0L) nextId++ else reminder.id
        val saved = reminder.copy(id = id)
        items.value = items.value.filter { it.id != id } + saved
        return id
    }

    override suspend fun delete(id: Long) {
        items.value = items.value.filter { it.id != id }
    }

    override suspend fun complete(id: Long) {
        lastCompletedId = id
        val reminder = items.value.find { it.id == id } ?: return
        val today = LocalDate(2025, 1, 15)
        val updated = reminder.copy(
            lastCompletedDate = today,
            nextDueDate = today.plus(reminder.intervalDays, DateTimeUnit.DAY)
        )
        items.value = items.value.filter { it.id != id } + updated
    }
}
