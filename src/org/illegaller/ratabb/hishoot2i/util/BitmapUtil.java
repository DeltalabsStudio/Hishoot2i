package org.illegaller.ratabb.hishoot2i.util;

import java.io.ByteArrayInputStream;

import org.illegaller.ratabb.hishoot2i.Constants;
import org.illegaller.ratabb.hishoot2i.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

public class BitmapUtil {

	// private static final int RADIUS_BLUR = 25;
	private static final String TAG = "BitmapUtil";
	@SuppressWarnings("unused")
	private static final String IMAGE_DEFAULT = "drawable://"
			+ R.drawable.img_default;

	public static Bitmap copyFrom(Bitmap src) {
		return src.copy(src.getConfig(), true);
	}

	public static Bitmap loadImage(Context context, String data, int dstWidth,
			int dstHeight) {
		return scaledBitmap(loadImage(context, data), dstWidth, dstHeight);
	}

	@SuppressWarnings("deprecation")
	public static Bitmap imageDefault(Context context) {
		Resources res = context.getResources();
		Drawable d = res.getDrawable(R.drawable.img_default);
		return ((BitmapDrawable) d).getBitmap();
	}

	public static Bitmap loadImage(Context context, String data) {
		if (data == null)
			return BitmapUtil.imageDefault(context);

		return ImageLoader.getInstance().loadImageSync(data,
				DisplayImageOptions.createSimple());

	}

	public static Bitmap scaledBitmap(Bitmap src, int dstWidth, int dstHeight) {
		return Bitmap.createScaledBitmap(src, dstWidth, dstHeight, true);
	}

	public static Bitmap scaleBitmapDown(Bitmap bitmap, int minSize) {
		final int minDimension = Math
				.min(bitmap.getWidth(), bitmap.getHeight());

		if (minDimension <= minSize) {
			// If the bitmap is small enough already, just return it
			return bitmap;
		}

		final float scaleRatio = minSize / (float) minDimension;
		return Bitmap.createScaledBitmap(bitmap,
				Math.round(bitmap.getWidth() * scaleRatio),
				Math.round(bitmap.getHeight() * scaleRatio), false);
	}

	public static Bitmap cropBitmap(Bitmap bitmap, int padding) {
		return Bitmap.createBitmap(bitmap, padding, padding, bitmap.getWidth()
				- (padding * 2), bitmap.getHeight() - (padding * 2));
	}

	/** Recycle ASAP the given {@code bitmap} if it is not null. */
	public static void recycleBitmap(Bitmap bitmap) {
		if (bitmap != null && !bitmap.isRecycled())
			bitmap.recycle();
	}

	public static void drawIt(Canvas canvas, Bitmap bitmap, int left, int top) {
		if (bitmap != null)
			canvas.drawBitmap(bitmap, left, top, null);
	}

	public static void drawIt(Canvas canvas, Bitmap bitmap, Rect bounds) {
		if (bitmap != null)
			canvas.drawBitmap(bitmap, null, bounds, null);
	}

	public static void drawItBlur(Canvas canvas, Bitmap source, Context context) {
		Rect bounds = new Rect();
		bounds.set(0, 0, source.getWidth(), source.getHeight());
		Pref pref = new Pref(context);
		int radius = pref.getSPref().getInt(Constants.KEY_PREF_BLUR_RADIUS, 25);
		try {
			// if (DeviceUtil.hasICS()) {
			// BitmapUtil.drawIt(canvas,
			// RsBlur.doBlur(source, radius, context), bounds);
			// } else {
			BitmapUtil.drawIt(canvas, StackBlur.doBlur(source, radius, false),
					bounds);
			// }
		} catch (OutOfMemoryError e) {
			BitmapUtil.drawIt(canvas, source, 0, 0);
			pref.putAndApply(Constants.KEY_PREF_BLUR_BG, false);
			pref.remove(Constants.KEY_PREF_SKIN_PACKAGE);
			System.gc();
			Log.e(TAG, "OutOfMemoryError on Blur");
		}
	}

	/*  */
	public static Bitmap DrawMe(Bitmap frame, int framex, int framey,
			Bitmap ss, int ssx, int ssy, Context context, int w, int h)
			throws OutOfMemoryError {

		Bitmap b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(b);
		canvas.drawBitmap(ss, ssx, ssy, null);
		canvas.drawBitmap(frame, framex, framey, null);

		return b;

	}

	@SuppressWarnings("deprecation")
	public static Bitmap getNine(int id, int x, int y, Context context) {
		Bitmap out = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(out);

		NinePatchDrawable bg = (NinePatchDrawable) context.getResources()
				.getDrawable(id);

		if (bg != null) {
			bg.setBounds(0, 0, x, y);
			bg.draw(c);
		}
		return out;
	}

	public static Bitmap resizeImage(Bitmap image, int maxWidth, int maxHeight)
			throws Throwable, OutOfMemoryError {
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		double imageAspect = (double) imageWidth / imageHeight;
		double canvasAspect = (double) maxWidth / maxHeight;
		double scaleFactor;

		if (imageAspect < canvasAspect) {
			scaleFactor = (double) maxHeight / imageHeight;
		} else {
			scaleFactor = (double) maxWidth / imageWidth;
		}

		float scaleWidth = ((float) scaleFactor) * imageWidth;
		float scaleHeight = ((float) scaleFactor) * imageHeight;

		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);

		return Bitmap.createScaledBitmap(image, (int) scaleWidth,
				(int) scaleHeight, true);
	}

	public static Bitmap fixMark(Bitmap source, int d) {
		double a = 1;
		switch (d) {
		case DisplayMetrics.DENSITY_LOW:
			a = 0.75;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			a = 1;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			a = 1.5;
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			a = 2;
			break;
		case DisplayMetrics.DENSITY_XXHIGH:
			a = 3;
			break;
		case DisplayMetrics.DENSITY_XXXHIGH:
			a = 4;
			break;
		default:
			break;
		}

		int w = (int) (source.getWidth() * a);
		int h = (int) (source.getHeight() * a);

		try {
			return resizeImage(source, w, h);
		} catch (OutOfMemoryError e) {
			return source;
		} catch (Throwable e) {
			return source;
		}

	}

	public static Bitmap getMark(String string, int density) {
		Bitmap result = BitmapFactory.decodeStream(new ByteArrayInputStream(
				Base64.decode(string, Base64.DEFAULT)));
		return fixMark(result, density);
	}

}