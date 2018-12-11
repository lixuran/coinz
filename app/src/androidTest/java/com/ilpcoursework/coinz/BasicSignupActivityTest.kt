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
 *  the test assumes that there is no testuser test4 with email test4@126.com existing on the cloud databsae or authentication.
 *  it is therefore highly recommanded that all accounts that start with test.* be deleted before the automated tests run
 *  also , change the autologin sign to false to disable auto login in the login activity to allow the test to run
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class BasicSignupActivityTest {

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
    fun basicSignupActivityTest() {
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

        val appCompatAutoCompleteTextView6 = onView(
                allOf(withId(R.id.username),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)))
        appCompatAutoCompleteTextView6.perform(scrollTo(), replaceText("test4"))

        val appCompatAutoCompleteTextView7 = onView(
                allOf(withId(R.id.username), withText("test4"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()))
        appCompatAutoCompleteTextView7.perform(closeSoftKeyboard())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val appCompatAutoCompleteTextView8 = onView(
                allOf(withId(R.id.email),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)))
        appCompatAutoCompleteTextView8.perform(scrollTo(), replaceText("test4@126.com"), closeSoftKeyboard())

        val appCompatEditText = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)))
        appCompatEditText.perform(scrollTo(), replaceText("testtest"), closeSoftKeyboard())



        val appCompatAutoCompleteTextView10 = onView(
                allOf(withId(R.id.email), withText("test4@126.com"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()))
        appCompatAutoCompleteTextView10.perform(closeSoftKeyboard())

        val appCompatAutoCompleteTextView12 = onView(
                allOf(withId(R.id.username), withText("test4"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()))
        appCompatAutoCompleteTextView12.perform(closeSoftKeyboard())

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

        val textView = onView(
                allOf(withId(R.id.gold_view), withText("0"),

                        isDisplayed()))
        textView.check(matches(withText("0")))

        val textView2 = onView(
                allOf(withId(R.id.user_name), withText("test4"),

                        isDisplayed()))
        textView2.check(matches(withText("test4")))

        val textView3 = onView(
                allOf(withId(R.id.user_email), withText("test4@126.com"),

                        isDisplayed()))
        textView3.check(matches(withText("test4@126.com")))

        val imageView = onView(
                allOf(withId(R.id.navhead_yellowcoinimgView),

                        isDisplayed()))
        imageView.check(matches(isDisplayed()))

        val imageView2 = onView(
                allOf(withId(R.id.navhead_goldimgView),

                        isDisplayed()))
        imageView2.check(matches(isDisplayed()))

        val textView4 = onView(
                allOf(withId(R.id.quid_view), withText("0"),

                        isDisplayed()))
        textView4.check(matches(withText("0")))

        val navigationMenuItemView = onView(
                withText("myinventory"))
        navigationMenuItemView.perform(click())
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
