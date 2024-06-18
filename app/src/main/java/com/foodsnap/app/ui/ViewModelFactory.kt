package com.foodsnap.app.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.foodsnap.app.data.Repository
import com.foodsnap.app.di.Injection
import com.foodsnap.app.ui.detail.FoodDetailViewModel
import com.foodsnap.app.ui.history.HistoryViewModel
import com.foodsnap.app.ui.image.ImageViewModel
import com.foodsnap.app.ui.login.LoginViewModel
import com.foodsnap.app.ui.main.MainViewModel
import com.foodsnap.app.ui.settings.changepassword.ChangePasswordViewModel
import com.foodsnap.app.ui.settings.editprofile.EditProfileViewModel
import com.foodsnap.app.ui.signup.SignUpViewModel
import com.foodsnap.app.ui.splashscreen.SplashScreenViewModel

class ViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            (modelClass.isAssignableFrom(MainViewModel::class.java)) -> {
                MainViewModel(repository) as T
            }

            (modelClass.isAssignableFrom(SplashScreenViewModel::class.java)) -> {
                SplashScreenViewModel(repository) as T
            }

            (modelClass.isAssignableFrom(SignUpViewModel::class.java)) -> {
                SignUpViewModel(repository) as T
            }

            (modelClass.isAssignableFrom(LoginViewModel::class.java)) -> {
                LoginViewModel(repository) as T
            }

            (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) -> {
                EditProfileViewModel(repository) as T
            }

            (modelClass.isAssignableFrom(ChangePasswordViewModel::class.java)) -> {
                ChangePasswordViewModel(repository) as T
            }

            (modelClass.isAssignableFrom(ImageViewModel::class.java)) -> {
                ImageViewModel(repository) as T
            }

            (modelClass.isAssignableFrom(FoodDetailViewModel::class.java)) -> {
                FoodDetailViewModel(repository) as T
            }

            (modelClass.isAssignableFrom(HistoryViewModel::class.java)) -> {
                HistoryViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}