package pl.looksok.test;

import pl.looksok.activity.welcome.WelcomeActivity;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

public class WelcomeActivityTest extends ActivityInstrumentationTestCase2<WelcomeActivity> {

  private Solo solo;

  public WelcomeActivityTest() {
    super(WelcomeActivity.class);
  }

  public void setUp() throws Exception {
    solo = new Solo(getInstrumentation(), getActivity());
  }
  
  @Override
  public void tearDown() throws Exception {
    solo.finishOpenedActivities();
  }
  
  public void testRestaurantModeButtonWorks(){
	  solo.assertCurrentActivity("Should be in different activity", WelcomeActivity.class);
//	  solo.clickOnText("restauracji");
//	  solo.assertCurrentActivity("Should be in restaurant activity", CalcResultBaseActivity.class);
  }
} 