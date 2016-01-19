package com.ag.controls.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ag.controls.R;
import com.ag.controls.common.util.ResourceUtil;

import java.util.Locale;

public class PagerSlidingTabStrip extends HorizontalScrollView {

    public interface IconTabProvider {
        int getPageIconResId(int position);
    }

    // @formatter:off
    private static final int[] ATTRS = new int[]{android.R.attr.textSize,
            android.R.attr.textColor};
    // @formatter:on

    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;

    private final PageListener pageListener = new PageListener();
    public OnPageChangeListener delegatePageListener;

    private LinearLayout tabsContainer;
    private ViewPager pager;

    private int tabCount;

    private int selectedPosition = 0;
    private int currentPosition = 0;
    private float currentPositionOffset = 0f;

    private Paint rectPaint;
    private Paint dividerPaint;

    private boolean checkedTabWidths = false;

    private int indicatorColor = 0xFF666666;
    private int underlineColor = 0x1A000000;
    private int dividerColor = 0x1A000000;

    private boolean shouldExpand = false;
    private boolean textAllCaps = true;

    private int scrollOffset = 52;
    private int indicatorHeight = 3;
    private int underlineHeight = 1;
    private int dividerPadding = 12;
    private int tabPadding = 24;
    private int dividerWidth = 1;

    private int tabTextSize = 12;
    private int tabTextColor = 0xFF666666;
    private int selectedTabTextColor = 0xFF666666;
    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.BOLD;

    private int lastScrollX = 0;

    private int tabBackgroundResId = 0;// R.drawable.background_tab;

    private Locale locale;
    private boolean firstScroll = true;

    //是否需要显示红点提示
    private boolean showNewsTip = false;

    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
        tabBackgroundResId = ResourceUtil.getDrawbleByName(context,
                "background_tab");
        setFillViewport(true);
        setWillNotDraw(false);

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();

        scrollOffset = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
        tabTextSize = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

        // get system attrs (android:textSize and android:textColor)

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
        tabTextColor = a.getColor(1, tabTextColor);

        a.recycle();

        // get custom attrs
        int[] attrsArray = ResourceUtil.getStyleableArray(context,
                "PagerSlidingTabStrip");
        a = context.obtainStyledAttributes(attrs, attrsArray);

        int index = ResourceUtil.getStyleable(context,
                "PagerSlidingTabStrip_indicatorColor");
        indicatorColor = a.getColor(index, indicatorColor);

        index = ResourceUtil.getStyleable(context,
                "PagerSlidingTabStrip_underlineColor");
        underlineColor = a.getColor(index, underlineColor);

        index = ResourceUtil.getStyleable(context,
                "PagerSlidingTabStrip_dividerColor");
        dividerColor = a.getColor(index, dividerColor);

        index = ResourceUtil.getStyleable(context,
                "PagerSlidingTabStrip_indicatorHeight");
        indicatorHeight = a.getDimensionPixelSize(index, indicatorHeight);

        index = ResourceUtil.getStyleable(context,
                "PagerSlidingTabStrip_underlineHeight");
        underlineHeight = a.getDimensionPixelSize(index, underlineHeight);

        index = ResourceUtil.getStyleable(context,
                "PagerSlidingTabStrip_dividerPadding_");
        dividerPadding = a.getDimensionPixelSize(index, dividerPadding);

        index = ResourceUtil.getStyleable(context,
                "PagerSlidingTabStrip_tabPaddingLeftRight");
        tabPadding = a.getDimensionPixelSize(index, tabPadding);

        index = ResourceUtil.getStyleable(context,
                "PagerSlidingTabStrip_tabBackground2");
        tabBackgroundResId = a.getResourceId(index, tabBackgroundResId);

        index = ResourceUtil.getStyleable(context,
                "PagerSlidingTabStrip_shouldExpand");
        shouldExpand = a.getBoolean(index, shouldExpand);

        index = ResourceUtil.getStyleable(context,
                "PagerSlidingTabStrip_scrollOffset");
        scrollOffset = a.getDimensionPixelSize(index, scrollOffset);

