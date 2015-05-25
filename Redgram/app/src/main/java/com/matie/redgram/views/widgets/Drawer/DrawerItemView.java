package com.matie.redgram.views.widgets.Drawer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matie.redgram.R;
import com.matie.redgram.models.DrawerItem;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by matie on 18/01/15.
 */
public class DrawerItemView extends RelativeLayout {

    @InjectView(R.id.itemRR) RelativeLayout rr;

    @InjectView(R.id.navigationDrawerItemTitleTV)
    TextView itemTitleTV;

    @InjectView(R.id.navigationDrawerItemIconIV)
    ImageView itemIconIV;

    final Resources res;

    public DrawerItemView(Context context){
        super(context);
        res = context.getResources();
    }

    public DrawerItemView(Context context, AttributeSet attrs){
        super(context, attrs);
        res = context.getResources();
    }

    public DrawerItemView(Context context, AttributeSet attrs, int defStyle){
        super(context,attrs,defStyle);
        res = context.getResources();
    }

    @Override
    protected void onFinishInflate(){
        super.onFinishInflate();
        //inject item views
        ButterKnife.inject(this);
    }

    //binds item object to item view
    public void bindTo(DrawerItem item){
        requestLayout(); //request for changes in view

        if(item.isMainItem()){
            //style the menu item.
            itemTitleTV.setText(item.getItemName());
            itemTitleTV.setTextSize(18);
            if(item.getItemIcon() != 0){
                itemIconIV.setImageDrawable(getIcon(item.getItemIcon()));
                itemIconIV.setVisibility(View.VISIBLE);
            }
        }else{
            //different style for non-menu items.
            itemTitleTV.setText(item.getItemName());
            itemTitleTV.setTextSize(14);
            if(item.getItemIcon() != 0){
                itemIconIV.setImageDrawable(getIcon(item.getItemIcon()));
                itemIconIV.setVisibility(View.VISIBLE);
            }
            rr.setBackgroundColor(res.getColor(R.color.grey_background));
        }

        if(item.isSelected()){
            //make it bold
            itemTitleTV.setTypeface(null, Typeface.BOLD);

        }else{
            //make it normal
            itemTitleTV.setTypeface(null, Typeface.NORMAL);
        }

    }

    private Drawable getIcon(int res){
        //`res` refers to icon ID in drawable folder
        return  getContext().getResources().getDrawable(res);
    }
}
