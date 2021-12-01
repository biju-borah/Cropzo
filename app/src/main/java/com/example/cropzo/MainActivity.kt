package com.example.cropzo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cropzo.fragment.CartFragment
import com.example.cropzo.fragment.CategoryFragment
import com.example.cropzo.fragment.HomeFragment
import com.example.cropzo.fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val home = HomeFragment()
        val category = CategoryFragment()
        val cart = CartFragment()
        val profile = ProfileFragment()

        setCurrentFragment(home)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->setCurrentFragment(home)
                R.id.category->setCurrentFragment(category)
                R.id.cart->setCurrentFragment(cart)
                R.id.profile->setCurrentFragment(profile)

            }
            true
        }

    }

    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
}