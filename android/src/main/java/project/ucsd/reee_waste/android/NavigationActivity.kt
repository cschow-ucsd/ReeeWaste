package project.ucsd.reee_waste.android

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.serialization.UnstableDefault
import project.ucsd.reee_waste.android.ui.NoSwipePagerAdapter
import project.ucsd.reee_waste.android.ui.rw.DashboardFragment
import project.ucsd.reee_waste.android.ui.rw.ListingsFragment
import project.ucsd.reee_waste.android.ui.rw.SettingsFragment
import project.ucsd.reee_waste.backendless.service.RwService

class NavigationActivity : AppCompatActivity() {
    private val rwServiceViewModel: RwServiceViewModel by viewModels()

    private val fragmentItems: IntArray by lazy {
        intArrayOf(R.id.menu_item_dashboard, R.id.menu_item_listings, R.id.menu_item_settings)
    }

    @UnstableDefault
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        rwServiceViewModel.rwServiceInstance.userToken =
                getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
                        .getString(RwService.USER_TOKEN, null)

        navNavNav.setOnNavigationItemSelectedListener(this::handleMenuItemSelected)
        vpNavContainer.adapter = NoSwipePagerAdapter(
                fm = supportFragmentManager,
                fragments = arrayOf(DashboardFragment(), ListingsFragment(), SettingsFragment())
        )
    }

    private fun handleMenuItemSelected(
            menuItem: MenuItem
    ): Boolean {
        val fragmentIndex = fragmentItems.indexOf(menuItem.itemId)
        vpNavContainer.setCurrentItem(fragmentIndex, true)
        return true
    }
}