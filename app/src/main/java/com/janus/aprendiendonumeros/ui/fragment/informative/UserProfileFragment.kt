package com.janus.aprendiendonumeros.ui.fragment.informative

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.janus.aprendiendonumeros.R
import com.janus.aprendiendonumeros.core.Resource
import com.janus.aprendiendonumeros.data.model.User
import com.janus.aprendiendonumeros.data.remote.UserProfileDataSource
import com.janus.aprendiendonumeros.databinding.FragmentUserProfileBinding
import com.janus.aprendiendonumeros.presentation.UserProfileViewModel
import com.janus.aprendiendonumeros.presentation.UserProfileViewModelFactory
import com.janus.aprendiendonumeros.repository.UserProfileImpl
import com.janus.aprendiendonumeros.repository.UserProfileProvider

class UserProfileFragment : Fragment(R.layout.fragment_user_profile) {

    private lateinit var binding: FragmentUserProfileBinding
    private val viewModel by viewModels<UserProfileViewModel>{ UserProfileViewModelFactory(UserProfileImpl(
        UserProfileDataSource()
    ))}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentUserProfileBinding.bind(view)

        viewModel.fetchUsers().observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Resource.Loading -> { binding.progressBar.visibility = View.VISIBLE }

                is Resource.Success -> {
                   showInfos(result)
                }
                is Resource.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvNickName.text = "ERROR"
                    binding.tvGender.text = "No se pudieron"
                    binding.tvTime.text = "cargar los datos"
                    binding.tvLevel.text = result.exception.message
                }
            }
        })

    }

    private fun showInfo(result: Resource.Success<User>){
        binding.progressBar.visibility = View.GONE

        binding.tvNickName.text = "nickName: "+result.data.nickname
        binding.tvGender.text = "gender: "+ result.data.gender
        binding.tvTime.text = "date: "+ result.data.birthDate
        binding.tvLevel.text = "level: "+ result.data.level
    }

    private fun showInfos(result: Resource.Success<List<User>>){
        binding.progressBar.visibility = View.GONE

        for(user in result.data){
            binding.tvNickName.text = "nickName: "+user.nickname
            binding.tvGender.text = "gender: "+ user.gender
            binding.tvTime.text = "date: "+ user.birthDate
            binding.tvLevel.text = "level: "+ user.level
        }
    }
}