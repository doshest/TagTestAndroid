package com.example.tagtest;



import com.example.tagtest.view.AddTagPopupWindow;
import com.example.tagtest.view.TagContainer;
import com.example.tagtest.view.TagImageView;


import android.R.layout;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Matrix;
import android.util.Log;
import android.view.Gravity;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import android.widget.Toast;

public class MainActivity extends Activity {
	private ImageView background;
	private AddTagPopupWindow popup;
//	private TagContainer container ;
	private TagImageView container;
	private MyPoint point ;
	RelativeLayout layout;

	
	private int pLeft;
	private int pRight;
	private int pTop;
	private int pBottom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		final ImageView iv = background;
		 iv.post(new Runnable(){   
			  
	            @Override  
	            public void run() {  
	                // TODO Auto-generated method stub  
	                  
	                //ImageView的宽和高  
	                Log.d("lxy", "iv_W = " + iv.getWidth() + ", iv_H = " + iv.getHeight());  
	  
	                //获得ImageView中Image的真实宽高，  
	                int dw = iv.getDrawable().getBounds().width();  
	                int dh = iv.getDrawable().getBounds().height();  
	                Log.d("lxy", "drawable_X = " + dw + ", drawable_Y = " + dh);  
	                  
	                //获得ImageView中Image的变换矩阵  
	                Matrix m = iv.getImageMatrix();  
	                float[] values = new float[10];  
	                m.getValues(values);  
	                  
	                //Image在绘制过程中的变换矩阵，从中获得x和y方向的缩放系数  
	                float sx = values[0];  
	                float sy = values[4];  
	                Log.d("lxy", "scale_X = " + sx + ", scale_Y = " + sy);  
	                  
	                //计算Image在屏幕上实际绘制的宽高  
	                int cw = (int)(dw * sx);  
	                int ch = (int)(dh * sy);  
	                Log.d("lxy", "caculate_W cw = " + cw + ", caculate_H = " + ch);  
	                pLeft = (iv.getWidth()-cw)/2;
	                pRight = pLeft + cw;
	                pTop = (iv.getHeight()-ch)/2;
	                pBottom = pTop + ch;
	                System.out.println("image area first: "+pLeft +"  "+pRight+"  "+pTop+" "+ pBottom);
	            }});  
	}
	boolean isViewPagerMove =false;
	private void initView(){
		layout = (RelativeLayout) findViewById(R.id.test_main);
		background = (ImageView)findViewById(R.id.background);
		popup = new AddTagPopupWindow(MainActivity.this, new MyClickListener());
		container = (TagImageView) findViewById(R.id.container);
		//container = new TagImageView(MainActivity.this);
		//RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		//container.setLayoutParams(params);
		//container.add
		//layout.addView(container);
	
		background.setScaleType(ScaleType.FIT_CENTER);
		background.setOnTouchListener(new View.OnTouchListener() {
			int x1 = 0;
			int y1 = 0;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						x1 =(int) event.getX();
						y1 = (int)event.getY();
						System.out.println("down");
						break;
					case MotionEvent.ACTION_UP:
						if(isViewPagerMove){
							return false;
						}
						isViewPagerMove = false;
						System.out.println("up");
						int x = (int)event.getX();
						int y = (int)event.getY();
						if((x<pLeft)||(x>pRight)||(y<pTop)||(y>pBottom)){
							System.out.println("out of earea");
							break;
						}
						popup.showAtLocation(v, Gravity.CENTER, 0, 0);
						point = null;
						point = new MyPoint(x , y);
						//return false;
						break;
					case MotionEvent.ACTION_MOVE:
						System.out.println("iamge move");
						int y2 = (int)event.getY();
						int x2 = (int)event.getX();
						//System.out.println(x1+" y1 "+y1+" x2 "+event.getX()+" y2 +"+event.getY());
						System.out.println(getDistance(x1, y1, x2, y2));
						if(getDistance(x1, y1, x2, y2)!=0){
							isViewPagerMove = true;
						}else{
							isViewPagerMove = false;
						}
						if(isViewPagerMove){
							return false;
						}else{
							break;	
						}
				}
				
				return true;
			}
		});
		//background.getParent().
	//	layout.removeView(background);
	//	container.addView(background);
		//container.add
	}

	class MyClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(popup.getTag_edit_attribute().equals("")){
				Toast.makeText(MainActivity.this, "标签名不能为空", 3000).show();
				return;
			}
			
			popup.dismiss();
			container.addTextTag(popup.getTag_edit_attribute(), point.x, point.y,
					pLeft,pRight,pTop,pBottom);
		//	container.addTag(MainActivity.this, popup.getTag_edit_attribute(), point.x, point.y,
				//	pLeft, pRight, pTop, pBottom);
			System.out.println("image area: "+pLeft +"  "+pRight+"  "+pTop+" "+ pBottom);
			popup.setEdit_attribute("");
		}
		
	}
	
	class MyPoint{
		public int x;
		public int y;
		public MyPoint (int x,int y){
			this.x = x;
			this.y = y;
		}
	
	}
	private int getDistance(int x1,int y1,int x2,int y2){
		return (x1-x2)*(x1-x2)+(y1-y2)*(y1-y2);
	}
	



}
