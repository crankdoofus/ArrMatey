package com.dnfapps.arrmatey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel : ViewModel(), KoinComponent {

    private val _greetingList = MutableStateFlow<List<String>>(listOf())
    val greetingsList: StateFlow<List<String>> get() = _greetingList

    private val greeting: Greeting by inject()

    init {
        viewModelScope.launch {
            greeting.greet().collect { phrase ->
                _greetingList.update { list -> list + phrase }
            }
        }
    }
}