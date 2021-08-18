package com.janus.aprendiendonumeros.ui.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.firebase.Timestamp
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.data.model.LogExercise
import com.janus.aprendiendonumeros.databinding.DialogEndOfExerciseBinding
import com.janus.aprendiendonumeros.ui.utilities.Numbers
import com.janus.aprendiendonumeros.ui.utilities.UIAnimations
import com.janus.aprendiendonumeros.ui.utilities.loadImageFromUrl

class EndOfExerciseDialog : DialogFragment(R.layout.dialog_end_of_exercise) {

    private lateinit var binding: DialogEndOfExerciseBinding
    private lateinit var logExercise: LogExercise
    private var userId: Int = 0
    private var actionReturnMenu: Int = 0
    private var actionNextExercise: Int = 0

    companion object {
        const val USER_ID: String = "user_id"
        const val ACTION_RETURN_MENU: String = "action_return_menu"
        const val ACTION_NEXT_EXERCISE: String = "action_next_exercise"
        private const val TIME_ZONE: String = "America/Mexico_City"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogEndOfExerciseBinding.bind(view)
        setUpData()
        initUI()
        setUpEvents()
    }

    private fun setUpEvents() {
        binding.btnRepeat.setOnClickListener { }
    }

    private fun setUpData() {
        val args: Bundle? = arguments
        if (args != null) {
            userId = args.getInt(USER_ID)
            actionReturnMenu = args.getInt(ACTION_RETURN_MENU)
            actionNextExercise = args.getInt(ACTION_NEXT_EXERCISE)

            logExercise = LogExercise(
                nameExercise = args.getString(LogExercise.NAME_EXERCISE, ""),
                attemptsCorrect = args.getInt(LogExercise.ATTEMPTS_CORRECT, 0),
                attemptsIncorrect = args.getInt(LogExercise.ATTEMPTS_INCORRECT, 0),
                attemptsTotal = args.getInt(LogExercise.ATTEMPTS_TOTAL, 0),
                date = Timestamp(
                    Numbers.getCurrentDate(TIME_ZONE)
                ),
                timeElapsedInMilliseconds = args.getInt(LogExercise.TIME_ELAPSED_IN_MILLISECONDS, 0)
            )
        }
    }

    private fun initUI() {
        isCancelable = false
        dialog?.let { dialog ->
            val matchParent: Int = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(matchParent, matchParent)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        binding.ivAvatar.loadImageFromUrl("https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/avatar_images%2Fjawi_normal.png?alt=media&token=7a732b9c-7eda-42a4-b85e-ff770809c12f")

        when {
            logExercise.attemptsCorrect <= logExercise.attemptsTotal -> badScore(logExercise)
            logExercise.attemptsIncorrect < (logExercise.attemptsTotal * 3) -> goodScore(logExercise)
            else -> perfectScore(logExercise)
        }
    }

    private fun badScore(log: LogExercise) {
        binding.tvScore.text = String.format("${log.attemptsCorrect}/${log.attemptsTotal}")
        binding.ivStarFirst.setImageResource(R.drawable.ic_with_star)
    }

    private fun goodScore(log: LogExercise) {
        badScore(log)
        binding.ivStarSecond.setImageResource(R.drawable.ic_with_star)
    }

    private fun perfectScore(log: LogExercise) {
        goodScore(log)
        binding.ivStarThird.setImageResource(R.drawable.ic_with_star)
        binding.lavConfetti.visibility = View.VISIBLE
        binding.lavConfetti.playAnimation()
        binding.ivGoldenFlash.visibility = View.VISIBLE

        UIAnimations(requireContext()).startAnimation(
            binding.ivGoldenFlash,
            R.anim.rotate_infinite,
        )
    }
}