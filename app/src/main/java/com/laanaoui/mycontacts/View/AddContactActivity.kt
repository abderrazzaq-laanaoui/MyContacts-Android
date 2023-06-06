package com.laanaoui.mycontacts.View

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.laanaoui.mycontacts.R
import com.laanaoui.mycontacts.Controller.ContactsController
import com.laanaoui.mycontacts.Controller.dtos.ContactRequest
import com.laanaoui.mycontacts.Model.Contact
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class AddContactActivity : AppCompatActivity() {
	private lateinit var contactsController: ContactsController
	private lateinit var photoImageView: ImageView
	private var photoUri: Uri? = null
	private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
		photoUri = uri
		findViewById<ImageView>(R.id.photoImageView).setImageURI(uri)
	}
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_add_contact)
		contactsController = ContactsController.create()
		val addButton = findViewById<MaterialButton>(R.id.addContactButton)
		val cancelButton = findViewById<MaterialButton>(R.id.cancelButton)
		photoImageView = findViewById(R.id.photoImageView)
		photoImageView.setOnClickListener {
			getContent.launch("image/*")
		}
		addButton.setOnClickListener(addContact())
		cancelButton.setOnClickListener {
			finish()
		}
	}

	private fun addContact(): View.OnClickListener {
		return View.OnClickListener {
			try {
				val firstName = findViewById<TextInputEditText>(R.id.firstNameEditText).text.toString()
				val lastName = findViewById<TextInputEditText>(R.id.lastNameEditText).text.toString()
				val email = findViewById<TextInputEditText>(R.id.emailEditText).text.toString()
				val phone = findViewById<TextInputEditText>(R.id.phoneEditText).text.toString()
				val tag = findViewById<TextInputEditText>(R.id.tagEditText).text.toString()
				val gender = if (findViewById<RadioButton>(R.id.maleRadioButton).isChecked) {
					"male"
				} else {
					"female"
				}
				val contact = ContactRequest(
					email,
					gender,
					tag,
					firstName,
					lastName,
					phone
				);
				contactsController.addContact(contact).enqueue(object : retrofit2.Callback<Contact> {
					override fun onFailure(call: Call<Contact>, t: Throwable) {
						Log.e("AddContactActivity", "Error: ${t.message}", t)
						Toast.makeText(this@AddContactActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
					}
					override fun onResponse(call: Call<Contact>, response: retrofit2.Response<Contact>) {
						Log.i("AddContactActivity", "$response");
						Toast.makeText(this@AddContactActivity, "Contact ${response.body()?.id} created", Toast.LENGTH_SHORT).show()
						if (photoUri != null) {
							val bitmap = photoImageView.drawable.toBitmap()
							val file = File(cacheDir, "photo.jpg")
							file.outputStream().use {
								bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
							}
							val multipart = MultipartBody.Part.createFormData(
								"photo",
								"photo.jpg",
								RequestBody.create(MediaType.parse("image/*"), file))
							contactsController.uploadPhoto(response.body()?.id!!, multipart).enqueue(object : retrofit2.Callback<Contact> {
								override fun onFailure(call: Call<Contact>, t: Throwable) {
									Toast.makeText(this@AddContactActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
								}

								override fun onResponse(
									call: Call<Contact>,
									response: Response<Contact>
								) {
								}
							})
						}
						setResult(Activity.RESULT_OK)
						finish()
					}
				})
			} catch (e: Exception) {
				Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
			}
		}
	}
}