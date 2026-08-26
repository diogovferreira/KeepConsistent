package com.dfcoding.keepconsistent.di

import TaskRepositoryImpl
import com.dfcoding.keepconsistent.data.auth.SupabaseConfig
import com.dfcoding.keepconsistent.data.local.KeepConsistentDataSource
import com.dfcoding.keepconsistent.data.repository.AuthRepository
import com.dfcoding.keepconsistent.data.repository.AuthRepositoryImpl
import com.dfcoding.keepconsistent.data.repository.OnBoardingRepository
import com.dfcoding.keepconsistent.data.repository.OnBoardingRepositoryImpl
import com.dfcoding.keepconsistent.data.repository.TaskRepository
import com.dfcoding.keepconsistent.database.ConsistentDatabase
import com.dfcoding.keepconsistent.ui.addtask.AddTaskScreenViewModel
import com.dfcoding.keepconsistent.ui.home.HomeScreenViewModel
import com.dfcoding.keepconsistent.ui.login.LoginViewModel
import com.dfcoding.keepconsistent.ui.login.forgotpassword.ForgotPasswordScreenViewModel
import com.dfcoding.keepconsistent.ui.login.signup.SignUpViewModel
import com.dfcoding.keepconsistent.ui.login.updatepassword.UpdatePasswordScreenViewModel
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.compose.auth.ComposeAuth
import io.github.jan.supabase.compose.auth.googleNativeLogin
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import org.koin.core.module.Module
import org.koin.dsl.module


expect val platformModule: Module

//For databases and repositories
val dataModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<OnBoardingRepository> { OnBoardingRepositoryImpl(get()) }
    single<TaskRepository> { TaskRepositoryImpl(get()) }
    single { ConsistentDatabase(get()) }
    single { KeepConsistentDataSource(get()) }
}


//new instance every time Koin resolves it. Right for use cases and ViewModels because they're cheap to create and you don't want stale state shared across screens.
val useCasesModule = module {

}


val viewModelModule = module {
    factory { LoginViewModel(get()) }
    factory { SignUpViewModel(get()) }
    factory { ForgotPasswordScreenViewModel(get()) }
    factory { UpdatePasswordScreenViewModel(get()) }
    factory { AddTaskScreenViewModel(get()) }
    factory { HomeScreenViewModel(get()) }
}

val networkModule = module {
    single {
        createSupabaseClient(
            supabaseUrl = SupabaseConfig.URL,
            supabaseKey = SupabaseConfig.ANON_KEY
        ) {
            install(Auth) {
                host = "auth-callback"
                scheme = "keepconsistent"
            }
            install(ComposeAuth) {
                googleNativeLogin(serverClientId = SupabaseConfig.GOOGLE_WEB_CLIENT_ID)
            }
            install(Postgrest)
        }
    }
}

val appModules = listOf(dataModule, viewModelModule, networkModule, useCasesModule,platformModule)