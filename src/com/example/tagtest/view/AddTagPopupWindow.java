package com.example.tagtest.view;



import com.example.tagtest.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;




public class AddTagPopupWindow extends PopupWindow {
	private View mDataView;
	/** 取消按钮 */
	private TextView text_cancel;
	/** 确认按钮 */
	private TextView txt_confirme;
	/** 文本输入框 */
	private EditText edit_tag;
	/** 背景 */
	private RelativeLayout rl_bg;


	public AddTagPopupWindow(Activity context, OnClickListener mOnClickListener) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mDataView = inflater.inflate(R.layout.my_data_window, null);
		edit_tag = (EditText) mDataView.findViewById(R.id.edit_tag);
		text_cancel = (TextView) mDataView.findViewById(R.id.text_cancel);
		txt_confirme = (TextView) mDataView.findViewById(R.id.txt_confirme);
		rl_bg = (RelativeLayout) mDataView.findViewById(R.id.rl_bg);
		txt_confirme.setOnClickListener(mOnClickListener);
		text_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		rl_bg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		this.setContentView(mDataView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0xcc000000);
		this.setBackgroundDrawable(dw);
	}

	@Override
	public void dismiss() {
		super.dismiss();

	}


	public String getTag_edit_attribute() {
		String result = edit_tag.getText().toString().trim();
		return result;
	}


	public void setEdit_attribute(String content) {
		edit_tag.setText(content);
	}

}
