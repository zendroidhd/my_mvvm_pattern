package com.technologies.zenlight.earncredits.userInterface.home.powerUpFragment.detailView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.technologies.zenlight.earncredits.R
import com.technologies.zenlight.earncredits.data.model.api.PowerUps
import com.technologies.zenlight.earncredits.databinding.PowerUpDetailLayoutBinding
import com.technologies.zenlight.earncredits.userInterface.home.powerUpFragment.PowerUpsCallbacks
import com.technologies.zenlight.earncredits.utils.showDeletePowerUpAlertDialog
import com.technologies.zenlight.earncredits.utils.showUserPowerupAlertDialog

class PowerUpDetailFragment : Fragment() {

    private lateinit var binding: PowerUpDetailLayoutBinding

    companion object {
        private var powerCallbacks: PowerUpsCallbacks? = null
        private var selectedPowerUp: PowerUps? = null
        fun newInstance(callbacks: PowerUpsCallbacks, powerUps: PowerUps): PowerUpDetailFragment {
            powerCallbacks = callbacks
            selectedPowerUp = powerUps
            return PowerUpDetailFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.power_up_detail_layout, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedPowerUp?.let { powerUp ->
            setIcon(powerUp)
            setIconHintText(powerUp)
            setStatsText(powerUp)
            binding.tvCost.text = "Cost: ${powerUp.cost}"
            binding.tvTitle.text = powerUp.description

            binding.btnDelete.setOnClickListener {
                showDeletePowerUpAlertDialog(activity, powerUp, ::deletePowerUp)
            }

            binding.btnUsePowerUp.setOnClickListener {
                showUserPowerupAlertDialog(activity, powerUp, powerUp.cost, ::usePowerUp)
            }
        }
    }

    private fun deletePowerUp(powerUp: PowerUps) {
        activity?.onBackPressed()
        powerCallbacks?.onDeletePowerUpClicked(powerUp)
    }

    private fun usePowerUp(powerUps: PowerUps) {
        activity?.onBackPressed()
        powerCallbacks?.onCompletePowerUpClicked(powerUps)
    }

    private fun setIcon(powerUp: PowerUps) {
        val context = binding.root.context
        when {
            powerUp.icon == "mushroom" -> binding.ivIcon.setImageDrawable(context.getDrawable(R.drawable.mario_mushroom))
            powerUp.icon == "shield" -> binding.ivIcon.setImageDrawable(context.getDrawable(R.drawable.shield))
            powerUp.icon == "fireball" -> binding.ivIcon.setImageDrawable(context.getDrawable(R.drawable.hadouken))
            powerUp.icon == "glove" -> binding.ivIcon.setImageDrawable(context.getDrawable(R.drawable.boxing_glove))
            powerUp.icon == "pizza" -> binding.ivIcon.setImageDrawable(context.getDrawable(R.drawable.pizza))
            else -> binding.ivIcon.setImageDrawable(context.getDrawable(R.drawable.ic_apple_power_up))
        }
    }

    private fun setIconHintText(powerUp: PowerUps) {
        when {
            powerUp.icon == "mushroom" -> binding.tvIconHintText.text = "Helps plumbers grow 2x their size!"
            powerUp.icon == "shield" -> binding.tvIconHintText.text = "Give me strength!"
            powerUp.icon == "fireball" -> binding.tvIconHintText.text = "Hadouken!"
            powerUp.icon == "glove" -> binding.tvIconHintText.text = "'One two, one two punch, Mac!'"
            powerUp.icon == "pizza" -> binding.tvIconHintText.text = "'Cowabunga!'"
            else -> binding.tvIconHintText.text = "Chomp, chomp, chomp"
        }
    }

    private fun setStatsText(powerUp: PowerUps) {
        when {
            powerUp.icon == "mushroom" -> {
                binding.tvStatsOne.text = "+100% Height"
                binding.tvStatsTwo.text = "+100% defense"
            }
            powerUp.icon == "shield" -> {
                binding.tvStatsOne.text = "+120% Attack"
                binding.tvStatsTwo.text = "+100% defense"
            }
            powerUp.icon == "fireball" -> {
                binding.tvStatsOne.text = "+150% Damage"
                binding.tvStatsTwo.text = "-25% defense"
            }
            powerUp.icon == "glove" -> {
                binding.tvStatsOne.text = "+120% punch damage"
                binding.tvStatsTwo.text = "-20% stamina"
            }
            powerUp.icon == "pizza" -> {
                binding.tvStatsOne.text = "+75% Health"
                binding.tvStatsTwo.text = ""
            }
            else -> {
                binding.tvStatsOne.text = "+25 Points"
                binding.tvStatsTwo.text = "+25% Chomp power"
            }
        }
    }
}