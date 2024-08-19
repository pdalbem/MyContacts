package com.ifsp.mycontacts.ui.deleteAll

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.ifsp.mycontacts.databinding.FragmentDeleteAllBinding
import com.ifsp.mycontacts.viewmodel.DatabaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteAllFragment : DialogFragment() {


    private lateinit var binding: FragmentDeleteAllBinding
    private val viewModel : DatabaseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentDeleteAllBinding.inflate(inflater,container,false)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnNegative.setOnClickListener {
                dismiss()
            }
            btnPositive.setOnClickListener {
                viewModel.deleteAllContacts()
                dismiss()
            }
        }
    }


}