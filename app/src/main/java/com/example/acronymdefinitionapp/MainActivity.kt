package com.example.acronymdefinitionapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.BaseAdapter
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.acronymdefinitionapp.adapter.AcronymDefAdapter
import com.example.acronymdefinitionapp.databinding.ActivityMainBinding
import com.example.acronymdefinitionapp.model.Definition
import com.example.acronymdefinitionapp.model.Lf
import com.example.acronymdefinitionapp.utils.RequestState
import com.example.acronymdefinitionapp.viewmodel.AcronymViewModel
import com.example.acronymdefinitionapp.viewmodel.ViewIntentRequest

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[AcronymViewModel::class.java]
    }

    private val acronAdapter by lazy {
        AcronymDefAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.apply {
            acronymViewModel = viewModel
            lifecycleOwner = this@MainActivity
            acronymAdapter = acronAdapter
            searchButton.setOnClickListener { viewModel.getViewState(ViewIntentRequest.SearchAcronym(searchText.text.toString())) }
        }
        setContentView(binding.root)

    }
}

@BindingAdapter("setAdapter")
fun setAdapter(
    recyclerView: RecyclerView,
    adapter: AcronymDefAdapter?
) {
    adapter?.let {
        recyclerView.adapter = it
    }
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("submitList")
fun submitList(recyclerView: RecyclerView, list: List<Lf>?) {
    val adapter = recyclerView.adapter as? AcronymDefAdapter
    adapter?.setDataDefinition(list ?: listOf())
    Log.d(TAG, "submitList: $list")
}