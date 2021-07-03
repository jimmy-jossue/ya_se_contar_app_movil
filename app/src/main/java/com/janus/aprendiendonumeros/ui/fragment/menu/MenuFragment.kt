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

        val item = MenuItemView(requireContext())
        item.setClickListener {
            item.changeIconButton(R.drawable.bg_item_menu_button_complete).run {
                findNavController().navigate(R.id.action_menu_to_userProfile)
            }
        }

        val itemBtnKnowNumbers = MenuItemView(requireContext())
        itemBtnKnowNumbers.getIcon()
            .loadImageFromUrl("https://firebasestorage.googleapis.com/v0/b/aprendiendo-numeros-8196e.appspot.com/o/activities_images%2Fic_know_numbers.png?alt=media&token=e405e492-5b6a-41f1-aeca-6aff59db125d")
        itemBtnKnowNumbers.setPosition(2)
        itemBtnKnowNumbers.setStatus(MenuItemView.STATUS_EMPTY)
        itemBtnKnowNumbers.setClickListener {
            findNavController().navigate(R.id.action_menu_to_knowNumbers)
        }

        binding.contentItemMenu.addView(item)
        binding.contentItemMenu.addView(itemBtnKnowNumbers)
    }

    private fun goTo(@IdRes idAction: Int) {
        findNavController().navigate(idAction)
    }
}