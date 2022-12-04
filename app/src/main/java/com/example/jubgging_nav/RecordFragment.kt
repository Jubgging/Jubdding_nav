package com.example.jubgging_nav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jubgging_nav.databinding.FragmentRecordBinding


class RecordFragment : Fragment() {

    var binding : FragmentRecordBinding? = null
    private lateinit var adapter: PloggingListAdapter
    val viewModel: PloggingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecordBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PloggingListAdapter(viewLifecycleOwner)

        binding?.recPlogging?.layoutManager = LinearLayoutManager(context)
        binding?.recPlogging?.adapter = adapter
        observerData()
    }

    fun observerData() {
        viewModel.fetchData().observe(viewLifecycleOwner, Observer {
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }

}