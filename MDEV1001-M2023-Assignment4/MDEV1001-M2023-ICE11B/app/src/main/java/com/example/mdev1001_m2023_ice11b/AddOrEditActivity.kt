package com.example.mdev1001_m2023_ice11b

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.mdev1001_m2023_ice11b.databinding.ActivityAddEditBinding
import com.example.mdev1001_m2023_ice7b.MovieAdapter

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class AddOrEditActivity : AppCompatActivity() {
    //movie fields


    private lateinit var binding: ActivityAddEditBinding
    private var imagetorage = Firebase.storage.reference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private  var currentFile : Uri? = null
    private lateinit var titleText : EditText
    private lateinit var studioText : EditText
    private lateinit var genresText : EditText
    private lateinit var directorText : EditText
    private lateinit var writorText : EditText
    private lateinit var actorsText : EditText
    private lateinit var lengthText : EditText
    private lateinit var mpaRatingText : EditText
    private lateinit var criticsRatingText : EditText
    private lateinit var editTextTextMultiLine : EditText
    private lateinit var yearText : EditText
    private lateinit var imageText : EditText

    private var movie : Movie? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_add_edit)




        var updateButton = findViewById<Button>(R.id.updateButton)
        var addEditText =  findViewById<TextView>(R.id.AddEditText)
        titleText = findViewById<EditText>(R.id.titleText)
        studioText = findViewById<EditText>(R.id.studioText)
        genresText = findViewById<EditText>(R.id.genresText)
        directorText = findViewById<EditText>(R.id.directorText)
        writorText = findViewById<EditText>(R.id.writorText)
        actorsText = findViewById<EditText>(R.id.actorsText)
        lengthText = findViewById<EditText>(R.id.lengthText)
        mpaRatingText = findViewById<EditText>(R.id.mpaRatingText)
        criticsRatingText = findViewById<EditText>(R.id.criticsRatingText)
        editTextTextMultiLine = findViewById<EditText>(R.id.editTextTextMultiLine)
        yearText = findViewById<EditText>(R.id.yearText)
        imageText = findViewById<EditText>(R.id.imageTView)


        //recevie movie
        val jsonString = intent.getStringExtra("AddEditKey")
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter<Movie>(Movie::class.java!!)

        if(!jsonString.isNullOrEmpty()){
            movie = jsonAdapter.fromJson(jsonString)
        }

        if(movie != null){
            updateButton.text = "Update"
            addEditText.text = "Edit Movie"

            titleText.setText(movie?.title)
            studioText.setText(movie?.studio)
            genresText.setText(movie?.genres?.joinToString(separator = ", "))
            directorText.setText(movie?.directors?.joinToString(separator = ", "))
            writorText.setText(movie?.writers?.joinToString(separator = ", "))
            actorsText.setText(movie?.actors?.joinToString(separator = ", "))
            lengthText.setText(movie?.length.toString())
            mpaRatingText.setText(movie?.mpaRating)
            criticsRatingText.setText(movie?.criticsRating.toString())
            editTextTextMultiLine.setText(movie?.shortDescription)
            yearText.setText(movie?.year.toString())
            imageText.setText(movie?.image)

        }
        else{
            updateButton.text = "Add"
            addEditText.text = "Add Movie"
        }


    }

    fun addOrUpdateClicked(view: View) {

        val service = RetrofitClient.retrofit.create(MovieApi::class.java)

        if(movie != null) {
            //do update
            GlobalScope.launch{
                //call put request
                val movieData = Movie(
                    _id = movie?._id!!,
                    movieID = movie?.movieID !!,
                    title= titleText.text.toString(),
                    studio= studioText.text.toString(),
                    genres= genresText.text.toString().split(",") ,
                    directors= directorText.text.toString().split(","),
                    writers=writorText.text.toString().split(","),
                    actors=actorsText.text.toString().split(","),
                    year= Integer.parseInt(yearText.text.toString()),
                    length= Integer.parseInt(lengthText.text.toString()),
                    shortDescription= editTextTextMultiLine.text.toString(),
                    mpaRating= mpaRatingText.text.toString(),
                    criticsRating= criticsRatingText.text.toString().toDouble(),
                    image = imageText.text.toString(),
                    documentID = "")

                val db = Firebase.firestore
                val movies = db.collection("movies")
                movies.get().addOnSuccessListener {

                }
                movies.document(movieData._id.toString()).set(movieData)

            }
        }else{
            //do add

            GlobalScope.launch{
                //call post request

                val movieData = Movie(
                    _id = UUID.randomUUID().toString(),
                    movieID = "",
                    title= titleText.text.toString(),
                    studio= studioText.text.toString(),
                    genres= genresText.text.toString().split(",") ,
                    directors= directorText.text.toString().split(","),
                    writers=writorText.text.toString().split(","),
                    actors=actorsText.text.toString().split(","),
                    year= Integer.parseInt(yearText.text.toString()),
                    length= Integer.parseInt(lengthText.text.toString()),
                    shortDescription= editTextTextMultiLine.text.toString(),
                    mpaRating= mpaRatingText.text.toString(),
                    criticsRating= criticsRatingText.text.toString().toDouble(),
                    image = imageText.text.toString(),
                    documentID = "")

                val db = Firebase.firestore
                val movies = db.collection("movies")
//                movies.get().addOnSuccessListener {
//
//                }
                movies.add(movieData)


//                service.addMovie(movieData).enqueue(object : Callback<Void> {
//                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
//                        if(response.isSuccessful){
//                            displayErrorMessage("moive Added" , "Success")
//                        }
//                        else{
//                            displayErrorMessage("moive not Added" , "Fail")
//
//                        }
//                    }
//
//                    override fun onFailure(call: Call<Void>, t: Throwable) {
//                        displayErrorMessage("Error- " + t.message , "Error")
//                    }
//                })
            }
        }

        //done with add/edit go back
        val i = Intent(this@AddOrEditActivity, MainActivity::class.java)
        startActivity(i)
        finish()
    }
    fun cancelClicked(view: View) {
        val i = Intent(this@AddOrEditActivity, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun displayErrorMessage(message: String, title: String) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}