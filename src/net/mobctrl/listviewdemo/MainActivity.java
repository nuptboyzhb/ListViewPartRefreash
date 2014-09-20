package net.mobctrl.listviewdemo;

import net.mobctrl.listviewdemo.ListAdapter.ViewHolder;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;
/**
 * @author 郑海波
 * @webset http://www.mobctrl.net
 * ListView的局部刷新
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends Activity implements UpdateCallback{

	@ViewById
	ListView listview;
	
	private ListAdapter adapter;
	
	@AfterViews
	void afterViews() {
		adapter = new ListAdapter(this);
		adapter.setUpdateCallback(this);
		listview.setAdapter(adapter);
		initDatas();
	}

	private void initDatas() {
		for(int i = 0;i<100;i++){
			Model model = new Model(i, "<Click> --> ");
			adapter.addData(model);
		}
	}

	@Override
	public void startProgress(final Model model,final int position) {
		/** start the Thread to update the Progress*/
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int i = 0;i<=100;i++){
					updateProgressInUiThread(model, i,position);
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	@UiThread
	void updateProgressInUiThread(Model model,int progress,int position){
		updateProgressPartly(progress,position);
	}
	
	
	private void updateProgressPartly(int progress,int position){
		int firstVisiblePosition = listview.getFirstVisiblePosition();
		int lastVisiblePosition = listview.getLastVisiblePosition();
		if(position>=firstVisiblePosition && position<=lastVisiblePosition){
			View view = listview.getChildAt(position - firstVisiblePosition);
			if(view.getTag() instanceof ViewHolder){
				ViewHolder vh = (ViewHolder)view.getTag();
				vh.pb.setProgress(progress);
			}
		}
	}
	
}
