package com.ilpcoursework.coinz.activities

import android.Manifest.permission.READ_CONTACTS
import android.app.LoaderManager.LoaderCallbacks
import android.content.CursorLoader
import android.content.Intent
import android.content.Loader
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.ilpcoursework.coinz.DAO.User
import com.ilpcoursework.coinz.LoginActivity
import com.ilpcoursework.coinz.R
import kotlinx.android.synthetic.main.activity_signup2.*
import java.util.*

@Suppress("DEPRECATION")
/**
 * A login screen that offers login via email/password.
 */
class SignupActivity : AppCompatActivity(), LoaderCallbacks<Cursor> {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private var mAuth: FirebaseAuth? = null
    private val tag = "SignupActivity"
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup2)
        // Set up the login form.
        mAuth = FirebaseAuth.getInstance()

        populateAutoComplete()

        //set button listener
        email_sign_in_button.setOnClickListener { switchToSignin() }
        sign_up_button.setOnClickListener{ _ -> signup()}

    }

    private fun switchToSignin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun signup() {
        // Reset errors.
        email.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()
        val usernameStr = username.text.toString()
        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            email.error = getString(R.string.error_field_required)
            focusView = email
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            email.error = getString(R.string.error_invalid_email)
            focusView = email
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            db.collection("users").whereEqualTo("username", usernameStr).get().addOnCompleteListener { task->
                if(task.isSuccessful){
                    // is the username registered?
                    if(task.result?.isEmpty!!){
                        Toast.makeText(this, "signing up",
                                Toast.LENGTH_SHORT).show()
                        email_sign_in_button.visibility = View.GONE
                        sign_up_button.visibility = View.GONE

                        mAuth?.createUserWithEmailAndPassword(emailStr, passwordStr)
                                ?.addOnCompleteListener(this) {task->
                                    //add user to authentication
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(tag, "createUserWithEmail:success")
                                        val userstore = User(usernameStr, emailStr)
                                        // Add a new document with the user email
                                        db.collection("users")
                                                .document(emailStr).set(userstore)
                                                .addOnSuccessListener {
                                                    Log.d(tag, "DocumentSnapshot added with ID: $usernameStr")
                                                }
                                                .addOnFailureListener(this) {
                                                    Log.w(tag, "Error adding document", it)
                                                }
                                        val user = mAuth?.currentUser
                                        updateUI(user)

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(tag, "createUserWithEmail:failure", task.exception)
                                        Toast.makeText(this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show()
                                        updateUI(null)
                                    }


                                }
                    }
                    else{
                        username.error="username already taken"
                    }
                }

            }
        }
    }

    /**
     * decide whether to stay in the current activity or go to welcoming activity
     * based on the provided user
     */
    private fun updateUI( user: FirebaseUser?) {
        if(user==null){
            // stay in current activity if the user is null
            //show the buttons
            email_sign_in_button.visibility = View.VISIBLE
            sign_up_button.visibility = View.VISIBLE
        }
        else{
            //go to welcoming activity if successfully signed up
            val intent = Intent(this, WelcomingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun populateAutoComplete() {
        if (!mayRequestContacts()) {
            return
        }
        loaderManager.initLoader(0, null, this)
    }

    private fun mayRequestContacts(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(email, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok,
                            { requestPermissions(arrayOf(READ_CONTACTS), REQUEST_READ_CONTACTS) })
        } else {
            requestPermissions(arrayOf(READ_CONTACTS), REQUEST_READ_CONTACTS)
        }
        return false
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete()
            }
        }
    }
    //check for email validity
    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }
    //check for pwd validity
    private fun isPasswordValid(password: String): Boolean =
            password.length > 4 && password.length <30

    //---- override loader functionalities ----

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<Cursor> {
        return CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?", arrayOf(ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE),

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC")
    }

    override fun onLoadFinished(cursorLoader: Loader<Cursor>, cursor: Cursor) {
        val emails = ArrayList<String>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS))
            cursor.moveToNext()
        }
        addEmailsToAutoComplete(emails)
    }

    override fun onLoaderReset(cursorLoader: Loader<Cursor>) {

    }

    private fun addEmailsToAutoComplete(emailAddressCollection: List<String>) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        val adapter = ArrayAdapter(this@SignupActivity,
                android.R.layout.simple_dropdown_item_1line, emailAddressCollection)

        email.setAdapter(adapter)
    }

    object ProfileQuery {
        val PROJECTION = arrayOf(
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY)
        val ADDRESS = 0
    }



    companion object {

        /**
         * Id to identity READ_CONTACTS permission request.
         */
        private const val REQUEST_READ_CONTACTS = 0

    }
}
