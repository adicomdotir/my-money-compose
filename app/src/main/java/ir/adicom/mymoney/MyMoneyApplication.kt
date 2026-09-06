package ir.adicom.mymoney


import android.app.Application
import ir.adicom.mymoney.di.AppContainer

class MyMoneyApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}