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
 *  the test assumes that there is no testuser test7 with email test7@126.com existing on the cloud databsae or authentication.
 *  it is therefore highly recommanded that all accounts that start with test.* be deleted before the automated tests run
 *  also , change the autologin sign to false to disable auto login in the login activity to allow the test to run
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class NavigationViewTest {

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
    fun navigationViewTest() {
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
        appCompatAutoCompleteTextView2.perform(scrollTo(), replaceText("test7"))

        val appCompatAutoCompleteTextView3 = onView(
                allOf(withId(R.id.username), withText("test7"),
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
        appCompatAutoCompleteTextView4.perform(scrollTo(), replaceText("test7@126.com"), closeSoftKeyboard())

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

        val appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),

                        isDisplayed()))
        appCompatImageButton.perform(click())

        val navigationMenuItemView = onView(
                withText("myinventory"))
        navigationMenuItemView.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val appCompatImageButton2 = onView(
                allOf(withContentDescription("Open navigation drawer"),

                        isDisplayed()))
        appCompatImageButton2.perform(click())

        val navigationMenuItemView2 = onView(
                withText("myprofile"))
        navigationMenuItemView2.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val appCompatImageButton3 = onView(
                allOf(withContentDescription("Open navigation drawer"),

                        isDisplayed()))
        appCompatImageButton3.perform(click())

        val navigationMenuItemView3 = onView(
                withText("map"))
        navigationMenuItemView3.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val textView = onView(
                allOf(withText("map"),

                        isDisplayed()))
        textView.check(matches(withText("map")))

        val imageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),

                        isDisplayed()))
        imageButton.check(matches(isDisplayed()))

        val appCompatImageButton4 = onView(
                allOf(withContentDescription("Open navigation drawer"),

                        isDisplayed()))
        appCompatImageButton4.perform(click())

        val navigationMenuItemView4 = onView(
                withText("companions"))
        navigationMenuItemView4.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val appCompatImageButton5 = onView(
                allOf(withContentDescription("Open navigation drawer"),

                        isDisplayed()))
        appCompatImageButton5.perform(click())
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
