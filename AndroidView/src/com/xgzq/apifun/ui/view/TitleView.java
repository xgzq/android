package com.xgzq.apifun.ui.view;

import com.xgzq.apifun.R;
import com.xgzq.apifun.tools.DimenUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author Administrator
 *
 */
public class TitleView extends LinearLayout
{
	
	/**
	 * TitleView默认背景颜色
	 */
	public static final int TITLE_VIEW_DEFAULT_BACKGROUND_COLOR = Color.parseColor("#a156d4");
	
	private int mBackgroundColor;
	
	/**
	 * TitleView默认高度
	 */
	public static final int TITLE_VIEW_DEFAULT_HEIGHT = 48;
	
	private int mViewHeight;
	
	/**
	 * Left默认是否是显示ImageButton（ImageButton和TextView两者显示其一）
	 */
	public static final boolean TITLE_VIEW_DEFAULT_LEFT_IMG_VISIBLE = false;
	
	private boolean showLeftImg;
	
	private int mLeftImgResId = -1;
	
	private String mLeftBackText;
	
	/**
	 * LeftText默认size
	 */
	public static final int TITLE_VIEW_DEFAULT_LEFT_TEXT_SIZE = 16;
	
	private float mLeftBackTextSize;
	
	/**
	 * LeftText默认Color
	 */
	public static final int TITLE_VIEW_DEFAULT_LEFT_TEXT_COLOR = Color.WHITE;
	
	private int mLeftBackTextColor;
	
	/**
	 * Center默认是否是显示TextView（TextView和Spinner两者显示其一）
	 */
	public static final boolean TITLE_VIEW_DEFAULT_CENTER_TITLE_VISIBLE = true;
	
	private boolean showCenterTitle;
	
	private String mCenterTitleText;
	
	/**
	 * Center中Title默认size
	 */
	public static final int TITLE_VIEW_DEFAULT_CENTER_TEXT_SIZE = 22;
	
	private float mCenterTitleTextSize;
	
	/**
	 * Center中Title默认Color
	 */
	public static final int TITLE_VIEW_DEFAULT_CENTER_TEXT_COLOR = Color.YELLOW;
	
	private int mCenterTitleTextColor;
	
	/**
	 * Right默认是否展示ImageButton
	 */
	public static final boolean TITLE_VIEW_DEFAULT_RIGHT_IMG_VISIBLE = false;
	
	private boolean showRightImg;
	
	private int mRightImgResId = -1;
	
	/**
	 * Right默认是否展示TextView
	 */
	public static final boolean TITLE_VIEW_DEFAULT_RIGHT_TEXT_VISIBLE = false;
	
	private boolean showRightText;
	
	private String mRightTipText;
	
	/**
	 * RightText默认size
	 */
	public static final int TITLE_VIEW_DEFAULT_RIGHT_TEXT_SIZE = 16;
	
	private float mRightTipTextSize;
	
	/**
	 * RightText默认Color
	 */
	public static final int TITLE_VIEW_DEFAULT_RIGHT_TEXT_COLOR = Color.BLACK;
	
	private int mRightTipTextColor;
	
	private LinearLayout mRootLinearLayout;
	
	private TextView mLeftTextView;
	private ImageButton mLeftImageButton;
	
	private TextView mCenterTextView;
	private Spinner mCenterSpinner;
	
	private TextView mRightTextView;
	private ImageButton mRightImageButton;
	
