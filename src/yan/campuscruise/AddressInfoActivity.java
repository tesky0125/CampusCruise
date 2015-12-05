package yan.campuscruise;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class AddressInfoActivity extends Activity {
	//页面
	private ViewPager viewPager; 
    private ArrayList<View> pageViews; 
    
    //小圆点
    private ImageView dotView; 
    private ImageView[] dotViews;
    
    // 包裹页面的LinearLayout
    private ViewGroup pageGroups;
    // 包裹小圆点的LinearLayout
    private ViewGroup dotGroups;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//生成页面解析器
        LayoutInflater inflater = getLayoutInflater(); 
        
        //对页面进行解析
        pageGroups = (ViewGroup)inflater.inflate(R.layout.page_container, null); 
        viewPager = (ViewPager)pageGroups.findViewById(R.id.guidePages);        
        dotGroups = (ViewGroup)pageGroups.findViewById(R.id.viewGroup);         
        
        //加载页面
        pageViews = new ArrayList<View>(); 
        pageViews.add(inflater.inflate(R.layout.info_address, null)); 
        pageViews.add(inflater.inflate(R.layout.info_address, null)); 
        pageViews.add(inflater.inflate(R.layout.info_address, null)); 
        
        //根据页面数为小圆点分配空间
        dotViews = new ImageView[pageViews.size()];                 
       
        //设置小圆点
        for (int i = 0; i < pageViews.size(); i++) { 
            dotView = new ImageView(this); 
            dotView.setLayoutParams(new LayoutParams(10,10)); 
            dotView.setPadding(20, 0, 20, 0); 
            dotViews[i] = dotView; 
           
            if (i == 0) { 
                //默认选中第一张图片
                dotViews[i].setBackgroundResource(R.drawable.page_indicator_focused); 
            } else { 
                dotViews[i].setBackgroundResource(R.drawable.page_indicator); 
            } 
           
            dotGroups.addView(dotViews[i]); 
        } 
       
        setContentView(pageGroups);
        
        viewPager.setAdapter(new GuidePageAdapter()); 
        viewPager.setOnPageChangeListener(new GuidePageChangeListener()); 
	}
	// 指引页面数据适配器
    class GuidePageAdapter extends PagerAdapter { 
       
        @Override 
        public int getCount() { 
            return pageViews.size(); 
        } 
 
        @Override 
        public boolean isViewFromObject(View v, Object obj) { 
            return v == obj; 
        } 
 
        @Override 
        public int getItemPosition(Object object) { 
            // TODO Auto-generated method stub 
            return super.getItemPosition(object); 
        } 
 
        @Override 
        public void destroyItem(View v, int position, Object obj) { 
            // TODO Auto-generated method stub 
            ((ViewPager) v).removeView(pageViews.get(position)); 
        } 
 
        @Override 
        public Object instantiateItem(View v, int position) { 
            // TODO Auto-generated method stub 
        	//这个必须实现
            ((ViewPager) v).addView(pageViews.get(position)); 
            return pageViews.get(position); 
        } 
 
        @Override 
        public void restoreState(Parcelable arg0, ClassLoader arg1) { 
            // TODO Auto-generated method stub 
 
        } 
 
        @Override 
        public Parcelable saveState() { 
            // TODO Auto-generated method stub 
            return null; 
        } 
 
        @Override 
        public void startUpdate(View arg0) { 
            // TODO Auto-generated method stub 
 
        } 
 
        @Override 
        public void finishUpdate(View arg0) { 
            // TODO Auto-generated method stub 
 
        } 
    }
   
    // 指引页面更改事件监听器
    class GuidePageChangeListener implements OnPageChangeListener { 
         
        @Override 
        public void onPageScrollStateChanged(int arg0) { 
            // TODO Auto-generated method stub 
 
        } 
 
        @Override 
        public void onPageScrolled(int arg0, float arg1, int arg2) { 
            // TODO Auto-generated method stub 
 
        } 
 
        @Override 
        public void onPageSelected(int position) { 
        	dotViews[position].setBackgroundResource(R.drawable.page_indicator_focused);
            for (int i = 0; i < dotViews.length; i++) {                                
                if (position != i) { 
                    dotViews[i].setBackgroundResource(R.drawable.page_indicator); 
                } 
            }
        } 
    } 

}
