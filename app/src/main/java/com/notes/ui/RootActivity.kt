package com.notes.ui

import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.notes.R
import com.notes.databinding.ActivityRootBinding
import com.notes.ui._base.FragmentNavigator
import com.notes.ui.list.NoteListFragment

class RootActivity : AppCompatActivity(), FragmentNavigator {
    private var viewBinding: ActivityRootBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityRootBinding.inflate(layoutInflater)
        this.viewBinding = viewBinding
        setContentView(viewBinding.root)
        if(!isExisting(R.id.container)) {
            supportFragmentManager
                .beginTransaction()
                .add(
                    viewBinding.container.id,
                    NoteListFragment()
                )
                .commit()
        }
    }

    override fun navigateTo(
        fragment: Fragment
    ) {
        val viewBinding = this.viewBinding ?: return
        supportFragmentManager
            .beginTransaction()
            .replace(
                viewBinding.container.id,
                fragment
            )
            .addToBackStack(fragment.toString())
            .commit()
    }

    private fun isExisting(@IdRes id: Int): Boolean{
        return supportFragmentManager.findFragmentById(id) != null
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

}