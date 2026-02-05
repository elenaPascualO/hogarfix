package com.hogarfix.di

import com.hogarfix.data.local.AppDatabase
import com.hogarfix.data.local.getDatabaseBuilder
import com.hogarfix.data.repository.InterventionRepositoryImpl
import com.hogarfix.data.storage.PhotoStorage
import com.hogarfix.data.storage.createPhotoStorage
import com.hogarfix.domain.repository.InterventionRepository
import com.hogarfix.domain.usecase.DeleteInterventionUseCase
import com.hogarfix.domain.usecase.GetInterventionsUseCase
import com.hogarfix.domain.usecase.SaveInterventionUseCase
import com.hogarfix.ui.screens.interventions.InterventionFormViewModel
import com.hogarfix.ui.screens.interventions.InterventionListViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::InterventionListViewModel)
    viewModelOf(::InterventionFormViewModel)
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
}

val useCaseModule = module {
    factoryOf(::GetInterventionsUseCase)
    factoryOf(::SaveInterventionUseCase)
    factoryOf(::DeleteInterventionUseCase)
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
