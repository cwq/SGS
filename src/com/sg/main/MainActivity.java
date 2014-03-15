package com.sg.main;

import com.sg.controller.UnitController;
import com.sg.property.common.ThresholdProperty;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends Activity implements OnCheckedChangeListener{
    /** Called when the activity is first created. */
    
    // Intent request codes
    private static final int FILE_EXPLORER_REQUEST = 10;
	private static final int CONNECT_DEVICE_REQUEST = 11;
	private static final int OPEN_BLUETOOTH_REQUEST = 12;
	
	
	private MainView mainView;
	
	
	private RadioGroup mRadioGroup;
	private RadioButton undo;
	private RadioButton redo;
	private RadioButton clear;
	private RadioButton save;
	private RadioButton open;
	private RadioButton pen;
	private RadioButton bluetooth;
	
	private HorizontalScrollView mHorizontalScrollView;//上面的水平滚动控件
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  //设置全屏
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);  //设置背光灯长亮
		
		
		initConfig();

        setContentView(R.layout.main);
        
        initMenu();
        
        //mainView = new MainView(this, mSynchronousThread);
        mainView = (MainView) findViewById(R.id.myview);

        //读取关联文件
        Intent intent = getIntent(); 
        String action = intent.getAction(); 
        if(Intent.ACTION_VIEW.equals(action)){
        	Uri uri = (Uri) intent.getData();
        	String path = uri.getPath();
        	//mainView.open(path, false);
        } 
    }
    
    private void initMenu() {
    	mRadioGroup = (RadioGroup)findViewById(R.id.radioGroup);
    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ThresholdProperty.BUTTON_WIDTH, LayoutParams.WRAP_CONTENT);
    	undo = (RadioButton)findViewById(R.id.undo);
    	redo = (RadioButton)findViewById(R.id.redo);
    	clear = (RadioButton)findViewById(R.id.clear);
    	save = (RadioButton)findViewById(R.id.save);
    	open = (RadioButton)findViewById(R.id.open);
    	pen = (RadioButton)findViewById(R.id.pen);
    	bluetooth = (RadioButton)findViewById(R.id.bluetooth);
    	undo.setLayoutParams(params);
    	redo.setLayoutParams(params);
    	clear.setLayoutParams(params);
    	save.setLayoutParams(params);
    	open.setLayoutParams(params);
    	bluetooth.setLayoutParams(params);
    	pen.setLayoutParams(params);
 		mHorizontalScrollView = (HorizontalScrollView)findViewById(R.id.horizontalScrollView);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
    	super.onActivityResult(requestCode, resultCode, data);
    	switch(requestCode){
    	case FILE_EXPLORER_REQUEST:
    		if(resultCode == RESULT_OK) {
    			String path = data.getStringExtra("path");
    			/*
        		if(mainView.open(path, false)) {
        			Toast.makeText(this, "文件读取成功", Toast.LENGTH_SHORT).show();
        		} else {
        			Toast.makeText(this, "文件读取失败", Toast.LENGTH_SHORT).show();
        		}
        		*/
    		}
    		break;
    	default:
			break;
    	}
    }
    /*
     * 初始化 阈值配置文件
     * */
    private void initConfig() {
    	DisplayMetrics dm = getResources().getDisplayMetrics();
    	ThresholdProperty.set((float)dm.densityDpi / DisplayMetrics.DENSITY_MEDIUM, dm.widthPixels);
    }
    
    //返回键对话框
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_MENU) {
    		if (mHorizontalScrollView.getVisibility() == ViewGroup.INVISIBLE) {
    			mHorizontalScrollView.setVisibility(ViewGroup.VISIBLE);
			} else {
				mHorizontalScrollView.setVisibility(ViewGroup.INVISIBLE);
			}
    		
    		return true;
    	}
        if(keyCode == KeyEvent.KEYCODE_BACK) {
        	exit();
        	return true;
        }else{
        	return super.onKeyDown(keyCode, event);
        }
    }
    
    
    private void exit() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("退出软件");
    	builder.setMessage("确认退出？");
    	builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){
    		public void onClick(DialogInterface dialog, int whichButton) {
    			//mainView.clear();
    			UnitController.getInstance().clear();
    			//closeBluetooth();
       		    finish();
    		}
    	});
    	builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
    		public void onClick(DialogInterface dialog, int whichButton) {
    			
    		}
    	});
    	AlertDialog dialog = builder.create();
    	dialog.setCanceledOnTouchOutside(true);
    	dialog.show();
    }
    

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.undo:
			//mainView.Undo();
			undo.setChecked(false);
			break;
		case R.id.redo:
			//mainView.Redo();
			redo.setChecked(false);
			break;
		case R.id.clear:
			//clear();
			UnitController.getInstance().clear();
			clear.setChecked(false);
			break;
		case R.id.save:
			//save();
			save.setChecked(false);
			break;
		case R.id.open:
			open.setChecked(false);
			Intent intent = new Intent(this,FileExplorerActivity.class); 
    		startActivityForResult(intent, FILE_EXPLORER_REQUEST);
			break;
		case R.id.pen:
			//if(mainView.isRecognize()) {
			//	Toast.makeText(this, "图形识别已关闭", Toast.LENGTH_SHORT).show();
			//	mainView.setRecognize(false);
			//} else {
			//	Toast.makeText(this, "图形识别已打开", Toast.LENGTH_SHORT).show();
			//	mainView.setRecognize(true);
			//}
			pen.setChecked(false);
			break;
		case R.id.bluetooth:
			//chooseDevice();
			bluetooth.setChecked(false);
			break;
		default:
			break;
		}
	}
    

}