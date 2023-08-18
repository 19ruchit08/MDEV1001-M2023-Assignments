package com.example.mdev1001_m2023_ice11b

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.mdev1001_m2023_ice11b.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var firebaseauth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseauth = FirebaseAuth.getInstance()


        binding.registeredButton.setOnClickListener{

            val username =binding.usernameEditText.text.toString()
            val password =binding.passwordEditText.text.toString()

            val db = FirebaseFirestore.getInstance()
            val docRef = db.collection("usernames").document(username)


            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val email = document.getString("email")
                        if (email != null) {
                            firebaseauth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        println("User logged in successfully.")
                                        val intent = Intent(this,MainActivity::class.java)
                                        startActivity(intent)
                                        // Perform your actions after successful login
                                    } else {
                                        println("Login failed: ${task.exception?.localizedMessage}")
                                        displayErrorMessage("Authentication Failed")
                                    }
                                }
                        } else {
                            println("Email not found for the provided username.")
                            displayErrorMessage("Authentication Failed")
                        }
                    } else {
                        println("Username not found.")
                        displayErrorMessage("Authentication Failed")
                    }
                }
                .addOnFailureListener { e ->
                    println("Error retrieving document: $e")
                    displayErrorMessage("Authentication Failed")
                }
        }
          /*  if( username.isNotEmpty() && password.isNotEmpty())
            {
                firebaseauth.signInWithEmailAndPassword(username, password).addOnCompleteListener{
                    if (it.isSuccessful){
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }


            }else{
                Toast.makeText(this, "Feild cannot be Empty", Toast.LENGTH_SHORT).show()
            }

        }*/


        binding.registerRedirectText.setOnClickListener{
            val intent = Intent(this,Register::class.java)
            startActivity(intent)
        }
    }
    private fun displayErrorMessage(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(message)

        builder.show()
    }

}