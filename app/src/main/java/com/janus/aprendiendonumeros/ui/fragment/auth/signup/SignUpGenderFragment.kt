package com.janus.aprendiendonumeros.ui.fragment.auth.signup

import android.view.View
import androidx.core.content.ContextCompat.getColor
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.data.model.User
import com.janus.aprendiendonumeros.databinding.FragmentSignUpGenderBinding
import com.janus.aprendiendonumeros.ui.base.BaseFragment
import com.janus.aprendiendonumeros.ui.listener.ControllablePager
import com.janus.aprendiendonumeros.ui.listener.ControllablePagerObserver
import com.janus.aprendiendonumeros.ui.utilities.Constant
import com.janus.aprendiendonumeros.ui.utilities.UIAnimations

class SignUpGenderFragment : BaseFragment(R.layout.fragment_sign_up_gender),
    ControllablePagerObserver {

    private lateinit var binding: FragmentSignUpGenderBinding
    private lateinit var controllablePager: ControllablePager
    private var gender: String? = null

    override fun initUI(view: View) {
        binding = FragmentSignUpGenderBinding.bind(view)
        addEvents()
    }

    // ControllablePagerObserver override method
    override fun setControllablePager(controllablePager: ControllablePager) {
        this.controllablePager = controllablePager
    }

    private fun addEvents() {
        binding.ivBoy.setOnClickListener { selectGender(it) }
        binding.ivGirl.setOnClickListener { selectGender(it) }
        binding.btnForward.setOnClickListener { saveDataUser() }
        binding.btnBackward.setOnClickListener { controllablePager.backward() }
    }

    private fun saveDataUser() {
        User.staticInstance.gender = gender
        controllablePager.forward()
    }

    private fun selectGender(view: View) {
        when (view.id) {
            binding.ivBoy.id -> changeGender(
                boyImage = R.drawable.ic_photo_profile_boy_selected,
                boyColor = R.color.primary_dark,
                girlImage = R.drawable.ic_photo_profile_girl_deselected,
                girlColor = R.color.gray
            )
            else -> changeGender(
                boyImage = R.drawable.ic_photo_profile_boy_deselected,
                boyColor = R.color.gray,
                girlImage = R.drawable.ic_photo_profile_girl_selected,
                girlColor = R.color.primary_dark
            )
        }
        animationSelectGender(view)

        gender = when (view.id) {
            binding.ivBoy.id -> Constant.GENDER_MALE
            else -> Constant.GENDER_MALE
        }
    }

    private fun changeGender(boyImage: Int, boyColor: Int, girlImage: Int, girlColor: Int) {
        binding.ivBoy.setImageResource(boyImage)
        binding.tvBoy.setTextColor(getColor(requireContext(), boyColor))
        binding.ivGirl.setImageResource(girlImage)
        binding.tvGirl.setTextColor(getColor(requireContext(), girlColor))
    }

    private fun animationSelectGender(view: View) {
        val anim = UIAnimations(requireContext())
        anim.startAnimation(view, R.anim.focus_attention)
    }
}