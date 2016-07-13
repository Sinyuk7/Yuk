package com.sinyuk.yuk.data.shot;

import com.sinyuk.yuk.AppModule;
import com.sinyuk.yuk.api.ApiComponent;
import com.sinyuk.yuk.api.ApiModule;
import com.sinyuk.yuk.ui.feeds.FeedsFragment;
import com.sinyuk.yuk.utils.scopes.PerActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Sinyuk on 16/7/6.
 */
@Singleton
@Component(modules = {ShotRepositoryModule.class, AppModule.class, ApiModule.class}, dependencies = {})
public interface ShotRepositoryComponent {
    ShotRepository getShotRepository();

    ShotLocalDataSource getShotLocalDataSource();

    ShotRemoteDataSource getShotRemoteDataSource();

    void inject(FeedsFragment target);
}