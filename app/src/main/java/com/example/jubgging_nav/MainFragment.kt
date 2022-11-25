package com.example.jubgging_nav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.jubgging_nav.databinding.FragmentMainBinding
import com.example.jubgging_nav.databinding.FragmentRecordBinding


class MainFragment : Fragment() {

    private var mBinding : FragmentMainBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater, container, false)
        mBinding = binding
        return mBinding?.root
    }

    override fun onDestroyView() {
        mBinding = null
        super.onDestroyView()
    }

    //binding되고 나서 불리는 함수
    // 버튼 눌려서 어떻게 할 지 설정
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding?.btnPlogging?.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_PloggingMapsFragment)

        }
    }
}

