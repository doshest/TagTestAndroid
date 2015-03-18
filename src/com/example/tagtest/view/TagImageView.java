package com.example.tagtest.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.tagtest.R;
import com.example.tagtest.util.Common;

import android.content.Context;
import android.util.AttributeSet;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * ΪͼƬ��ӱ�ǩ�����һ��RelativeLayout ʹ��ʱ�������һ��ͼƬ����.
 * @author doshest
 *
 */
public class TagImageView extends RelativeLayout {
	boolean isTagMove = false;
	/** �����Ķ��� */
	private Context context;
	/** ����ӿռ� tag view �ļ��� */
	private List<View> tagViewList;
	/** ��ǩӦ�ó���ķ���*/
	private transient boolean directionLeft = true;

	
	/**
	 * ��ǩ�߽�
	 */
	private int pLeft;
	private int pRight;
	private int pTop;
	private int pBottom;
	

	public TagImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		
	}

	public TagImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public TagImageView(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 *��ӻ���ǩ
	 * @param content
	 * @param tagX
	 * @param tagY
	 * @param left
	 * @param right
	 * @param top
	 * @param bottom
	 */
	public void addTextTag(String content, int tagX, int tagY,int left,int right,int top,int bottom) {
		if (tagViewList == null)
			tagViewList = new ArrayList<View>();
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.tag, null);
		final TextView text = (TextView) view.findViewById(R.id.tag_text);
		FilterTagLayout layout = (FilterTagLayout) view.findViewById(R.id.tag_layout);
		text.setText(content);
	 
		setTagViewOnTouchListener(layout);
		this.addView(layout);
		if(tagX>((right-left)/2)){//����
			System.out.println("add right"+tagX+"  "+tagY);
			directionLeft = false;
			layout.isDirectionLeft = false;
			
			layout.setBackgroundResource(R.drawable.preview_tag_right);
			layout.setPadding(Common.dipToPX(context, 4), //left
					Common.dipToPX(context, 0), //top
					Common.dipToPX(context, 34)//right
					, Common.dipToPX(context, 0));//bottom
		}else{//����
			System.out.println("add left" +tagX+"  "+tagY);
			directionLeft = true;
			layout.isDirectionLeft = true;
			layout.setBackgroundResource(R.drawable.preview_tag_left);
			layout.setPadding(Common.dipToPX(context, 34), //left
					Common.dipToPX(context, 0),//top
					Common.dipToPX(context, 4)//right
					, Common.dipToPX(context, 0));//bottom
		}
		setTagViewPosition(layout, tagX, tagY,left,right,top,bottom);
		tagViewList.add(layout);
		

	}

