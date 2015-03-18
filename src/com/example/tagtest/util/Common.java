package com.example.tagtest.util;

import android.content.Context;
import android.util.TypedValue;

public class Common {
	   public static int dipToPX(final Context ctx, float dip) {
			return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, ctx.getResources().getDisplayMetrics());
		}
}
