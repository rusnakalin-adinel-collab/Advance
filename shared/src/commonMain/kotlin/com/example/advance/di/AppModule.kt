package com.example.advance.di


import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.example.advance.data.posts.service.AppPostApiService
import com.example.advance.data.posts.service.PostApiService
import com.example.advance.data.posts.AppPostRepository
import com.example.advance.domain.posts.create.CreatePostUseCase
import com.example.advance.domain.posts.edit.EditPostUseCase
import com.example.advance.domain.posts.obtain.ObtainPostsUseCase
import com.example.advance.domain.posts.remove.RemovePostUseCase
import com.example.advance.domain.posts.PostRepository
import com.example.advance.presentation.AppViewModel

val networkModule = module {
    single {
        HttpClient {
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println("KtorLogger: $message")
                    }
                }
            }
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    singleOf(::AppPostApiService) { bind<PostApiService>() }
}
val appModule = module {
    includes(networkModule)
    singleOf(::AppPostRepository) { bind<PostRepository>() }
    factoryOf(::CreatePostUseCase)
    factoryOf(::EditPostUseCase)
    factoryOf(::ObtainPostsUseCase)
    factoryOf(::RemovePostUseCase)
    viewModelOf(::AppViewModel)
}