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
 *  the test assumes that there is no testuser test6 with email test6@126.com existing on the cloud databsae or authentication.
 *  it is therefore highly recommanded that all accounts that start with test.* be deleted before the automated tests run
 *  also , change the autologin sign to false to disable auto login in the login activity to allow the test to run
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class ProfileActivityTest {

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
    fun profileActivityTest() {
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
        appCompatAutoCompleteTextView.perform(scrollTo(), replaceText("test6"), closeSoftKeyboard())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val appCompatAutoCompleteTextView2 = onView(
                allOf(withId(R.id.email),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(`is`("android.support.design.widget.TextInputLayout")),
                                        0),
                                0)))
        appCompatAutoCompleteTextView2.perform(scrollTo(), replaceText("test6@126.com"), closeSoftKeyboard())

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
                withText("myprofile"))
        navigationMenuItemView.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val textView = onView(
                allOf(withText("profile"),

                        isDisplayed()))
        textView.check(matches(withText("profile")))

        val imageView = onView(
                allOf(withId(R.id.imagedolr),

                        isDisplayed()))
        imageView.check(matches(isDisplayed()))

        val imageView2 = onView(
                allOf(withId(R.id.imagequid),

                        isDisplayed()))
        imageView2.check(matches(isDisplayed()))

        val imageView3 = onView(
                allOf(withId(R.id.imageshil),

                        isDisplayed()))
        imageView3.check(matches(isDisplayed()))

        val imageView4 = onView(
                allOf(withId(R.id.imagepeny),

                        isDisplayed()))
        imageView4.check(matches(isDisplayed()))

        val imageView5 = onView(
                allOf(withId(R.id.imagegold),

                        isDisplayed()))
        imageView5.check(matches(isDisplayed()))

        val textView2 = onView(
                allOf(withId(R.id.dolr), withText("ancient dragon bone: 0 kg"),

                        isDisplayed()))
        textView2.check(matches(withText("ancient dragon bone: 0 kg")))

        val textView3 = onView(
                allOf(withId(R.id.quid), withText("blood dragon bone: 0 kg"),

                        isDisplayed()))
        textView3.check(matches(withText("blood dragon bone: 0 kg")))

        val textView4 = onView(
                allOf(withId(R.id.shil), withText("frost dragon bone: 0 kg"),

                        isDisplayed()))
        textView4.check(matches(withText("frost dragon bone: 0 kg")))

        val textView5 = onView(
                allOf(withId(R.id.peny), withText("fire dragon bone: 0 kg"),

                        isDisplayed()))
        textView5.check(matches(withText("fire dragon bone: 0 kg")))

        val textView6 = onView(
                allOf(withId(R.id.gold), withText("gold dragon bone: 0"),

                        isDisplayed()))
        textView6.check(matches(withText("gold dragon bone: 0")))

        val button = onView(
                allOf(withId(R.id.contract_button),

                        isDisplayed()))
        button.check(matches(isDisplayed()))

        val button2 = onView(
                allOf(withId(R.id.signout_button),

                        isDisplayed()))
        button2.check(matches(isDisplayed()))

        val button3 = onView(
                allOf(withId(R.id.signout_button),

                        isDisplayed()))
        button3.check(matches(isDisplayed()))

        val appCompatButton4 = onView(
                allOf(withId(R.id.contract_button), withText("yarl's Contract"),

                        isDisplayed()))
        appCompatButton4.perform(click())

        val textView7 = onView(
                allOf(withId(R.id.textView4), withText("The Yarl's Contract"),

                        isDisplayed()))
        textView7.check(matches(withText("The Yarl's Contract")))

        val textView8 = onView(
                allOf(withId(R.id.textView5), withText("let it here by be known, it is the yarl's will that the following facts should be known, and rules be complied when come to the issues around bounty on the dragons:\n\n        1. the dragons that roam around the country side , when approached and killed by the advantures, drop dragon bones.It is a valuable resource as it can be made into one of the finest armors and weapons(that is, if you are level 100 at blacksmith, though) Different type of dragons drop different type of scales.\n        2. the dragon bones that is collected from the dragons can be exchange to gold , subject to exchange rate given by the steward, which would update everyday as the steward sees fit.. larger piece of scale would have better price porpotional to the size.the advantagers can exchange at most 25 pieces of scales for gold at each day.\n        3. the dragon bones that hasn't been exchanged for gold yet get stored in the your inventory, which you can use to send to a friend as a gift.Anyone coin that received as a gift can be exchanged for gold, even if the daily exchange limit has been reached.\n        4. you can only send bones to those advantures you know. you can add companions by their username. you can send at most 30 coins a day.\n        5. under the yarl 's generous offer, advantures can use their gold to purchase any properties and shops available in the city, provided that they have enough gold. After a property is purchased, it would produce a gold coin at the property's location. The properties produce gold every day.\n        6. you can see a dragon only when you gets close enough to it.\n\n        "),

                        isDisplayed()))
        textView8.check(matches(isDisplayed()))

        val button4 = onView(
                allOf(withId(android.R.id.button1),

                        isDisplayed()))
        button4.check(matches(isDisplayed()))

        val appCompatButton5 = onView(
                allOf(withId(android.R.id.button1), withText("close")
                       ))
        appCompatButton5.perform(scrollTo(), click())

        val appCompatButton6 = onView(
                allOf(withId(R.id.signout_button), withText("Sign out"),

                        isDisplayed()))
        appCompatButton6.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val button5 = onView(
                withId(R.id.email_sign_in_button))
        button5.check(matches(isDisplayed()))
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
