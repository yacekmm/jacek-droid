package pl.looksok.test;

import pl.looksok.activity.welcome.WelcomeActivity;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

public class WelcomeScreenTest extends ActivityInstrumentationTestCase2<WelcomeActivity> {

  private Solo solo;

  public WelcomeScreenTest() {
    super(WelcomeActivity.class);
  }

  public void setUp() throws Exception {
    solo = new Solo(getInstrumentation(), getActivity());
  }

  
  @Override
  public void tearDown() throws Exception {
    solo.finishOpenedActivities();
  }
  
  public void testPartyModeButtonWorks(){
	  solo.assertCurrentActivity("Should be in different activity", WelcomeActivity.class);
  }
} 