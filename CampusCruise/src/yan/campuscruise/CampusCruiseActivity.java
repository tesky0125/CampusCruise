package yan.campuscruise;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKLocationManager;
import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;
import com.baidu.mapapi.Overlay;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.RouteOverlay;
import com.baidu.mapapi.TransitOverlay;

public class CampusCruiseActivity extends MapActivity {
	
	public static MapView mapView = null;
	private MapController mapCtrl = null;
	private MyLocationOverlay locOverlay = null;//用户自己的位置层
	private MKLocationManager locMgr = null;
	private Bitmap locmarker = null;
	private Drawable marker = null;
	public static View popView;
	public static TextView describeView;
	private ImageView imgRdCtrler;
	private View layoutToolbar;
	private RadioButton rbLoc;
	private RadioButton rbSearch;
	private RadioButton rbMore;
	private View layoutSearch;
	private AutoCompleteTextView autoCompleteSearch;
	private ImageButton btnDropdown;
	private ImageButton btnsSearch;	
	private ImageButton btnsNavi;	
	private ImageView imgZoomOut;
	private ImageView imgZoomIn;
	private MKSearch mSearch = null;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        new LoadingAsyncTask(this).execute();
        
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //将上面的代码实现更改为xml实现
        setContentView(R.layout.campus_mapview);
        
        //获取应用程序mGlobalApp,以调用mBMapMgr对百度地图进行控制
        CampusCruiseApp app = (CampusCruiseApp) this.getApplication();//获取应用程序application,即mGlobalApp
        if(app.mBMapMgr == null){
        	app.mBMapMgr = new BMapManager(this.getApplication());
        	app.mBMapMgr.init(app.mStrKey, new CampusCruiseApp.MyGeneralListener());
        }
        app.mBMapMgr.start();
        super.initMapActivity(app.mBMapMgr);
        
        locMgr =  app.mBMapMgr.getLocationManager();
        
        //初始化控件并为各自设定响应的监听器
        initViews();
        
        setListeners();
        
		initMapSettings();
		
		
		locOverlay = new MyLocationOverlay(this, mapView);//定位图层，可以显示罗盘和定位标志
		mapView.getOverlays().add(locOverlay);

		//用于LocationOverlay定位后在位置上打上圆点标记
		locmarker = BitmapFactory.decodeResource(getResources(), R.drawable.icon_loc_normal);
		