        index = ResourceUtil.getStyleable(context,
                "PagerSlidingTabStrip_textAllCaps_");
        textAllCaps = a.getBoolean(index, textAllCaps);

        //获取红点提示的属性
        showNewsTip = a.getBoolean(R.styleable.PagerSlidingTabStrip_showNewsTip, false);
        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0,
                LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;

        if (pager.getAdapter() == null) {
            throw new IllegalStateException(
                    "ViewPager does not have adapter instance.");
        }

        pager.setOnPageChangeListener(pageListener);

        notifyDataSetChanged();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void notifyDataSetChanged() {

        tabsContainer.removeAllViews();

        tabCount = pager.getAdapter().getCount();

        for (int i = 0; i < tabCount; i++) {

            if (pager.getAdapter() instanceof IconTabProvider) {
                addIconTab(i,
                        ((IconTabProvider) pager.getAdapter())
                                .getPageIconResId(i));
            } else {
                addTextTab(i, pager.getAdapter().getPageTitle(i).toString(), showNewsTip);
            }

        }

        updateTabStyles();

        checkedTabWidths = false;

        getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {

                    @SuppressWarnings("deprecation")
                    @SuppressLint("NewApi")
                    @Override
                    public void onGlobalLayout() {

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            getViewTreeObserver().removeGlobalOnLayoutListener(
                                    this);
                        } else {
                            getViewTreeObserver().removeOnGlobalLayoutListener(
                                    this);
                        }

                        currentPosition = pager.getCurrentItem();
                        scrollToChild(currentPosition, 0);
                    }
                });

    }

    private void addTextTab(final int position, String title) {

        DisplayMetrics dm = getResources().getDisplayMetrics();

        TextView tab = new TextView(getContext());
        tab.setText(title);
        // tab.setFocusable(true);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();
        tab.setWidth(dm.widthPixels / tabCount);

        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });

        tabsContainer.addView(tab);

    }

    /**
     * 添加可以带红点提示的Tab
     *
     * @param position
     * @param title
     * @param isNeedBadgeView 是否需要显示红点提示
     */
    private void addTextTab(final int position, String title, boolean isNeedBadgeView) {
        if (!isNeedBadgeView) {
            addTextTab(position, title);
            return;
        }

        DisplayMetrics dm = getResources().getDisplayMetrics();
        
        TextView textView = new TextView(getContext());
        textView.setText(title);
        textView.setGravity(Gravity.CENTER);
        textView.setSingleLine();
        textView.setWidth(dm.widthPixels / tabCount);
        tabsContainer.addView(textView);

        if (showNewsTipIndexs != null && showNewsTipIndexs.length > 0) {
            for (int i = 0; i < showNewsTipIndexs.length; i++) {
                if (position == showNewsTipIndexs[i]) {
                    CircleBadgeView redDot = new CircleBadgeView(getContext(), textView, 6, 6);
                    redDot.setBadgePosition(CircleBadgeView.POSITION_TOP_RIGHT);
                    redDot.setBadgeBackgroundColor(Color.RED);
                    redDot.setBackgroundColor(Color.RED);
                    redDot.show();
                    break;
                }
            }
        }

        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });
    }

    private void addIconTab(final int position, int resId) {

        ImageButton tab = new ImageButton(getContext());
        // tab.setFocusable(true);
        tab.setImageResource(resId);
        // tab.getLayoutParams().width = 500;

        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });

        tabsContainer.addView(tab);

    }

    private void updateTabStyles() {

        for (int i = 0; i < tabCount; i++) {

            View v = tabsContainer.getChildAt(i);

            v.setLayoutParams(defaultTabLayoutParams);
            v.setBackgroundResource(tabBackgroundResId);
            // if (shouldExpand) {
            // v.setPadding(0, 0, 0, 0);
            // } else {
            // v.setPadding(tabPadding, 0, tabPadding, 0);
            // }

            if (v instanceof TextView) {

                TextView tab = (TextView) v;
                tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                tab.setTypeface(tabTypeface, tabTypefaceStyle);
                tab.setTextColor(tabTextColor);

                // setAllCaps() is only available from API 14, so the upper case
                // is made manually if we are on a
                // pre-ICS-build
                if (textAllCaps) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        tab.setAllCaps(true);
                    } else {
                        tab.setText(tab.getText().toString()
                                .toUpperCase(locale));
                    }
                }
                if (i == selectedPosition) {
                    tab.setTextColor(selectedTabTextColor);
                }
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (!shouldExpand
                || MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            return;
        }

        int myWidth = getMeasuredWidth();
        int childWidth = 0;
        for (int i = 0; i < tabCount; i++) {
            childWidth += tabsContainer.getChildAt(i).getMeasuredWidth();
        }

        if (!checkedTabWidths && childWidth > 0 && myWidth > 0) {

            if (childWidth <= myWidth) {
                for (int i = 0; i < tabCount; i++) {
                    tabsContainer.getChildAt(i).setLayoutParams(
                            expandedTabLayoutParams);
                }
            }

            checkedTabWidths = true;
        }
    }

    private void scrollToChild(int position, int offset) {

        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || tabCount == 0) {
            return;
        }

        final int height = getHeight();

        // draw indicator line

        rectPaint.setColor(indicatorColor);

        // default: line below current tab
        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        // if there is an offset, start interpolating left and right coordinates
        // between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset)
                    * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset)
                    * lineRight);
        }

        canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height,
                rectPaint);

        // draw underline

        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(),
                height, rectPaint);

        // draw divider

        dividerPaint.setColor(dividerColor);
        for (int i = 0; i < tabCount - 1; i++) {
            View tab = tabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(),
                    height - dividerPadding, dividerPaint);
        }
    }

    private class PageListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            System.out.println("PageListener-onPageScrolled==" + position);
            currentPosition = position;
