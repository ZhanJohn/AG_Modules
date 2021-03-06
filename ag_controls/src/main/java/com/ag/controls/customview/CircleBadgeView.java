package com.ag.controls.customview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TabWidget;
import android.widget.TextView;

/**
 * 圆形角标
 */
public class CircleBadgeView extends TextView {
    /**
     * 左上角
     */
    public static final int POSITION_TOP_LEFT = 1;
    /**
     * 右上角
     */
    public static final int POSITION_TOP_RIGHT = 2;
    /**
     * 左下角
     */
    public static final int POSITION_BOTTOM_LEFT = 3;
    /**
     * 右下角
     */
    public static final int POSITION_BOTTOM_RIGHT = 4;
    /**
     * 居中
     */
    public static final int POSITION_CENTER = 5;
    /**
     * 右边，垂直居中
     */
    public static final int POSITION_RIGHT_CENTER_VERTICAL = 6;
    /**
     * 左边，垂直居中
     */
    public static final int POSITION_LEFT_CENTER_VERTICAL = 7;
    /**
     * 顶部，水平居中
     */
    public static final int POSITION_TOP_CENTER_HORIZONTAL = 8;
    /**
     * 底部，水平居中
     */
    public static final int POSITION_BOTTOM_CENTER_HORIZONTAL = 9;

    private static final int DEFAULT_MARGIN_DIP = 2;//5
    private static final int DEFAULT_LR_PADDING_DIP = 2;//5
    private static final int DEFAULT_CORNER_RADIUS_DIP = 8;
    private static final int DEFAULT_POSITION = POSITION_RIGHT_CENTER_VERTICAL;
    private static final int DEFAULT_BADGE_COLOR = Color.RED;
    private static final int DEFAULT_TEXT_COLOR = Color.WHITE;

    private static Animation fadeIn;
    private static Animation fadeOut;

    private Context context;
    private View target;

    private int badgePosition;
    private int badgeMargin;
    private int badgeColor;

    private boolean isShown;

    //private ShapeDrawable badgeBg;

    private int targetTabIndex;

    private Paint mBgPaint = new Paint();

    PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

    private int width;
    private int height;

    public CircleBadgeView(Context context) {
        this(context, (AttributeSet) null, android.R.attr.textViewStyle);
        mBgPaint.setColor(Color.WHITE);
        mBgPaint.setAntiAlias(true);
    }

