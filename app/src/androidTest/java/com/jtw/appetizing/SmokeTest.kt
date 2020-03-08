package com.jtw.appetizing

// import org.junit.Test
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class SmokeTest {
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun applicationSmokeTest() {
        Thread.sleep(2000)
        onView(withText("Breakfast")).perform(click())

        Thread.sleep(2000)
        onView(withText("Full English Breakfast")).perform(click())

        Thread.sleep(2000)

        val stringMatcher = object : BaseMatcher<String>() {
            override fun describeTo(description: Description?) {
            }

            override fun matches(item: Any?): Boolean {
                if (item !is String) {
                    return false
                }

                return item.contains("Greasy") &&
                        item.contains("UnHealthy") &&
                        item.contains("HangoverFood")
            }

        }
        onView(withText(stringMatcher)).check(matches(isDisplayed()));
    }

}