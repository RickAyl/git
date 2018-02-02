package com.eostek.rick.demo.designviewgroup.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.eostek.rick.demo.designviewgroup.R;

/**
 * Created by a on 17-6-21.
 */

public class IconDesignView extends View {

    /**
     * TYPE_CIRCLE / TYPE_ROUND
     */
    private int type;
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_ROUND = 1;

    /**
     * 图片
     */
    private Bitmap mSrc;

    /**
     * 圆角的大小
     */
    private int mRadius;

    /**
     * 控件的宽度
     */
    private int mWidth;
    /**
     * 控件的高度
     */
    private int mHeight;

    public IconDesignView(Context context) {
        this(context, null);
    }

    public IconDesignView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconDesignView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray tpArray =
                context.
                        getTheme().
                        obtainStyledAttributes(attrs, R.styleable.CustomImageView, defStyleAttr, 0);
        int defCount = tpArray.getIndexCount();

        for (int i = 0; i < defCount; i++) {
            int attr = tpArray.getIndex(i);
            switch (attr) {
                case R.styleable.CustomImageView_borderRadius:
                    mRadius = tpArray.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                    10f, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomImageView_type:
                    type = tpArray.getInt(attr, 0);
                    break;

                case R.styleable.CustomImageView_src1:
                    mSrc = BitmapFactory.
                            decodeResource(getResources(), tpArray.getResourceId(attr, 0));
                    break;

                default:
                    break;
            }
        }
        tpArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureModeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        switch (measureModeWidth) {
            case MeasureSpec.EXACTLY:
                mWidth = width;
                break;
            case MeasureSpec.AT_MOST:
                int measureSize = mSrc.getWidth() + getPaddingLeft() + getPaddingRight();
                mWidth = Math.min(measureSize, width);
                break;
            case MeasureSpec.UNSPECIFIED:
                mWidth = mSrc.getWidth() + getPaddingLeft() + getPaddingRight();
                break;
            default:
                break;
        }
        int measureModeHeight = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        switch (measureModeHeight) {
            case MeasureSpec.EXACTLY:
                mHeight = height;
                break;
            case MeasureSpec.AT_MOST:
                int measureSize = mSrc.getHeight() + getPaddingBottom() + getPaddingTop();
                mHeight = Math.min(measureSize, height);
                break;
            case MeasureSpec.UNSPECIFIED:
                mHeight = mSrc.getHeight() + getPaddingBottom() + getPaddingTop();
                break;
            default:
                break;
        }

        setMeasuredDimension(mWidth, mHeight);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        switch (type) {
            case TYPE_CIRCLE:
                int min = Math.min(mWidth, mHeight);
                mSrc = Bitmap.createScaledBitmap(mSrc, min, min, false);
                canvas.drawBitmap(createCircleImage(mSrc, min), 0, 0, null);
                break;

            case TYPE_ROUND:
                canvas.drawBitmap(createRoundConerImage(mSrc), 0, 0, null);
                break;

            default:
                break;
        }
    }


    /**
     * 根据原图和变长绘制圆形图片
     *
     * @param source
     * @param min
     * @return
     */
    private Bitmap createCircleImage(Bitmap source, int min) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        /**
         * 首先绘制圆形
         */
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        /**
         * 使用SRC_IN，参考上面的说明
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * 绘制图片
         */
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }


    /**
     * 根据原图添加圆角
     *
     * @param source
     * @return
     */
    private Bitmap createRoundConerImage(Bitmap source) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rect, mRadius, mRadius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    public void testMethon(){
        animate();
    }
}
