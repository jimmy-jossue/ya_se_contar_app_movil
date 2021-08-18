package com.janus.aprendiendonumeros.ui.fragment.game

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import com.google.firebase.firestore.FirebaseFirestore
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.LogExercise
import com.janus.aprendiendonumeros.data.model.Rank
import com.janus.aprendiendonumeros.data.model.ResourceImage
import com.janus.aprendiendonumeros.data.remote.ImageDataSource
import com.janus.aprendiendonumeros.data.remote.ImageDataSource.Level
import com.janus.aprendiendonumeros.databinding.FragmentHowManyBinding
import com.janus.aprendiendonumeros.presentation.ResourceImageViewModel
import com.janus.aprendiendonumeros.presentation.ResourceImageViewModelFactory
import com.janus.aprendiendonumeros.repository.resourceimage.ResourceImageImpl
import com.janus.aprendiendonumeros.ui.base.BaseFragment
import com.janus.aprendiendonumeros.ui.utilities.*

class HowManyFragment : BaseFragment(R.layout.fragment_how_many) {

    private lateinit var binding: FragmentHowManyBinding
    private lateinit var listNumbers: List<Int>
    private lateinit var listImages: List<ResourceImage>
    private lateinit var level: Level
    private lateinit var randomResourceImage: ResourceImage
    private var indexNumbers: Int = 0
    private val soundCorrect: Sound =
        Sound("https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/general_sounds%2Fcorrect.mp3?alt=media&token=341fa8c6-fd7c-4ab2-9024-a9aac170c99f")
    private val soundIncorrect: Sound =
        Sound("https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/general_sounds%2Fincorrect.mp3?alt=media&token=5ab58ab0-2514-4fb7-8227-b1de04235bd4")
    private val log: LogExercise = LogExercise()
    private val viewModel by viewModels<ResourceImageViewModel> {
        ResourceImageViewModelFactory(
            ResourceImageImpl(
                ImageDataSource()
            )
        )
    }

    override fun initUI(view: View) {
        level = Level.FIRST
        binding = FragmentHowManyBinding.bind(view)
        generateListOfNumbers()
        binding.containerImages.post { initGame() }
        addEvents()
    }

    private fun addEvents() {
        binding.btnBackToMenu.setOnClickListener { goTo(R.id.action_howMany_to_menu) }
        binding.btnOptionOne.setOnClickListener { evaluateOption((it as AppCompatButton)) }
        binding.btnOptionTwo.setOnClickListener { evaluateOption((it as AppCompatButton)) }
        binding.btnOptionThree.setOnClickListener { evaluateOption((it as AppCompatButton)) }
        binding.btnRepeatQuestion.setOnClickListener { }
    }


    private fun initGame() {
        viewModel.fetchResourceImage(level).observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> binding.containerProgressBar.progressBar.visibility =
                    View.VISIBLE
                is Resource.Success -> {
                    binding.containerProgressBar.progressBar.visibility = View.GONE
                    listImages = result.data
                    generateQuestion(listNumbers[indexNumbers])
                }
                is Resource.Failure -> {
                    binding.containerProgressBar.progressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun generateListOfNumbers() {
        listNumbers = (1..9).toList().shuffled()
        //Numbers.getRandomNumberList(1, 9)
    }

    private fun addImages(totalImages: Int) {
        val randomNumber: Int = (listImages.indices).random()
        randomResourceImage = listImages[randomNumber]

        val imageViewSize = when {
            (totalImages < 3) -> resources.getDimension(R.dimen.img_size_large)
            (totalImages < 5) -> resources.getDimension(R.dimen.img_size_medium)
            else -> resources.getDimension(R.dimen.img_size_small)
        }.toInt()

        val containerWidth = (binding.containerImages.measuredWidth - imageViewSize)
        val containerHeight = (binding.containerImages.measuredHeight - imageViewSize)

        for (i: Int in 1..totalImages) {
            var x: Int = (0..containerWidth).random()
            var y: Int = (0..containerHeight).random()

            while (binding.containerImages.positionIsRepeated(x, y, imageViewSize)) {
                x = (0..containerWidth).random()
                y = (0..containerHeight).random()
            }
            val view: ImageView = createImage(x, y, imageViewSize)
            binding.containerImages.addView(view)
            binding.containerImages.requestLayout()
            binding.containerImages.invalidate()
        }
    }

    private fun createImage(x: Int, y: Int, size: Int): ImageView {
        val layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(size, size)
        val image = ImageView(context).apply {
            this.layoutParams = layoutParams
            this.adjustViewBounds = true
            this.x = x.toFloat()
            this.y = y.toFloat()
        }
        image.loadImageFromUrl(randomResourceImage.icon)
        return image
    }

    private fun generateTextQuestion() {
        val howMany: String =
            if (randomResourceImage.gender == Constant.GENDER_FEMALE) "Cuántas" else "Cuántos"
        var name: String
        FirebaseFirestore.getInstance().collection("vocabularies").document("es")
            .get()
            .addOnCompleteListener {
                name = it.result?.get(randomResourceImage.name).toString()
                binding.tvQuestion.text = getString(R.string.how_many_question_es, howMany, name)
            }
    }

    private fun generateOptions(number: Int) {
        val listOptions: List<AppCompatButton> =
            listOf(binding.btnOptionOne, binding.btnOptionTwo, binding.btnOptionThree)
        val answerPosition: Int = (0..2).random()
        var distractionOne: Int
        var distractionTwo: Int

        val rank: Rank = when (level) {
            Level.FIRST -> Rank(1, listNumbers.size)
            Level.SECOND -> Rank.getRank(number, 5, listNumbers)
            else -> Rank.getRank(number, 2, listNumbers)
        }

        do {
            distractionOne = (rank.min..rank.max).random()
        } while (distractionOne == number)
        do {
            distractionTwo = (rank.min..rank.max).random()
        } while (distractionTwo == number || distractionTwo == distractionOne)

        when (answerPosition) {
            0 -> {
                listOptions[0].text = number.toString()
                listOptions[1].text = distractionOne.toString()
                listOptions[2].text = distractionTwo.toString()
            }
            1 -> {
                listOptions[0].text = distractionOne.toString()
                listOptions[1].text = number.toString()
                listOptions[2].text = distractionTwo.toString()
            }
            else -> {
                listOptions[0].text = distractionOne.toString()
                listOptions[1].text = distractionTwo.toString()
                listOptions[2].text = number.toString()
            }
        }

        listOptions.forEach {
            if (it.visibility == View.INVISIBLE) {
                it.visibility = View.VISIBLE
            }
        }
    }

    private fun evaluateOption(button: AppCompatButton) {
        if (listNumbers[indexNumbers] == button.text.toString().toInt()) {
            correct()
        } else {
            incorrect()
        }

        indexNumbers++
        if (indexNumbers < listNumbers.size) {
            binding.containerImages.removeViews()
            generateQuestion(listNumbers[indexNumbers])
        } else {
            finish(log)
        }
    }

    private fun finish(log: LogExercise) {
        mContext.showDialogEndOfExercise("jimmy", 0, 0, log)
    }


    private fun generateQuestion(number: Int) {
        addImages(number)
        generateTextQuestion()
        generateOptions(number)
    }

    private fun incorrect() {
        log.attemptsIncorrect++
        soundIncorrect.play()
    }

    private fun correct() {
        log.attemptsCorrect++
        soundCorrect.play()
    }
}