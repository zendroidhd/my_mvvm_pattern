package com.technologies.zenlight.earncredits.userInterface.home.history.powerUpsHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.technologies.zenlight.earncredits.R
import com.technologies.zenlight.earncredits.data.model.api.PowerUps
import com.technologies.zenlight.earncredits.databinding.ChallengesHistoryListRowBinding

class PowerUpsHistoryAdapter(private val powerUps: ArrayList<PowerUps>) : RecyclerView.Adapter<PowerUpsHistoryAdapter.ViewHolder>() {


    inner class ViewHolder(val binding: ChallengesHistoryListRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(powerUp: PowerUps) {
            binding.tvTitle.text = powerUp.description
            binding.tvCompletedTime.text = powerUp.getActualUseDate()
            binding.tvCost.text = "Cost: ${powerUp.cost}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ChallengesHistoryListRowBinding = DataBindingUtil.inflate(inflater, R.layout.challenges_history_list_row, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = powerUps.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(powerUps[position])
    }

}