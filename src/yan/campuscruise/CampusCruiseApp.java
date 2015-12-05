package yan.campuscruise;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;

import android.app.Application;
import android.widget.Toast;

/*
 * CampusCruiseApp:整个应用程序的APP，可以在其中对百度地图BMapManager进行初始化，便于应用于整个应用程序的各个Activity中
 * 注意：需要在<application>标签中加上对应的APP名称
 */
public class CampusCruiseApp extends Application {
	
	private static CampusCruiseApp mGlobalApp;//用于控制整个应用程序
	
	public BMapManager mBMapMgr = null;//百度地图的主要管理类
	
	public String mStrKey = "DFDAD4D595E95568733E7B44E2EEB99A82043F80";
	public boolean m_bKeyRight = true;

	
	public static class MyGeneralListener implements MKGeneralListener{

		@Override
		public void onGetNetworkState(int iError) {
			// TODO Auto-generated method stub
			Toast.makeText(CampusCruiseApp.mGlobalApp.getApplicationContext(), "您的网络出错啦！",Toast.LENGTH_LONG).show();			
		}

		@Override
		public void onGetPermissionState(int iError) {
			// TODO Auto-generated method stub
			Toast.makeText(CampusCruiseApp.mGlobalApp.getApplicationContext(), "请输入正确的授权Key！",Toast.LENGTH_LONG).show();
			CampusCruiseApp.mGlobalApp.m_bKeyRight = false;			
		}
		
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		mGlobalApp = this;
		
		mBMapMgr = new BMapManager(this);
		mBMapMgr.init(mStrKey, new MyGeneralListener());
		super.onCreate();
	}	

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		if(mBMapMgr != null){
			mBMapMgr.destroy();
			mBMapMgr = null;
		}
		super.onTerminate();
	}
	
	
}
