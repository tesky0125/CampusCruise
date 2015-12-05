package yan.campuscruise;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RouteSearchActivity extends Activity {

	private AutoCompleteTextView autoTextStart;
	private AutoCompleteTextView autoTextGoal;
	private ImageButton imgBtnExchange;
	private ImageButton imgBtnStart;
	private ImageButton imgBtnGoal;
	private Button btnTabTransit;
	private Button btnTabWalk;
	private boolean isTransitSeclected = false;
	private ImageButton imgBtnSearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.route_search);
		initViews();
		initListeners();
	}

	private void initViews() {
		// TODO Auto-generated method stub
		autoTextStart = (AutoCompleteTextView) this.findViewById(R.id.autotextview_roadsearch_start);
		autoTextGoal = (AutoCompleteTextView) this.findViewById(R.id.autotextview_roadsearch_goals);
		imgBtnExchange = (ImageButton) this.findViewById(R.id.imagebtn_roadsearch_exchange);
		imgBtnStart = (ImageButton) this.findViewById(R.id.imagebtn_roadsearch_startoption);
		imgBtnGoal = (ImageButton) this.findViewById(R.id.imagebtn_roadsearch_goalsoption);
		btnTabTransit = (Button) this.findViewById(R.id.imagebtn_roadsearch_tab_transit);
		btnTabWalk = (Button) this.findViewById(R.id.imagebtn_roadsearch_tab_walk);
		btnTabWalk.setBackgroundResource(R.drawable.mode_walk_on);
		imgBtnSearch = (ImageButton) this.findViewById(R.id.imagebtn_roadsearch_search);
		imgBtnSearch.setEnabled(false);
	}

	
	private void initListeners() {
		// TODO Auto-generated method stub
		autoTextStart.addTextChangedListener(watcher);
		autoTextGoal.addTextChangedListener(watcher);
		
		imgBtnExchange.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String tmp = autoTextStart.getText().toString();
				autoTextStart.setText(autoTextGoal.getText().toString());
				autoTextGoal.setText(tmp);
			}
		});
		
		btnTabTransit.setOnClickListener(tabClickListener);
		btnTabWalk.setOnClickListener(tabClickListener);
		
		imgBtnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(R.id.imagebtn_roadsearch_startoption);
			}
		});
		imgBtnGoal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(R.id.imagebtn_roadsearch_goalsoption);
			}
		});
		
		imgBtnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				processResult();
			}
		});
		
	}
	
	TextWatcher watcher = new TextWatcher() {
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
//			System.out.println("text changed");
			if(!autoTextStart.getText().toString().equals("") && !autoTextGoal.getText().toString().equals("")){
				imgBtnSearch.setEnabled(true);
				imgBtnExchange.setEnabled(true);
			}else{
				imgBtnSearch.setEnabled(false);
				imgBtnExchange.setEnabled(false);
			}
				
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	};
	
	OnClickListener tabClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == btnTabTransit){
				isTransitSeclected = true;
				btnTabTransit.setBackgroundResource(R.drawable.mode_transit_on);
				btnTabWalk.setBackgroundResource(R.drawable.mode_walk_off);				
			}else if(v == btnTabWalk){
				isTransitSeclected = false;
				btnTabWalk.setBackgroundResource(R.drawable.mode_walk_on);
				btnTabTransit.setBackgroundResource(R.drawable.mode_transit_off);
			}
			
		}
	};
	
	
	private void processResult(){
		String start = autoTextStart.getText().toString();
		String goal = autoTextGoal.getText().toString();	
//		System.out.println("start:"+start+",goal:"+goal);
		if(start.equals("") || goal.equals("")){
			Toast.makeText(this, "请输入地点", Toast.LENGTH_SHORT).show();
			return ;
		}
		Intent it = new Intent();
		
		it.putExtra("start", start);
		it.putExtra("goal", goal);
		it.putExtra("isTransit", isTransitSeclected);
		
		setResult(RESULT_OK, it);
		this.finish();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch(id){
		case R.id.imagebtn_roadsearch_startoption:
			return getPopupDialog("请选择起点：");
		case R.id.imagebtn_roadsearch_goalsoption:
			return getPopupDialog("请选择终点：");
		default:
			break;
		}
		return super.onCreateDialog(id);
	}


	private Dialog getPopupDialog(final String title){
		
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
				if(title.equals("请选择起点：")){
					autoTextStart.setText(items[position]);
					dismissDialog(R.id.imagebtn_roadsearch_startoption);
				}else if(title.equals("请选择终点：")){
					autoTextGoal.setText(items[position]);
					dismissDialog(R.id.imagebtn_roadsearch_goalsoption);
				}
			}
		});
		
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(title);
		builder.setView(lv);
		return  builder.create();
	}	
}
