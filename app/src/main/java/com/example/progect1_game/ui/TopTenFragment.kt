package com.example.progect1_game.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progect1_game.R
import com.example.progect1_game.interfaces.CallbackTopTenClicked
import com.example.progect1_game.logic.TopTenManager

class TopTenFragment : Fragment() {

    companion object {
        var callback: CallbackTopTenClicked? = null
    }

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_top_ten, container, false)
        recyclerView = v.findViewById(R.id.topTen_RV_scores)

        val scores = TopTenManager
            .getInstance()
            .getScores()

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = TopTenAdapter(scores, callback)

        return v
    }
}


