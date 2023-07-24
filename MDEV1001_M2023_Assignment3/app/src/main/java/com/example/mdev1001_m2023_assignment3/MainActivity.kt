package com.example.mdev1001_m2023_assignment3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val APIKEY = "90782399"

class MainActivity : AppCompatActivity(), MovieAdapter.OnItemClickListener  {

    private lateinit var recyclerView: RecyclerView
    private lateinit var movieadapter: MovieAdapter
    private lateinit var moviesearchText: EditText
    private lateinit var MovieService : MovieApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moviesearchText = findViewById(R.id.searchBar)

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


        MovieService = Api.retrofit.create(MovieApiService::class.java)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun onSearchPressed(view: View) {

        if (moviesearchText.toString().isEmpty() || moviesearchText.text.isBlank()) {
            displayErrorMessage("Search field cannot be empty!")
            return
        }
        var requiredMovieListToShow = mutableListOf<DataClass>()

        GlobalScope.launch(Dispatchers.Main) {
            try {

                val call = MovieService.searchMovies(APIKEY, moviesearchText.text.toString())

                call.enqueue(object : Callback<MovieSearch> {
                    override fun onResponse(
                        call: Call<MovieSearch>,
                        response: Response<MovieSearch>
                    ) {
                        if (response.isSuccessful) {
                            val searchResponse: MovieSearch? = response.body()
                            searchResponse?.let {
                                val movies: List<Search> = it.Search!!
                                if (!movies.isNullOrEmpty()) {
                                    for (m in movies) {
                                        MovieService.MovieById(APIKEY, m.imdbId.toString()).enqueue(object : Callback<DataClass>{
                                            override fun onResponse(
                                                call: Call<DataClass>,
                                                response: Response<DataClass>
                                            ) {
                                                if (response.isSuccessful) {
                                                    val movieResponse: DataClass? = response.body()
                                                    movieResponse?.let {m ->
                                                        requiredMovieListToShow.add(m)
                                                    }

                                                    if (requiredMovieListToShow.size > 0) {
                                                        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                                                        movieadapter = MovieAdapter(requiredMovieListToShow)
                                                        recyclerView.adapter = movieadapter
                                                        //set on item click event to the recycler adapter
                                                        movieadapter.setOnItemClickListener(this@MainActivity)
                                                    }
                                                }
                                            }

                                            override fun onFailure(call: Call<DataClass>, t: Throwable) {
                                                displayErrorMessage(t.message.toString())
                                                println(t.message)
                                            }

                                        })
                                    }
                                }
                            }
                        } else {
                            displayErrorMessage("Something went wrong, please try again")
                        }
                    }

                    override fun onFailure(call: Call<MovieSearch>, t: Throwable) {
                        displayErrorMessage(t.message.toString())
                        println(t.message)
                    }
                })

            } catch (e: Exception) {
                displayErrorMessage(e.message.toString())
            }

        }


    }


    private fun displayErrorMessage(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onItemClick(model: DataClass) {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter<DataClass>(DataClass::class.java!!)
        val movie = jsonAdapter.toJson(model)

        val i = Intent(this@MainActivity, MovieDetail::class.java)
        i.putExtra("DetailKey", movie)
        startActivity(i);
    }
}