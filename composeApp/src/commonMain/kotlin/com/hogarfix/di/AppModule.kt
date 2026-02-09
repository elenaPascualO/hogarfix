package com.hogarfix.di

import com.hogarfix.data.local.AppDatabase
import com.hogarfix.data.local.getDatabaseBuilder
import com.hogarfix.data.repository.HomeItemRepositoryImpl
import com.hogarfix.data.repository.InterventionRepositoryImpl
import com.hogarfix.data.repository.ProfessionalRepositoryImpl
import com.hogarfix.data.repository.ReminderRepositoryImpl
import com.hogarfix.data.storage.PhotoStorage
import com.hogarfix.data.storage.createPhotoStorage
import com.hogarfix.domain.repository.HomeItemRepository
import com.hogarfix.domain.repository.InterventionRepository
import com.hogarfix.domain.repository.ProfessionalRepository
import com.hogarfix.domain.repository.ReminderRepository
import com.hogarfix.domain.usecase.CompleteReminderUseCase
import com.hogarfix.domain.usecase.DeleteHomeItemUseCase
import com.hogarfix.domain.usecase.DeleteInterventionUseCase
import com.hogarfix.domain.usecase.DeleteProfessionalUseCase
import com.hogarfix.domain.usecase.DeleteReminderUseCase
import com.hogarfix.domain.usecase.GetHomeItemsUseCase
import com.hogarfix.domain.usecase.GetInterventionsUseCase
import com.hogarfix.domain.usecase.GetProfessionalsUseCase
import com.hogarfix.domain.usecase.GetRemindersUseCase
import com.hogarfix.domain.usecase.SaveHomeItemUseCase
import com.hogarfix.domain.usecase.SaveInterventionUseCase
import com.hogarfix.domain.usecase.SaveProfessionalUseCase
import com.hogarfix.domain.usecase.SaveReminderUseCase
import com.hogarfix.ui.screens.home.HomeViewModel
import com.hogarfix.ui.screens.interventions.InterventionFormViewModel
import com.hogarfix.ui.screens.interventions.InterventionListViewModel
import com.hogarfix.ui.screens.inventory.HomeItemFormViewModel
import com.hogarfix.ui.screens.inventory.HomeItemListViewModel
import com.hogarfix.ui.screens.professionals.ProfessionalFormViewModel
import com.hogarfix.ui.screens.professionals.ProfessionalListViewModel
import com.hogarfix.ui.screens.reminders.ReminderFormViewModel
import com.hogarfix.ui.screens.reminders.ReminderListViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::InterventionListViewModel)
    viewModelOf(::InterventionFormViewModel)
    viewModelOf(::HomeItemListViewModel)
    viewModelOf(::HomeItemFormViewModel)
    viewModelOf(::ProfessionalListViewModel)
    viewModelOf(::ProfessionalFormViewModel)
    viewModelOf(::ReminderListViewModel)
    viewModelOf(::ReminderFormViewModel)
}

val dataModule = module {
    single {
        getDatabaseBuilder()
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
    single { get<AppDatabase>().interventionDao() }
    single { get<AppDatabase>().homeItemDao() }
    single { get<AppDatabase>().professionalDao() }
    single { get<AppDatabase>().reminderDao() }
    single<PhotoStorage> { createPhotoStorage() }
}

val repositoryModule = module {
    singleOf(::InterventionRepositoryImpl) bind InterventionRepository::class
    singleOf(::HomeItemRepositoryImpl) bind HomeItemRepository::class
    singleOf(::ProfessionalRepositoryImpl) bind ProfessionalRepository::class
    singleOf(::ReminderRepositoryImpl) bind ReminderRepository::class
}

val useCaseModule = module {
    factoryOf(::GetInterventionsUseCase)
    factoryOf(::SaveInterventionUseCase)
    factoryOf(::DeleteInterventionUseCase)
    factoryOf(::GetHomeItemsUseCase)
    factoryOf(::SaveHomeItemUseCase)
    factoryOf(::DeleteHomeItemUseCase)
    factoryOf(::GetProfessionalsUseCase)
    factoryOf(::SaveProfessionalUseCase)
    factoryOf(::DeleteProfessionalUseCase)
    factoryOf(::GetRemindersUseCase)
    factoryOf(::SaveReminderUseCase)
    factoryOf(::DeleteReminderUseCase)
    factoryOf(::CompleteReminderUseCase)
}

fun appModules(): List<Module> = listOf(
    appModule,
    dataModule,
    repositoryModule,
    useCaseModule
)

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModules())
    }
}
