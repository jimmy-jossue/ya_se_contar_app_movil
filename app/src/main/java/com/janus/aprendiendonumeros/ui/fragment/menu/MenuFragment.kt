package com.janus.aprendiendonumeros.ui.fragment.menu

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.User
import com.janus.aprendiendonumeros.databinding.FragmentKnowNumbersBinding
import com.janus.aprendiendonumeros.databinding.FragmentMenuBinding

class MenuFragment : Fragment(R.layout.fragment_menu) {

    private lateinit var binding: FragmentMenuBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMenuBinding.bind(view)

        val item = MenuItemView(requireContext())
        item.setClickListener(View.OnClickListener {
            item.changeIconButton(R.drawable.bg_item_menu_button_complete).run {
                findNavController().navigate(R.id.action_menu_to_userProfile)
            }
        })

        val item2 = MenuItemView(requireContext())
        item2.setClickListener(View.OnClickListener {

            val users = FirebaseFirestore.getInstance().collection("users")

            val data1 = hashMapOf(
                "nikname" to "Joseph",
                "gender" to "niño",
                "birthDate" to Timestamp.now(),
                "level" to 10
            )
            val data = hashMapOf(
                "nikname" to "Belen",
                "gender" to "niña hermosa ❤",
                "birthDate" to Timestamp.now(),
                "level" to 1000
            )

            users.document("Joseph").set(data)
            users.document("Belen").set(data)

                .addOnSuccessListener {
                Toast.makeText(requireContext(),"datos ingresados", Toast.LENGTH_SHORT).show()
            }
        })

        binding.contentItemMenu.addView(item)
        binding.contentItemMenu.addView(item2)
    }
}