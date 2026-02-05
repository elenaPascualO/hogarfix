package com.hogarfix.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val appModule = module {
    // ViewModels will be added here in Phase 2+
    // Example: viewModelOf(::InterventionListViewModel)
}

val dataModule = module {
    // Database and DAOs will be added here
    // Example: singleOf(::AppDatabase)
    // Example: single { get<AppDatabase>().interventionDao() }
}

val repositoryModule = module {
    // Repositories will be added here
    // Example: singleOf(::InterventionRepositoryImpl) { bind<InterventionRepository>() }
}

val useCaseModule = module {
    // UseCases will be added here
    // Example: factoryOf(::GetInterventionsUseCase)
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