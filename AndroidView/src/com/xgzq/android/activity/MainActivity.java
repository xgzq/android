package com.xgzq.android.activity;

import com.xgzq.android.view.DrawerView;
import com.xgzq.android.view.R;
import com.xgzq.android.view.R.id;
import com.xgzq.android.view.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity
{
	
	private DrawerView mDrawerView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDrawerView = (DrawerView) findViewById(R.id.main_drawer);
		
		Button btn = new Button(this);
		btn.setText("Button");
		btn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Toast.makeText(MainActivity.this, "xxxx", Toast.LENGTH_LONG).show();;
				if(mDrawerView.shownMenu())
				{
					mDrawerView.hiddenMenu();
				}
				else
				{
					mDrawerView.showMenu();
				}
			}
		});
		
		mDrawerView.setContentView(btn);
		mDrawerView.setTranslateTogether(true);
	}
}
