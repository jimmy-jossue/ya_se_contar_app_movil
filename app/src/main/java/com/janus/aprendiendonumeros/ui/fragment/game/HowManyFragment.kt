package com.janus.aprendiendonumeros.ui.fragment.game

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FirebaseFirestore
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.Exercise
import com.janus.aprendiendonumeros.data.model.Rank
import com.janus.aprendiendonumeros.data.model.ResourceImage
import com.janus.aprendiendonumeros.data.remote.ImageDataSource
import com.janus.aprendiendonumeros.data.remote.ImageDataSource.Level
import com.janus.aprendiendonumeros.databinding.FragmentHowManyBinding
import com.janus.aprendiendonumeros.domain.resourceimage.ImageImpl
import com.janus.aprendiendonumeros.presentation.ImageViewModel
import com.janus.aprendiendonumeros.presentation.ImageViewModelFactory
import com.janus.aprendiendonumeros.ui.base.BaseGameFragment
import com.janus.aprendiendonumeros.ui.base.MultiFigurePrinter
import com.janus.aprendiendonumeros.ui.dialog.LoadingDialog
import com.janus.aprendiendonumeros.ui.fragment.menu.MenuFragmentArgs
import com.janus.aprendiendonumeros.ui.utilities.UIAnimations
import com.janus.aprendiendonumeros.ui.utilities.animationJumpCoin
import com.janus.aprendiendonumeros.ui.utilities.linkViews
import com.janus.aprendiendonumeros.ui.utilities.removeViews
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HowManyFragment : BaseGameFragment(R.layout.fragment_how_many,
    Exercise.NAME_HOW_MANY,
    Exercise.NAME_LESS_OR_MORE), MultiFigurePrinter {

    private lateinit var binding: FragmentHowManyBinding
    private val args: MenuFragmentArgs by navArgs()
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(requireActivity()) }
    private val anim: UIAnimations by lazy { UIAnimations(requireContext()) }
    private lateinit var listNumbers: List<Int>
    override lateinit var activity: Activity
    override lateinit var listImages: List<ResourceImage>
    override lateinit var randomResourceImage: ResourceImage
    private var indexNumbers: Int = 0
    private val viewModel by viewModels<ImageViewModel> {
        ImageViewModelFactory(
            ImageImpl(ImageDataSource())
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
        level = Level.FIRST
        generateListOfNumbers()
        attemptsTotal = listNumbers.size
        activity = mContext
    }

    private fun generateListOfNumbers() {
        listNumbers = (1..9).toList().shuffled()
    }

    override fun initGame() {
        viewModel.getImages(level).observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> loadingDialog.startDialog("Cargando imagenes...")
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    listImages = result.data
                    generateQuestion(listNumbers[indexNumbers])
                }
                is Resource.Failure -> loadingDialog.dismiss()
            }
        })
    }

    private fun addEvents() {
        binding.btnBackToMenu.setOnClickListener {
            finish(waitTime = 0) { }
        }
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
                val question = it.result?.get(randomResourceImage.question).toString()
                binding.tvQuestion.text = question
            }
    }

    private fun generateOptions(number: Int) {
        val rank: Rank = when (level) {
            Level.FIRST -> Rank(1, listNumbers.size)
            Level.SECOND -> Rank.getRank(number, 5, listNumbers)
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

    private fun orderOption(firstOption: Int, secondOption: Int, thirdOption: Int) {
        val listOptions = listOf(binding.btnOptionOne, binding.btnOptionTwo, binding.btnOptionThree)
        listOptions[0].text = firstOption.toString()
        listOptions[1].text = secondOption.toString()
        listOptions[2].text = thirdOption.toString()
    }

    private fun evaluateOption(button: AppCompatButton) {
        if (listNumbers[indexNumbers] == button.text.toString().toInt()) {
            correct(action = {
                lifecycleScope.launch {
                    informationResponse(button, true)
                    //binding.root.animationJumpCoin(button, 3)
                    binding.root.animationJumpCoin(button,
                        binding.containerCoins.x,
                        binding.containerCoins.y,
                        3)
                    binding.tvCoin.addCoins(binding.containerCoins, 1)
                }
            })
        } else {
            incorrect(action = {
                lifecycleScope.launch {
                    playSoundIncorrect()
                    informationResponse(button, false)
                }
            })
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
                binding.tvCoin.addCoins(binding.containerCoins, 20)
            }
            finish(
                waitTime = 2000,
                actionBefore = {

                }
            )
        }
    }

    private fun generateQuestion(number: Int) {
        addImages(binding.containerImages, number)
        generateTextQuestion()
        generateOptions(number)
        optionsIsEnabled(true)
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

    private fun TextView.addCoins(view: View, addCoins: Int) {
        val textView: TextView = this
        val waitTime: Long = (2000 / addCoins).toLong()
        lifecycleScope.launch {
            for (coin in 1..addCoins) {
                coins++
                textView.text = coins.toString()
                playSoundCoin()
                UIAnimations(context).startAnimation(view, R.anim.fast_zoom)
                delay(waitTime)
            }
        }
    }
}