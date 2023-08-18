package com.example.mdev1001_m2023_ice7b

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mdev1001_m2023_ice11b.Movie
import com.example.mdev1001_m2023_ice11b.R
import com.squareup.picasso.Picasso

class MovieAdapter(private var movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieAdapter.ViewHolder {

        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MovieAdapter.ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
        holder.itemView.findViewById<ConstraintLayout>(R.id.rowLayout).setOnClickListener {
            onItemClickListener?.onItemClick(movie)
        }
    }

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(model: Movie)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }
    override fun getItemCount(): Int {
        return movies.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(movie: Movie) {
            itemView.findViewById<TextView>(R.id.titleTextView).text = movie.title
            itemView.findViewById<TextView>(R.id.studioTextView).text = movie.studio
            var rating = itemView.findViewById<TextView>(R.id.ratingTextView)
            rating.text = movie.criticsRating.toString()


            if (movie.criticsRating > 7) {
                rating.backgroundTintList =
                    itemView.context.resources.getColorStateList(R.color.green, null)
                rating.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            } else if (movie.criticsRating > 5) {
                rating.backgroundTintList =
                    itemView.context.resources.getColorStateList(R.color.yellow, null)
                rating.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            } else {
                rating.backgroundTintList =
                    itemView.context.resources.getColorStateList(R.color.red, null)
                rating.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            }

            val imageView = itemView.findViewById<ImageView>(R.id.imageView)
            try {
                Picasso.get()
                    .load(movie.image) // Replace 'movie.image' with your actual image URL
                    .into(imageView)
            } catch (e: Exception) {
                Log.e("PicassoError", e.message, e)
            }
        }

    }

    fun getItem(position: Int): Movie {
        return movies[position]
    }

    fun removeItem(_id: String) {
        movies = movies.filter{ it._id !=  _id}
    }
}