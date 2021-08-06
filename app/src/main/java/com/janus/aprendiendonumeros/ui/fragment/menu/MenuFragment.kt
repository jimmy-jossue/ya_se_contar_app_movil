package com.janus.aprendiendonumeros.ui.fragment.menu

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.User
import com.janus.aprendiendonumeros.data.remote.UserProfileDataSource
import com.janus.aprendiendonumeros.databinding.FragmentMenuBinding
import com.janus.aprendiendonumeros.presentation.UserProfileViewModel
import com.janus.aprendiendonumeros.presentation.UserProfileViewModelFactory
import com.janus.aprendiendonumeros.repository.userprofile.UserProfileImpl
import com.janus.aprendiendonumeros.ui.utilities.goTo
import com.janus.aprendiendonumeros.ui.utilities.loadImageFromUrl

class MenuFragment : Fragment(R.layout.fragment_menu) {

    private lateinit var binding: FragmentMenuBinding
    private val args: MenuFragmentArgs by navArgs()
    private val viewModel by viewModels<UserProfileViewModel> {
        UserProfileViewModelFactory(
            UserProfileImpl(
                UserProfileDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMenuBinding.bind(view)

        binding.civUserProfile.setOnClickListener { goTo(R.id.action_menu_to_signIn) }
        binding.btnSettings.setOnClickListener { goTo(R.id.action_opening_to_signIn) }

        val iconKnowNumbers =
            "https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/activities_images%2Fic_know_numbers.png?alt=media&token=e405e492-5b6a-41f1-aeca-6aff59db125d"
        val iconSelectAndCount =
            "https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/activities_images%2Fic_how_many.png?alt=media&token=45b6fa8d-ca55-4384-bf95-93c1f57e3e3c"
        val iconMoveAndCount =
            "https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/activities_images%2Fic_blocked_activit.png?alt=media&token=a368a4af-ba68-444c-aac8-91a5aa1521ce"
        val iconHowMany =
            "https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/activities_images%2Fic_blocked_activit.png?alt=media&token=a368a4af-ba68-444c-aac8-91a5aa1521ce"
        val iconLessOrMore =
            "https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/activities_images%2Fic_blocked_activit.png?alt=media&token=a368a4af-ba68-444c-aac8-91a5aa1521ce"
        val iconOrderAndCount =
            "https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/activities_images%2Fic_blocked_activit.png?alt=media&token=a368a4af-ba68-444c-aac8-91a5aa1521ce"

        val itemBtnKnowNumbers: MenuItemView = createItemMenu(
            iconKnowNumbers,
            0,
            MenuItemView.STATUS_EMPTY,
            R.id.action_menu_to_knowNumbers
        )
        val itemBtnSelectAndCount: MenuItemView = createItemMenu(
            iconSelectAndCount,
            1,
            MenuItemView.STATUS_EMPTY,
            R.id.action_menu_to_howMany
        )
        val itemBtnMoveAndCount: MenuItemView = createItemMenu(
            iconMoveAndCount,
            1,
            MenuItemView.STATUS_LOCKED,
            R.id.action_menu_to_moveAndCount
        )
        val itemBtnHowMany: MenuItemView =
            createItemMenu(iconHowMany, 1, MenuItemView.STATUS_LOCKED, R.id.action_menu_to_howMany)
        val itemBtnLessOrMore: MenuItemView = createItemMenu(
            iconLessOrMore,
            2,
            MenuItemView.STATUS_LOCKED,
            R.id.action_menu_to_lessOrMore
        )
        val itemBtnOrderAndCount: MenuItemView = createItemMenu(
            iconOrderAndCount,
            2,
            MenuItemView.STATUS_LOCKED,
            R.id.action_menu_to_orderAndCount
        )

        binding.contentItemMenu.addView(itemBtnKnowNumbers)
        binding.contentItemMenu.addView(itemBtnSelectAndCount)
        binding.contentItemMenu.addView(itemBtnMoveAndCount)
        binding.contentItemMenu.addView(itemBtnHowMany)
        binding.contentItemMenu.addView(itemBtnLessOrMore)
        binding.contentItemMenu.addView(itemBtnOrderAndCount)
    }

    private fun initUI() {
        val userId: String = args.userId
        viewModel.fetchUser(userId).observe(viewLifecycleOwner, { result ->
            when (result) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                }
                is Resource.Failure -> {
                    binding.progressBar.visibility = View.GONE
                }
            }
        })
    }

    private fun createItemMenu(
        icon: String,
        position: Int,
        status: Int,
        @IdRes destination: Int
    ): MenuItemView {
        val item = MenuItemView(requireContext())
        item.getIcon().loadImageFromUrl(icon)
        item.setPosition(position)
        item.setStatus(status)

        item.setClickListener { goTo(destination) }
        return item
    }

    private fun setupData(user: User) {
        binding.civUserProfile.loadImageFromUrl(user.image)
    }
}