//			currentPositionOffset = positionOffset;

            if (!firstScroll) {
                currentPositionOffset = positionOffset;
            }
            firstScroll = false;

            scrollToChild(position, (int) (positionOffset * tabsContainer
                    .getChildAt(position).getWidth()));

            invalidate();

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            System.out.println("PageListener-onPageScrollStateChanged=="
                    + state);
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(pager.getCurrentItem(), 0);
            }

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            selectedPosition = position;
            updateTabStyles();
            System.out.println("PageListener-onPageSelected==" + position);
            if (delegatePageListener != null) {
                System.out.println("PageListener-delegatePageListener != null");
                delegatePageListener.onPageSelected(position);
            }
        }

    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        this.dividerColor = getResources().getColor(resId);
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setDividerPadding(int dividerPaddingPx) {
        this.dividerPadding = dividerPaddingPx;
        invalidate();
    }

    public int getDividerPadding() {
        return dividerPadding;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        requestLayout();
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }

    public void setTextSize(int textSizePx) {
        this.tabTextSize = textSizePx;
        updateTabStyles();
    }

    public int getTextSize() {
        return tabTextSize;
    }

    public void setTextColor(int textColor) {
        this.tabTextColor = textColor;
        updateTabStyles();
    }

    public void setTextColorResource(int resId) {
        this.tabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public int getTextColor() {
        return tabTextColor;
    }

    public void setSelectTabTextColor(int selectTabTextColor) {
        this.selectedTabTextColor = selectTabTextColor;
        updateTabStyles();
    }

    public void setTypeface(Typeface typeface, int style) {
        this.tabTypeface = typeface;
        this.tabTypefaceStyle = style;
        updateTabStyles();
    }

    public void setTabBackground(int resId) {
        this.tabBackgroundResId = resId;
    }

    public int getTabBackground() {
        return tabBackgroundResId;
    }

    public void setTabPaddingLeftRight(int paddingPx) {
        this.tabPadding = paddingPx;
        updateTabStyles();
    }

    public int getTabPaddingLeftRight() {
        return tabPadding;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    //需要显示红点的索引，从0开始
    private int[] showNewsTipIndexs;

    /**
     * 设置哪些tab需要显示红点
     *
     * @param index
     */
    public void setShowNewsTipIndexes(int... index) {
        showNewsTipIndexs = index;
        Log.v("setShowNewsTipIndexes", "" + (showNewsTipIndexs == null ? "NULL" : showNewsTipIndexs.length));
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}