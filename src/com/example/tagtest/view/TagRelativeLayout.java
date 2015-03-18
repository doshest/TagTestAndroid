package com.example.tagtest.view;

import java.util.ArrayList;
import java.util.List;

import com.example.tagtest.R;
import com.example.tagtest.util.Common;

import android.content.Context;
import android.graphics.Color;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TagRelativeLayout extends RelativeLayout{
	
	private transient boolean directionLeft = true;
	private transient boolean isMove = false;
	
	/**
	 * 标签边界
	 */
	private int pLeft;
	private int pRight;
	private int pTop;
	private int pBottom;
	private Context context;

	private GestureDetector mGestureDetector;
	public TagRelativeLayout(Context context,String content,int tagX, int tagY,int left,int right,int top,int bottom) {
		super(context);
		TagTouchListener tagTouchListener = new TagTouchListener();
		setOnTouchListener(tagTouchListener);
		this.context = context;
		this.setGravity(Gravity.CENTER_VERTICAL);
		// TODO Auto-generated constructor stub
		addTextTag(content, tagX, tagY, left, right, top, bottom);
		System.out.println("tag add");
	}
	
	public void addTextTag(String content, int tagX, int tagY,int left,int right,int top,int bottom) {
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	//	View view = mInflater.inflate(R.layout.tag, null);
		final TextView text = new TextView(context);
		text.setGravity(Gravity.CENTER_VERTICAL);
		text.setTextColor(Color.WHITE);
		text.setText(content);
		
	
		//this.addView(layout);
		if(tagX>((right-left)/2)){//向右
			System.out.println("add right"+tagX+"  "+tagY);
			directionLeft = false;
			setBackgroundResource(R.drawable.preview_tag_right);
			setPadding(Common.dipToPX(context, 4), //left
					Common.dipToPX(context, 0), //top
					Common.dipToPX(context, 34)//right
					, Common.dipToPX(context, 0));//bottom
		}else{//向左
			System.out.println("add left" +tagX+"  "+tagY);
			directionLeft = true;
			setBackgroundResource(R.drawable.preview_tag_left);
			setPadding(Common.dipToPX(context, 34), //left
					Common.dipToPX(context, 0),//top
					Common.dipToPX(context, 4)//right
					, Common.dipToPX(context, 0));//bottom
		}
		setTagViewPositionFirst(this, tagX, tagY,left,right,top,bottom);
	
		
		addView(text);

	}


	
	public class TagTouchListener implements OnTouchListener{
		int startx = 0;
		int starty = 0;
		@Override
		public boolean onTouch(View v, MotionEvent event) {
		//	v.dispatchTouchEvent(event);
			if (v.getId() == R.id.tag_layout) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startx = (int) event.getRawX();
					starty = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					// 获取手指移动的距离
				
					int x = (int) event.getRawX();
					int y = (int) event.getRawY();
					int dx = x - startx;
					int dy = y - starty;
					// 更改imageView在窗体的位置
					setTagViewPosition(v, dx, dy);
					// 获取移动后的位置
					startx = (int) event.getRawX();
					starty = (int) event.getRawY();
					
					getParent().requestDisallowInterceptTouchEvent(true);
					break;
					//return false;
				//	break;
				case MotionEvent.ACTION_UP:	
				
					changeBackGround(v);
					//return false;
					getParent().requestDisallowInterceptTouchEvent(false);
					break;
				case MotionEvent.ACTION_CANCEL:
					getParent().requestDisallowInterceptTouchEvent(false);
					break;
				}
			}
			return true;
		}
		
			
		
		
	
		
		
	}
	
	/**
	 * 第一次初始化左上角坐标
	 */
	private int firstLeft =0;
	private int firstTop = 0;
	
	private void setTagViewPositionFirst(final View tagView, final int dx, final int dy,int left,int right,int top,int bottom ){
		System.out.println("first add "+dx+"  "+dy);
		this.pLeft = left;
		this.pRight = right;
		this.pTop = top;
		this.pBottom = bottom;
		if(tagView.getWidth()==0){
			ViewTreeObserver obv = tagView.getViewTreeObserver();
			obv.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
				
				@Override
				public boolean onPreDraw() {
					if(!directionLeft){
						 firstLeft = dx - tagView.getMeasuredWidth()+Common.dipToPX(context, 14);// 14 = 12 + 2;
					}else{
						firstLeft = dx -Common.dipToPX(context, 14);//14 = 12 +2;
					}
					firstTop = dy - tagView.getMeasuredHeight()/2;
					setTagViewPosition( tagView, firstLeft, firstTop);
					tagView.getViewTreeObserver().removeOnPreDrawListener(this);
					return true;
				}
			});
			
		}else{
			if(!directionLeft){
				firstLeft = dx - tagView.getWidth()+Common.dipToPX(context, 14);// 14 = 12 + 2;
			}else{
				firstLeft = dx -Common.dipToPX(context, 14);//14 = 12 +2;
			}
			firstTop = dy - tagView.getHeight()/2;//tagView.getHeight()/2;
			
			setTagViewPosition( tagView, firstLeft, firstTop);
		}
	}
	/**
	 * 最后添加标签，包括移动重新布局标签
	 * @param tagView
	 * @param dx
	 * @param dy
	 */
	private void setTagViewPosition(View tagView, int dx, int dy) {

		int left = tagView.getLeft() + dx;
		int top = tagView.getTop() + dy;
		
		if (left < pLeft) {
			left = pLeft;
		} else if ((left + tagView.getWidth()) >= pRight) {
			left = pRight - tagView.getWidth();
		}
		if (top < pTop) {
			top = pTop;
		} else if ((top + tagView.getHeight()) >= pBottom) {
			top = pBottom - tagView.getHeight();
		}
		
		int right = left + tagView.getWidth();
		int bottom = top + tagView.getHeight();
		tagView.layout(left, top, right, bottom);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tagView.getLayoutParams();
		params.leftMargin = left;
		params.topMargin = top;

		tagView.setLayoutParams(params);
		
	}
	
	/**
	 * 移动标签最后位置调整方向
	 * @param tagView
	 */
	public void changeBackGround(View tagView){

		int tagLeft = tagView.getLeft();
		int tagTop = tagView.getTop();
		int tagRight = tagView.getRight();
		int tagBottom = tagView.getBottom();
		int tagWidth = tagView.getWidth();
		
		if(directionLeft && (tagLeft>((pRight-pLeft)/2))){
			System.out.println("up should right"  );
			tagRight = tagLeft+Common.dipToPX(context, 28);
			tagLeft = tagRight - tagWidth;
			directionLeft = false;
			
			tagView.setBackgroundResource(R.drawable.preview_tag_right);
			tagView.setPadding(Common.dipToPX(context, 4), //left
					Common.dipToPX(context, 0), //top
					Common.dipToPX(context, 34)//right
					, Common.dipToPX(context,0));//bottom
			
			System.out.println("up should right over" );
		}
		if(!directionLeft && tagRight<((pRight-pLeft)/2)){
			
			System.out.println("up should left" );
			tagLeft = tagRight - Common.dipToPX(context, 28);
			tagRight = tagLeft + tagWidth;
			tagView.setBackgroundResource(0);
			tagView.setBackgroundResource(R.drawable.preview_tag_left);
			tagView.setPadding(Common.dipToPX(context,34), //left
					Common.dipToPX(context, 0), //top
					Common.dipToPX(context, 4)//right
					, Common.dipToPX(context, 0));//bottom
					
			directionLeft = true;
			System.out.println("up should left over" );
		
		}
		tagView.layout(tagLeft, tagTop, tagRight, tagBottom);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tagView.getLayoutParams();
		params.leftMargin = tagLeft;
		tagView.setLayoutParams(params);
	}
	
	
	
	


}
