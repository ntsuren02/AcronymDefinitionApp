package com.example.acronymdefinitionapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.acronymdefinitionapp.model.Definition
import com.example.acronymdefinitionapp.model.Lf
import com.example.acronymdefinitionapp.network.AcronymRepository
import com.example.acronymdefinitionapp.network.AcronymRepositoryImpl
import com.example.acronymdefinitionapp.utils.BaseViewModel
import com.example.acronymdefinitionapp.utils.RequestState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class AcronymViewModel(
    private val acronymRepository: AcronymRepository = AcronymRepositoryImpl(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BaseViewModel() {

    private val _definition: MutableLiveData<List<Lf>> = MutableLiveData()
    val acronymDefinition: LiveData<List<Lf>> get() = _definition

    var definitions: List<Lf> = mutableListOf()

    fun getViewState(viewIntentRequest: ViewIntentRequest) {
        when (viewIntentRequest) {
            is ViewIntentRequest.SearchAcronym -> {
                handleSearch(viewIntentRequest.acronym)
            }
        }
    }

    private fun handleSearch(acronym: String) {
        viewModelSafeScope.launch(ioDispatcher) {
            acronymRepository.getAcronymDefinition(acronym).collect {
                when (it) {
                    is RequestState.SUCCESS<*> -> {
                        definitions = (it.definitions as List<Definition>)[0].lfs
                        _definition.postValue(definitions)
                    }
                    else -> {
                        _definition.postValue(null)
                    }
                }
            }
        }
    }
}

sealed class ViewIntentRequest {
    class SearchAcronym(val acronym: String) : ViewIntentRequest()
}

