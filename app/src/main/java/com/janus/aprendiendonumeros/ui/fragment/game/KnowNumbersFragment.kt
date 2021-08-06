package com.janus.aprendiendonumeros.ui.fragment.game

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.ResourceImage
import com.janus.aprendiendonumeros.data.remote.ResourceImageDataSource
import com.janus.aprendiendonumeros.databinding.FragmentKnowNumbersBinding
import com.janus.aprendiendonumeros.presentation.ResourceImageViewModel
import com.janus.aprendiendonumeros.presentation.ResourceImageViewModelFactory
import com.janus.aprendiendonumeros.repository.resourceimage.ResourceImageImpl
import com.janus.aprendiendonumeros.ui.utilities.goTo
import com.janus.aprendiendonumeros.ui.utilities.loadImageFromUrl
import com.janus.aprendiendonumeros.ui.utilities.positionIsRepeated

class KnowNumbersFragment : Fragment(R.layout.fragment_know_numbers) {

    private lateinit var binding: FragmentKnowNumbersBinding
    private lateinit var level: ResourceImageDataSource.Level
    private lateinit var listImages: List<ResourceImage>
    private lateinit var randomResourceImage: ResourceImage
    private var indexNumbers: Int = 1
    private val viewModel by viewModels<ResourceImageViewModel> {
        ResourceImageViewModelFactory(
            ResourceImageImpl(
                ResourceImageDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentKnowNumbersBinding.bind(view)
        level = ResourceImageDataSource.Level.FIRST

        binding.btnBackToMenu.setOnClickListener { goTo(R.id.action_knowNumbers_to_menu) }
        binding.btnNext.setOnClickListener { next() }
        binding.btnPrevious.setOnClickListener { previous() }
        binding.containerImages.post { initGame() }
    }

    private fun initGame() {
        viewModel.fetchResourceImage(level).observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    Toast.makeText(context, "Success\n $indexNumbers", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                    listImages = result.data
                    addImages(indexNumbers)
                    binding.tvQuantity.text = indexNumbers.toString()
                }
                is Resource.Failure -> {
                    Toast.makeText(
                        context,
                        "Failure: ${result.exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar.visibility = View.GONE
                }
            }
        })
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
        }
    }

    private fun createImage(x: Int, y: Int, size: Int): ImageView {
        val layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(size, size)
        val image = ImageView(context)
        image.layoutParams = layoutParams
        image.adjustViewBounds = true
        image.x = x.toFloat()
        image.y = y.toFloat()
        image.loadImageFromUrl(randomResourceImage.icon)
        return image
    }

    private fun next() {
        if (indexNumbers < 9) {
            binding.containerImages.removeAllViews()
            indexNumbers++
            addImages(indexNumbers)
            binding.tvQuantity.text = indexNumbers.toString()
        }

        if (indexNumbers == 9) {
            binding.btnNext.visibility = View.INVISIBLE
        }

        if (binding.btnPrevious.visibility != View.VISIBLE) {
            binding.btnPrevious.visibility = View.VISIBLE
        }
    }

    private fun previous() {
        if (indexNumbers > 1) {
            binding.containerImages.removeAllViews()
            indexNumbers--
            addImages(indexNumbers)
            binding.tvQuantity.text = indexNumbers.toString()
        }
        if (indexNumbers == 1) {
            binding.btnPrevious.visibility = View.INVISIBLE
        }

        if (binding.btnNext.visibility != View.VISIBLE) {
            binding.btnNext.visibility = View.VISIBLE
        }
    }
}