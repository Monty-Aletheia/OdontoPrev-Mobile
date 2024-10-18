package br.com.fiap.challenge.ui.login

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import br.com.fiap.challenge.R
import br.com.fiap.challenge.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {
    lateinit var binding: ActivityMenuBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMenuBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.imageViewProfileIcon.setOnClickListener{
            navigateTo(R.id.profileFragment)
        }

        binding.imageViewAddIcon.setOnClickListener{
            navigateTo(R.id.addConsultationFragment)
        }

        binding.imageViewListIcon.setOnClickListener{
            navigateTo(R.id.consultationListFragment)
        }


    }

    private fun navigateTo(destination: Int) {
        val navController = findNavController(R.id.fragmentContainerView4)
        navController.navigate(destination)
    }
}