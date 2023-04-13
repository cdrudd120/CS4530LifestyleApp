package com.example.cs4530lifestyleapp

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class LifestyleApplication: Application() {
    //Get a global scope for all coroutines
    val applicationScope = CoroutineScope(SupervisorJob())

    // Inject scope and application context into singleton database
    val database by lazy{ DatabaseRoom.getDatabase(this,applicationScope)}

    // Inject database dao and scope into singleton repository. Unlike the example from
    // "Android room with a view" codelab (which uses a viewmodelscope in the ViewModel), we
    // maintain a single global scope used for all coroutine operations in the repository and db.
    // If the viewmodel needs to spin up coroutines for some inconceivable reason, you can use
    // viewmodelscope inside the viewmodel
    val repository by lazy{ Repository.getInstance(database.dao(),applicationScope)}
}