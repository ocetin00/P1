package com.oguzhancetin.p1.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.oguzhancetin.p1.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {

    override fun getViewBinding(): FragmentMainBinding {
        return FragmentMainBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCashier.setOnClickListener { goToCashierPage() }
        binding.buttonCustomer.setOnClickListener { goToCustomerPage() }
        binding.buttonCrash.setOnClickListener {
            throw RuntimeException("Test Crash") // Force a crash
        }
    }

    private fun goToCashierPage() {
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToCashierFragment())
    }

    private fun goToCustomerPage() {
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToCustomerFragment())
    }


}