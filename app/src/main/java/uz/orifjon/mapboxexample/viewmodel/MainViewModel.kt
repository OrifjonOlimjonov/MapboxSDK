package uz.orifjon.mapboxexample.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.orifjon.mapboxexample.models.UserLocation

class MainViewModel : ViewModel() {


    companion object val list: MutableLiveData<UserLocation> = MutableLiveData()

    fun setValue(location: UserLocation) {
        list.postValue(location)
    }

    fun getValue(): LiveData<UserLocation> {
        return list
    }


}