package com.janus.aprendiendonumeros.ui.fragment.auth.signin

import android.view.View
import android.widget.ToggleButton
import androidx.fragment.app.Fragment
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.databinding.FragmentSignInBinding
import com.janus.aprendiendonumeros.ui.adapter.SignUpViewPagerAdapter
import com.janus.aprendiendonumeros.ui.base.BaseFragment
import com.janus.aprendiendonumeros.ui.listener.NotifyQuestionListener

class SignInFragment : BaseFragment(R.layout.fragment_sign_in), NotifyQuestionListener {

    private lateinit var binding: FragmentSignInBinding

    override fun initUI(view: View) {
        binding = FragmentSignInBinding.bind(view)
        mContext.setQuestionTarget(this)
        setUpPager()
        addEvents()
    }

    private fun setUpPager() {
        val listFragments: List<Fragment> = listOf(SignInChildrenFragment(), SignInAdultFragment())
        val adapter = SignUpViewPagerAdapter(this)
        adapter.setListFragment(listFragments)
        binding.pager.adapter = adapter
        binding.pager.isUserInputEnabled = false
    }

    private fun addEvents() {
        binding.tgAgeMode.setOnClickListener {
            val toggle: ToggleButton = it as ToggleButton
            changeMode(toggle.isChecked)
        }
    }

    private fun changeMode(isAdultMode: Boolean) {
        if (isAdultMode) {
            mContext.showDialogConfirmBeingAdult()
        } else {
            binding.pager.currentItem = 0
        }
    }

    override fun update(answer: Boolean) {
        if (answer) {
            binding.pager.currentItem = 1
        } else {
            binding.tgAgeMode.isChecked = false
        }
    }
}