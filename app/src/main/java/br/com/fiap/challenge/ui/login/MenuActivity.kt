package br.com.fiap.challenge.ui.login

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import br.com.fiap.challenge.R
import br.com.fiap.challenge.animations.AnimationsManager
import br.com.fiap.challenge.databinding.ActivityMenuBinding
import br.com.fiap.challenge.models.FragmentType

class MenuActivity : AppCompatActivity() {
    lateinit var binding: ActivityMenuBinding
    private lateinit var navController: NavController
    private lateinit var animationsManager: AnimationsManager
    private var currentFragment: FragmentType = FragmentType.PROFILE
    private var previousFragment: FragmentType? = null



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

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView4) as NavHostFragment
        navController = navHostFragment.navController

        animationsManager = AnimationsManager()


        binding.imageViewProfileIcon.setOnClickListener{
            navigateTo(FragmentType.PROFILE)
        }

        binding.imageViewAddIcon.setOnClickListener{
            navigateTo(FragmentType.ADD)
        }

        binding.imageViewListIcon.setOnClickListener{
            navigateTo(FragmentType.LIST)
        }


    }

    private fun navigateTo(destination: FragmentType) {

        if (currentFragment == destination) return

        previousFragment = currentFragment
        currentFragment =  destination
        val navOptions = animationsManager.getNavOptionsFoFragment(destination, previousFragment)

        when (destination){
            FragmentType.PROFILE -> navController.navigate(R.id.profileFragment, null, navOptions)
            FragmentType.LIST -> navController.navigate(R.id.consultationListFragment, null, navOptions)
            FragmentType.ADD -> navController.navigate(R.id.addConsultationFragment, null, navOptions)
        }




    }
}