package com.example.SerarchNearBy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PopupOverlay;
public class LocationMapView extends MapView {
    static PopupOverlay pop  = null;
    public LocationMapView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    public LocationMapView(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    public LocationMapView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (!super.onTouchEvent(event)){
            if (pop != null && event.getAction() == MotionEvent.ACTION_UP)
                pop.hidePop();
        }
        return true;
    }
}