    public CircleBadgeView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
        mBgPaint.setColor(Color.WHITE);
        mBgPaint.setAntiAlias(true);
    }

    /**
     * Constructor -
     * <p>
     * create a new BadgeView instance attached to a target {@link View}.
     *
     * @param context context for this view.
     * @param target  the View to attach the badge to.
     */
    public CircleBadgeView(Context context, View target) {
        this(context, null, android.R.attr.textViewStyle, target, 0);
    }

    public CircleBadgeView(Context context, View target, int width, int height) {
        this(context, null, android.R.attr.textViewStyle, target, 0);
        this.width = width;
        this.height = height;
    }

    /**
     * Constructor -
     * <p>
     * create a new BadgeView instance attached to a target {@link TabWidget}
     * tab at a given index.
     *
     * @param context context for this view.
     * @param target  the TabWidget to attach the badge to.
     * @param index   the position of the tab within the target.
     */
    public CircleBadgeView(Context context, TabWidget target, int index) {
        this(context, null, android.R.attr.textViewStyle, target, index);
    }

    public CircleBadgeView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, null, 0);
    }

    public CircleBadgeView(Context context, AttributeSet attrs, int defStyle, View target, int tabIndex) {
        super(context, attrs, defStyle);
        init(context, target, tabIndex);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int max = Math.max(measuredWidth, measuredHeight);
        setMeasuredDimension(max, max);
    }


    @Override
    public void setBackgroundColor(int color) {
        //super.setBackgroundColor(Color.RED);
        mBgPaint.setColor(color);
    }


    @Override
    public void draw(Canvas canvas) {
        canvas.setDrawFilter(pfd);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, Math.max(getWidth(), getHeight()) / 2, mBgPaint);
        super.draw(canvas);
    }

    private void init(Context context, View target, int tabIndex) {
        this.context = context;
        this.target = target;
        this.targetTabIndex = tabIndex;

        // apply defaults
        badgePosition = DEFAULT_POSITION;
        badgeMargin = dipToPixels(DEFAULT_MARGIN_DIP);
        badgeColor = DEFAULT_BADGE_COLOR;

        setTypeface(Typeface.DEFAULT_BOLD);
        int paddingPixels = dipToPixels(DEFAULT_LR_PADDING_DIP);
        setPadding(paddingPixels, 0, paddingPixels, 0);
        setTextColor(DEFAULT_TEXT_COLOR);

        fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(200);

        fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(200);

        isShown = false;

        if (this.target != null) {
            applyTo(this.target);
        } else {
            show();
        }

    }

    private void applyTo(View target) {
        ViewGroup.LayoutParams lp = target.getLayoutParams();
        ViewParent parent = target.getParent();
        FrameLayout container = new FrameLayout(context);

        if (target instanceof TabWidget) {
            // set target to the relevant tab child container
            target = ((TabWidget) target).getChildTabViewAt(targetTabIndex);
            this.target = target;

            ((ViewGroup) target).addView(container,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

            this.setVisibility(View.GONE);
            container.addView(this);
        } else {
            ViewGroup group = (ViewGroup) parent;
            int index = group.indexOfChild(target);

            group.removeView(target);
            group.addView(container, index, lp);

            container.addView(target);

            this.setVisibility(View.GONE);
            container.addView(this);

            group.invalidate();
        }
    }

    /**
     * Make the badge visible in the UI.
     */
    public void show() {
        show(false, null);
    }

    /**
     * Make the badge visible in the UI.
     *
     * @param animate flag to apply the default fade-in animation.
     */
    public void show(boolean animate) {
        show(animate, fadeIn);
    }

    /**
     * Make the badge visible in the UI.
     *
     * @param anim Animation to apply to the view when made visible.
     */
    public void show(Animation anim) {
        show(true, anim);
    }

    /**
     * Make the badge non-visible in the UI.
     */
    public void hide() {
        hide(false, null);
    }

    /**
     * Make the badge non-visible in the UI.
     *
     * @param animate flag to apply the default fade-out animation.
     */
    public void hide(boolean animate) {
        hide(animate, fadeOut);
    }

    /**
     * Make the badge non-visible in the UI.
     *
     * @param anim Animation to apply to the view when made non-visible.
     */
    public void hide(Animation anim) {
        hide(true, anim);
    }

    /**
     * Toggle the badge visibility in the UI.
     */
    public void toggle() {
        toggle(false, null, null);
    }

    /**
     * Toggle the badge visibility in the UI.
     *
     * @param animate flag to apply the default fade-in/out animation.
     */
    public void toggle(boolean animate) {
        toggle(animate, fadeIn, fadeOut);
    }

    /**
     * Toggle the badge visibility in the UI.
     *
     * @param animIn  Animation to apply to the view when made visible.
     * @param animOut Animation to apply to the view when made non-visible.
     */
    public void toggle(Animation animIn, Animation animOut) {
        toggle(true, animIn, animOut);
    }

    private void show(boolean animate, Animation anim) {
        if (getBackground() == null) {
//          if (badgeBg == null) {
//              badgeBg = getDefaultBackground();
//          }
//          setBackgroundDrawable(badgeBg);
        }
        applyLayoutParams();

        if (animate) {
            this.startAnimation(anim);
        }
        this.setVisibility(View.VISIBLE);
        isShown = true;
    }

    private void hide(boolean animate, Animation anim) {
        this.setVisibility(View.GONE);
        if (animate) {
            this.startAnimation(anim);
        }
        isShown = false;
    }

    private void toggle(boolean animate, Animation animIn, Animation animOut) {
        if (isShown) {
            hide(animate && (animOut != null), animOut);
        } else {
            show(animate && (animIn != null), animIn);
        }
    }

    /**
     * Increment the numeric badge label. If the current badge label cannot be converted to
     * an integer value, its label will be set to "0".
     *
     * @param offset the increment offset.
     */
    public int increment(int offset) {
        CharSequence txt = getText();
        int i;
        if (txt != null) {
            try {
                i = Integer.parseInt(txt.toString());
            } catch (NumberFormatException e) {
                i = 0;
            }
        } else {
            i = 0;
        }
        i = i + offset;
        setText(String.valueOf(i));
        return i;
    }

    /**
     * Decrement the numeric badge label. If the current badge label cannot be converted to
     * an integer value, its label will be set to "0".
     *
     * @param offset the decrement offset.
     */
    public int decrement(int offset) {
        return increment(-offset);
    }

    private ShapeDrawable getDefaultBackground() {

        int r = dipToPixels(DEFAULT_CORNER_RADIUS_DIP);
        float[] outerR = new float[]{r, r, r, r, r, r, r, r};

        RoundRectShape rr = new RoundRectShape(outerR, null, null);
        OvalShape os = new OvalShape();
        ShapeDrawable drawable = new ShapeDrawable(rr);
        drawable.getPaint().setColor(badgeColor);

        return drawable;

    }

    private void applyLayoutParams() {
        int min = Math.min(width, height);
        FrameLayout.LayoutParams lp;
        if (min <= 0) {
            lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            width = dipToPixels(width);
            height = dipToPixels(height);
            lp = new FrameLayout.LayoutParams(width, height);
        }

        switch (badgePosition) {
            case POSITION_TOP_LEFT:
                lp.gravity = Gravity.LEFT | Gravity.TOP;
                lp.setMargins(badgeMargin, badgeMargin, 0, 0);
                break;
            case POSITION_TOP_RIGHT:
                lp.gravity = Gravity.RIGHT | Gravity.TOP;
                lp.setMargins(0, badgeMargin, badgeMargin, 0);
                break;
            case POSITION_BOTTOM_LEFT:
                lp.gravity = Gravity.LEFT | Gravity.BOTTOM;
                lp.setMargins(badgeMargin, 0, 0, badgeMargin);
                break;
            case POSITION_BOTTOM_RIGHT:
                lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
                lp.setMargins(0, 0, badgeMargin, badgeMargin);
                break;
            case POSITION_CENTER:
                lp.gravity = Gravity.CENTER;
                lp.setMargins(0, 0, 0, 0);
                break;
            case POSITION_RIGHT_CENTER_VERTICAL:
                lp.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
                lp.setMargins(0, badgeMargin, 0, badgeMargin);
                break;
            case POSITION_LEFT_CENTER_VERTICAL:
                lp.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
                lp.setMargins(0, badgeMargin, 0, badgeMargin);
                break;
            case POSITION_TOP_CENTER_HORIZONTAL:
                lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
                lp.setMargins(0, badgeMargin, badgeMargin, 0);
                break;
            case POSITION_BOTTOM_CENTER_HORIZONTAL:
                lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
                lp.setMargins(0, badgeMargin, badgeMargin, 0);
                break;
            default:
                break;
        }
        setGravity(Gravity.CENTER);
        setLayoutParams(lp);

    }

    /**
     * Returns the target View this badge has been attached to.
     */
    public View getTarget() {
        return target;
    }

    /**
     * Is this badge currently visible in the UI?
     */
    @Override
    public boolean isShown() {
        return isShown;
    }

    /**
     * Returns the positioning of this badge.
     * <p>
     * one of POSITION_TOP_LEFT, POSITION_TOP_RIGHT, POSITION_BOTTOM_LEFT, POSITION_BOTTOM_RIGHT.
     */
    public int getBadgePosition() {
        return badgePosition;
    }

    /**
     * Set the positioning of this badge.
     *
     * @param layoutPosition one of POSITION_TOP_LEFT, POSITION_TOP_RIGHT, POSITION_BOTTOM_LEFT, POSITION_BOTTOM_RIGHT.
     */
    public void setBadgePosition(int layoutPosition) {
        this.badgePosition = layoutPosition;
    }

    /**
     * Returns the horizontal/vertical margin from the target View that is applied to this badge.
     */
    public int getBadgeMargin() {
        return badgeMargin;
    }

    /**
     * Set the horizontal/vertical margin from the target View that is applied to this badge.
     *
     * @param badgeMargin the margin in pixels.
     */
    public void setBadgeMargin(int badgeMargin) {
        this.badgeMargin = badgeMargin;
    }

    /**
     * Returns the color value of the badge background.
     */
    public int getBadgeBackgroundColor() {
        return badgeColor;
    }

    /**
     * Set the color value of the badge background.
     *
     * @param badgeColor the badge background color.
     */
    public void setBadgeBackgroundColor(int badgeColor) {
        this.badgeColor = badgeColor;
        //badgeBg = getDefaultBackground();
    }

    private int dipToPixels(int dip) {
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
        return (int) px;
    }
}