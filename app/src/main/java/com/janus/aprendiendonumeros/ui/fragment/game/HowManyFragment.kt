package com.janus.aprendiendonumeros.ui.fragment.game

import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.children
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.core.TextProvider
import com.janus.aprendiendonumeros.data.model.Exercise
import com.janus.aprendiendonumeros.data.model.LogExercise
import com.janus.aprendiendonumeros.data.remote.FigureDataSource.Level
import com.janus.aprendiendonumeros.databinding.FragmentHowManyBinding
import com.janus.aprendiendonumeros.presentation.HowManyViewModel
import com.janus.aprendiendonumeros.ui.animation.coins.AnimationReward
import com.janus.aprendiendonumeros.ui.base.BaseGameFragment
import com.janus.aprendiendonumeros.ui.base.MultiFigurePrinter
import com.janus.aprendiendonumeros.ui.utilities.Constant
import com.janus.aprendiendonumeros.ui.utilities.message
import com.janus.aprendiendonumeros.ui.utilities.removeViews
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HowManyFragment : BaseGameFragment(
    R.layout.fragment_how_many,
    Exercise.NAME_HOW_MANY,
    Exercise.NAME_LESS_OR_MORE
), MultiFigurePrinter {

    private lateinit var binding: FragmentHowManyBinding
    private val howManyViewModel: HowManyViewModel by viewModels()
    private lateinit var reward: AnimationReward
    private val questionFemale =
        TextProvider.exerciseHowMany["question_female"] ?: "¿Cuántas ##### hay?"
    private val questionMale =
        TextProvider.exerciseHowMany["question_male"] ?: "¿Cuántos ##### hay?"

    override fun initUI(view: View) {
        super.initUI(view)
        binding = FragmentHowManyBinding.bind(view)
        howManyViewModel.onCreate(Level.FIRST.toString())
        binding.containerImages.post { initObservers() }
        binding.containerCoins.post {
            reward = AnimationReward(binding.root)
            reward.setPositionDestinationCoins(binding.containerCoins.x, binding.containerCoins.y)
            reward.setSizeCoins(binding.ivCoin.width, binding.ivCoin.height)
        }
        setUpEvents()
    }

    private fun initObservers() {
        howManyViewModel.isLoading.observe(this, { isLoading ->
            if (isLoading) startLoading("Cargando...") else dismissLoading()
        })
        howManyViewModel.isFinishing.observe(this, { isFinishing ->
            if (isFinishing) endGame(3000)
        })
        howManyViewModel.index.observe(this, { progressValue ->
            binding.progressBar.progress = progressValue
        })
        howManyViewModel.coins.observe(this, { coins ->
            binding.tvCoin.text = coins.toString()
            animator.startAnimation(binding.containerCoins, R.anim.fast_zoom)
        })

        howManyViewModel.game.observe(this, { game ->
            viewLifecycleOwner.lifecycleScope.launch {
                binding.containerImages.removeViews()

                if (game.questionAudio.isNotEmpty()) {
                    questionAudio.reset()
                    questionAudio.setUrl(game.questionAudio)
                }
                setUpQuestionText(game.figure.gender, game.figure.plural) {
                    sayInstruction(game.questionAudio, binding.tvQuestion.text.toString())
                }
                setUpFigures(game.number, game.figure.icon)
                setUpOptions(game.number, game.firstDistraction, game.secondDistraction)
            }
        })
    }

    private fun setUpEvents() {
        binding.btnBackToMenu.setOnClickListener { endGame() }
        binding.btnOptionOne.setOnClickListener { evaluateOption((it as AppCompatButton)) }
        binding.btnOptionTwo.setOnClickListener { evaluateOption((it as AppCompatButton)) }
        binding.btnOptionThree.setOnClickListener { evaluateOption((it as AppCompatButton)) }
        binding.btnRepeatQuestion.setOnClickListener {
            sayInstruction(
                urlQuestionAudio = howManyViewModel.game.value!!.questionAudio,
                textQuestion = binding.tvQuestion.text.toString(),
            )
        }
        returnToMainMenu()
    }

    override fun initGame() {}

    private fun setUpQuestionText(
        gender: String,
        keyPluralNameFigure: String,
        onEndAction: () -> Unit = {},
    ) {
        val incompleteQuestion = when (gender) {
            Constant.GENDER_MALE -> questionMale
            else -> questionFemale
        }
        viewLifecycleOwner.lifecycleScope.launch {
            val figureName = TextProvider.figures[keyPluralNameFigure] ?: "imagenes"
            val question = incompleteQuestion.replace("#####", figureName)
            binding.tvQuestion.text = question
        }
        onEndAction()
    }

    private fun setUpFigures(number: Int, icon: String, onEndAction: () -> Unit = {}) {
        viewLifecycleOwner.lifecycleScope.launch {
            setUpFigures(binding.containerImages, number, icon)
            binding.containerImages.children.forEach { figure ->
                animator.fadeInWithScale(figure)
                delay(50)
            }
            onEndAction()
        }
    }

    private fun setUpOptions(firstOption: Int, secondOption: Int, thirdOption: Int) {
        val listOptions =
            listOf(binding.btnOptionOne, binding.btnOptionTwo, binding.btnOptionThree).shuffled()
        listOptions[0].text = firstOption.toString()
        listOptions[1].text = secondOption.toString()
        listOptions[2].text = thirdOption.toString()
    }

    private fun evaluateOption(button: AppCompatButton) {
        val chosenOptionText = button.text.toString()
        if (chosenOptionText.isNotEmpty()) {
            val chosenOptionInt = chosenOptionText.toInt()
            val correctAnswer = binding.containerImages.childCount
            val positionX = (button.x + (button.width / 2)) - binding.ivCoin.width
            val positionY = (button.y + (button.height / 2)) - binding.ivCoin.height

            when (chosenOptionInt) {
                correctAnswer -> correctAnswerAnimation(positionX, positionY)
                else -> incorrectAnswerAnimation()
            }
            howManyViewModel.nextNumber()
        } else howManyViewModel.nextNumber()
    }

    private fun incorrectAnswerAnimation() {
        viewLifecycleOwner.lifecycleScope.launch {
            playSoundIncorrect()
        }
    }

    private fun correctAnswerAnimation(positionX: Float, positionY: Float) {
        viewLifecycleOwner.lifecycleScope.launch {
            playSoundCorrect()
            reward.onEndAnimationCoin = {
                playSoundCoin()
            }
            reward.give(AnimationReward.REWARD_SIMPLE, positionX, positionY)
            howManyViewModel.addCoins(AnimationReward.REWARD_SIMPLE)
        }
    }

    private fun giveFeedback(isAnswerCorrect: Boolean) {
        if (isAnswerCorrect) {
            mContext.message("give_Feedback answer = $isAnswerCorrect")
        } else {
            mContext.message("give_Feedback answer = $isAnswerCorrect")
        }
    }

    private fun endGame(delayTimeSeconds: Long = 0) {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(delayTimeSeconds)
            navigateToFragmentEndExercise(
                LogExercise(
                    nameExercise = nameExercise,
                    level = Level.FIRST.toString(),
                    attemptsCorrect = 0,
                    attemptsIncorrect = 0,
                    attemptsTotal = 0,
                    timeElapsedInSeconds = 0
                )
            )
        }
    }

    private fun hideOptions() {
        animator.fadeOutWithScale(binding.btnOptionOne)
        animator.fadeOutWithScale(binding.btnOptionTwo)
        animator.fadeOutWithScale(binding.btnOptionThree)
    }

    private fun showOptions() {
        animator.fadeOutWithScale(binding.btnOptionOne)
        animator.fadeOutWithScale(binding.btnOptionTwo)
        animator.fadeOutWithScale(binding.btnOptionThree)
    }

    override fun onPause() {
        reward.onDestroy()
        talkative.onDestroy()
        super.onPause()
    }
}
/*
    private val args: HowManyFragmentArgs by navArgs()
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(requireActivity()) }
    private val anim: UIAnimations by lazy { UIAnimations(requireContext()) }
    private lateinit var listNumbers: List<Int>
    override lateinit var activity: Activity
    override lateinit var listImages: List<Figure>
    override lateinit var randomFigure: Figure
    private var indexNumbers: Int = 0
    private val rewardCoins: Int = 3
    private val perfectRewardCoins: Int = 9
    private val viewModel by viewModels<ImageViewModel> {
        ImageViewModelFactory(
            FigureImpl(FigureDataSource())
        )
    }

    override fun initUI(view: View) {
        super.initUI(view)
        binding = FragmentHowManyBinding.bind(view)
        setUpData()
        binding.containerImages.post { initGame() }
        addEvents()
    }

    private fun setUpData() {
        userId = args.userId
        generateListOfNumbers()
        logExercise.nameExercise = Exercise.NAME_HOW_MANY
        logExercise.level = Level.FIRST.toString()
        logExercise.attemptsTotal = listNumbers.size
        activity = mContext
    }

    private fun generateListOfNumbers() {
        listNumbers = (1..9).toList().shuffled()
    }

    override fun initGame() {
        viewModel.getImages(logExercise.level).observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> loadingDialog.startDialog("Cargando imagenes...")
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    listImages = result.data
                    generateQuestion()//(listNumbers[indexNumbers])
                }
                is Resource.Failure -> loadingDialog.dismiss()
            }
        })
    }

    private fun addEvents() {
        binding.btnBackToMenu.setOnClickListener { finish(0) }
        binding.btnOptionOne.setOnClickListener { evaluateOption((it as AppCompatButton)) }
        binding.btnOptionTwo.setOnClickListener { evaluateOption((it as AppCompatButton)) }
        binding.btnOptionThree.setOnClickListener { evaluateOption((it as AppCompatButton)) }
        binding.btnRepeatQuestion.setOnClickListener { }
        returnToMainMenu()
    }

    private fun generateTextQuestion() {
        FirebaseFirestore.getInstance().collection("vocabularies").document("es")
            .get()
            .addOnCompleteListener {
                val question = it.result?.get(randomFigure.question).toString()
                binding.tvQuestion.text = question
            }
    }

    private fun orderOption(firstOption: Int, secondOption: Int, thirdOption: Int) {
        val listOptions = listOf(binding.btnOptionOne, binding.btnOptionTwo, binding.btnOptionThree)
        listOptions[0].text = firstOption.toString()
        listOptions[1].text = secondOption.toString()
        listOptions[2].text = thirdOption.toString()
    }

    private fun optionsIsEnabled(isEnabled: Boolean) {
        binding.btnOptionOne.isEnabled = isEnabled
        binding.btnOptionTwo.isEnabled = isEnabled
        binding.btnOptionThree.isEnabled = isEnabled
    }

    private suspend fun informationResponse(button: AppCompatButton, isCorrect: Boolean) {
        optionsIsEnabled(false)
        val idImage: Int = when (isCorrect) {
            true -> R.drawable.ic_correct
            false -> R.drawable.ic_incorrect
        }
        binding.ivInfoResponse.setImageResource(idImage)
        binding.root.linkViews(binding.ivInfoResponse.id, button.id)
        anim.startAnimation(binding.ivInfoResponse, R.anim.animation_response)
        delay(1000)
        binding.ivInfoResponse.visibility = View.INVISIBLE
    }

    override fun setUpQuestion() {

    }

    override fun generateQuestion() {
        val number = listNumbers[indexNumbers]
        addImages(binding.containerImages, number)
        generateTextQuestion()
        generateOptions()
        optionsIsEnabled(true)
    }

    override fun generateOptions() {
        val number = listNumbers[indexNumbers]
        val rank: Rank = when (logExercise.level) {
            Level.FIRST.toString() -> Rank(1, listNumbers.size)
            Level.SECOND.toString() -> Rank.getRank(number, 5, listNumbers)
            else -> Rank.getRank(number, 2, listNumbers)
        }

        var distractionOne: Int
        var distractionTwo: Int

        do {
            distractionOne = (rank.min..rank.max).random()
        } while (distractionOne == number)
        do {
            distractionTwo = (rank.min..rank.max).random()
        } while (distractionTwo == number || distractionTwo == distractionOne)

        when ((0..2).random()) {
            0 -> orderOption(number, distractionOne, distractionTwo)
            1 -> orderOption(distractionTwo, number, distractionOne)
            else -> orderOption(distractionOne, distractionTwo, number)
        }
    }

    override fun evaluateOption(
        button: AppCompatButton,
    ) {
        val answer: Int = listNumbers[indexNumbers]
        val chosenOption: Int = button.text.toString().toInt()

        if (answer == chosenOption) {
            isCorrect(button)
        } else {
            isIncorrect(button)
        }

        indexNumbers++
        if (indexNumbers < listNumbers.size) {
            lifecycleScope.launch {
                delay(1500)
                binding.containerImages.removeViews()
                generateQuestion()
            }
        } else {
            finish(button)
        }
    }

    private fun isCorrect(button: AppCompatButton) {
        correct {
            lifecycleScope.launch {
                playSoundCorrect()
                informationResponse(button, true)
                binding.root.animationJumpCoin(button, rewardCoins)
                binding.tvCoin.addCoins(binding.containerCoins, rewardCoins)
                coins + rewardCoins
            }
        }
    }

    private fun isIncorrect(button: AppCompatButton) {
        incorrect {
            lifecycleScope.launch {
                playSoundIncorrect()
                informationResponse(button, false)
            }
        }
    }

    private fun finish(button: AppCompatButton) {
        if (isPerfectScore()) {
            lifecycleScope.launch {
                binding.root.animationJumpCoin(button, perfectRewardCoins)
                binding.tvCoin.addCoins(binding.containerCoins, perfectRewardCoins)
                coins + perfectRewardCoins
            }
        }
        finish(2000)
    }

    private suspend fun TextView.addCoins(view: View, addCoins: Int) {
        val waitTime: Long = (800 / addCoins).toLong()
        var coins = this.text.toString().toInt()

        for (coin in 1..addCoins) {
            coins++
            this.text = coins.toString()
            playSoundCoin()
            anim.startAnimation(view, R.anim.fast_zoom)
            delay(waitTime)
        }
    }

}


 */
