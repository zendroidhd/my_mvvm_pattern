package com.technologies.zenlight.earncredits.userInterface.home.history.challengesHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.technologies.zenlight.earncredits.R
import com.technologies.zenlight.earncredits.data.model.api.Challenges
import com.technologies.zenlight.earncredits.databinding.ChallengesHistoryListRowBinding

class ChallengesHistoryAdapter(private val challenges: ArrayList<Challenges>, private val callbacks: ChallengesHistoryCallbacks)
    : RecyclerView.Adapter<ChallengesHistoryAdapter.ChallengesViewHolder>()  {


    inner class ChallengesViewHolder(val binding: ChallengesHistoryListRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(challenge: Challenges) {
            binding.tvTitle.text = challenge.description
            binding.tvCompletedTime.text = challenge.getActualCompletedTime()
            binding.tvCost.text = "Credits: ${challenge.credit}"
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ChallengesHistoryListRowBinding = DataBindingUtil.inflate(inflater, R.layout.challenges_history_list_row,parent,false)
        return ChallengesViewHolder(binding)
    }

    override fun getItemCount() = challenges.size

    override fun onBindViewHolder(holder: ChallengesViewHolder, position: Int) {
        holder.bind(challenges[position])
    }
}