	public TitleView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initView(attrs);
	}

	public TitleView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(attrs);
	}

	public TitleView(Context context)
	{
		super(context);
		initView(null);
	}

	private void initView(AttributeSet attrs)
	{
		findViews();
		if(attrs != null)
		{
			TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TitleView);
			
			//根视图
			if(mRootLinearLayout != null)
			{
				mBackgroundColor = typedArray.getColor(R.styleable.TitleView_backgroundColor,TITLE_VIEW_DEFAULT_BACKGROUND_COLOR);
				mRootLinearLayout.setBackgroundColor(mBackgroundColor);
				
				mViewHeight = (int) typedArray.getDimension(R.styleable.TitleView_viewHeight, DimenUtil.dp2Px(getContext(), TITLE_VIEW_DEFAULT_HEIGHT));
				mRootLinearLayout.getLayoutParams().height = mViewHeight;
			}
			
			//左边视图
			showLeftImg = typedArray.getBoolean(R.styleable.TitleView_isBackImg, TITLE_VIEW_DEFAULT_LEFT_IMG_VISIBLE);
			if(showLeftImg)
			{
				if(mLeftTextView != null)
				{
					mLeftTextView.setVisibility(GONE);
				}
				if(mLeftImageButton != null)
				{
					mLeftImageButton.setVisibility(VISIBLE);
					mLeftImgResId = typedArray.getResourceId(R.styleable.TitleView_backImgScr, R.drawable.ic_ab_back_holo_dark_am);
					mLeftImageButton.setBackgroundResource(mLeftImgResId);
				}
			}
			else
			{
				if(mLeftTextView != null)
				{
					mLeftTextView.setVisibility(VISIBLE);
					setLeftTextClickable(true);
					setLeftTextClickListener(new OnClickListener()
					{
						
						@Override
						public void onClick(View arg0)
						{
							
						}
					});
					String leftText = typedArray.getString(R.styleable.TitleView_backText);
					mLeftBackText = leftText != null ? leftText : getContext().getResources().getString(R.string.view_title_tv_left_text);
					mLeftTextView.setText(mLeftBackText);
					mLeftBackTextSize = typedArray.getDimension(R.styleable.TitleView_backTextSize, DimenUtil.dp2Sp(getContext(), TITLE_VIEW_DEFAULT_LEFT_TEXT_SIZE));
					mLeftTextView.setTextSize(mLeftBackTextSize);
					mLeftBackTextColor = typedArray.getColor(R.styleable.TitleView_backTextColor, TITLE_VIEW_DEFAULT_LEFT_TEXT_COLOR);
					mLeftTextView.setTextColor(mLeftBackTextColor);
				}
				if(mLeftImageButton != null)
				{
					mLeftImageButton.setVisibility(GONE);
				}
			}
			
			//中间视图
			showCenterTitle = typedArray.getBoolean(R.styleable.TitleView_isTitle, TITLE_VIEW_DEFAULT_CENTER_TITLE_VISIBLE);
			if(showCenterTitle)
			{
				if(mCenterSpinner != null)
				{
					mCenterSpinner.setVisibility(GONE);
				}
				if(mCenterTextView != null)
				{
					mCenterTextView.setVisibility(VISIBLE);
					String title = typedArray.getString(R.styleable.TitleView_title);
					mCenterTitleText = title != null ? title : getContext().getResources().getString(R.string.view_title_tv_center_text);
					mCenterTextView.setText(mCenterTitleText);
					mCenterTitleTextSize = typedArray.getDimension(R.styleable.TitleView_titleSize, DimenUtil.dp2Sp(getContext(), TITLE_VIEW_DEFAULT_CENTER_TEXT_SIZE));
					mCenterTextView.setTextSize(mCenterTitleTextSize);
					mCenterTitleTextColor = typedArray.getColor(R.styleable.TitleView_titleColor, TITLE_VIEW_DEFAULT_CENTER_TEXT_COLOR);
					mCenterTextView.setTextColor(mCenterTitleTextColor);
				}
			}
			else
			{
				if(mCenterSpinner != null)
				{
					mCenterSpinner.setVisibility(VISIBLE);
					//TODO Spinner
				}
				if(mCenterTextView != null)
				{
					mCenterTextView.setVisibility(GONE);
				}
			}
			
			//右边视图
			showRightImg = typedArray.getBoolean(R.styleable.TitleView_tipImgVisibility, TITLE_VIEW_DEFAULT_RIGHT_IMG_VISIBLE);
			if(showRightImg)
			{
				if(mRightImageButton != null)
				{
					mRightImageButton.setVisibility(VISIBLE);
					mRightImgResId = typedArray.getResourceId(R.styleable.TitleView_tipImgScr, R.drawable.ic_menu_home);
					mRightImageButton.setBackgroundResource(mRightImgResId);
				}
			}
			else
			{
				if(mRightImageButton != null)
				{
					mRightImageButton.setVisibility(GONE);
				}
			}
			showRightText = typedArray.getBoolean(R.styleable.TitleView_tipTextVisibility, TITLE_VIEW_DEFAULT_RIGHT_TEXT_VISIBLE);
			if(showRightText)
			{
				if(mRightTextView != null)
				{
					mRightTextView.setVisibility(VISIBLE);
					String tip = typedArray.getString(R.styleable.TitleView_tip);
					mRightTipText = tip != null ? tip : getContext().getResources().getString(R.string.view_title_tv_right_text);
					mRightTextView.setText(mRightTipText);
					mRightTipTextSize = typedArray.getDimension(R.styleable.TitleView_tipSize, DimenUtil.dp2Sp(getContext(), TITLE_VIEW_DEFAULT_RIGHT_TEXT_SIZE));
					mRightTextView.setTextSize(mRightTipTextSize);
					mRightTipTextColor = typedArray.getColor(R.styleable.TitleView_tipColor, TITLE_VIEW_DEFAULT_RIGHT_TEXT_COLOR);
					mRightTextView.setTextColor(mRightTipTextColor);
				}
			}
			else
			{
				if(mRightTextView != null)
				{
					mRightTextView.setVisibility(GONE);
				}
			}
		}
	}

	private void findViews()
	{
		View root = LayoutInflater.from(getContext()).inflate(R.layout.view_title, this);
		if(root != null)
		{
			mRootLinearLayout = (LinearLayout) root.findViewById(R.id.view_title_ll_root);
			
			mLeftTextView = (TextView) root.findViewById(R.id.view_title_tv_left);
			mLeftImageButton = (ImageButton) root.findViewById(R.id.view_title_img_btn_left);
			
			mCenterTextView = (TextView) root.findViewById(R.id.view_title_tv_center_title);
			mCenterSpinner = (Spinner) root.findViewById(R.id.view_title_spinner_center);
			
			mRightTextView = (TextView) root.findViewById(R.id.view_title_tv_right);
			mRightImageButton = (ImageButton) root.findViewById(R.id.view_title_img_btn_right);
			
			mLeftTextView.setVisibility(GONE);
			mCenterSpinner.setVisibility(GONE);
			mRightImageButton.setVisibility(GONE);
		}
	}

	public int getBackgroundColor()
	{
		return mBackgroundColor;
	}

	public void setBackgroundColor(int mBackgroundColor)
	{
		if(mRootLinearLayout != null)
		{
			mRootLinearLayout.setBackgroundColor(mBackgroundColor);
			this.mBackgroundColor = mBackgroundColor;
		}
	}

	public int getViewHeight()
	{
		return mViewHeight;
	}

	public void setViewHeight(int mViewHeight)
	{
		if(mRootLinearLayout != null)
		{
			mRootLinearLayout.getLayoutParams().height = mViewHeight;
			this.mViewHeight = mViewHeight;
		}
	}

	public boolean isShowLeftImg()
	{
		return showLeftImg;
	}

	public void setShowLeftImg(boolean showLeftImg)
	{
		if(mLeftTextView != null && mLeftImageButton != null)
		{
			if(showLeftImg)
			{
				mLeftTextView.setVisibility(GONE);
				mLeftImageButton.setVisibility(VISIBLE);
			}
			else
			{
				mLeftTextView.setVisibility(VISIBLE);
				mLeftImageButton.setVisibility(GONE);
			}
			this.showLeftImg = showLeftImg;
		}
	}

	public int getLeftImgResId()
	{
		return mLeftImgResId;
	}

	public void setLeftImgResId(int mLeftImgResId)
	{
		if(mLeftImageButton != null)
		{
			mLeftImageButton.setBackgroundResource(mLeftImgResId);
			this.mLeftImgResId = mLeftImgResId;
		}
	}

	public String getLeftBackText()
	{
		return mLeftBackText;
	}

	public void setLeftBackText(String mLeftBackText)
	{
		if(mLeftTextView != null)
		{
			mLeftTextView.setText(mLeftBackText);
			this.mLeftBackText = mLeftBackText;
		}
	}

	public float getLeftBackTextSize()
	{
		return mLeftBackTextSize;
	}

	public void setLeftBackTextSize(float mLeftBackTextSize)
	{
		if(mLeftTextView != null)
		{
			mLeftTextView.setTextSize(mLeftBackTextSize);
			this.mLeftBackTextSize = mLeftBackTextSize;
		}
	}

	public int getLeftBackTextColor()
	{
		return mLeftBackTextColor;
	}

	public void setLeftBackTextColor(int mLeftBackTextColor)
	{
		if(mLeftTextView != null)
		{
			mLeftTextView.setTextColor(mLeftBackTextColor);
			this.mLeftBackTextColor = mLeftBackTextColor;
		}
	}

	public boolean isShowCenterTitle()
	{
		return showCenterTitle;
	}

	public void setShowCenterTitle(boolean showCenterTitle)
	{
		if(mCenterTextView != null && mCenterSpinner != null)
		{
			if(showCenterTitle)
			{
				mCenterTextView.setVisibility(VISIBLE);
				mCenterSpinner.setVisibility(GONE);
			}
			else
			{
				mCenterTextView.setVisibility(GONE);
				mCenterSpinner.setVisibility(VISIBLE);
			}
			this.showCenterTitle = showCenterTitle;
		}
	}

	public String getCenterTitleText()
	{
		return mCenterTitleText;
	}

	public void setCenterTitleText(String mCenterTitleText)
	{
		if(mCenterTextView != null)
		{
			mCenterTextView.setText(mCenterTitleText);
			this.mCenterTitleText = mCenterTitleText;
		}
	}

	public float getCenterTitleTextSize()
	{
		return mCenterTitleTextSize;
	}

	public void setCenterTitleTextSize(float mCenterTitleTextSize)
	{
		if(mCenterTextView != null)
		{
			mCenterTextView.setTextSize(mCenterTitleTextSize);
			this.mCenterTitleTextSize = mCenterTitleTextSize;
		}
	}

	public int getCenterTitleTextColor()
	{
		return mCenterTitleTextColor;
	}

	public void setCenterTitleTextColor(int mCenterTitleTextColor)
	{
		if(mCenterTextView != null)
		{
			mCenterTextView.setTextColor(mCenterTitleTextColor);
			this.mCenterTitleTextColor = mCenterTitleTextColor;
		}
	}

	public boolean isShowRightImg()
	{
		return showRightImg;
	}

	public void setShowRightImg(boolean showRightImg)
	{
		if(mRightImageButton != null)
		{
			if(showRightImg)
			{
				mRightImageButton.setVisibility(VISIBLE);
			}
			else
			{
				mRightImageButton.setVisibility(GONE);
			}
			this.showRightImg = showRightImg;
		}
	}

	public int getRightImgResId()
	{
		return mRightImgResId;
	}

	public void setRightImgResId(int mRightImgResId)
	{
		if(mRightImageButton != null)
		{
			mRightImageButton.setBackgroundResource(mRightImgResId);
			this.mRightImgResId = mRightImgResId;
		}
	}

	public boolean isShowRightText()
	{
		return showRightText;
	}

	public void setShowRightText(boolean showRightText)
	{
		if(mRightTextView != null)
		{
			if(showRightText)
			{
				mRightTextView.setVisibility(VISIBLE);
			}
			else
			{
				mRightTextView.setVisibility(GONE);
			}
			this.showRightText = showRightText;
		}
	}

	public String getRightTipText()
	{
		return mRightTipText;
	}

	public void setRightTipText(String mRightTipText)
	{
		if(mRightTextView != null)
		{
			mRightTextView.setText(mRightTipText);
			this.mRightTipText = mRightTipText;
		}
	}

	public float getRightTipTextSize()
	{
		return mRightTipTextSize;
	}

	public void setRightTipTextSize(float mRightTipTextSize)
	{
		if(mRightTextView != null)
		{
			mRightTextView.setTextSize(mRightTipTextSize);
			this.mRightTipTextSize = mRightTipTextSize;
		}
	}

	public int getRightTipTextColor()
	{
		return mRightTipTextColor;
	}

	public void setRightTipTextColor(int mRightTipTextColor)
	{
		if(mRightTextView != null)
		{
			mRightTextView.setTextColor(mRightTipTextColor);
			this.mRightTipTextColor = mRightTipTextColor;
		}
	}

	public LinearLayout getRootLinearLayout()
	{
		return mRootLinearLayout;
	}

	public TextView getLeftTextView()
	{
		return mLeftTextView;
	}

	public ImageButton getLeftImageButton()
	{
		return mLeftImageButton;
	}

	public TextView getCenterTextView()
	{
		return mCenterTextView;
	}

	public Spinner getCenterSpinner()
	{
		return mCenterSpinner;
	}

	public TextView getRightTextView()
	{
		return mRightTextView;
	}

	public ImageButton getRightImageButton()
	{
		return mRightImageButton;
	}

	public void setLeftTextClickable(boolean enable)
	{
		if(mLeftTextView != null)
		{
			mLeftTextView.setClickable(enable);
		}
	}
	
	public void setLeftTextClickListener(OnClickListener l)
	{
		if(mLeftTextView != null)
		{
			mLeftTextView.setOnClickListener(l);
		}
	}
	
	public void setLeftImgClickable(boolean enable)
	{
		if(mLeftImageButton != null)
		{
			mLeftImageButton.setClickable(enable);
		}
	}
	
	public void setLeftImgClickListener(OnClickListener l)
	{
		if(mLeftImageButton != null)
		{
			mLeftImageButton.setOnClickListener(l);
		}
	}
	
	public void setRightTextClickable(boolean enable)
	{
		if(mRightTextView != null)
		{
			mRightTextView.setClickable(enable);
		}
	}
	
	public void setRightTextClickListener(OnClickListener l)
	{
		if(mRightTextView != null)
		{
			mRightTextView.setOnClickListener(l);
		}
	}
	
	public void setRightImgClickable(boolean enable)
	{
		if(mRightImageButton != null)
		{
			mRightImageButton.setClickable(enable);
		}
	}
	
	public void setRightImgClickListener(OnClickListener l)
	{
		if(mRightImageButton != null)
		{
			mRightImageButton.setOnClickListener(l);
		}
	}
}
