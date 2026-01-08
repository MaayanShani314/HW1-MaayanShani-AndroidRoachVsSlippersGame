package com.example.progect1_game.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.progect1_game.R
import com.example.progect1_game.interfaces.CallbackTopTenClicked
import com.example.progect1_game.logic.Score

class TopTenAdapter(
    private val scores: List<Score>,
    private val callback: CallbackTopTenClicked?
) : RecyclerView.Adapter<TopTenAdapter.ScoreViewHolder>() {

    inner class ScoreViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val lblRank: TextView =
            itemView.findViewById(R.id.item_LBL_rank)

        val lblName: TextView =
            itemView.findViewById(R.id.item_LBL_name)

        val lblScore: TextView =
            itemView.findViewById(R.id.item_LBL_score)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_top_ten, parent, false)
        return ScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val score = scores[position]

        holder.lblRank.text = "(${position + 1})"

        holder.lblName.text = score.name

        holder.lblScore.text = score.odometer.toString()

        holder.itemView.setOnClickListener {

            val cities = listOf(
                Pair(32.0853, 34.7818),
                Pair(31.7683, 35.2137),
                Pair(32.7940, 34.9896),
                Pair(31.2529, 34.7915),
                Pair(32.3215, 34.8532),
                Pair(31.8044, 34.6553)
            )
            val cityIndex = position % cities.size
            val baseCity = cities[cityIndex]

            val indexInCity = position / cities.size

            val radius = 0.002 + indexInCity * 0.004

            val angle = position * 60.0 * Math.PI / 180.0

            val latOffset = radius * Math.cos(angle)
            val lonOffset = radius * Math.sin(angle)

            callback?.onItemClicked(
                score.name,
                baseCity.first + latOffset,
                baseCity.second + lonOffset
            )
        }
    }

    override fun getItemCount(): Int = scores.size
}

