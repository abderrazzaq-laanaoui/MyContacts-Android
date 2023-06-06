package com.laanaoui.mycontacts.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.laanaoui.mycontacts.R
import com.squareup.picasso.Picasso
import com.laanaoui.mycontacts.Controller.ContactsController
import com.laanaoui.mycontacts.Controller.dtos.ContactRequest
import com.laanaoui.mycontacts.Model.Contact
import com.laanaoui.mycontacts.Utils.ContactReadModel
import retrofit2.Call
import retrofit2.Response

class UpdateContactActivity : AppCompatActivity() {

	private lateinit var contactsController: ContactsController
	private lateinit var contact: ContactReadModel
	private lateinit var contactFirstNameTextInputEditText: TextInputEditText
	private lateinit var contactLastNameTextInputEditText: TextInputEditText
	private lateinit var contactEmailTextInputEditText: TextInputEditText
	private lateinit var contactPhoneTextInputEditText: TextInputEditText
	private lateinit var tagTextInputEditText: TextInputEditText
	private lateinit var contactMaleRadioButton: RadioButton
	private lateinit var contactFemaleRadioButton: RadioButton
	private lateinit var contactGLSIDRadioButton: RadioButton
	private lateinit var contactIIBDCCRadioButton: RadioButton
	private lateinit var contactPhotoImageView: ImageView
	private lateinit var cancelButton: MaterialButton
	private lateinit var updateButton: MaterialButton

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_update_contact)
		contactsController = ContactsController.create()

		contactFirstNameTextInputEditText = findViewById(R.id.firstNameEditText)
		contactLastNameTextInputEditText = findViewById(R.id.lastNameEditText)
		contactEmailTextInputEditText = findViewById(R.id.emailEditText)
		contactPhoneTextInputEditText = findViewById(R.id.phoneEditText)
		contactMaleRadioButton = findViewById(R.id.maleRadioButton)
		contactFemaleRadioButton = findViewById(R.id.femaleRadioButton)
		contactPhotoImageView = findViewById(R.id.photoImageView)
		tagTextInputEditText = findViewById(R.id.tagEditText)
		cancelButton = findViewById(R.id.cancelButton)
		updateButton = findViewById(R.id.updateButton)

		cancelButton.setOnClickListener {
			finish()
		}

		updateButton.setOnClickListener { updateContact() }

		val contactId = intent.getStringExtra("contactId")
		if (contactId != null) {
			contactsController.getContact(contactId).enqueue(object : retrofit2.Callback<Contact> {
				override fun onResponse(call: Call<Contact>, response: Response<Contact>) {
					if (response.isSuccessful) {
						contact = ContactReadModel(response.body()!!)
						contactFirstNameTextInputEditText.setText(contact.firstName)
						contactLastNameTextInputEditText.setText(contact.lastName)
						contactEmailTextInputEditText.setText(contact.emailAddress)
						contactPhoneTextInputEditText.setText(contact.phoneNumber)
						tagTextInputEditText.setText(contact.tag)
						if (contact.gender == "male") {
							contactMaleRadioButton.isChecked = true
						} else {
							contactFemaleRadioButton.isChecked = true
						}
						Log.i("UpdateContactActivity", contact.photoUrl.toString())
						if(contact.photoUrl.endsWith(".jpg") || contact.photoUrl.endsWith(".png"))
						 	Picasso.get().load(contact.photoUrl).into(contactPhotoImageView)
					}
				}
				override fun onFailure(call: Call<Contact>, t: Throwable) {
					Log.d("UpdateContactActivity", t.message.toString())
					Toast.makeText(this@UpdateContactActivity, "Error", Toast.LENGTH_LONG).show()
				}
			})
		} else {
			finish()
		}
	}

	private fun updateContact() {
		val firstName = contactFirstNameTextInputEditText.text.toString()
		val lastName = contactLastNameTextInputEditText.text.toString()
		val email = contactEmailTextInputEditText.text.toString()
		val phone = contactPhoneTextInputEditText.text.toString()
		val tag = tagTextInputEditText.text.toString()
		val gender = if (contactMaleRadioButton.isChecked) {
			"male"
		} else {
			"female"
		}

		val updatedContact = ContactRequest(email, gender, tag, firstName, lastName, phone)
		contactsController.updateContact(contact.id, updatedContact).enqueue(object : retrofit2.Callback<Contact> {
			override fun onResponse(call: Call<Contact>, response: Response<Contact>) {
				if (response.isSuccessful) {
					Toast.makeText(this@UpdateContactActivity, "Contact updated", Toast.LENGTH_LONG).show()
					finish()
				}
			}
			override fun onFailure(call: Call<Contact>, t: Throwable) {
				Toast.makeText(this@UpdateContactActivity, "Error", Toast.LENGTH_LONG).show()
			}
		})
	}
}