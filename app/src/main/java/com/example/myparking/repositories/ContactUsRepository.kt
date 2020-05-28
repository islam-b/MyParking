package com.example.myparking.repositories


import androidx.lifecycle.MutableLiveData
import com.example.myparking.models.ContactUsModel
import com.example.myparking.services.ContactUsService
import com.example.myparking.utils.InjectorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ContactUsRepository {

    var service: ContactUsService

    init {
        service = InjectorUtils.privideContactUsService()
    }


    fun sendMessage(automobilisteId: Int,objet:String,message:String): MutableLiveData<Pair<Boolean,String>> {
        var data = MutableLiveData<Pair<Boolean,String>>(null)
        val request = ContactUsModel(automobilisteId,objet,message)
        service.sendMessage(request).enqueue(object : Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable) {
                data.postValue(Pair(false, "Une erreur s'est produite. Veuillez réessayez."))
            }

            override fun onResponse(
                call: Call<Any>,
                response: Response<Any>
            ) {
                if (response.code()==200 || response.code()==201) {
                    data.postValue(Pair(true, "Message envoyé avec succés."))

                } else {
                    data.postValue(Pair(false, "Une erreur s'est produite. Veuillez réessayez."))

                }
            }
        })
        return data
    }


    companion object {
        @Volatile
        private var instance: ContactUsRepository? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ContactUsRepository().also { instance = it }
            }
    }
}