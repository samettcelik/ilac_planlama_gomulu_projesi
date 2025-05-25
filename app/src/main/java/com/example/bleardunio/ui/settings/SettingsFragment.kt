package com.example.bleardunio.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bleardunio.databinding.FragmentSettingsBinding
import de.hdodenhof.circleimageview.BuildConfig

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupUI()
    }

    private fun setupUI() {
        // Sürüm bilgisini ayarla
        binding.tvAppVersionValue.text = BuildConfig.VERSION_NAME
        
        // Yayın tarihini ayarla
        binding.tvReleaseDateValue.text = "26.05.2025"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}