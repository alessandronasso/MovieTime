package com.lab.movietime.View.Activity.Activity.Activity


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.lab.movietime.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SplashActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(SplashActivity::class.java)

    @Test
    fun splashActivityTest() {
        val appCompatImageView = onView(
                allOf(withId(R.id.play_pause_button), withContentDescription("Play button"),
                        childAtPosition(
                                allOf(withId(R.id.controls_container),
                                        childAtPosition(
                                                withClassName(`is`("android.widget.FrameLayout")),
                                                1)),
                                4),
                        isDisplayed()))
        appCompatImageView.perform(click())

        val appCompatImageView2 = onView(
                allOf(withId(R.id.play_pause_button), withContentDescription("Play button"),
                        childAtPosition(
                                allOf(withId(R.id.controls_container),
                                        childAtPosition(
                                                withClassName(`is`("android.widget.FrameLayout")),
                                                1)),
                                4),
                        isDisplayed()))
        appCompatImageView2.perform(click())

        val materialButton = onView(
                allOf(withId(R.id.favButton), withText("Add to watchlist"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        1),
                                4)))
        materialButton.perform(scrollTo(), click())

        pressBack()

        pressBack()

        val bottomNavigationItemView = onView(
                allOf(withId(R.id.bbn_item2), withContentDescription("Search"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.BottomNavigation),
                                        0),
                                1),
                        isDisplayed()))
        bottomNavigationItemView.perform(click())

        pressBack()

        val materialCheckBox = onView(
                allOf(withId(R.id.checkbox_adv), withText("Advanced search"),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout2),
                                        childAtPosition(
                                                withId(R.id.fragment_container),
                                                0)),
                                3),
                        isDisplayed()))
        materialCheckBox.perform(click())

        val appCompatSpinner = onView(
                allOf(withId(R.id.spinnerGenre),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.hiddenTab),
                                        0),
                                1),
                        isDisplayed()))
        appCompatSpinner.perform(click())


        val appCompatEditText = onView(
                allOf(withId(R.id.releaseYear),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.hiddenTab),
                                        1),
                                1),
                        isDisplayed()))
        appCompatEditText.perform(replaceText("2010\n"), closeSoftKeyboard())

        val appCompatSpinner2 = onView(
                allOf(withId(R.id.spinnerVote),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.hiddenTab),
                                        2),
                                2),
                        isDisplayed()))
        appCompatSpinner2.perform(click())

        val appCompatSpinner3 = onView(
                allOf(withId(R.id.spinnerOperator),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.hiddenTab),
                                        2),
                                1),
                        isDisplayed()))
        appCompatSpinner3.perform(click())

        val appCompatSpinner4 = onView(
                allOf(withId(R.id.spinnerOperator),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.hiddenTab),
                                        2),
                                1),
                        isDisplayed()))
        appCompatSpinner4.perform(click())


        val appCompatSpinner5 = onView(
                allOf(withId(R.id.spinnerVote),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.hiddenTab),
                                        2),
                                2),
                        isDisplayed()))
        appCompatSpinner5.perform(click())

        val appCompatImageButton = onView(
                allOf(withId(R.id.search_btn),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout2),
                                        childAtPosition(
                                                withId(R.id.fragment_container),
                                                0)),
                                2),
                        isDisplayed()))
        appCompatImageButton.perform(click())

        val floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout2),
                                        childAtPosition(
                                                withId(R.id.fragment_container),
                                                0)),
                                10),
                        isDisplayed()))
        floatingActionButton.perform(click())

        val floatingActionButton2 = onView(
                allOf(withId(R.id.fab2),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout2),
                                        childAtPosition(
                                                withId(R.id.fragment_container),
                                                0)),
                                8),
                        isDisplayed()))
        floatingActionButton2.perform(click())

        val floatingActionButton3 = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout2),
                                        childAtPosition(
                                                withId(R.id.fragment_container),
                                                0)),
                                10),
                        isDisplayed()))
        floatingActionButton3.perform(click())

        val floatingActionButton4 = onView(
                allOf(withId(R.id.fab1),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout2),
                                        childAtPosition(
                                                withId(R.id.fragment_container),
                                                0)),
                                9),
                        isDisplayed()))
        floatingActionButton4.perform(click())

        val floatingActionButton5 = onView(
                allOf(withId(R.id.fab),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout2),
                                        childAtPosition(
                                                withId(R.id.fragment_container),
                                                0)),
                                10),
                        isDisplayed()))
        floatingActionButton5.perform(click())

        val floatingActionButton6 = onView(
                allOf(withId(R.id.fab3),
                        childAtPosition(
                                allOf(withId(R.id.linearLayout2),
                                        childAtPosition(
                                                withId(R.id.fragment_container),
                                                0)),
                                7),
                        isDisplayed()))
        floatingActionButton6.perform(click())

        val bottomNavigationItemView2 = onView(
                allOf(withId(R.id.bbn_item3), withContentDescription("Watchlist"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.BottomNavigation),
                                        0),
                                2),
                        isDisplayed()))
        bottomNavigationItemView2.perform(click())

        pressBack()
    }

    private fun childAtPosition(
            parentMatcher: Matcher<View>, position: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
