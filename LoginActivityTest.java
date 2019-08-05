package com.rbbn.connectme;

import android.app.Activity;
import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import com.genband.mobile.api.utilities.MobileError;
import com.rbbn.connectme.modules.registration.RegistrationModuleCallback;
import com.rbbn.connectme.ui.LoginActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Iterator;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.PositionAssertions.isAbove;
import static android.support.test.espresso.assertion.PositionAssertions.isBelow;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {

    private static final String USER_NAME = "adem8@spidr.com";
    private static final String CALLEE_NAME = "adem7@spidr.com";
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
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.password_edit_text)).perform(typeText(USER_PASSWORD));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.login_button)).perform(click());
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testIsPasswordTextFieldWrong() {
        onView(withId(R.id.user_name_edit_text)).perform(typeText(USER_NAME));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.password_edit_text)).perform(typeText(USER_PASSWORD_WRONG));
        Espresso.closeSoftKeyboard();
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
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.user_name_edit_text)).perform(clearText());
        onView(withId(R.id.user_name_edit_text)).perform(typeText(RANDOM_USER_NAME + "@spidr.com"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.login_button)).perform(click());
        onView(withText(R.string.failed_to_login)).inRoot(withDecorView(not(is(mActivity.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void testAreUsernameAndPasswordTurnsEmptyAfterReOpen() {
        loginMethod();
        Espresso.closeSoftKeyboard();
        mActivity.finishActivity();
        mActivity.launchActivity(null);
        onView(withId(R.id.user_name_edit_text)).check(matches(withText(EMPTY)));
        onView(withId(R.id.password_edit_text)).check(matches(withText(EMPTY)));
    }

    @Test
    public void testIsUsernameAcceptSpacesAtTheEnd() {
        onView(withId(R.id.user_name_edit_text)).perform(typeText(USERNAME_BUT_HAS_SPACES));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.password_edit_text)).perform(typeText(USER_PASSWORD));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.login_button)).perform(click());
        onView(withText(R.string.failed_to_login)).inRoot(withDecorView(not(is(mActivity.getActivity().getWindow().getDecorView())))).check(matches(not(isDisplayed())));
    }

    @Test
    public void testIsPasswordAcceptSpacesAtTheEnd() {
        onView(withId(R.id.user_name_edit_text)).perform(typeText(USER_NAME));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.password_edit_text)).perform(typeText(PASSWORD_BUT_HAS_SPACES));
        Espresso.closeSoftKeyboard();
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
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.password_edit_text)).perform(typeText(USER_PASSWORD));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.login_button)).perform(click());
        isLoginSucceed = true;
        sleep(2000);
        onView(withId(R.id.callee_edit_text)).check(matches(isDisplayed()));
        onView(withId(R.id.callee_edit_text)).perform(typeText(USER_NAME_WRONG));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.start_call_button)).perform(click());
        sleep(5000);
        onView(withId(R.id.callee_edit_text)).check(matches(isDisplayed()));
    }

    @Test
    public void testIsCalleeTextFieldAcceptWithoutDomain() throws InterruptedException {
        loginMethod();
        onView(withId(R.id.login_button)).perform(click());
        isLoginSucceed = true;
        sleep(2500);
        onView(withId(R.id.callee_edit_text)).perform(typeText(USER_NAME_BUT_HAS_NO_DOMAIN));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.start_call_button)).perform(click());
        sleep(2500);
        onView(withText(R.string.failed_to_start_call)).inRoot(withDecorView(not(is(mActivity.getActivity().getWindow().getDecorView())))).check(matches(not(isDisplayed())));
    }

    @Test
    public void testIsCalleeTextFieldAcceptSpaces() throws InterruptedException {
        loginMethod();
        onView(withId(R.id.login_button)).perform(click());
        isLoginSucceed = true;
        sleep(2500);
        onView(withId(R.id.callee_edit_text)).perform(typeText(USERNAME_BUT_HAS_SPACES));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.start_call_button)).perform(click());
        sleep(2500);
        onView(withText(R.string.failed_to_start_call)).inRoot(withDecorView(not(is(mActivity.getActivity().getWindow().getDecorView())))).check(matches(not(isDisplayed())));
    }

    @Test
    public void testIsCallingSucceeded() throws InterruptedException {
        loginMethod();
        onView(withId(R.id.login_button)).perform(click());
        isLoginSucceed = true;
        onView(withId(R.id.callee_edit_text)).perform(typeText(CALLEE_NAME));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.start_call_button)).perform(click());
        sleep(5000);
        onView(withText(R.string.failed_to_start_call)).inRoot(withDecorView(not(is(mActivity.getActivity().getWindow().getDecorView())))).check(matches(not(isDisplayed())));
        onView(withText(R.id.call_end_button)).perform(click());
    }

    @Test
    public void testIsUsernameFieldTextAcceptsWithoutDomain(){
        onView(withId(R.id.user_name_edit_text)).perform(typeText(USER_NAME_BUT_HAS_NO_DOMAIN));
        onView(withId(R.id.password_edit_text)).perform(typeText(USER_PASSWORD));
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
    public void testIsCalleeTextFieldAcceptsRandomUserWithValidDomain() throws InterruptedException {
        loginMethod();
        onView(withId(R.id.login_button)).perform(click());
        sleep(2500);
        isLoginSucceed = true;
        onView(withId(R.id.callee_edit_text)).perform(typeText(RANDOM_USER_NAME + "@spidr.com"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.start_call_button)).perform(click());
        sleep(5000);
        onView(withText(R.string.failed_to_start_call)).inRoot(withDecorView(not(is(mActivity.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }



    @Test
    public void isUsernameSameAsUserTextField() {

    }

    @Test
    public void isPasswordSameAsPasswordTextField() {

    }

    @Test
    public void isCalleeNameSameAsCalleeTextField() {

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

    private Activity getActivityInstance(){
        final Activity[] currentActivity = {null};

        getInstrumentation().runOnMainSync(new Runnable(){
            public void run(){
                Collection<Activity> resumedActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                Iterator<Activity> it = resumedActivity.iterator();
                currentActivity[0] = it.next();
            }
        });

        return currentActivity[0];
    }
}
