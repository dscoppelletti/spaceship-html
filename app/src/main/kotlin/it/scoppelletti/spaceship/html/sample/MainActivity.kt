package it.scoppelletti.spaceship.html.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import it.scoppelletti.spaceship.html.app.HtmlViewFragment
import it.scoppelletti.spaceship.html.sample.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val fragment = supportFragmentManager.findFragmentById(
                R.id.contentFrame)
        if (fragment == null) {
            navigateToFragment(R.id.cmd_htmlview)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (navigateToFragment(item.itemId)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun navigateToFragment(itemId: Int): Boolean {
        val fragment = when (itemId) {
            R.id.cmd_htmlview -> {
                HtmlViewFragment.newInstance(
                        HtmlViewFragment.URL_RESOURCE + "raw/test.html")
            }

            R.id.cmd_htmltext -> {
                HtmlTextFragment.newInstance()
            }

            else -> {
                return false
            }
        }

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.contentFrame, fragment)
                .commit()

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        when (supportFragmentManager.findFragmentById(R.id.contentFrame)) {
            is HtmlViewFragment -> {
                menu?.findItem(R.id.cmd_htmlview)?.isChecked = true
            }

            is HtmlTextFragment -> {
                menu?.findItem(R.id.cmd_htmltext)?.isChecked = true
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }
}
