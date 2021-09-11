package com.janus.aprendiendonumeros.ui.fragment.game

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.Exercise
import com.janus.aprendiendonumeros.data.model.ResourceImage
import com.janus.aprendiendonumeros.data.remote.ImageDataSource
import com.janus.aprendiendonumeros.databinding.FragmentKnowNumbersBinding
import com.janus.aprendiendonumeros.domain.resourceimage.ImageImpl
import com.janus.aprendiendonumeros.presentation.ImageViewModel
import com.janus.aprendiendonumeros.presentation.ImageViewModelFactory
import com.janus.aprendiendonumeros.ui.base.BaseGameFragment
import com.janus.aprendiendonumeros.ui.base.MultiFigurePrinter
import com.janus.aprendiendonumeros.ui.dialog.LoadingDialog

class KnowNumbersFragment : BaseGameFragment(
    R.layout.fragment_know_numbers,
    Exercise.NAME_KNOW_NUMBERS,
    Exercise.NAME_HOW_MANY
), MultiFigurePrinter {
    private lateinit var binding: FragmentKnowNumbersBinding
    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(requireActivity()) }
    private val args: KnowNumbersFragmentArgs by navArgs()
    override lateinit var activity: Activity
    override lateinit var listImages: List<ResourceImage>
    override lateinit var randomResourceImage: ResourceImage
    private var indexNumbers: Int = 1

    private val viewModel by viewModels<ImageViewModel> {
        ImageViewModelFactory(
            ImageImpl(ImageDataSource())
        )
    }

    override fun initUI(view: View) {
        super.initUI(view)
        binding = FragmentKnowNumbersBinding.bind(view)
        setUpData()
        setUpEvents()
        binding.containerImages.post { initGame() }
        returnToMainMenu()
    }

    private fun setUpData() {
        level = ImageDataSource.Level.FIRST
        userId = args.userId
        attemptsTotal = 9
        activity = mContext
    }

    private fun setUpEvents() {
        binding.btnBackToMenu.setOnClickListener {
            finish(100) {}
        }
        binding.btnNext.setOnClickListener { next() }
        binding.btnPrevious.setOnClickListener { previous() }
    }

    override fun initGame() {
        viewModel.getImages(level).observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> loadingDialog.startDialog("Cargando...")
                is Resource.Success -> {
                    loadingDialog.dismiss()
                    listImages = result.data
                    addImages(binding.containerImages, indexNumbers)
                    binding.tvQuantity.text = indexNumbers.toString()
                }
                is Resource.Failure -> {
                    loadingDialog.dismiss()
                    Toast.makeText(
                        context,
                        "Failure: ${result.exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun next() {
        if (indexNumbers < attemptsTotal) {
            binding.containerImages.removeAllViews()
            indexNumbers++
            correct {}
            addImages(binding.containerImages, indexNumbers)
            binding.tvQuantity.text = indexNumbers.toString()
        }

        if (indexNumbers == attemptsTotal) {
            binding.btnNext.visibility = View.INVISIBLE
            correct {}
            finish() {
                binding.btnNext.isEnabled = false
                binding.btnPrevious.isEnabled = false
            }
        }
        if (binding.btnPrevious.visibility != View.VISIBLE) binding.btnPrevious.visibility =
            View.VISIBLE
    }

    private fun previous() {
        if (indexNumbers > 1) {
            binding.containerImages.removeAllViews()
            indexNumbers--
            addImages(binding.containerImages, indexNumbers)
            binding.tvQuantity.text = indexNumbers.toString()
        }
        if (indexNumbers == 1) binding.btnPrevious.visibility = View.INVISIBLE
        if (binding.btnNext.visibility != View.VISIBLE) binding.btnNext.visibility = View.VISIBLE
    }
}