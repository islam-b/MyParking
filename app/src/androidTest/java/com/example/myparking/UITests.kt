package com.example.myparking


import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.Espresso.onView

import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.MediumTest
import androidx.test.rule.ActivityTestRule
import com.example.myparking.activities.LoginActivity
import kotlinx.android.synthetic.main.activity_home.view.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityUITest {
    private lateinit var username: String
    private lateinit var password: String

    @get:Rule
    var activityRule: ActivityTestRule<LoginActivity> = ActivityTestRule(LoginActivity::class.java)



    @Test
    fun `ensureLoginCorrect`() {
        // Type text and then press the button.
        username = "amira"
        password = "password"
        onView(withId(R.id.user_name))
            .perform(
                typeText(username), closeSoftKeyboard()
            )
        onView(withId(R.id.user_password)).perform(
            typeText(password)
        )

        onView(withId(R.id.login_btn)).perform(click())
        // Check progress bar is visible.
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
    }
}

/*onView(withId(R.id.user_name)).check(matches(withText(username)))
// ...
*//*onView(withId(R.id.changeText)).perform(click())*/


@RunWith(AndroidJUnit4::class)
@LargeTest
class ParkingListUITest {

    @get:Rule
    var activityRule: ActivityTestRule<HomeActivity> = ActivityTestRule(HomeActivity::class.java)


    @Test
    fun `ensureHomeActivityToListScenario`() {
        // check near parkings load
        onView(withId(R.id.home_id_all)).check(matches(hasDescendant(withText("Parkings autour de vous"))))
        // check see parling list works
        onView(withId(R.id.see_more_parkings))
            .perform(click())
        onView(withId(R.id.parkings_list)).check(matches(hasDescendant(withText("Parking"))))

    }
    @Test
    fun `ensureFilterDialogIsDisplayed`(){
        onView(withId(R.id.nav_view)).perform(click())
        onView(withText("Filtrer")).check(matches(isDisplayed()))
    }
}

