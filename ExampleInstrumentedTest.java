package com.rbbn.connectme;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.genband.mobile.api.services.call.CallInterface;
import com.genband.mobile.api.utilities.MobileError;
import com.rbbn.connectme.modules.registration.RegistrationModuleCallback;
import com.rbbn.connectme.ui.LoginActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.PositionAssertions.isBelow;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private CallInterface activeCall;

    private static final String USER_NAME = "adem7@spidr.com";
    private static final String CALLEE_NAME = "adem8@spidr.com";
    private static final String USER_PASSWORD = "1234";

    private static final String USER_NAME_WRONG = "thisIsWrongUserName";
    private static final String USER_PASSWORD_WRONG = "12345";
    private static final String PASSWORD_HINT = "Password";
    private static final String CALLEE_HINT = "Enter callee address";
    private static final String USERNAME_HINT = "Type Username";
    private static final String USER_NAME_BUT_HAS_NO_DOMAIN = "adem7";
    private static final String RANDOM_USER_NAME = "randomUsername";
    private static final String EMPTY = "";
    private static final String USERNAME_BUT_HAS_SPACES = "adem8@spider.com ";
    private static final String PASSWORD_BUT_HAS_SPACES = "1234  ";

    private Boolean isLoginSucceed = false;

    @Rule
    public ActivityTestRule<LoginActivity> mActivity = new ActivityTestRule<>(LoginActivity.class);

    private LoginActivity mActivity_1;

    @Before
    public void setup() {
        //mActivity.launchActivity(null);
        mActivity_1 = mActivity.getActivity();
        //Thread.sleep(500);
    }

    @Test
    public void testIsUserTextFieldWrong() {
        onView(withId(R.id.user_name_edit_text)).perform(typeText(USER_NAME_WRONG));
        closeSoftKeyboard();
        onView(withId(R.id.password_edit_text)).perform(typeText(USER_PASSWORD));
        closeSoftKeyboard();
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testIsPasswordTextFieldWrong() {
        onView(withId(R.id.user_name_edit_text)).perform(typeText(USER_NAME));
        closeSoftKeyboard();
        onView(withId(R.id.password_edit_text)).perform(typeText(USER_PASSWORD_WRONG));
        closeSoftKeyboard();
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testIsPasswordTextFieldHintWrong() throws InterruptedException {
        sleep(2500);
        onView(withId(R.id.password_edit_text)).check(matches(withHint(PASSWORD_HINT)));
    }

    @Test
    public void testIsUserTextFieldHintWrong() throws InterruptedException {
        sleep(2500);
        onView(withId(R.id.user_name_edit_text)).check(matches(withHint(USERNAME_HINT)));
    }

    @Test
    public void testIsAllUIElementsAreVisible() {
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
        onView(withId(R.id.user_name_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.authorization_name_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.password_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
        onView(withId(R.id.environment_image_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testIsUserTextFieldEmpty() {
        loginMethod();
        onView(withId(R.id.user_name_edit_text)).perform(clearText());
        onView(withId(R.id.login_button)).perform(click());
        onView(withText(R.string.failed_to_login)).inRoot(withDecorView(not(is(mActivity.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void testIsPasswordTextFieldEmpty() {
        loginMethod();
        onView(withId(R.id.password_edit_text)).perform(clearText());
        onView(withId(R.id.login_button)).perform(click());
        onView(withText(R.string.failed_to_login)).inRoot(withDecorView(not(is(mActivity.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void testIsAllUIElementsAreWellPlaced() {
        onView(withId(R.id.imageView)).check(isAbove(withId(R.id.user_name_edit_text)));
        onView(withId(R.id.authorization_name_edit_text)).check(isBelow(withId(R.id.user_name_edit_text)));
        onView(withId(R.id.password_edit_text)).check(isBelow(withId(R.id.authorization_name_edit_text)));
        onView(withId(R.id.login_button)).check(isBelow(withId(R.id.password_edit_text)));
    }

    @Test
    public void testIsLoginSucceeded() throws InterruptedException {
        loginMethod();
        onView(withId(R.id.login_button)).perform(click());
        isLoginSucceed = true;
        sleep(2500);
        onView(withId(R.id.callee_edit_text)).check(matches(isDisplayed()));
    }

    @Test
    public void testIsUsernameInputAcceptsRandomUsernameWithValidDomainEnd() {
        loginMethod();
        closeSoftKeyboard();
        onView(withId(R.id.user_name_edit_text)).perform(clearText());
        onView(withId(R.id.user_name_edit_text)).perform(typeText(RANDOM_USER_NAME + "@spidr.com"));
        closeSoftKeyboard();
        onView(withId(R.id.login_button)).perform(click());
        onView(withText(R.string.failed_to_login)).inRoot(withDecorView(not(is(mActivity.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void testAreUsernameAndPasswordTurnsEmptyAfterReOpen() {
        loginMethod();
        closeSoftKeyboard();
        mActivity.finishActivity();
        mActivity.launchActivity(null);
        onView(withId(R.id.user_name_edit_text)).check(matches(withText(EMPTY)));
        onView(withId(R.id.password_edit_text)).check(matches(withText(EMPTY)));
    }

    @Test
    public void testIsUsernameTrimSpacesAtTheEnd() {
        onView(withId(R.id.user_name_edit_text)).perform(typeText(USERNAME_BUT_HAS_SPACES));
        closeSoftKeyboard();
        onView(withId(R.id.password_edit_text)).perform(typeText(USER_PASSWORD));
        closeSoftKeyboard();
        onView(withId(R.id.login_button)).perform(click());
        onView(withText(R.string.failed_to_login)).inRoot(withDecorView(not(is(mActivity.getActivity().getWindow().getDecorView())))).check(matches(not(isDisplayed())));
    }

    @Test
    public void testIsPasswordTrimSpacesAtTheEnd() {
        onView(withId(R.id.user_name_edit_text)).perform(typeText(USER_NAME));
        closeSoftKeyboard();
        onView(withId(R.id.password_edit_text)).perform(typeText(PASSWORD_BUT_HAS_SPACES));
        closeSoftKeyboard();
        onView(withId(R.id.login_button)).perform(click());
        onView(withText(R.string.failed_to_login)).inRoot(withDecorView(not(is(mActivity.getActivity().getWindow().getDecorView())))).check(matches(not(isDisplayed())));
    }

    @Test
    public void testIsCalleeTextFieldEmpty() throws InterruptedException {
        loginMethod();
        onView(withId(R.id.login_button)).perform(click());
        isLoginSucceed = true;
        sleep(2500);
        onView(withId(R.id.start_call_button)).perform(click());
        sleep(2500);
        onView(withText(R.string.failed_to_start_call)).inRoot(withDecorView(not(is(mActivity.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void testIsCalleeTextFieldWrong() throws InterruptedException {
        onView(withId(R.id.user_name_edit_text)).perform(typeText(USER_NAME));
        closeSoftKeyboard();
        onView(withId(R.id.password_edit_text)).perform(typeText(USER_PASSWORD));
        closeSoftKeyboard();
        onView(withId(R.id.login_button)).perform(click());
        isLoginSucceed = true;
        sleep(2000);
        onView(withId(R.id.callee_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.callee_edit_text)).perform(typeText(USER_NAME_WRONG));
        closeSoftKeyboard();
        onView(withId(R.id.start_call_button)).perform(click());
        sleep(5000);
        onView(withId(R.id.callee_edit_text)).check(matches(isDisplayed()));
    }

    @Test
    public void testIsCalleeTextFieldAcceptUsernamesWithoutDomain() throws InterruptedException {
        loginMethod();
        onView(withId(R.id.login_button)).perform(click());
        isLoginSucceed = true;
        sleep(2500);
        onView(withId(R.id.callee_edit_text)).perform(typeText(USER_NAME_BUT_HAS_NO_DOMAIN));
        closeSoftKeyboard();
        onView(withId(R.id.start_call_button)).perform(click());
        sleep(5000);
        onView(withText(R.string.failed_to_start_call)).inRoot(withDecorView(not(is(mActivity.getActivity().getWindow().getDecorView())))).check(matches(not(isDisplayed())));
    }

    @Test
    public void testIsCalleeTextFieldTrimSpaces() throws InterruptedException {
        loginMethod();
        onView(withId(R.id.login_button)).perform(click());
        isLoginSucceed = true;
        sleep(2500);
        onView(withId(R.id.callee_edit_text)).perform(typeText(USERNAME_BUT_HAS_SPACES));
        closeSoftKeyboard();
        onView(withId(R.id.start_call_button)).perform(click());
        sleep(2500);
        onView(withText(R.string.failed_to_start_call)).inRoot(withDecorView(not(is(mActivity.getActivity().getWindow().getDecorView())))).check(matches(not(isDisplayed())));
    }

    @Test
    public void testIsCallingSucceeded() throws InterruptedException {
        loginMethod();
        onView(withId(R.id.login_button)).perform(click());
        sleep(2000);
        isLoginSucceed = true;
        onView(withId(R.id.callee_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.callee_edit_text)).perform(typeText(USER_NAME));
        closeSoftKeyboard();
        onView(withId(R.id.start_call_button)).perform(click());
        sleep(5000);
        onView(withId(R.id.accept_call_button)).perform(click());
        sleep(5000);
        onView(withId(R.id.call_end_button)).perform(click());
    }

    @Test
    public void testIsUsernameFieldTextAcceptsUsernamesWithoutDomain() {
        onView(withId(R.id.user_name_edit_text)).perform(typeText(USER_NAME_BUT_HAS_NO_DOMAIN));
        closeSoftKeyboard();
        onView(withId(R.id.password_edit_text)).perform(typeText(USER_PASSWORD));
        closeSoftKeyboard();
        onView(withId(R.id.login_button)).perform(click());
        onView(withText(R.string.failed_to_login)).inRoot(withDecorView(not(is(mActivity.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void testIsCalleeTextFieldVisible() throws InterruptedException {
        loginMethod();
        onView(withId(R.id.login_button)).perform(click());
        sleep(2500);
        isLoginSucceed = true;
        onView(withId(R.id.callee_edit_text)).check(matches(isDisplayed()));
    }

    @Test
    public void testIsCalleeTextFieldHintWrong() throws InterruptedException {
        loginMethod();
        onView(withId(R.id.login_button)).perform(click());
        sleep(2500);
        isLoginSucceed = true;
        onView(withId(R.id.callee_edit_text)).check(matches(withHint(CALLEE_HINT)));
    }

    @Test
    public void testAcceptingIncomingCallSuccess() throws InterruptedException {
        loginMethod();
        onView(withId(R.id.login_button)).perform(click());
        sleep(2000);
        isLoginSucceed = true;
        onView(withId(R.id.callee_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.callee_edit_text)).perform(typeText(USER_NAME));
        closeSoftKeyboard();
        onView(withId(R.id.start_call_button)).perform(click());
        sleep(5000);
        onView(withId(R.id.accept_call_button)).check(matches(isDisplayed()));
        onView(withId(R.id.accept_call_button)).perform(click());
        sleep(5000);
        onView(withId(R.id.call_end_button)).perform(click());
    }

    @Test
    public void testRejectingIncomingCallSuccess() throws InterruptedException {
        loginMethod();
        onView(withId(R.id.login_button)).perform(click());
        isLoginSucceed = true;
        onView(withId(R.id.callee_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.callee_edit_text)).perform(typeText(USER_NAME));
        closeSoftKeyboard();
        onView(withId(R.id.start_call_button)).perform(click());
        sleep(5000);
        onView(withId(R.id.reject_call_button)).check(matches(isDisplayed()));
        onView(withId(R.id.reject_call_button)).perform(click());
        sleep(5000);
    }

    @Test
    public void testIsSpeakerButtonWorking() throws InterruptedException {
        loginMethod();
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.callee_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.callee_edit_text)).perform(typeText(USER_NAME));
        closeSoftKeyboard();
        onView(withId(R.id.start_call_button)).perform(click());
        sleep(5000);
        onView(withId(R.id.accept_call_button)).check(matches(isDisplayed()));
        onView(withId(R.id.accept_call_button)).perform(click());
        sleep(5000);
        onView(withId(R.id.localVideoView)).perform(swipeUp());
        sleep(5000);
        onView(withId(R.id.speaker_button)).check(matches(isDisplayed()));
        onView(withId(R.id.speaker_button)).perform(click());
        sleep(5000);
    }

    @Test
    public void testIsHoldButtonWorking() throws InterruptedException {
        loginMethod();
        onView(withId(R.id.login_button)).perform(click());
        isLoginSucceed = true;
        sleep(2000);
        onView(withId(R.id.callee_edit_text)).perform(typeText(USER_NAME));
        closeSoftKeyboard();
        onView(withId(R.id.start_call_button)).perform(click());
        sleep(5000);
        onView(withId(R.id.accept_call_button)).check(matches(isDisplayed()));
        onView(withId(R.id.accept_call_button)).perform(click());
        sleep(5000);
        onView(withId(R.id.localVideoView)).perform(swipeUp());
        sleep(5000);
        onView(withId(R.id.hold_button)).check(matches(isDisplayed()));
        onView(withId(R.id.hold_button)).perform(click());
        sleep(5000);
    }

    @Test
    public void testIsVideoStartStopButtonWorking() throws InterruptedException {
        loginMethod();
        onView(withId(R.id.login_button)).perform(click());
        sleep(2000);
        isLoginSucceed = true;
        onView(withId(R.id.callee_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.callee_edit_text)).perform(typeText(USER_NAME));
        closeSoftKeyboard();
        onView(withId(R.id.start_call_button)).perform(click());
        sleep(5000);
        onView(withId(R.id.localVideoView)).perform(swipeUp());
        sleep(5000);
        onView(withId(R.id.video_toggle_button)).check(matches(isDisplayed()));
        onView(withId(R.id.video_toggle_button)).perform(click());
        sleep(5000);
        onView(withId(R.id.call_end_button)).perform(click());
    }

    @After
    public void tearDown() {
        if (isLoginSucceed) {
            logoutMethod();
        }
    }

    private void loginMethod() {
        onView(withId(R.id.user_name_edit_text)).perform(typeText(USER_NAME));
        closeSoftKeyboard();
        onView(withId(R.id.password_edit_text)).perform(typeText(USER_PASSWORD));
        closeSoftKeyboard();
    }

    private void logoutMethod() {
        RegistrationModule.sharedInstance(getContext()).unRegisterClient(getContext(), new RegistrationModuleCallback() {
            @Override
            public void onSuccess() {
                isLoginSucceed = false;
            }

            @Override
            public void onFail(MobileError error) {
            }
        });
    }
}