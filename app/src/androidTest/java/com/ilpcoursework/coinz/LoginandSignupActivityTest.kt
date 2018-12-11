package com.ilpcoursework.coinz


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
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
 *  the test assumes that there is no testuser test3 with email test3@126.com existing on the cloud databsae or authentication.
 *  it is therefore highly recommanded that all accounts that start with test.* be deleted before the automated tests run
 *  also , change the autologin sign to false to disable auto login in the login activity to allow the test to run
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginandSignupActivityTest {

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
    fun loginandSignupActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val textView = onView(
                allOf(withId(R.id.title), withText("Skyrim Local Edition"),

                        isDisplayed()))
        textView.check(matches(withText("Skyrim Local Edition")))

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val textView2 = onView(
                allOf(withId(R.id.subtitle), withText("also known as coinZ"),


                        isDisplayed()))
        textView2.check(matches(withText("also known as coinZ")))

        val textView3 = onView(
                allOf(withId(R.id.welcomeinfo), withText("Welcome to Whiterun!"),

                        isDisplayed()))
        textView3.check(matches(withText("Welcome to Whiterun!")))


        val button = onView(
                allOf(withId(R.id.email_sign_in_button),

                        isDisplayed()))
        button.check(matches(isDisplayed()))

        val button2 = onView(
                allOf(withId(R.id.sign_up_button),

                        isDisplayed()))
        button2.check(matches(isDisplayed()))

        val button3 = onView(
                allOf(withId(R.id.sign_up_button),

                        isDisplayed()))
        button3.check(matches(isDisplayed()))

        val appCompatButton = onView(
                allOf(withId(R.id.sign_up_button), withText("Register"),

                        isDisplayed()))
        appCompatButton.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        pressBack()

        val textView4 = onView(
                allOf(withId(R.id.title2), withText("Skyrim Local Edition"),

                        isDisplayed()))
        textView4.check(matches(withText("Skyrim Local Edition")))

        val textView5 = onView(
                allOf(withId(R.id.subtitle2), withText("also known as coinZ"),

                        isDisplayed()))
        textView5.check(matches(withText("also known as coinZ")))

        val textView6 = onView(
                allOf(withId(R.id.welcomeinfo2), withText("Welcome to Whiterun!"),

                        isDisplayed()))
        textView6.check(matches(withText("Welcome to Whiterun!")))


        val button4 = onView(
                allOf(withId(R.id.sign_up_button),

                        isDisplayed()))
        button4.check(matches(isDisplayed()))

        val button5 = onView(
                allOf(withId(R.id.email_sign_in_button),

                        isDisplayed()))
        button5.check(matches(isDisplayed()))

        val button6 = onView(
                allOf(withId(R.id.email_sign_in_button),

                        isDisplayed()))
        button6.check(matches(isDisplayed()))

        val appCompatAutoCompleteTextView = onView(
                allOf(withId(R.id.username)
                        ))
        appCompatAutoCompleteTextView.perform(scrollTo(), replaceText("test3"), closeSoftKeyboard())

        val appCompatAutoCompleteTextView2 = onView(
                allOf(withId(R.id.email)))
        appCompatAutoCompleteTextView2.perform(scrollTo(), replaceText("test3@126.co"), closeSoftKeyboard())

        val appCompatEditText = onView(
                allOf(withId(R.id.password)))
        appCompatEditText.perform(scrollTo(), replaceText("tes"), closeSoftKeyboard())

        val appCompatAutoCompleteTextView3 = onView(
                allOf(withId(R.id.email), withText("test3@126.co")))
        appCompatAutoCompleteTextView3.perform(scrollTo(), replaceText("test3@126.com"))

        val appCompatAutoCompleteTextView4 = onView(
                allOf(withId(R.id.email), withText("test3@126.com"),

                        isDisplayed()))
        appCompatAutoCompleteTextView4.perform(closeSoftKeyboard())

        val appCompatEditText2 = onView(
                allOf(withId(R.id.password), withText("tes")))
        appCompatEditText2.perform(scrollTo(), replaceText("testtest"))

        val appCompatEditText3 = onView(
                allOf(withId(R.id.password), withText("testtest"),

                        isDisplayed()))
        appCompatEditText3.perform(closeSoftKeyboard())

        val appCompatButton2 = onView(
                allOf(withId(R.id.sign_up_button), withText("Sign up")))
        appCompatButton2.perform(scrollTo(), click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(15000)

        val textView7 = onView(
                allOf(withId(R.id.textView2), withText("Welcome, Companion, to the city of Whiterun! This City has heard of your bravery on the battel field, and the yarl himself has been deeply improessed.\n\n        As his stward, it would be my pleasure to bring his lordship's offer to you.As you might have seen on the way here, the country of whiterun is currently possessed by ancient dragons from the second era.\n\n        Many has died under their claws.The people need a savior, now  more than ever. Therefore the yarl has ordered you, as well as other advantures, to fight against those monsters, beat them and bring back peace and hope to the land of Skyrim.\n      The location of their nest has been marked on your map, be sure to note it down before you set off. As you slay those dragons, you would get different kinds of dragon scales,\n        a valuable resource that can be sold for gold, or even better,used to purchase properties from me! Since this is war time, our fianase is relatively short,the yarl is only willing to pay for 25 pieces of scales per day,,but you can always send your spare scales to your fellow companions so that they may put it to better use! \n\n        Here is a copy of the yarl's contract in case you would like to find out more about it. Good luck and may we meet in Sovngarde again!"),

                        isDisplayed()))
        textView7.check(matches(isDisplayed()))

        val button7 = onView(
                allOf(withId(R.id.button),

                        isDisplayed()))
        button7.check(matches(isDisplayed()))

        val appCompatButton3 = onView(
                allOf(withId(R.id.button), withText("Got it!"),

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

        val textView8 = onView(
                allOf(withText("map"),

                        isDisplayed()))
        textView8.check(matches(withText("map")))

        val imageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),

                        isDisplayed()))
        imageButton.check(matches(isDisplayed()))

        val appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar)
                                        ),
                                1),
                        isDisplayed()))
        appCompatImageButton.perform(click())

        val navigationMenuItemView = onView(
                withText("sign out"))
        navigationMenuItemView.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val button8 = onView(
                allOf(withId(R.id.email_sign_in_button),
                        isDisplayed()))
        button8.check(matches(isDisplayed()))

        val textView9 = onView(
                allOf(withId(R.id.title), withText("Skyrim Local Edition"),

                        isDisplayed()))
        textView9.check(matches(withText("Skyrim Local Edition")))

        val appCompatAutoCompleteTextView5 = onView(
                allOf(withId(R.id.email)))
        appCompatAutoCompleteTextView5.perform(scrollTo(), click())

        val appCompatAutoCompleteTextView6 = onView(
                allOf(withId(R.id.email),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)))
        appCompatAutoCompleteTextView6.perform(scrollTo(), click())

        val appCompatAutoCompleteTextView7 = onView(
                allOf(withId(R.id.email),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)))
        appCompatAutoCompleteTextView7.perform(scrollTo(), replaceText("test3@126.com"), closeSoftKeyboard())

        val appCompatEditText4 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)))
        appCompatEditText4.perform(scrollTo(), replaceText("testtest"), closeSoftKeyboard())

        val appCompatAutoCompleteTextView100 = onView(
                allOf(withId(R.id.email), withText("test3@126.com")))
        appCompatAutoCompleteTextView100.perform(scrollTo(), replaceText("ttttff"))

        val appCompatButton4 = onView(
                allOf(withId(R.id.email_sign_in_button), withText("Sign in"),
                        childAtPosition(
                                allOf(withId(R.id.email_login_form),
                                        childAtPosition(
                                                withId(R.id.login_form),
                                                0)),
                                2)))
        appCompatButton4.perform(scrollTo(), click())

        val button9 = onView(
                allOf(withId(R.id.email_sign_in_button),
                        childAtPosition(
                                allOf(withId(R.id.email_login_form),
                                        childAtPosition(
                                                withId(R.id.login_form),
                                                0)),
                                2),
                        isDisplayed()))
        button9.check(matches(isDisplayed()))



        val appCompatAutoCompleteTextView10 = onView(
                allOf(withId(R.id.email), withText("ttttff")))
        appCompatAutoCompleteTextView10.perform(scrollTo(), replaceText("test3@126.com"))

        val appCompatAutoCompleteTextView11 = onView(
                allOf(withId(R.id.email), withText("test3@126.com"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()))
        appCompatAutoCompleteTextView11.perform(closeSoftKeyboard())



        val appCompatButton7 = onView(
                allOf(withId(R.id.email_sign_in_button), withText("Sign in"),
                        childAtPosition(
                                allOf(withId(R.id.email_login_form),
                                        childAtPosition(
                                                withId(R.id.login_form),
                                                0)),
                                2)))
        appCompatButton7.perform(scrollTo(), click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(15000)

        val textView11 = onView(
                allOf(withText("map"),

                        isDisplayed()))
        textView11.check(matches(withText("map")))
        val appCompatImageButton2 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(`is`("android.support.design.widget.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()))
        appCompatImageButton2.perform(click())

        val navigationMenuItemView2 = onView(
                withText("sign out"))
        navigationMenuItemView2.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val appCompatButton8 = onView(
                allOf(withId(R.id.sign_up_button), withText("Register"),

                        isDisplayed()))
        appCompatButton8.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val appCompatAutoCompleteTextView12 = onView(
               withId(R.id.username))
        appCompatAutoCompleteTextView12.perform(scrollTo(), replaceText("test3"), closeSoftKeyboard())

        val appCompatAutoCompleteTextView13 = onView(
               withId(R.id.email))
        appCompatAutoCompleteTextView13.perform(scrollTo(), replaceText("test3@126.com"), closeSoftKeyboard())

        val appCompatEditText9 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)))
        appCompatEditText9.perform(scrollTo(), replaceText("testtest"), closeSoftKeyboard())

        val appCompatButton9 = onView(
                allOf(withId(R.id.sign_up_button), withText("Sign up"),
                        childAtPosition(
                                allOf(withId(R.id.email_login_form),
                                        childAtPosition(
                                                withId(R.id.login_form),
                                                0)),
                                3)))
        appCompatButton9.perform(scrollTo(), click())

        val textView12 = onView(
                allOf(withId(R.id.title2), withText("Skyrim Local Edition"),

                        isDisplayed()))
        textView12.check(matches(withText("Skyrim Local Edition")))

        val button12 = onView(
                allOf(withId(R.id.sign_up_button),

                        isDisplayed()))
        button12.check(matches(isDisplayed()))

        val appCompatAutoCompleteTextView14 = onView(
                allOf(withId(R.id.email), withText("test3@126.com"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)))
        appCompatAutoCompleteTextView14.perform(scrollTo(), click())

        val appCompatAutoCompleteTextView15 = onView(
                allOf(withId(R.id.username), withText("test3"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)))
        appCompatAutoCompleteTextView15.perform(scrollTo(), replaceText("test4"))

        val appCompatAutoCompleteTextView16 = onView(
                allOf(withId(R.id.username), withText("test4"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()))
        appCompatAutoCompleteTextView16.perform(closeSoftKeyboard())

        val appCompatButton10 = onView(
                allOf(withId(R.id.sign_up_button), withText("Sign up"),
                        childAtPosition(
                                allOf(withId(R.id.email_login_form),
                                        childAtPosition(
                                                withId(R.id.login_form),
                                                0)),
                                3)))
        appCompatButton10.perform(scrollTo(), click())

        val textView13 = onView(
                allOf(withId(R.id.title2), withText("Skyrim Local Edition"),

                        isDisplayed()))
        textView13.check(matches(withText("Skyrim Local Edition")))

        val button13 = onView(
                allOf(withId(R.id.sign_up_button),

                        isDisplayed()))
        button13.check(matches(isDisplayed()))

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(50)

        val appCompatAutoCompleteTextView17 = onView(
                allOf(withId(R.id.email), withText("test3@126.com"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)))
        appCompatAutoCompleteTextView17.perform(scrollTo(), click())

        val appCompatAutoCompleteTextView18 = onView(
                allOf(withId(R.id.email), withText("test3@126.com"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)))
        appCompatAutoCompleteTextView18.perform(scrollTo(), replaceText("test4@126.com"))

        val appCompatAutoCompleteTextView19 = onView(
                allOf(withId(R.id.email), withText("test4@126.com"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()))
        appCompatAutoCompleteTextView19.perform(closeSoftKeyboard())

        val appCompatEditText13 = onView(
                allOf(withId(R.id.password), withText("testtest"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)))
        appCompatEditText13.perform(scrollTo(), replaceText(""))

        val appCompatEditText14 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()))
        appCompatEditText14.perform(closeSoftKeyboard())

        val appCompatEditText15 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)))
        appCompatEditText15.perform(scrollTo(), click())

        val appCompatEditText16 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)))
        appCompatEditText16.perform(scrollTo(), replaceText("t"), closeSoftKeyboard())

        val appCompatButton11 = onView(
                allOf(withId(R.id.sign_up_button), withText("Sign up"),
                        childAtPosition(
                                allOf(withId(R.id.email_login_form),
                                        childAtPosition(
                                                withId(R.id.login_form),
                                                0)),
                                3)))
        appCompatButton11.perform(scrollTo(), click())

        val button14 = onView(
                allOf(withId(R.id.sign_up_button),
                        childAtPosition(
                                allOf(withId(R.id.email_login_form),
                                        childAtPosition(
                                                withId(R.id.login_form),
                                                0)),
                                3),
                        isDisplayed()))
        button14.check(matches(isDisplayed()))
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