/*
private fun evaluateOption(button: AppCompatButton) {
    if (listNumbers[indexNumbers] == button.text.toString().toInt()) {
        correct {
            lifecycleScope.launch {
                playSoundCorrect()
                informationResponse(button, true)
                binding.root.animationJumpCoin(button, rewardCoins)
                binding.tvCoin.addCoins(binding.containerCoins, rewardCoins)
                coins + rewardCoins
            }
        }
    } else {
        incorrect {
            lifecycleScope.launch {
                playSoundIncorrect()
                informationResponse(button, false)
            }
        }
    }

    indexNumbers++
    if (indexNumbers < listNumbers.size) {
        lifecycleScope.launch {
            delay(1500)
            binding.containerImages.removeViews()
            generateQuestion(listNumbers[indexNumbers])
        }
    } else {
        if (isPerfectScore()) {
            lifecycleScope.launch {
                binding.root.animationJumpCoin(button, perfectRewardCoins)
                binding.tvCoin.addCoins(binding.containerCoins, perfectRewardCoins)
                coins + perfectRewardCoins
            }
        }
        finish(2000 )
    }
}
 */
/*
private fun generateQuestion(number: Int) {
    addImages(binding.containerImages, number)
    generateTextQuestion()
    generateOptions(number)
    optionsIsEnabled(true)
}
*/
/*
    private fun generateOptions(number: Int) {
        val rank: Rank = when (logExercise.level) {
            Level.FIRST.toString() -> Rank(1, listNumbers.size)
            Level.SECOND.toString() -> Rank.getRank(number, 5, listNumbers)
            else -> Rank.getRank(number, 2, listNumbers)
        }

        var distractionOne: Int
        var distractionTwo: Int

        do {
            distractionOne = (rank.min..rank.max).random()
        } while (distractionOne == number)
        do {
            distractionTwo = (rank.min..rank.max).random()
        } while (distractionTwo == number || distractionTwo == distractionOne)

        when ((0..2).random()) {
            0 -> orderOption(number, distractionOne, distractionTwo)
            1 -> orderOption(distractionTwo, number, distractionOne)
            else -> orderOption(distractionOne, distractionTwo, number)
        }
    }
     */