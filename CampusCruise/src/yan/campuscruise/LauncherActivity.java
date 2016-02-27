package yan.campuscruise;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;


/*
 * 应用程序启动画面，被废弃，调整到CampusCruiseActivity中
 */
public class LauncherActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.main);
		//异步方式一：AsyncTask
		new LoadingAsyncTask(this).execute();
		//异步方式二：Thread
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//处理数据加载
			}
		}, 5000);
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
				Thread.sleep(2000);
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
			Intent it = new Intent(context, CampusCruiseActivity.class);
			context.startActivity(it);
			((Activity)context).finish();
		}
		
	}
}
