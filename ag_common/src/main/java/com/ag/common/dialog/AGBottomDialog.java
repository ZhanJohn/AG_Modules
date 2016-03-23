package com.ag.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ag.common.res.AGResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Dialog底部弹出
 * 项目引用时需要自定义dialog_bottom_layout和dialog_bottom_item
 */
public class AGBottomDialog {
    private Context context;
    private Dialog dialog;
    private TextView txt_title;
    private TextView txt_cancel;
    private LinearLayout lLayout_content;
    private ScrollView sLayout_content;
    private boolean showTitle = false;
    private List<SheetItem> sheetItemList;
    private Display display;
    private OnSheetItemClickListener onSheetItemClickListener;

    public AGBottomDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public AGBottomDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                AGResource.getLayoutByName(context,"dialog_bottom_layout"), null);
        view.setMinimumWidth(display.getWidth());

        sLayout_content = (ScrollView) view.findViewById(AGResource.getIdByName(context,"sLayout_content"));
        lLayout_content = (LinearLayout) view
                .findViewById(AGResource.getIdByName(context,"lLayout_content"));
        txt_title = (TextView) view.findViewById(AGResource.getIdByName(context,"txt_title"));
        txt_cancel = (TextView) view.findViewById(AGResource.getIdByName(context,"txt_cancel"));
        txt_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog = new Dialog(context, AGResource.getStyleByName(context,"DialogBottomStyle"));
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

        return this;
    }

    public AGBottomDialog setTitle(String title) {
        showTitle = true;
        txt_title.setVisibility(View.VISIBLE);
        txt_title.setText(title);
        return this;
    }

    public AGBottomDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public AGBottomDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public AGBottomDialog addSheetItem(String strItem, SheetItemColor color) {
        if (sheetItemList == null) {
            sheetItemList = new ArrayList<SheetItem>();
        }
        sheetItemList.add(new SheetItem(strItem, color));
        return this;
    }

    private void setSheetItems() {
        if (sheetItemList == null || sheetItemList.size() <= 0) {
            return;
        }

        int size = sheetItemList.size();

        // 循环添加条目
        for (int i = 1; i <= size; i++) {
            final int index = i;
            SheetItem sheetItem = sheetItemList.get(i - 1);
            String strItem = sheetItem.name;
            SheetItemColor color = sheetItem.color;

            LinearLayout itemLayout=(LinearLayout) View.inflate(context,AGResource.getLayoutByName(context,"dialog_bottom_item"),null);
            TextView dialog_item_msg=(TextView)itemLayout.findViewById(AGResource.getIdByName(context,"dialog_item_msg"));
            View dialog_item_line=itemLayout.findViewById(AGResource.getIdByName(context,"dialog_item_line"));

            if(i==size){
                dialog_item_line.setVisibility(View.GONE);
            }

            // 字体颜色
            if (color == null) {
                dialog_item_msg.setTextColor(Color.parseColor(SheetItemColor.Blue
                        .getName()));
            } else {
                dialog_item_msg.setTextColor(Color.parseColor(color.getName()));
            }

            dialog_item_msg.setText(sheetItem.name);

            // 点击事件
            itemLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onSheetItemClickListener!=null)
                        onSheetItemClickListener.onClick(index);
                    dialog.dismiss();
                }
            });

            lLayout_content.addView(itemLayout);
        }
    }

    public void show() {
        setSheetItems();
        dialog.show();
    }

    public AGBottomDialog setOnSheetItemClickListener(OnSheetItemClickListener listener){
        this.onSheetItemClickListener=listener;
        return this;
    }

    public interface OnSheetItemClickListener {
        void onClick(int which);
    }

    public class SheetItem {
        String name;
        SheetItemColor color;

        public SheetItem(String name, SheetItemColor color) {
            this.name = name;
            this.color = color;
        }
    }

    public enum SheetItemColor {
        Blue("#5e9eee"), Red("#ff0000"),Black("#000000");

        private String name;

        private SheetItemColor(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
