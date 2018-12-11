package com.ilpcoursework.coinz


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
/**
 *
 *  the test assumes that there is no testuser test5 with email test5@126.com existing on the cloud databsae or authentication.
 *  it is therefore highly recommanded that all accounts that start with test.* be deleted before the automated tests run
 *  also , change the autologin sign to false to disable auto login in the login activity to allow the test to run
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class MywalletActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(LoginActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_CONTACTS",
                    "android.permission.ACCESS_FINE_LOCATION")

    @Test
    fun mywalletActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val appCompatButton = onView(
                allOf(withId(R.id.sign_up_button), withText("Register"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.constraint.ConstraintLayout")),
                                        1),
                                5),
                        isDisplayed()))
        appCompatButton.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val appCompatAutoCompleteTextView = onView(
                allOf(withId(R.id.username),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)))
        appCompatAutoCompleteTextView.perform(scrollTo(), replaceText("test"), closeSoftKeyboard())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val appCompatAutoCompleteTextView2 = onView(
                allOf(withId(R.id.username), withText("test"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)))
        appCompatAutoCompleteTextView2.perform(scrollTo(), replaceText("test5"))

        val appCompatAutoCompleteTextView3 = onView(
                allOf(withId(R.id.username), withText("test5"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()))
        appCompatAutoCompleteTextView3.perform(closeSoftKeyboard())

        val appCompatAutoCompleteTextView4 = onView(
                allOf(withId(R.id.email),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)))
        appCompatAutoCompleteTextView4.perform(scrollTo(), replaceText("test5@126.com"), closeSoftKeyboard())

        val appCompatEditText = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)))
        appCompatEditText.perform(scrollTo(), replaceText("testtest"), closeSoftKeyboard())

        val appCompatButton2 = onView(
                allOf(withId(R.id.sign_up_button), withText("Sign up"),
                        childAtPosition(
                                allOf(withId(R.id.email_login_form),
                                        childAtPosition(
                                                withId(R.id.login_form),
                                                0)),
                                3)))
        appCompatButton2.perform(scrollTo(), click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val appCompatButton3 = onView(
                allOf(withId(R.id.button), withText("Got it!"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()))
        appCompatButton3.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)
//
//        val zoomButton = onView(
//            withClassName(`is`("android.widget.ZoomButton")))
//        zoomButton.perform(click())

        val appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(`is`("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()))
        appCompatImageButton.perform(click())

        val navigationMenuItemView = onView(
                withText("myinventory"))
        navigationMenuItemView.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val textView = onView(
                allOf(withText("My Inventory"),

                        isDisplayed()))
        textView.check(matches(withText("My Inventory")))

        val imageButton = onView(
                allOf(withId(R.id.imageButton),

                        isDisplayed()))
        imageButton.check(matches(isDisplayed()))

        val textView2 = onView(
                allOf(withId(R.id.textView3), withText("Today's Rate!"),

                        isDisplayed()))
        textView2.check(matches(withText("Today's Rate!")))

        val imageButton2 = onView(
                allOf(withId(R.id.blue_button),

                        isDisplayed()))
        imageButton2.check(matches(isDisplayed()))

        val textView3 = onView(
                allOf(withId(R.id.greenrate), withText("83 gold per kg."),

                        isDisplayed()))
        textView3.check(matches(isDisplayed()))

        val appCompatImageButton2 = onView(
                allOf(withId(R.id.blue_button),

                        isDisplayed()))
        appCompatImageButton2.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val textView4 = onView(
                allOf(withText("sub inventory"),

                        isDisplayed()))
        textView4.check(matches(withText("sub inventory")))
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
