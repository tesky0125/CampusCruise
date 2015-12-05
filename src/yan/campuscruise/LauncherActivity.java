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
 * Ӧ�ó����������棬��������������CampusCruiseActivity��
 */
public class LauncherActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.main);
		//�첽��ʽһ��AsyncTask
		new LoadingAsyncTask(this).execute();
		//�첽��ʽ����Thread
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//�������ݼ���
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
            pdialog.setMessage("���ڼ��ص�ͼ��Դ...");
            pdialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			//�����ڴ˴��������ݼ���
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
