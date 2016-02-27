package yan.campuscruise;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;

import android.app.Application;
import android.widget.Toast;

/*
 * CampusCruiseApp:����Ӧ�ó����APP�����������ж԰ٶȵ�ͼBMapManager���г�ʼ��������Ӧ��������Ӧ�ó���ĸ���Activity��
 * ע�⣺��Ҫ��<application>��ǩ�м��϶�Ӧ��APP����
 */
public class CampusCruiseApp extends Application {
	
	private static CampusCruiseApp mGlobalApp;//���ڿ�������Ӧ�ó���
	
	public BMapManager mBMapMgr = null;//�ٶȵ�ͼ����Ҫ������
	
	public String mStrKey = "DFDAD4D595E95568733E7B44E2EEB99A82043F80";
	public boolean m_bKeyRight = true;

	
	public static class MyGeneralListener implements MKGeneralListener{

		@Override
		public void onGetNetworkState(int iError) {
			// TODO Auto-generated method stub
			Toast.makeText(CampusCruiseApp.mGlobalApp.getApplicationContext(), "���������������",Toast.LENGTH_LONG).show();			
		}

		@Override
		public void onGetPermissionState(int iError) {
			// TODO Auto-generated method stub
			Toast.makeText(CampusCruiseApp.mGlobalApp.getApplicationContext(), "��������ȷ����ȨKey��",Toast.LENGTH_LONG).show();
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
