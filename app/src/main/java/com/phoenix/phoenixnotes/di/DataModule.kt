package com.phoenix.phoenixnotes.di

import com.phoenix.phoenixnotes.data.DataRepository
import com.phoenix.phoenixnotes.data.DataRepositorySource
import com.phoenix.phoenixnotes.data.fake.FakeData
import com.phoenix.phoenixnotes.data.fake.FakeDataSource
import com.phoenix.phoenixnotes.data.local.LocalData
import com.phoenix.phoenixnotes.data.local.LocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindsFakeData(fakeData: FakeData): FakeDataSource

    @Binds
    abstract fun bindsLocalData(localData: LocalData): LocalDataSource

    @Binds
    abstract fun bindsDataRepository(dataRepository: DataRepository): DataRepositorySource


}