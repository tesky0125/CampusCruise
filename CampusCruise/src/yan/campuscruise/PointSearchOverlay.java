package yan.campuscruise;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.Projection;

public class PointSearchOverlay extends ItemizedOverlay<OverlayItem>{
	
	private Context context; 
	private Drawable marker;
	private List<OverlayItem> geoList = new ArrayList<OverlayItem>();

 	
	public PointSearchOverlay(Context context, Drawable marker ,List<OverlayItem> geoItems) {
		super(boundCenterBottom(marker));
		// TODO Auto-generated constructor stub
		this.context = context;
		this.marker = marker;
		this.geoList = geoItems;

		populate(); //����createItem,��mGeoList�����ݰ󶨵�ItemizedOverlay
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		Projection projection = mapView.getProjection(); 
		for (int index = size() - 1; index >= 0; index--) 
		{
			OverlayItem overLayItem = getItem(index); //ItemizedOverlay

			String title = overLayItem.getTitle();
			Point point = projection.toPixels(overLayItem.getPoint(), null); 

			Paint paint = new Paint();
			paint.setColor(Color.DKGRAY);
			paint.setTypeface(Typeface.SERIF);
			paint.setTextSize(15);
			canvas.drawText(title, point.x-15, point.y+15, paint); // �����ı�
		}

		super.draw(canvas, mapView, shadow);
		//����һ��drawable�߽磬ʹ�ã�0,0�������drawable�ײ����һ�����ĵ�һ������
		boundCenterBottom(marker);
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return geoList.get(i);//��mGeoList������������OverlayItem,Ҳ����mGeoList��OverlayItem��ӦItemizedOverlay��OverlayItem
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return geoList.size();
	}

	@Override
	public boolean onTap(GeoPoint geoPoint, MapView mapView) {
		// TODO Auto-generated method stub
		CampusCruiseActivity.popView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				processAddrInfo(context);
			}

		});
		// ��ȥ����������
		CampusCruiseActivity.popView.setVisibility(View.GONE);
		return super.onTap(geoPoint, mapView);
	}

	@Override
	protected boolean onTap(int i) {
		// TODO Auto-generated method stub
		setFocus(geoList.get(i));
		// ��������λ��,��ʹ֮��ʾ
		GeoPoint pt = geoList.get(i).getPoint();
		CampusCruiseActivity.describeView.setText(geoList.get(i).getSnippet());
		CampusCruiseActivity.mapView.updateViewLayout( CampusCruiseActivity.popView,
                new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                		pt, MapView.LayoutParams.BOTTOM_CENTER));
		CampusCruiseActivity.popView.setVisibility(View.VISIBLE);
		return true;
	}

	
	//

	private void processAddrInfo(Context context) {
		// TODO Auto-generated method stub
		Intent it = new Intent(context, AddressInfoActivity.class);
		Bundle addrinfo = new Bundle();
		//
		it.putExtra("addrinfo", addrinfo);
		context.startActivity(it);
	}
}
