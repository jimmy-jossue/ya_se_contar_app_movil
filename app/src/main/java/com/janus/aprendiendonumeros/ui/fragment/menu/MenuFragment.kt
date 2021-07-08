package com.janus.aprendiendonumeros.ui.fragment.menu

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.databinding.FragmentMenuBinding
import com.janus.aprendiendonumeros.ui.utilities.loadImageFromUrl

class MenuFragment : Fragment(R.layout.fragment_menu) {

    private lateinit var binding: FragmentMenuBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMenuBinding.bind(view)

        binding.civUserProfile.setOnClickListener {
            findNavController().navigate(R.id.action_menu_to_signUp)
        }


        val itemBtnKnowNumbers: MenuItemView = createItemMenu(
            icon = "https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/activities_images%2Fic_know_numbers.png?alt=media&token=e405e492-5b6a-41f1-aeca-6aff59db125d",
            position = 0,
            status = MenuItemView.STATUS_EMPTY,
            destination = R.id.action_menu_to_knowNumbers
        )

        val itemBtnSelectAndCount: MenuItemView = createItemMenu(
            icon = "https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/activities_images%2Fic_how_many.png?alt=media&token=45b6fa8d-ca55-4384-bf95-93c1f57e3e3c",
            position = 1,
            status = MenuItemView.STATUS_EMPTY,
            destination = R.id.action_menu_to_selectAndCount
        )

        val itemBtnMoveAndCount: MenuItemView = createItemMenu(
            icon = "https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/activities_images%2Fic_how_many.png?alt=media&token=45b6fa8d-ca55-4384-bf95-93c1f57e3e3c",
            position = 1,
            status = MenuItemView.STATUS_LOCKED,
            destination = R.id.action_menu_to_moveAndCount
        )

        val itemBtnHowMany: MenuItemView = createItemMenu(
            icon = "https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/activities_images%2Fic_how_many.png?alt=media&token=45b6fa8d-ca55-4384-bf95-93c1f57e3e3c",
            position = 1,
            status = MenuItemView.STATUS_LOCKED,
            destination = R.id.action_menu_to_howMany
        )

        val itemBtnLessOrMore: MenuItemView = createItemMenu(
            icon = "https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/activities_images%2Fic_how_many.png?alt=media&token=45b6fa8d-ca55-4384-bf95-93c1f57e3e3c",
            position = 1,
            status = MenuItemView.STATUS_LOCKED,
            destination = R.id.action_menu_to_lessOrMore
        )

        val itemBtnOrderAndCount: MenuItemView = createItemMenu(
            icon = "https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/activities_images%2Fic_how_many.png?alt=media&token=45b6fa8d-ca55-4384-bf95-93c1f57e3e3c",
            position = 2,
            status = MenuItemView.STATUS_LOCKED,
            destination = R.id.action_menu_to_orderAndCount
        )

        binding.contentItemMenu.addView(itemBtnKnowNumbers)
        binding.contentItemMenu.addView(itemBtnSelectAndCount)
        binding.contentItemMenu.addView(itemBtnMoveAndCount)
        binding.contentItemMenu.addView(itemBtnHowMany)
        binding.contentItemMenu.addView(itemBtnLessOrMore)
        binding.contentItemMenu.addView(itemBtnOrderAndCount)
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

    private fun goTo(@IdRes idAction: Int) {
        findNavController().navigate(idAction)
    }
}