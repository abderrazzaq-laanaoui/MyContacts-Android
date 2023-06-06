package com.laanaoui.mycontacts.Controller;

import com.laanaoui.mycontacts.Controller.dtos.ContactRequest
import com.laanaoui.mycontacts.Utils.PagedList
import com.laanaoui.mycontacts.Model.Contact
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import rx.schedulers.Schedulers

interface ContactsController {

	companion object {
		fun create(): ContactsController {
			val retrofit = Retrofit.Builder()
				.addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
				.addConverterFactory(GsonConverterFactory.create())
				.baseUrl("http://192.168.1.7:8090/api/collections/")
				.build()
			return retrofit.create(ContactsController::class.java)
		}
	}

	@GET("contacts/records")
	fun getContacts(): Call<PagedList<Contact>>

	@GET("contacts/records/{id}")
	fun getContact(@Path("id") id: String): Call<Contact>

	@POST("contacts/records")
	fun addContact(@Body contact: ContactRequest): Call<Contact>

	@PATCH("contacts/records/{id}")
	@Multipart
	fun uploadPhoto(@Path("id") id: String, @Part photo: MultipartBody.Part): Call<Contact>

	@PATCH("contacts/records/{id}")
	fun updateContact(@Path("id") id: String, @Body contact: ContactRequest): Call<Contact>

	@DELETE("contacts/records/{id}")
	fun deleteContact(@Path("id") id: String): Call<Void>

}
