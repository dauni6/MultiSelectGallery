package com.dontsu.multiselectgallery

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhotoViewModel : ViewModel() {

    val photoList = MutableLiveData<String>()

    init {
        photoList.value = "0/10"
    }
}