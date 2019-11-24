package com.example.newsapp.injections.modules

import android.content.Context
import com.example.newsapp.database.AppDataBase
import com.example.newsapp.repositories.NewsRepository
import com.example.newsapp.rest.services.NewsServices
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun getNewsRepository(@com.example.newsapp.injections.AppContext context: Context, newsServices: NewsServices, dataBase: AppDataBase): NewsRepository {
        return NewsRepository(context,newsServices,dataBase)
    }
}