		//用于SearchLocation找到目标后在位置上打上气泡标记
		marker = this.getResources().getDrawable(R.drawable.icon_marka);
		marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());	

		
		popView=super.getLayoutInflater().inflate(R.layout.popview, null);
		describeView = (TextView) popView.findViewById(R.id.describe);
		mapView.addView( popView,new MapView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                		null, MapView.LayoutParams.TOP_LEFT));
		popView.setVisibility(View.GONE);
		
		
	    initSearchModel();  
		
		scheduleHideToolbar();
    }

	private void initMapSettings() {
		mapView.setTraffic(true);
//		mapView.setBuiltInZoomControls(true);
		//设置在缩放动画过程中也显示overlay,默认为不绘制
		mapView.setDrawOverlayWhenZooming(true);
		mapCtrl = mapView.getController();
		mapCtrl.setZoom(15);       

		processLocation();

	}
	
	private void initSearchModel() {
		
		CampusCruiseApp app = (CampusCruiseApp) this.getApplication();
		// 初始化搜索模块，注册事件监听
	    mSearch = new MKSearch();
	    mSearch.init(app.mBMapMgr, new MKSearchListener(){

			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
				// 错误号可参考MKEvent中的定义
				if (error != 0 || res == null) {
					Toast.makeText(CampusCruiseActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
					return;
				}
				RouteOverlay routeOverlay = new RouteOverlay(CampusCruiseActivity.this, mapView);
			    // 此处仅展示一个方案作为示例
			    routeOverlay.setData(res.getPlan(0).getRoute(0));
			    mapView.getOverlays().clear();
			    mapView.getOverlays().add(routeOverlay);
			    mapView.invalidate();
			    
			    mapCtrl.animateTo(res.getStart().pt);	
			    mapCtrl.setZoom(16);
			}

			public void onGetTransitRouteResult(MKTransitRouteResult res,int error) {
				System.out.println("onGetResult");
				if (error != 0 || res == null) {
					Toast.makeText(CampusCruiseActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
					return;
				}
				TransitOverlay  routeOverlay = new TransitOverlay(CampusCruiseActivity.this, mapView);
			    // 此处仅展示一个方案作为示例
			    routeOverlay.setData(res.getPlan(0));
			    mapView.getOverlays().clear();
			    mapView.getOverlays().add(routeOverlay);
			    mapView.invalidate();
			    
			    mapCtrl.animateTo(res.getStart().pt);
			    mapCtrl.setZoom(16);
			}

			public void onGetWalkingRouteResult(MKWalkingRouteResult res,int error) {
				System.out.println("onGetResult");
				if (error != 0 || res == null) {
					Toast.makeText(CampusCruiseActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
					return;
				}
				RouteOverlay routeOverlay = new RouteOverlay(CampusCruiseActivity.this, mapView);
			    // 此处仅展示一个方案作为示例
			    routeOverlay.setData(res.getPlan(0).getRoute(0));
			    mapView.getOverlays().clear();
			    mapView.getOverlays().add(routeOverlay);
			    mapView.invalidate();
			    
			    mapCtrl.animateTo(res.getStart().pt);
			    mapCtrl.setZoom(16);
			}
			public void onGetAddrResult(MKAddrInfo res, int error) {
			}
			public void onGetPoiResult(MKPoiResult res, int arg1, int arg2) {
			}
	    });
	}

	
    private void initViews(){
    	mapView = (MapView) findViewById(R.id.mapView);
    	imgRdCtrler = (ImageView) this.findViewById(R.id.img_ctrler);
    	layoutToolbar =  this.findViewById(R.id.layout_toolbar);
    	rbLoc = (RadioButton) this.findViewById(R.id.radio_loc);
		rbSearch = (RadioButton) this.findViewById(R.id.radio_search);
		rbMore = (RadioButton) this.findViewById(R.id.radio_more);    
		layoutSearch = (View) this.findViewById(R.id.layout_search);
		layoutSearch.setVisibility(View.INVISIBLE);
		btnsSearch = (ImageButton) this.findViewById(R.id.btn_search);
		btnsNavi = (ImageButton) this.findViewById(R.id.btn_navi);
		autoCompleteSearch = (AutoCompleteTextView) this.findViewById(R.id.edit_search);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.arr_geopoints, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		autoCompleteSearch.setAdapter(adapter);
		btnDropdown = (ImageButton) this.findViewById(R.id.btn_dropdown);
		imgZoomOut  = (ImageView) this.findViewById(R.id.img_zoomout);
		imgZoomIn = (ImageView) this.findViewById(R.id.img_zoomin);
		
    }
    
    
    private void setListeners(){
    	
    	imgRdCtrler.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isToolbarHide){
					if(hideTimer!=null)
						hideTimer.cancel();
				}
				int anim = isToolbarHide?R.anim.push_right_in:R.anim.push_right_out;
				AnimationSet animSet = (AnimationSet) AnimationUtils.loadAnimation(CampusCruiseActivity.this, anim); 
				animSet.setFillAfter(true);

				if(isToolbarHide){
					isToolbarHide = false;
					scheduleHideToolbar();
				}else{
					isToolbarHide = true;
				}

				layoutToolbar.startAnimation(animSet);
				
			}
		});
    	
    	rbLoc.setOnClickListener(radioListener);
		rbSearch.setOnClickListener(radioListener);
		rbMore.setOnClickListener(radioListener);
		
		btnDropdown.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(R.id.btn_dropdown);
			}
		});
		
		btnsSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				processSearch();
			}

		});
		
		btnsNavi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				processRouteSearch();
			}

		});
		
		imgZoomOut.setOnClickListener(zoomListener);
		imgZoomIn.setOnClickListener(zoomListener);
    }
    
    View.OnClickListener zoomListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int zoom = mapView.getZoomLevel();
			switch(v.getId()){
			case R.id.img_zoomout:
				if(zoom == 18){
					Toast.makeText(CampusCruiseActivity.this, "已经达到地图最大比例", Toast.LENGTH_SHORT).show();
					return ;
				}
				mapCtrl.setZoom(++zoom);
				break;
			case R.id.img_zoomin:
				if(zoom == 15){
					Toast.makeText(CampusCruiseActivity.this, "地图比例已超出学校范围", Toast.LENGTH_SHORT).show();
					return ;
				}
				mapCtrl.setZoom(--zoom);
				break;
			}
		}
	};

	private void processRouteSearch() {
		// TODO Auto-generated method stub
        Intent it = new Intent(this, RouteSearchActivity.class);

        this.startActivityForResult(it, 0);
	}
	
	
	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		switch(resultCode){
		case RESULT_OK:
			if(intent != null){
				String start = intent.getStringExtra("start");
				String goal = intent.getStringExtra("goal");
				boolean isTransit = intent.getBooleanExtra("isTransit",false);
				
				GeoPoint ptStart = addr2Point(start);
				GeoPoint ptGoal = addr2Point(goal);
				
				//如何使用经纬度构造MKPlanNode节点
				MKPlanNode startNode = new MKPlanNode();
				startNode.pt = ptStart;
				MKPlanNode goalNode = new MKPlanNode();
				goalNode.pt = ptGoal;
				
//				System.out.println(startNode.pt.getLatitudeE6()+","+goalNode.pt.getLatitudeE6());
				if (isTransit) {
					mSearch.transitSearch("武汉", startNode, goalNode);//公交
				} else {
					mSearch.walkingSearch(null, startNode, null, goalNode);
				}
			}
		}
	}

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(!isToolbarHide){
				AnimationSet animSet = (AnimationSet) AnimationUtils.loadAnimation(CampusCruiseActivity.this, R.anim.push_right_out); 
				animSet.setFillAfter(true);
        		layoutToolbar.startAnimation(animSet);
        		isToolbarHide = true;
			}
		}
    	
    };
    private boolean isToolbarHide = false;
    private Timer hideTimer = null;
    
	private void scheduleHideToolbar()
	{		
	    TimerTask task = new TimerTask() {            
	        @Override  
	        public void run() {  
	        	handler.sendEmptyMessage(0);       
	        }  
	    };
	    hideTimer = new Timer(); 
	    hideTimer.schedule(task, 5000); 
	}
	
	private void processSearch() {
		// TODO Auto-generated method stub
		String keyword =  autoCompleteSearch.getText().toString();
		if(keyword.equals(""))
		{
			Toast.makeText(this, "请输入/选择地点", Toast.LENGTH_SHORT).show();
			return;
		}
		
		List<OverlayItem> geoItems = new ArrayList<OverlayItem>();	
		GeoPoint pt = this.addr2Point(keyword);
		geoItems.add(new OverlayItem(pt, "", keyword));
		
		mapView.getOverlays().clear();
		mapView.getOverlays().add(new PointSearchOverlay(this, marker ,geoItems));
		mapCtrl.animateTo(geoItems.get(0).getPoint());
		mapCtrl.setZoom(17); 
	}   
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch(id){
		case R.id.btn_dropdown:
			return getPopupDialog("请选择地点：");
		case KeyEvent.KEYCODE_BACK:
			return exitDialog();
		default:
			break;
		}
		return super.onCreateDialog(id);
	}



	private Dialog getPopupDialog(String title){
		
		ListView lv = new ListView(this);
		lv.setCacheColorHint(Color.argb(0, 0, 0, 0));//阻止ListView鼠标点击滑动变黑
		lv.setSelector(this.getResources().getDrawable(R.drawable.list_item_bg));//去掉系统默认设置
		//ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.arr_geopoints, android.R.layout.simple_list_item_1);		
		final CharSequence [] items = this.getResources().getStringArray(R.array.arr_geopoints);
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1, items);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> lv, View v, int position,long id) {
				autoCompleteSearch.setText(items[position]);
				dismissDialog(R.id.btn_dropdown);
			}
		});
		
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(title);
		builder.setView(lv);
		return  builder.create();
	}
	
	
	private OnClickListener radioListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.radio_loc:
				processLocation();
				break;
			case R.id.radio_search:
				preProcessSearch();
				break;
			case R.id.radio_more:
//				processMore();
				break;
			default:
				break;
			}
		}

	};	 
	
	private void preProcessSearch() {
		// TODO Auto-generated method stub
		int result = layoutSearch.getVisibility();
		if(result == View.INVISIBLE){
			layoutSearch.setVisibility(View.VISIBLE);
			btnsSearch.requestFocus();
		}else if(result == View.VISIBLE){
			layoutSearch.setVisibility(View.INVISIBLE);
		}
	}
	
	private void processLocation(){
		CampusCruiseApp app = (CampusCruiseApp) CampusCruiseActivity.this.getApplication();
//		MKLocationManager locMgr = app.mBMapMgr.getLocationManager();	
		try{
			Location location = locMgr.getLocationInfo();
			updateMapView(location.getLatitude(),location.getLongitude());
		}catch(Exception e)
		{
			Toast.makeText(this, "暂时无法获取您的位置", Toast.LENGTH_SHORT).show();
			mapCtrl.animateTo(CampusMapGeoPoints.POS_CAMPUS);
		}
	}
	
	
	//定义定位监听器,onResume时注册此listener，onPause时需要Remove
	private LocationListener locListener = new LocationListener(){
		@Override
		public void onLocationChanged(Location location) {
			if(location != null){
//				updateMapView(location.getLatitude(),location.getLongitude());
			}
		}
    };
    
  
	private void updateMapView(double latitude, double longitude) {
		// TODO Auto-generated method stub
		GeoPoint geoPoint = new GeoPoint((int)(latitude*1E6), (int)(longitude*1E6));
		mapCtrl.animateTo(geoPoint);
		List<Overlay> listOverlay = mapView.getOverlays();
//		listOverlay.clear();
		listOverlay.add(new LocationOverlay(this,geoPoint,locmarker));		 
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		CampusCruiseApp app = (CampusCruiseApp) this.getApplication();
		//加载定位监听器
		locMgr.requestLocationUpdates(locListener);
		locOverlay.enableMyLocation();
		locOverlay.enableCompass();
		app.mBMapMgr.start();		
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		CampusCruiseApp app = (CampusCruiseApp) this.getApplication();
		//取消定位监听器
		locMgr.removeUpdates(locListener);
		locOverlay.disableMyLocation();
		locOverlay.disableCompass();
		app.mBMapMgr.stop();
		super.onPause();
	}


	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			System.out.println("ListView--->KEYCODE_BACK ："+keyCode);
			showDialog(KeyEvent.KEYCODE_BACK);
			return false;			
		}

		return super.onKeyDown(keyCode, event);
	}

	private Dialog exitDialog() {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("退出");
		builder.setMessage("确认要退出地图？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				CampusCruiseActivity.this.finish();
				System.exit(0);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dismissDialog(KeyEvent.KEYCODE_BACK);
			}
		});
		return builder.create();
	}
	
	/*
	 * 将String地点转化为相应的经纬度
	 */
	public GeoPoint addr2Point(String strAddr){
		GeoPoint pt = null;
		if(this.getResources().getString(R.string.POS_CAMPUS).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_CAMPUS;
		}else if(this.getResources().getString(R.string.POS_LAB).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_LAB;
		}else if(this.getResources().getString(R.string.POS_APARTMENT_MASTER).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_APARTMENT_MASTER;
		}else if(this.getResources().getString(R.string.POS_CCB).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_CCB;
		}else if(this.getResources().getString(R.string.POS_MESS_HALL1).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_MESS_HALL1;
		}else if(this.getResources().getString(R.string.POS_PLAYGROUND).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_PLAYGROUND;
		}else if(this.getResources().getString(R.string.POS_BOC).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_BOC;
		}else if(this.getResources().getString(R.string.POS_APARTMENT_AREA1).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_APARTMENT_AREA1;
		}else if(this.getResources().getString(R.string.POS_APARTMENT_AREA2).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_APARTMENT_AREA2;
		}else if(this.getResources().getString(R.string.POS_APARTMENT_AREA3).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_APARTMENT_AREA3;
		}else if(this.getResources().getString(R.string.POS_APARTMENT_AREA5).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_APARTMENT_AREA5;
		}else if(this.getResources().getString(R.string.POS_MESS_HALL3).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_MESS_HALL3;
		}else if(this.getResources().getString(R.string.POS_MESS_HALL6).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_MESS_HALL6;
		}else if(this.getResources().getString(R.string.POS_RECEIPT).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_RECEIPT;
		}else if(this.getResources().getString(R.string.POS_MARKET3).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_MARKET3;
		}else if(this.getResources().getString(R.string.POS_YOUTH_GROUND).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_YOUTH_GROUND;
		}else if(this.getResources().getString(R.string.POS_MAIN_BUILDING).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_MAIN_BUILDING;
		}else if(this.getResources().getString(R.string.POS_EAST_BUILDING).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_EAST_BUILDING;
		}else if(this.getResources().getString(R.string.POS_DISTRIBUTE_BUILDING).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_DISTRIBUTE_BUILDING;
		}else if(this.getResources().getString(R.string.POS_ALLIENCE).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_ALLIENCE;
		}else if(this.getResources().getString(R.string.POS_SHIP_ENGINE_BUILDING).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_SHIP_ENGINE_BUILDING;
		}else if(this.getResources().getString(R.string.POS_VOYAGE_BUILDING).equals(strAddr)){
			 pt = CampusMapGeoPoints.POS_VOYAGE_BUILDING;
		}		
		return pt;
	}
	
	
	private class LoadingAsyncTask extends AsyncTask<Void,Void,Void>{
		private Context context;
		private ProgressDialog pdialog;
		
		public LoadingAsyncTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
            pdialog = new ProgressDialog(context);   
            pdialog.setIcon(null);
            pdialog.setMessage("正在加载地图资源...");
            pdialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			//可以在此处加入数据加载
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null ;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pdialog.dismiss();

		}
		
	}
}