/**
 * Ϊ��ǩ���touch����
 * @param layoutView
 */
	public void setTagViewOnTouchListener(RelativeLayout layoutView) {
		layoutView.setOnTouchListener(new OnTouchListener() {
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
						// ��ȡ��ָ�ƶ��ľ���
						isTagMove = true;
						int x = (int) event.getRawX();
						int y = (int) event.getRawY();
						int dx = x - startx;
						int dy = y - starty;
						// ����imageView�ڴ����λ��
						setTagViewPosition(v, dx, dy);
						// ��ȡ�ƶ����λ��
						startx = (int) event.getRawX();
						starty = (int) event.getRawY();
						
						getParent().requestDisallowInterceptTouchEvent(true);
						break;
						//return false;
					//	break;
					case MotionEvent.ACTION_UP:	
						isTagMove = false;
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
		});
	}
	/**
	 * ��һ�γ�ʼ�����Ͻ�����
	 */
	private int firstLeft =0;
	private int firstTop = 0;
	
	private void setTagViewPosition(final View tagView, final int dx, final int dy,int left,int right,int top,int bottom ){
		this.pLeft = left;
		this.pRight = right;
		this.pTop = top;
		this.pBottom = bottom;
		final FilterTagLayout temp = (FilterTagLayout) tagView;
		if(tagView.getWidth()==0){
			ViewTreeObserver obv = tagView.getViewTreeObserver();
			obv.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
				
				@Override
				public boolean onPreDraw() {
					if(temp.isDirectionLeft){//directionleft
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
			if(!temp.isDirectionLeft){
				firstLeft = dx - tagView.getWidth()+Common.dipToPX(context, 14);// 14 = 12 + 2;
			}else{
				firstLeft = dx -Common.dipToPX(context, 14);//14 = 12 +2;
			}
			firstTop = dy - tagView.getHeight()/2;//tagView.getHeight()/2;
			
			setTagViewPosition( tagView, firstLeft, firstTop);
			
			if(positions == null){
				positions = new HashMap<View, RelativePosition>();
			}
			positions.put(tagView, 
					new RelativePosition(tagView, firstLeft, firstTop,directionLeft));
		}
	}
	/**
	 * �����ӱ�ǩ�������ƶ����²��ֱ�ǩ
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

	
	public List<View> getTagViewList() {
		return tagViewList;
	}


	public void setTagViewList(List<View> tagViewList) {
		this.tagViewList = tagViewList;
	}

/**
 * ��ձ�ǩ
 */
	public void clearTagView() {
		if (tagViewList != null && tagViewList.size() > 0) {
			removeViews(1, tagViewList.size());
			tagViewList.clear();
		}
	}
	/**
	 *ɾ����ǩ
	 * @param v
	 */
	public void removeTag(View v){
			if(v!=null)
			 tagViewList.remove(v);
		
		removeView(v);
	}
	/**
	 * �ƶ���ǩ���λ�õ�������
	 * @param tagView
	 */
	public void changeBackGround(View tagView){
		FilterTagLayout temp = (FilterTagLayout) tagView;
		int tagLeft = tagView.getLeft();
		int tagTop = tagView.getTop();
		int tagRight = tagView.getRight();
		int tagBottom = tagView.getBottom();
		int tagWidth = tagView.getWidth();
		
		if(temp.isDirectionLeft && ((tagLeft+Common.dipToPX(context, 14))>((pRight-pLeft)/2))){
			System.out.println("up should right"  );
			tagRight = tagLeft+Common.dipToPX(context, 28);
			tagLeft = tagRight - tagWidth;
			directionLeft = false;
			temp.isDirectionLeft = false;
			//if(tagView.getBackground().equals(o))
			tagView.setBackgroundResource(R.drawable.preview_tag_right);
			tagView.setPadding(Common.dipToPX(context, 4), //left
					Common.dipToPX(context, 0), //top
					Common.dipToPX(context, 34)//right
					, Common.dipToPX(context,0));//bottom
			
			System.out.println("up should right over" );
		}
		if(!temp.isDirectionLeft && (tagRight-Common.dipToPX(context, 14))<((pRight-pLeft)/2)){
			
			System.out.println("up should left" );
			tagLeft = tagRight - Common.dipToPX(context, 28);
			tagRight = tagLeft + tagWidth;
			//tagView.setBackgroundResource(0);
			tagView.setBackgroundResource(R.drawable.preview_tag_left);
			tagView.setPadding(Common.dipToPX(context,34), //left
					Common.dipToPX(context, 0), //top
					Common.dipToPX(context, 4)//right
					, Common.dipToPX(context, 0));//bottom
					
			directionLeft = true;
			temp.isDirectionLeft= true;
			System.out.println("up should left over" );
		
		}
		tagView.layout(tagLeft, tagTop, tagRight, tagBottom);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tagView.getLayoutParams();
		params.leftMargin = tagLeft;
		tagView.setLayoutParams(params);
		invalidate();
		//tagView.invalidate();
		//tagView.requestLayout();
		if(positions == null){
			positions = new HashMap<View, RelativePosition>();
		}
		positions.put(tagView, 
				new RelativePosition(tagView, tagLeft, tagTop,directionLeft));
		
	}
	private Map<View,RelativePosition>positions ;
	public class RelativePosition{
		float xDirection;
		float yDirection;
		View tag;
		boolean isLeft;
		//x left y top
		public RelativePosition(View tag,int x,int y,boolean isLeft){
			if(isLeft){//����
				this.xDirection = ((float)(x +Common.dipToPX(context, 14)))/TagImageView.this.getWidth();
				this.yDirection = ((float)(y+tag.getHeight()/2))/TagImageView.this.getHeight();
			}else{//����
				this.xDirection = ((float)(x +tag.getWidth()-Common.dipToPX(context, 14)))/TagImageView.this.getWidth();
				this.yDirection = ((float)(y+tag.getHeight()/2))/TagImageView.this.getHeight();
			}
			
			this.tag = tag;
			this.isLeft = isLeft;
		}
	}
	
	public Map<View,RelativePosition> getAllTagPositions(){
		return this.positions;
	}

	
}


