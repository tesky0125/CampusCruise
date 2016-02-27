package yan.campuscruise;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.widget.Toast;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.Overlay;
import com.baidu.mapapi.Projection;

public class LocationOverlay extends Overlay{
	private Bitmap bmpBallon = null;
	private GeoPoint geoPt = null;
	private Context context;
	public LocationOverlay(Context context, GeoPoint geoPoint, Bitmap marker) {
		this.context = context;
		this.bmpBallon = marker;
		this.geoPt = geoPoint;
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// TODO Auto-generated method stub
		super.draw(canvas, mapView, shadow);
		if(!shadow){
			Projection proj = mapView.getProjection();
			Point pt = new Point();
			proj.toPixels(geoPt, pt);
			canvas.drawBitmap(bmpBallon, pt.x - bmpBallon.getWidth()/2, pt.y - bmpBallon.getHeight()/2, null);
		}
	}

	@Override
	public boolean onTap(GeoPoint geoPoint, MapView mapView) {
		// TODO Auto-generated method stub
		System.out.println(geoPoint.getLongitudeE6()+","+geoPoint.getLatitudeE6());
		return true;
	}


}
