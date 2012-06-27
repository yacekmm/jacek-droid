package pl.looksok.test.activity;

import pl.looksok.activity.addperson.AddNewPerson;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.ViewAsserts;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EnterPaysActivityTest extends
		ActivityUnitTestCase<AddNewPerson> {
	
	private AddNewPerson mActivity;
	private View origin;
	
	private TextView mTitileText;
	private TextView mNewPersonNameText;
	private TextView mNewPersonNameEdit;
	private TextView mPayText;
	private TextView mPayEdit;
	private Button mAddPErsonButton;

	public EnterPaysActivityTest() {
		super(AddNewPerson.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		
		startActivity(new Intent(), null, null);
		mActivity = getActivity();
		origin = mActivity.getWindow().getDecorView();
		
		mTitileText = (TextView)mActivity.findViewById(pl.looksok.R.id.EnterPays_TextView_Title);
		mNewPersonNameText = (TextView)mActivity.findViewById(pl.looksok.R.id.EnterPays_TextView_Name);
		mNewPersonNameEdit = (TextView)mActivity.findViewById(pl.looksok.R.id.EnterPays_EditText_Name);
		mPayText = (TextView)mActivity.findViewById(pl.looksok.R.id.EnterPays_TextView_Pay);
		mPayEdit = (TextView)mActivity.findViewById(pl.looksok.R.id.EnterPays_EditText_Pay);
		mAddPErsonButton = (Button)mActivity.findViewById(pl.looksok.R.id.EnterPays_Button_AddPerson);
	}
	
	public void testInitialization(){
		assertNotNull(mActivity);
		assertNotNull(mTitileText);
		assertNotNull(mNewPersonNameText);
		assertNotNull(mNewPersonNameEdit);
		assertNotNull(mPayText);
		assertNotNull(mPayEdit);
		assertNotNull(mAddPErsonButton);
	}
	
	public void testViewsOnScreen(){
		ViewAsserts.assertOnScreen(origin, mTitileText);
		ViewAsserts.assertOnScreen(origin, mNewPersonNameText);
		ViewAsserts.assertOnScreen(origin, mNewPersonNameEdit);
		ViewAsserts.assertOnScreen(origin, mPayText);
		ViewAsserts.assertOnScreen(origin, mPayEdit);
		ViewAsserts.assertOnScreen(origin, mAddPErsonButton);
	}
	
	public void testViewsAlignment(){
		ViewAsserts.assertHorizontalCenterAligned(origin, mTitileText);
		
		ViewAsserts.assertLeftAligned(origin, mTitileText);
		ViewAsserts.assertRightAligned(origin, mTitileText);
		ViewAsserts.assertTopAligned(origin, mTitileText);
	}
}
