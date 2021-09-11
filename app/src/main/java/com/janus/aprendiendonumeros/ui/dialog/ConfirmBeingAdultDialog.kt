package com.janus.aprendiendonumeros.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.databinding.DialogConfirmBeingAdultBinding
import com.janus.aprendiendonumeros.ui.utilities.UIAnimations
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ConfirmBeingAdultDialog : DialogFragment(R.layout.dialog_confirm_being_adult) {

    private lateinit var binding: DialogConfirmBeingAdultBinding
    private lateinit var listener: Listener
    private val anim: UIAnimations by lazy { UIAnimations(requireContext()) }
    private var countAnswer: Int = 0
    var isCorrect: Boolean = false

    companion object {
        private const val LIMIT_COUNT_ANSWER: Int = 3
        private const val DELAY_DURATION: Long = 1000
        private const val MIN_NUMBER: Int = 5
        private const val MAX_NUMBER: Int = 35
    }

    interface Listener {
        fun isPositive(dialog: ConfirmBeingAdultDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogConfirmBeingAdultBinding.bind(view)
        initUI()
        setUpRandomsNumbers()
        setUpEvents()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as Listener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                (context.toString() +
                        " must implement ConfirmBeingAdultDialog.Listener")
            )
        }
    }

    private fun initUI() {
        isCancelable = false
        dialog?.let { dialog ->
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        anim.startAnimation(binding.tvFirstNumber, R.anim.floating_first_number)
        anim.startAnimation(binding.tvSecondNumber, R.anim.floating_second_number)
    }

    private fun setUpRandomsNumbers() {
        binding.tvFirstNumber.text = (MIN_NUMBER..MAX_NUMBER).random().toString()
        binding.tvSecondNumber.text = (MIN_NUMBER..MAX_NUMBER).random().toString()
    }

    private fun setUpEvents() {
        binding.btnCheck.setOnClickListener { confirmSumAnswer() }
        binding.btnClose.setOnClickListener { listener.isPositive(this@ConfirmBeingAdultDialog) }
    }

    private fun confirmSumAnswer() {
        val firstNumber: Int = binding.tvFirstNumber.text.toString().toInt()
        val secondNumber: Int = binding.tvSecondNumber.text.toString().toInt()
        val sumAnswer: String = (firstNumber + secondNumber).toString()
        val possibleSumAnswer: String = binding.etAnswer.text.toString()

        if (sumAnswer == possibleSumAnswer) {
            isCorrect = true
            listener.isPositive(this)
        } else {
            countAnswer++
            validateCount()
        }
    }

    private fun validateCount() {
        if (countAnswer != LIMIT_COUNT_ANSWER) {
            incorrectAnswer()
        } else {
            incorrectAnswer()
            lifecycleScope.launch {
                delay(DELAY_DURATION)
                listener.isPositive(this@ConfirmBeingAdultDialog)
            }
        }
    }

    private fun incorrectAnswer() {
        lifecycleScope.launch {
            binding.btnCheck.isEnabled = false
            anim.startAnimation(binding.etAnswer, R.anim.attention)
            anim.startAnimation(binding.tvToastIncorrect, R.anim.jump_and_disappear)
            delay(DELAY_DURATION)
            setUpRandomsNumbers()
            binding.btnCheck.isEnabled = true
        }
    }
}