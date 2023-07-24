package com.example.mdev1001_m2023_assignment3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.picasso.Picasso

class MovieDetail : AppCompatActivity() {

    private lateinit var titleText : TextView
    private lateinit var genresText : TextView
    private lateinit var directorText : TextView
    private lateinit var writorText : TextView
    private lateinit var actorsText : TextView
    private lateinit var lengthText : TextView
    private lateinit var mpaRatingText : TextView
    private lateinit var criticsRatingText : TextView
    private lateinit var editTextTextMultiLine : TextView
    private lateinit var yearText : TextView
    private lateinit var posterImageView : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        titleText = findViewById<EditText>(R.id.titleText)
        genresText = findViewById<EditText>(R.id.genresText)
        directorText = findViewById<EditText>(R.id.directorText)
        writorText = findViewById<EditText>(R.id.writorText)
        actorsText = findViewById<EditText>(R.id.actorsText)
        lengthText = findViewById<EditText>(R.id.lengthText)
        mpaRatingText = findViewById<EditText>(R.id.mpaRatingText)
        criticsRatingText = findViewById<EditText>(R.id.criticsRatingText)
        editTextTextMultiLine = findViewById<EditText>(R.id.editTextTextMultiLine)
        yearText = findViewById<EditText>(R.id.yearText)
        posterImageView = findViewById(R.id.imagePoster)

        val jsonString = intent.getStringExtra("DetailKey")
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter<DataClass>(DataClass::class.java!!)
        val movie = jsonAdapter.fromJson(jsonString)

        if (movie != null){
            titleText.text = movie.Title
            genresText.text = movie.Genre
            directorText.text = movie.Director
            writorText.text = movie.Writer
            actorsText.text = movie.Actors
            lengthText.text = movie.Runtime
            mpaRatingText.text = movie.Rated
            criticsRatingText.text = movie.imdbRating
            editTextTextMultiLine.text = movie.Plot
            yearText.text = movie.Released

            if(!movie.Poster.isNullOrEmpty()) {
                Picasso.get()
                    .load(movie.Poster)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(R.drawable.ic_error)
                    .into(posterImageView)
            }
            else
            {
                posterImageView.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        }


    }

    fun onBackClicked(view: View) {
        finish()
    }
}