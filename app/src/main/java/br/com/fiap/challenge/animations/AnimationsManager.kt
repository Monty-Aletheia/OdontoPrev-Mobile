package br.com.fiap.challenge.animations

import androidx.navigation.NavOptions
import br.com.fiap.challenge.R
import br.com.fiap.challenge.models.FragmentType

class AnimationsManager {

    private fun getExitAnimation(currentFragment: FragmentType, previousFragment: FragmentType? = null): Int {
        return when (currentFragment){
            FragmentType.PROFILE -> R.anim.slide_out_right
            FragmentType.LIST -> R.anim.slide_out_left
            FragmentType.ADD -> {
                if (previousFragment == FragmentType.PROFILE){
                    R.anim.slide_out_left
                } else {
                    R.anim.slide_out_right
                }
            }
        }
    }


    private fun getEnterAnimation(currentFragment: FragmentType,previousFragment: FragmentType? = null): Int {
        return when(currentFragment){
            FragmentType.PROFILE -> R.anim.slide_in_left
            FragmentType.LIST -> R.anim.slide_in_right
            FragmentType.ADD -> {
                if (previousFragment == FragmentType.PROFILE){
                    R.anim.slide_in_right
                } else {
                    R.anim.slide_in_left
                }
            }

        }

    }


    fun getNavOptionsFoFragment(currentFragment: FragmentType, previousFragment: FragmentType? = null): NavOptions {
        return NavOptions.Builder()
            .setEnterAnim(getEnterAnimation(currentFragment, previousFragment))
            .setExitAnim(getExitAnimation(currentFragment, previousFragment))
            .build()

    }

}

