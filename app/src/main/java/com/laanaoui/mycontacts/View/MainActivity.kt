package com.laanaoui.mycontacts.View

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.laanaoui.mycontacts.R
import com.laanaoui.mycontacts.Controller.ContactsController
import com.laanaoui.mycontacts.Utils.PagedList
import com.laanaoui.mycontacts.Model.Contact
import com.laanaoui.mycontacts.Utils.ContactAdapter
import com.laanaoui.mycontacts.Utils.ContactReadModel
import retrofit2.Call
import retrofit2.Response

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
	private lateinit var addContactFab: FloatingActionButton
	private lateinit var contactsController: ContactsController
	private lateinit var contactsView: RecyclerView
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		contactsController = ContactsController.create()
		addContactFab = findViewById(R.id.addContactFab)
		addContactFab.setOnClickListener(openAddContactActivity())
		contactsView = findViewById(R.id.contactRecyclerView)
		contactsView.layoutManager = LinearLayoutManager(this)
		displayContacts()
	}

	private fun displayContacts() {
		contactsController.getContacts().enqueue(object : retrofit2.Callback<PagedList<Contact>> {
			override fun onResponse(
				call: Call<PagedList<Contact>>,
				response: Response<PagedList<Contact>>
			) {
				if (response.isSuccessful) {
					val contacts = response.body()
					val contactsList: List<ContactReadModel> = contacts!!.items.map { s ->
						ContactReadModel(s)
					}
					val adapter = ContactAdapter(contactsList)
					adapter.contactsChanged = {
						displayContacts()
					}
					contactsView.adapter = adapter
				} else {
					Log.e("MainActivity", "Error getting contacts"+response.errorBody().toString())
					Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG).show()
				}
			}

			override fun onFailure(call: Call<PagedList<Contact>>, t: Throwable) {
				Log.e("MainActivity", "Error getting contacts", t)
				Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG).show()
			}

		})
	}

	private fun openAddContactActivity(): View.OnClickListener {
		return View.OnClickListener {
			val intent = Intent(this, AddContactActivity::class.java)
			startActivityForResult(intent, 1)
		}
	}
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == 1) {
			if (resultCode == Activity.RESULT_OK) {
				Thread.sleep(2000)
				displayContacts()
			}
		}
	}
}