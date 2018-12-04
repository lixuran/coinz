package com.ilpcoursework.coinz

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_friendinfo.*

class friendinfoActivity : AppCompatActivity() {
    private var firestore: FirebaseFirestore? = null
    private var firestoreChat: DocumentReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friendinfo)
        setSupportActionBar(toolbar)
//        send_message_button.setOnClickListener { _ -> sendMessage() }
//        firestore = FirebaseFirestore.getInstance()
//// Use com.google.rebase.Timestamp objects instead of java.util.Date objects
//        val settings = FirebaseFirestoreSettings.Builder()
//                .setTimestampsInSnapshotsEnabled(true)
//                .build()
//        firestore?.firestoreSettings = settings
//        firestoreChat = firestore?.collection(COLLECTION_KEY)
//                ?.document(DOCUMENT_KEY)
//        realtimeUpdateListener()
        send_message_button.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    companion object {
        private const val TAG = "KommunicatorActivity"
        private const val COLLECTION_KEY = "Chat"
        private const val DOCUMENT_KEY = "Message"
        private const val NAME_FIELD = "Name"
        private const val TEXT_FIELD = "Text"
    }

//    private fun sendMessage() {
//        // create a message of the form f "Name": str1, "Text": str2 g
//        val newMessage = mapOf(
//                NAME_FIELD to name_text.text.toString(),
//                TEXT_FIELD to outgoing_message_text.text.toString())
//// send the message and listen for success or failure
//        firestoreChat?.set(newMessage)
//                ?.addOnSuccessListener { toast("Message sent!") } // anko
//                ?.addOnFailureListener { e -> Log.e(TAG, e.message) }
//    }
//
//    private fun realtimeUpdateListener() {
//        firestoreChat?.addSnapshotListener { documentSnapshot, e ->
//            when {
//                e != null -> Log.e(TAG, e.message)
//                documentSnapshot != null && documentSnapshot.exists()   -> {
//                    with(documentSnapshot) {
//                        val incoming = "${data?.get(NAME_FIELD)}:${data?.get(TEXT_FIELD)}"
//                        incoming_message_text.text = incoming
//                    }
//                }
//            }
//        }
//    }
}
