package com.example.tagtest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class TagContainer extends RelativeLayout{

	public TagContainer(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
	}
	public TagContainer(Context context,AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
		
	}
	
	public void addTag(Context context,String content,int tagX, int tagY,int left,int right,int top,int bottom){
		TagRelativeLayout tag = new TagRelativeLayout(context, content, tagX, tagY, left, right, top, bottom);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT
				, LayoutParams.WRAP_CONTENT);
		tag.setLayoutParams(params);
		addView(tag);
		this.invalidate();
	}
	


}
