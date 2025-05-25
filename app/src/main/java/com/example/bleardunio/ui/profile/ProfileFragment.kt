package com.example.bleardunio.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.bleardunio.R
import com.example.bleardunio.databinding.FragmentProfileBinding
import com.example.bleardunio.ui.auth.LoginActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var userEmail: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        loadUserEmail()
        setupUI()
        setupListeners()
    }

    private fun loadUserEmail() {
        // Shared Preferences'ten e-posta bilgisini al
        val sharedPrefs = requireActivity().getSharedPreferences("user_prefs", 0)
        userEmail = sharedPrefs.getString("email", null)
    }

    private fun setupUI() {
        // Sabit kullanıcı adı göster
        binding.tvUserName.text = "Samet Çelik"
        
        // E-posta durumuna göre UI'ı güncelle
        if (userEmail.isNullOrEmpty()) {
            binding.tvUserEmail.text = "Henüz e-posta adresi koyulmadı"
            binding.btnAddEmail.visibility = View.VISIBLE
        } else {
            binding.tvUserEmail.text = userEmail
            binding.btnAddEmail.text = "E-posta eklendi"
            binding.btnAddEmail.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_light))
            binding.btnAddEmail.isEnabled = false
        }
    }

    private fun setupListeners() {
        // E-posta ekleme butonu tıklama
        binding.btnAddEmail.setOnClickListener {
            showAddEmailDialog()
        }
        
        // E-posta değiştirme tıklama
        binding.tvChangeEmail.setOnClickListener {
            showChangeEmailDialog()
        }
        
        // Hesap silme tıklama
        binding.tvDeleteAccount.setOnClickListener {
            showDeleteAccountConfirmation()
        }
        
        // Çıkış yapma butonu tıklama
        binding.btnLogout.setOnClickListener {
            logout()
        }
    }
    
    private fun showAddEmailDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("E-posta Ekle")
        
        val input = EditText(requireContext())
        input.hint = "E-posta adresinizi girin"
        builder.setView(input)
        
        builder.setPositiveButton("Ekle") { _, _ ->
            val email = input.text.toString().trim()
            if (email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                saveEmail(email)
                binding.tvUserEmail.text = email
                binding.btnAddEmail.text = "E-posta eklendi"
                binding.btnAddEmail.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_light))
                binding.btnAddEmail.isEnabled = false
            } else {
                Toast.makeText(context, "Geçerli bir e-posta adresi girin", Toast.LENGTH_SHORT).show()
            }
        }
        
        builder.setNegativeButton("İptal") { dialog, _ -> dialog.cancel() }
        builder.show()
    }
    
    private fun showChangeEmailDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("E-posta Değiştir")
        
        val input = EditText(requireContext())
        input.hint = "Yeni e-posta adresinizi girin"
        if (!userEmail.isNullOrEmpty()) {
            input.setText(userEmail)
        }
        builder.setView(input)
        
        builder.setPositiveButton("Değiştir") { _, _ ->
            val email = input.text.toString().trim()
            if (email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                saveEmail(email)
                binding.tvUserEmail.text = email
            } else {
                Toast.makeText(context, "Geçerli bir e-posta adresi girin", Toast.LENGTH_SHORT).show()
            }
        }
        
        builder.setNegativeButton("İptal") { dialog, _ -> dialog.cancel() }
        builder.show()
    }
    
    private fun saveEmail(email: String) {
        // E-postayı Shared Preferences'e kaydet
        val sharedPrefs = requireActivity().getSharedPreferences("user_prefs", 0)
        sharedPrefs.edit().putString("email", email).apply()
        userEmail = email
    }
    
    private fun showDeleteAccountConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Hesabı Sil")
            .setMessage("Hesabınızı silmek istediğinize emin misiniz?")
            .setPositiveButton("Evet") { _, _ ->
                deleteAccount()
            }
            .setNegativeButton("Hayır", null)
            .show()
    }
    
    private fun deleteAccount() {
        // Hesap silme işlemini gerçekleştir (sadece e-posta temizleme ve çıkış yapma)
        val sharedPrefs = requireActivity().getSharedPreferences("user_prefs", 0)
        sharedPrefs.edit().remove("email").apply()
        
        Toast.makeText(context, "Hesabınız silindi", Toast.LENGTH_SHORT).show()
        
        // Oturumu kapat
        logout()
    }
    
    private fun logout() {
        // Show a confirmation message
        Toast.makeText(context, "Çıkış yapılıyor...", Toast.LENGTH_SHORT).show()
        
        // Navigate to the login screen
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 