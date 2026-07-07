package com.dfcoding.modelrepocompose.di

import org.koin.core.module.Module
import org.koin.dsl.module


expect val platformModule: Module

//For databases and repositories
val dataModule = module {

}


//new instance every time Koin resolves it. Right for use cases and ViewModels because they're cheap to create and you don't want stale state shared across screens.
val useCasesModule = module {

}


val viewModelModule = module {


}

val networkModule = module {

}

val appModules = listOf(dataModule, viewModelModule,networkModule, useCasesModule)