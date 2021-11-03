package com.example.agilesprinters;

import static org.junit.Assert.assertTrue;

import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test class for Login Activity. All the UI tests are written here. Robotium test framework is
 used.
 @author Leen Alzebdeh
 The tests being run are:
    1. checkEmptySignIn: Tests that the activity correctly handles empty email/ password fields (throws a message) <br>
 then checks handling of information that does not match any user.
    2. correctInfoTest: enters the information of a user that exists and ensures the activity signs the user in correctly. <br>
 Note: There is no dedicated unit test class for the login activity as the Android test class is sufficient for <br>
    what needs to be checked.
 */
public class LoginAndroidTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<Login> rule =
            new ActivityTestRule<>(Login.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Click sign in button when email field is empty and check for error message
     * then do the same for when password field is empty
     * then have both email and password be incorrect and check for error message
     */
    @Test
    public void checkEmptySignIn() {
        //Asserts that the current activity is the Login Activity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", Login.class);

        //click the login button when email and password fields are empty
        trySignIn(-1, "", "Email is required");
        //enter text into email then click the login button while the password field is empty
        trySignIn(R.id.email, "null", "Password is required");
        //enter text into password then click the login button while the info given does not match a user
        trySignIn(R.id.password, "null", "Email or password entered is incorrect");
    }

    /**
     * This function enters given text into an editText if desired,
     * then sign in is pressed and a message is checked for on screen
     * @param id
     *  Give the edittext field to have text entered into,
     * if -1 then no field will be updated {@see int}
     * @param input
     *  Give the string to be entered into the specified editText {@See String}
     * @param resultText
     *  Give the expected text that is expected to appear on screen after sign in is clicked
     */
    private void trySignIn(int id, String input, String resultText){
        String signInStr = solo.getString(R.string.action_sign_in);  //string of sign in button

        if (id!=-1) {
            solo.enterText((EditText) solo.getView(id), input);  //enter input into edit text
        }
        solo.clickOnButton(signInStr);
        /* True if there is a text as given in input on the screen
        , wait at least 2 seconds and find one minimum match. */
        assertTrue(solo.waitForText(resultText, 1, 2000));
    }

    /**
     * Function registers a test user then tests if they can be logged in successfully
     */
    @Test
    public void correctInfoTest(){
        String signInStr = solo.getString(R.string.action_sign_in);  //string of sign in button

        registerTestUser();  //register a test user

        //enter the log in info of the test user
        solo.enterText((EditText) solo.getView(R.id.email), solo.getString(R.string.email_test));
        solo.enterText((EditText) solo.getView(R.id.password), solo.getString(R.string.password_test));
        solo.clickOnButton(signInStr);  //click the login button

        //Asserts that the current activity is the Home Activity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", Home.class);
    }

    //will move or import from register
    private void registerTestUser() {
        //Asserts that the current activity is the Login Activity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", Login.class);

        //click register button
        solo.clickOnText(solo.getString(R.string.action_register));
        //Asserts that the current activity is the Register Activity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", Register.class);

        //register a test user
        solo.enterText((EditText) solo.getView(R.id.LastName), solo.getString(R.string.last_test));
        solo.enterText((EditText) solo.getView(R.id.FirstName), solo.getString(R.string.first_test));
        solo.enterText((EditText) solo.getView(R.id.EditTextEmail), solo.getString(R.string.email_test));
        solo.enterText((EditText) solo.getView(R.id.TextPassword), solo.getString(R.string.password_test));
        solo.enterText((EditText) solo.getView(R.id.TextConfirmPassword), solo.getString(R.string.password_test));

        solo.clickOnButton(solo.getString(R.string.create_account)); //Select register text
        //Asserts that the current activity is the Login Activity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", Login.class);
    }


        /**
         * Close activity after each test
         * @throws Exception
         */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }


}
