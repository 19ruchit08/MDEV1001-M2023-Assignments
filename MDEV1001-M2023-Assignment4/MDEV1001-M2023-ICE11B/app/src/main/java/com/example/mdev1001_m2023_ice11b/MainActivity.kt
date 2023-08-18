package com.example.mdev1001_m2023_ice11b

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mdev1001_m2023_ice7b.MovieAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity(), MovieAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieadapter: MovieAdapter
    private lateinit var db: DatabaseReference

    private lateinit var storage: StorageReference
    private var timer: Timer? = null
    private var lastUpdated: Int = 0
    private var movieArray = emptyArray<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                LinearLayoutManager.VERTICAL
            )
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        movieadapter = MovieAdapter(emptyList())
        recyclerView.adapter = movieadapter

        //polling
        lastUpdated = (Date().time / 1000).toInt()
        readData();

//        fetchMovies()
//        startPollingForUpdates()

    }

    private fun readData() {
        val db = Firebase.firestore
        val movies = db.collection("movies")
        var movieList = emptyList<Movie>()
        movies.get().addOnSuccessListener {

            it.forEach { queryDocumentSnapshot ->

                val genres = queryDocumentSnapshot.data["genres"].toString().replace("[","").replace("]","").trim().split(",")
                val directors = queryDocumentSnapshot.data["directors"].toString().replace("[","").replace("]","").trim().split(",")
                val writers = queryDocumentSnapshot.data["writers"].toString().replace("[","").replace("]","").trim().split(",")
                val actors = queryDocumentSnapshot.data["actors"].toString().replace("[","").replace("]","").trim().split(",")

                val movie: Movie = Movie(
                    title = queryDocumentSnapshot.data["title"].toString(),
                    movieID = queryDocumentSnapshot.data["movieID"].toString(),
                    _id = queryDocumentSnapshot.id,
                    documentID = queryDocumentSnapshot.id,
                    studio =  queryDocumentSnapshot.data["studio"].toString(),
                    genres = genres,
                    directors = directors,
                    writers = writers,
                    actors = actors,
                    year = queryDocumentSnapshot.data["year"].toString().toInt(),
                    length = queryDocumentSnapshot.data["length"].toString().toInt(),
                    shortDescription = queryDocumentSnapshot.data["shortDescription"] as String,
                    mpaRating = queryDocumentSnapshot.data["mpaRating"] as String,
                    criticsRating = queryDocumentSnapshot.data["criticsRating"].toString().toDouble(),
                    image = queryDocumentSnapshot.data["image"].toString(),
                )
                movieArray += movie




                Log.d("snapshot: ${it}", movie.title)
            }
            movieadapter = MovieAdapter(movieArray.toList())
            recyclerView.adapter = movieadapter


            val swipeToDeleteCallback = DetectGesture(this@MainActivity, movieadapter)

            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchHelper.attachToRecyclerView(recyclerView)

            movieadapter.setOnItemClickListener(this@MainActivity)
            Log.d("Movie array", movieArray.size.toString())
        }


    }

    private fun fetchMovies() {
        val service = RetrofitClient.retrofit.create(MovieApi::class.java)
        val call = service.getMovies()

        call.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
                if (response.isSuccessful) {
                    val movies = response.body()
                    if (!movies.isNullOrEmpty()) {
                        movieadapter = MovieAdapter(movies)
                        recyclerView.adapter = movieadapter


                        val swipeToDeleteCallback = DetectGesture(this@MainActivity, movieadapter)
                        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
                        itemTouchHelper.attachToRecyclerView(recyclerView)

                        movieadapter.setOnItemClickListener(this@MainActivity)

                    } else {
                        displayErrorMessage("No movies available.")
                    }
                } else {
                    displayErrorMessage("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                displayErrorMessage("Error: ${t.message}")
            }
        })
    }

    private fun displayErrorMessage(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    fun addMovieClicked(view: View) {
        val i = Intent(this@MainActivity, AddOrEditActivity::class.java)
        startActivity(i);
        finish()
    }

    override fun onItemClick(model: Movie) {
        val i = Intent(this@MainActivity, AddOrEditActivity::class.java)

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter<Movie>(Movie::class.java!!)
        val movie = jsonAdapter.toJson(model)
        i.putExtra("AddEditKey", movie)
        startActivity(i);
    }

    private fun startPollingForUpdates() {
        stopPollingForUpdates() // Stop any existing timers

        // Schedule a timer to check for updates every 5 seconds
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                checkForUpdates()
            }
        }, 0, 5000)
    }

    private fun stopPollingForUpdates() {
        timer?.cancel()
        timer = null
    }

    private fun checkForUpdates() {
        val service = RetrofitClient.retrofit.create(MovieApi::class.java)
        service.checkForUpdates().enqueue(object : Callback<UpdatedResponse> {
            override fun onResponse(
                call: Call<UpdatedResponse>,
                response: Response<UpdatedResponse>
            ) {
                if (response.isSuccessful) {
                    val updatedResponse = response.body()
                    if (updatedResponse != null && lastUpdated < updatedResponse.lastUpdated) {
                        lastUpdated = updatedResponse.lastUpdated.toInt()
                        fetchMovies()
                    }
                } else {
                    displayErrorMessage("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UpdatedResponse>, t: Throwable) {
                displayErrorMessage("Error: ${t.message}")
            }
        })
    }

    fun logoutClicked(view: View) {

        //clear auth token
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("AuthToken").apply()

        val i = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(i);
        finish()
    }
}