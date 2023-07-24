package com.example.mdev1001_m2023_assignment3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class MovieAdapter(private var movies: List<DataClass>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

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
        fun onItemClick(model: DataClass)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(movie: DataClass) {
            itemView.findViewById<TextView>(R.id.titleTextView).text = movie.Title
            itemView.findViewById<TextView>(R.id.directorText).text = movie.Director
            itemView.findViewById<TextView>(R.id.YearTextView).text = movie.Year
            var rating = itemView.findViewById<TextView>(R.id.ratingTextView)
            rating.text = movie.imdbRating


            if ((movie.imdbRating)!!.toDouble() > 7) {
                rating.backgroundTintList =
                    itemView.context.resources.getColorStateList(R.color.green, null)
                rating.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            } else if (movie.imdbRating!!.toDouble() > 5) {
                rating.backgroundTintList =
                    itemView.context.resources.getColorStateList(R.color.yellow, null)
                rating.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            } else {
                rating.backgroundTintList =
                    itemView.context.resources.getColorStateList(R.color.red, null)
                rating.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            }

            // Bind other views as needed
        }

    }
}