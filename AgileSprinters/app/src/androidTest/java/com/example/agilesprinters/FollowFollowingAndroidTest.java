package com.example.agilesprinters;

import static org.junit.Assert.assertEquals;

import android.widget.EditText;
import android.widget.TextView;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import java.util.ArrayList;

/**
 * This class tests the behaviour of the FollowFollowing Activity
 */
public class FollowFollowingAndroidTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule< >
            (LoginActivity.class, true, true);

    /**
     * This function runs the setup for each test before the start of each test
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        solo.assertCurrentActivity("Wrong", LoginActivity.class);

        // Logging In to test account
        solo.enterText((EditText) solo.getView(R.id.email), "hari@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.password), "password");
        solo.hideSoftKeyboard();
        solo.clickOnView(solo.getView(R.id.loginBtn));

        // checks to make sure the activity has switched to the Home activity
        solo.assertCurrentActivity("Wrong", HomeActivity.class);
    }

    /**
     * This function tests all the followers info retrieval
     */
    @Test
    public void stage1_followers() {
        solo.assertCurrentActivity("Wrong", HomeActivity.class);

        solo.clickOnView(solo.getView(R.id.followerCount));
        solo.waitForDialogToOpen(1000);

        TextView textView = (TextView) solo.getView(R.id.titleTextView);
        String str = textView.getText().toString();

        assertEquals(str, "Followers");

        ArrayList<TextView> textViewArrayList = solo.clickInList(0, 0);
        String str1 = textViewArrayList.get(0).getText().toString();
        assertEquals(str1, "HariTest User");

        solo.assertCurrentActivity("Wrong", OtherUserScreenActivity.class);
    }

    /**
     * This function tests all the following users info retrieval
     */
    @Test
    public void stage2_following() {
        solo.assertCurrentActivity("Wrong", HomeActivity.class);

        solo.clickOnView(solo.getView(R.id.followingCount));
        solo.waitForDialogToOpen(1000);

        TextView textView = (TextView) solo.getView(R.id.titleTextView);
        String str = textView.getText().toString();

        assertEquals(str, "Following");

        ArrayList<TextView> textViewArrayList = solo.clickInList(0, 0);
        String str1 = textViewArrayList.get(0).getText().toString();
        assertEquals(str1, "HariTest User");

        solo.assertCurrentActivity("Wrong", OtherUserScreenActivity.class);
    }

    /**
     * This function handles the actions needed to properly finish the test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {

        solo.goBack();
        solo.waitForDialogToOpen(1000);
        solo.assertCurrentActivity("Wrong", HomeActivity.class);

        solo.clickOnView(solo.getView(R.id.homeUserButton));

        solo.assertCurrentActivity("Wrong", EditUserActivity.class);

        solo.clickOnView(solo.getView(R.id.signOutbutton));

        solo.assertCurrentActivity("Wrong", LoginActivity.class);

        solo.finishOpenedActivities();
    }
}
