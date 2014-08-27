package com.example.bluno;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.bluno.BleCmd;
import com.example.bluno.ProgressWheel;
import com.example.bluno.BluetoothLeService;
import com.example.bluno.SampleGattAttributes;
import com.example.bluno.DeviceControlActivity;
import com.larswerkman.colorpicker.ColorPicker;
import com.larswerkman.colorpicker.ColorPicker.OnColorChangedListener;




import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class BLUNOActivity extends Activity {
	
    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final int CMD_OK = 1,CMD_ERR = 2,CMD_KEY = 3,CMD_ADC = 4;
    private String mDeviceName;
    private String mDeviceAddress;
    public  static String mBleCmd;
    private static BluetoothGattCharacteristic mSCharacteristic;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
	private LeDeviceListAdapter mLeDeviceListAdapter=null;
	private BluetoothAdapter mBluetoothAdapter;
	private boolean mScanning;

	private static final int REQUEST_ENABLE_BT = 1;

	private AlertDialog mScanDeviceDialog;
	
	static class ViewHolder {
		TextView deviceName;
		TextView deviceAddress;
	}
	

	
    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
	
    public static final int LogicalHigh=0x1;
    public static final int LogicalLow=0x0;

    
    public boolean mConnected = false;
	
	public static final String SerialPortUUID="0000dfb1-0000-1000-8000-00805f9b34fb";

	private enum titleModeEnum{isNull, isScanning, isToScan, isConnecting , isConnected};
	titleModeEnum titleMode = titleModeEnum.isNull;
	

	private PlainProtocol mPlainProtocol= new PlainProtocol();

	private boolean isServiceBind=false;
	
	private ProgressWheel progressWheel;
	private Typeface txtotf;
	private boolean isColorChange=false;
	private ImageView titleImageView;
	private ImageView ledImage = null;
	private ImageView joystickImage = null;
	private ImageView PotentiometerImage = null;
	private ImageView arduinoinputdispArea = null;
	private EditText oledSubmitEditText = null;
	private me.imid.view.SwitchButton buzzerSwitch, relaySwitch;
	private ImageView oledSubmitButton,oledClearButton;
	private TextView analogTextDisp;
    private TextView txtTemp;
    private TextView txtHumidity;
	private ColorPicker picker;
    
    public static final int LEDMode=0;
    public static final int RockerMode=1;
    public static final int KnobMode=2;
    private byte Modestates = LEDMode;

	private static Handler receivedHandler = new Handler();
	private Runnable PotentiometerRunnable = new Runnable() {
		@Override
		public void run() {
			if(Modestates == KnobMode)
			{
	        	if(mBluetoothLeService!= null && mSCharacteristic != null){
					mBleCmd = BleCmd.Knob;
					mSCharacteristic.setValue(mPlainProtocol.write(mBleCmd));
					mBluetoothLeService.writeCharacteristic(mSCharacteristic);
					//Thread thread= new Thread(mAutoTest);
					//thread.start();
					System.out.println("update BleCmdReadPotentiometer");
	            }
			}
			receivedHandler.postDelayed(PotentiometerRunnable, 50);
		}
	};
	private Runnable temperatureRunnable = new Runnable() {
		
		
		
		@Override
		public void run() {
        	if(mBluetoothLeService!= null && mSCharacteristic != null){
				mBleCmd = BleCmd.Temperature;
				mSCharacteristic.setValue(mPlainProtocol.write(mBleCmd));
				mBluetoothLeService.writeCharacteristic(mSCharacteristic);
				//Thread thread= new Thread(mAutoTest);
				//thread.start();
				System.out.println("update temperature");

            }
			receivedHandler.postDelayed(temperatureRunnable, 1000);
		}
	};
	private Runnable humidityRunnable = new Runnable() {
		
		@Override
		public void run() {
			if(mBluetoothLeService!= null && mSCharacteristic != null){
				mBleCmd = BleCmd.Humidity;
				mSCharacteristic.setValue(mPlainProtocol.write(mBleCmd));
				mBluetoothLeService.writeCharacteristic(mSCharacteristic);
				//Thread thread= new Thread(mAutoTest);
				//thread.start();
				System.out.println("update humidity");

            }
			
			receivedHandler.postDelayed(humidityRunnable, 1000);
		}
	};

	private boolean isLastSwitchOn=false;
	private Runnable colorRunnable = new Runnable() {
		
		@Override
		public void run() {
			if(Modestates == LEDMode)
			{
				if(picker.mIsSwitchOn)
				{
					if(isColorChange || (isLastSwitchOn==false))
					{
						if(mBluetoothLeService!= null && mSCharacteristic != null){
							
							mBleCmd = BleCmd.RGBLed;
							mSCharacteristic.setValue(mPlainProtocol.write(mBleCmd,
									((picker.getColor() & 0x00ff0000)>>16),
									((picker.getColor() & 0x0000ff00)>>8),
									((picker.getColor() & 0x000000ff)>>0)
									));
							mBluetoothLeService.writeCharacteristic(mSCharacteristic);
			
			            }
					}
					isColorChange=false;
					isLastSwitchOn=true;
				}else{
					if(isLastSwitchOn)
					{
						if(mBluetoothLeService!= null && mSCharacteristic != null){
							
							mBleCmd = BleCmd.RGBLed;
							mSCharacteristic.setValue(mPlainProtocol.write(mBleCmd,0,0,0));
							mBluetoothLeService.writeCharacteristic(mSCharacteristic);
			            }
					}
					isLastSwitchOn=false;
				}
			}
//			System.out.println("update color");
			receivedHandler.postDelayed(colorRunnable, 50);
		}
	};
	
	// Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            System.out.println("mServiceConnection onServiceConnected");
        	mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        	System.out.println("mServiceConnection onServiceDisconnected");
            mBluetoothLeService = null;
        }
    };
	
 // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            System.out.println("mGattUpdateReceiver->onReceive->action="+action);
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
	    		receivedHandler.post(humidityRunnable); 
	    		receivedHandler.post(temperatureRunnable);
	    		switch(Modestates)
	    		{
	    		case LEDMode:
	    			receivedHandler.post(colorRunnable);
	    			break;
	    		case KnobMode:
	    			receivedHandler.post(PotentiometerRunnable);
	    			break;
	    		}

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                receivedHandler.removeCallbacks(humidityRunnable); 
	    		receivedHandler.removeCallbacks(temperatureRunnable);
	    		receivedHandler.removeCallbacks(colorRunnable); 
	    		receivedHandler.removeCallbacks(PotentiometerRunnable);
                titleMode = titleModeEnum.isToScan;
                titleImageView.setImageResource(R.drawable.title_scan);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
            	for (BluetoothGattService gattService : mBluetoothLeService.getSupportedGattServices()) {
            		System.out.println("ACTION_GATT_SERVICES_DISCOVERED  "+
            				gattService.getUuid().toString());
            	}
            	getGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
            	if(titleMode == titleModeEnum.isConnecting)
            	{
	                titleMode = titleModeEnum.isConnected;
	                titleImageView.setImageResource(R.drawable.title_connected);
            	}
            	System.out.println("displayData "+intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            	
            	mPlainProtocol.mReceivedframe.append(intent.getStringExtra(BluetoothLeService.EXTRA_DATA)) ;
            	System.out.print("mPlainProtocol.mReceivedframe:");
            	System.out.println(mPlainProtocol.mReceivedframe.toString());

            	while(mPlainProtocol.available())
            	{
                	System.out.print("receivedCommand:");
                	System.out.println(mPlainProtocol.receivedCommand);
            		
            		if(mPlainProtocol.receivedCommand.equals(BleCmd.Rocker))
            		{
            			if(Modestates == RockerMode)
                		{
    	            		System.out.println("received Rocker");
    	            		
    	            		switch(mPlainProtocol.receivedContent[0]){
    			        	case 0:	//None input
    			        		arduinoinputdispArea.setImageResource(R.drawable.inputbutton_none);
    			        		break;
    			        	case 1:	//center button pressed 
    			        		arduinoinputdispArea.setImageResource(R.drawable.inputbutton_right);
    			        		break;
    			        	case 2:	//up button pressed 
    			        		arduinoinputdispArea.setImageResource(R.drawable.inputbutton_up);
    			        		break;
    			        	case 3:	//left button pressed 
    			        		arduinoinputdispArea.setImageResource(R.drawable.inputbutton_left);
    			        		break;
    			        	case 4:	//down button pressed 
    			        		arduinoinputdispArea.setImageResource(R.drawable.inputbutton_down);
    			        		break;
    			        	case 5:	//right button pressed 
    			        		arduinoinputdispArea.setImageResource(R.drawable.inputbutton_center);
    			        		break;
    			        	default:
    			        		Log.e(getLocalClassName(), "Unkown joystick state: " + mPlainProtocol.receivedContent[0]);
    			        		break;
    			        	}
    	            	}
            		}
            		else if(mPlainProtocol.receivedCommand.equals(BleCmd.Temperature))
            		{
                		System.out.println("received Temperature");
                		txtTemp.setText(mPlainProtocol.receivedContent[0] + "¡ C");
            		}
                	else if(mPlainProtocol.receivedCommand.equals(BleCmd.Humidity)){
	            		System.out.println("received Humidity");
	            		txtHumidity.setText(mPlainProtocol.receivedContent[0] + " %");
                	}
                	else if(mPlainProtocol.receivedCommand.equals(BleCmd.Knob)){
	            		System.out.println("received Knob");            
	            		float pgPos = mPlainProtocol.receivedContent[0] / 3.75f;//Adjust display value to the angle
	            		progressWheel.setProgress(Math.round(pgPos));
	            		analogTextDisp.setText(String.valueOf(mPlainProtocol.receivedContent[0]));
                	}
            	}
            }
        }
    };
	
	
    //configure the buzzer switch and the relay switch 
	private void controlSwitch() {
		buzzerSwitch = (me.imid.view.SwitchButton)this.findViewById(R.id.buzzerSwitch);
		buzzerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked) {
		            // The toggle is enabled
		        	if(mBluetoothLeService!= null && mSCharacteristic != null){
						mBleCmd = BleCmd.Buzzer;
						mSCharacteristic.setValue(mPlainProtocol.write(mBleCmd, 1));
						mBluetoothLeService.writeCharacteristic(mSCharacteristic);
	                }
		        } else {
		            // The toggle is disabled
		        	if(mBluetoothLeService!= null && mSCharacteristic != null){
						mBleCmd = BleCmd.Buzzer;
						mSCharacteristic.setValue(mPlainProtocol.write(mBleCmd, 0));
						mBluetoothLeService.writeCharacteristic(mSCharacteristic);
	                }
		        }
		    }
		});
		
		
		relaySwitch = (me.imid.view.SwitchButton)this.findViewById(R.id.relaySwitch);
		relaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if (isChecked) {
		            // The toggle is enabled
		        	if(mBluetoothLeService!= null && mSCharacteristic != null){
						mBleCmd = BleCmd.Relay;
						mSCharacteristic.setValue(mPlainProtocol.write(mBleCmd, 1));
						mBluetoothLeService.writeCharacteristic(mSCharacteristic);
	                }
		        } else {
		            // The toggle is disabled
		        	if(mBluetoothLeService!= null && mSCharacteristic != null){
						mBleCmd = BleCmd.Relay;
						mSCharacteristic.setValue(mPlainProtocol.write(mBleCmd, 0));
						mBluetoothLeService.writeCharacteristic(mSCharacteristic);
	                }
		        }
		    }
		});
	}

	//configure the oled Submit component
	private void oledSubmitEditArea() {
		oledSubmitEditText = (EditText) this.findViewById(R.id.editArea);
		oledSubmitEditText.setBackgroundResource(R.drawable.edittext);
		
		oledSubmitEditText.setTypeface(txtotf);
		//Clear testing text
		oledSubmitEditText.setText("");
		oledSubmitEditText.setOnEditorActionListener(new OnEditorActionListener(){

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				if(mBluetoothLeService!= null && mSCharacteristic != null){
					mBleCmd = BleCmd.Disp;
					mSCharacteristic.setValue(mPlainProtocol.write(mBleCmd + oledSubmitEditText.getText(), 0,0));
					System.out.print("mSCharacteristic of edit text:");
					System.out.println(new String(mSCharacteristic.getValue()));
					mBluetoothLeService.writeCharacteristic(mSCharacteristic);
					}
				return false;
			}
		});

		oledSubmitButton = (ImageView)this.findViewById(R.id.uploadbutton);
		oledSubmitButton.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
				Log.i(getLocalClassName(),"oled submit text");
				//Note: edit BLE submission function here
				if(mBluetoothLeService!= null && mSCharacteristic != null){
				mBleCmd = BleCmd.Disp;
				mSCharacteristic.setValue(mPlainProtocol.write(mBleCmd + oledSubmitEditText.getText(), 0, 0));
				mBluetoothLeService.writeCharacteristic(mSCharacteristic);
				}
				
	        }
	    });
		
		oledClearButton = (ImageView)this.findViewById(R.id.clearbutton);
		
//		oledClearButton.setOnTouchListener(new OnTouchListener(){
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if(event.getActionMasked() == MotionEvent.ACTION_DOWN)
//				{
//					if(mBluetoothLeService!= null && mSCharacteristic != null){
//						mBleCmd = BleCmd.Relay;
//						mSCharacteristic.setValue(mPlainProtocol.write(mBleCmd, 1));
//						mBluetoothLeService.writeCharacteristic(mSCharacteristic);
//					}
//				}
//				if(event.getActionMasked() == MotionEvent.ACTION_UP)
//				{
//					if(mBluetoothLeService!= null && mSCharacteristic != null){
//						mBleCmd = BleCmd.Relay;
//						mSCharacteristic.setValue(mPlainProtocol.write(mBleCmd, 0));
//						mBluetoothLeService.writeCharacteristic(mSCharacteristic);
//					}
//				}
//				return false;
//			}
//			
//		});
		
		oledClearButton.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
				Log.i(getLocalClassName(),"Clear Text");
				//Clear text after submission
				if(mBluetoothLeService!= null && mSCharacteristic != null){
					mBleCmd = BleCmd.Disp;
					mSCharacteristic.setValue(mPlainProtocol.write(mBleCmd, 0, 0));
					mBluetoothLeService.writeCharacteristic(mSCharacteristic);
				}
				oledSubmitEditText.setText("");
	        }
	    });
		
		
	}
	
	//configure the color Picker wheel
	private void CreatePicker() {

		picker = (ColorPicker) findViewById(R.id.picker);
		
		picker.setOnColorChangedListener(new OnColorChangedListener(){

			@Override
			public void onColorChanged(int color) {
				isColorChange=true;
			}
		});
		

	}
    
	//configure the font
	private void FontConfig() {
		
		// Font path
		String fontPath = "fonts/yueregular.otf";
        txtTemp = (TextView) findViewById(R.id.tempdisp);
        txtHumidity = (TextView) findViewById(R.id.humiditydisp);
        
        txtotf = Typeface.createFromAsset(getAssets(), fontPath);
        txtTemp.setTypeface(txtotf);
        txtHumidity.setTypeface(txtotf);
        
        analogTextDisp = (TextView) findViewById(R.id.analogTextDisp);
        analogTextDisp.setTypeface(txtotf);

	}
	
	
	//configure the Image View switching part in the center of the UI
	public void imageConfig(){	

		ledImage = (ImageView)this.findViewById(R.id.ledbutton);
		joystickImage = (ImageView)this.findViewById(R.id.joystickbutton);
		PotentiometerImage = (ImageView)this.findViewById(R.id.potbutton);
		
		arduinoinputdispArea = (ImageView)this.findViewById(R.id.pot_input_Area);
		
		ledImage.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
				Log.i(getLocalClassName(),v.getDrawableState().toString());
				
				ledImage.setImageResource(R.drawable.led_tab_seleted);
				joystickImage.setImageResource(R.drawable.joystick_unselected);
				PotentiometerImage.setImageResource(R.drawable.pot_unseleted);

				// modeManager area - LED controller mode
				picker.setVisibility(View.VISIBLE);
				arduinoinputdispArea.setVisibility(View.INVISIBLE);
				analogTextDisp.setVisibility(View.INVISIBLE);

				Modestates = LEDMode;

				progressWheel.setVisibility(View.INVISIBLE);
				
	    		isLastSwitchOn=false;

				if(mConnected)
				{
		    		receivedHandler.post(colorRunnable);
		    		receivedHandler.removeCallbacks(PotentiometerRunnable);
				}

	        }
	    });
		joystickImage.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
				Log.i(getLocalClassName(),v.getDrawableState().toString());
				
				ledImage.setImageResource(R.drawable.led_tab_unseleted);
				joystickImage.setImageResource(R.drawable.joystick_selecte);
				PotentiometerImage.setImageResource(R.drawable.pot_unseleted);

				// modeManager area - joystick state display Mode
				picker.setVisibility(View.INVISIBLE);
        		arduinoinputdispArea.setImageResource(R.drawable.inputbutton_none);
				arduinoinputdispArea.setVisibility(View.VISIBLE);
				analogTextDisp.setVisibility(View.INVISIBLE);
				// check the testingThread function for the display control code
				Modestates = RockerMode;
				
				progressWheel.setVisibility(View.INVISIBLE);
				
				if(mConnected)
				{
		    		receivedHandler.removeCallbacks(colorRunnable);
					if(mBluetoothLeService!= null && mSCharacteristic != null){
						
						mBleCmd = BleCmd.RGBLed;
						mSCharacteristic.setValue(mPlainProtocol.write(mBleCmd,0,0,0));
						mBluetoothLeService.writeCharacteristic(mSCharacteristic);
		            }
		    		receivedHandler.removeCallbacks(PotentiometerRunnable);
				}
	        }
	    });
		
		PotentiometerImage.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View v) {
				Log.i(getLocalClassName(),v.getDrawableState().toString());
				
				ledImage.setImageResource(R.drawable.led_tab_unseleted);
				picker.setVisibility(View.INVISIBLE);
				
				joystickImage.setImageResource(R.drawable.joystick_unselected);
				
				PotentiometerImage.setImageResource(R.drawable.pot_selected);
				
        		arduinoinputdispArea.setImageResource(R.drawable.potmeter);

				arduinoinputdispArea.setVisibility(View.INVISIBLE);

				// modeManager area - joystick state display Mode
				picker.setVisibility(View.INVISIBLE);
				arduinoinputdispArea.setVisibility(View.VISIBLE);
				analogTextDisp.setVisibility(View.VISIBLE);
				// check the testingThread function for the display control code
				Modestates = KnobMode;
				
				//Add-ons progress wheel effect
				progressWheel.setVisibility(View.VISIBLE);
				
				//circleBounds.width()/2) + rimWidth + paddingLeft, 
				//(circleBounds.height()/2) + rimWidth + paddingTop, 
				//circleRadius
				if(mConnected)
				{
		    		receivedHandler.post(PotentiometerRunnable);
		    		receivedHandler.removeCallbacks(colorRunnable);
					if(mBluetoothLeService!= null && mSCharacteristic != null){
						
						mBleCmd = BleCmd.RGBLed;
						mSCharacteristic.setValue(mPlainProtocol.write(mBleCmd,0,0,0));
						mBluetoothLeService.writeCharacteristic(mSCharacteristic);
		            }
				}
	        }
	    });
	}
	
	//configure the progress Wheel of the Potentiometer
	private void progressWheelConfig() {
		progressWheel = (ProgressWheel) findViewById(R.id.pw_spinner);
		
	}

	//configure the title image which shows the connection state
	void titleImageConfig()
	{
		
        titleImageView =  (ImageView)findViewById(R.id.title_image_view);
        titleImageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(titleMode==titleModeEnum.isConnecting){
		        	titleMode=titleModeEnum.isToScan;
	                titleImageView.setImageResource(R.drawable.title_scan);
				}
				else{
					receivedHandler.removeCallbacks(humidityRunnable); 
					receivedHandler.removeCallbacks(temperatureRunnable); 
					receivedHandler.removeCallbacks(colorRunnable); 
					receivedHandler.removeCallbacks(PotentiometerRunnable);
					
					if(isServiceBind)
					{
				        unbindService(mServiceConnection);
				        isServiceBind=false;
					}
			        mBluetoothLeService = null;
			        
		        	titleMode=titleModeEnum.isScanning;
	                titleImageView.setImageResource(R.drawable.title_scanning);
	                if(!mScanning)
	                {
						scanLeDevice(true);
	                }
					mScanDeviceDialog.show();
				}
			}
		});


	}
	
	// Device scan callback.
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, int rssi,
				byte[] scanRecord) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					System.out.println("mLeScanCallback onLeScan run ");
					mLeDeviceListAdapter.addDevice(device);
					mLeDeviceListAdapter.notifyDataSetChanged();
				}
			});
		}
	};
	
	// Adapter for holding devices found through scanning.
	private class LeDeviceListAdapter extends BaseAdapter {
		private ArrayList<BluetoothDevice> mLeDevices;
		private LayoutInflater mInflator;

		public LeDeviceListAdapter() {
			super();
			mLeDevices = new ArrayList<BluetoothDevice>();
			mInflator =  BLUNOActivity.this.getLayoutInflater();
		}

		public void addDevice(BluetoothDevice device) {
			if (!mLeDevices.contains(device)) {
				mLeDevices.add(device);
			}
		}

		public BluetoothDevice getDevice(int position) {
			return mLeDevices.get(position);
		}

		public void clear() {
			mLeDevices.clear();
		}

		@Override
		public int getCount() {
			return mLeDevices.size();
		}

		@Override
		public Object getItem(int i) {
			return mLeDevices.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			ViewHolder viewHolder;
			// General ListView optimization code.
			if (view == null) {
				view = mInflator.inflate(R.layout.listitem_device, null);
				viewHolder = new ViewHolder();
				viewHolder.deviceAddress = (TextView) view
						.findViewById(R.id.device_address);
				viewHolder.deviceName = (TextView) view
						.findViewById(R.id.device_name);
				System.out.println("mInflator.inflate  getView");
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}

			BluetoothDevice device = mLeDevices.get(i);
			final String deviceName = device.getName();
			if (deviceName != null && deviceName.length() > 0)
				viewHolder.deviceName.setText(deviceName);
			else
				viewHolder.deviceName.setText(R.string.unknown_device);
			viewHolder.deviceAddress.setText(device.getAddress());

			return view;
		}
	}

	private void scanLeDevice(final boolean enable) {
		if (enable) {
			// Stops scanning after a pre-defined scan period.
//			mHandler.postDelayed(new Runnable() {
//				@Override
//				public void run() {
//
//				}
//			}, SCAN_PERIOD);
			System.out.println("mBluetoothAdapter.startLeScan");
			if(mLeDeviceListAdapter != null)
			{
				mLeDeviceListAdapter.clear();
				mLeDeviceListAdapter.notifyDataSetChanged();
			}
			mScanning = true;
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
	}
	
	boolean initiate()
	{
		// Use this check to determine whether BLE is supported on the device.
		// Then you can
		// selectively disable BLE-related features.
		if (!getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			return false;
		}
		
		// Initializes a Bluetooth adapter. For API level 18 and above, get a
		// reference to
		// BluetoothAdapter through BluetoothManager.
		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = bluetoothManager.getAdapter();
	
		// Checks if Bluetooth is supported on the device.
		if (mBluetoothAdapter == null) {
			return false;
		}
		return true;
	}
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("BLUNOActivity onCreate");
		setContentView(R.layout.activity_bluno);
		
		if(!initiate())
		{
			Toast.makeText(this, R.string.error_bluetooth_not_supported,
					Toast.LENGTH_SHORT).show();
			finish();
		}
		
		imageConfig();
		FontConfig();
		progressWheelConfig();
		titleImageConfig();
		CreatePicker();
		oledSubmitEditArea();
		controlSwitch();
		
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("BlUNOActivity onResume");
		

		// Ensures Bluetooth is enabled on the device. If Bluetooth is not
		// currently enabled,
		// fire an intent to display a dialog asking the user to grant
		// permission to enable it.
		if (!mBluetoothAdapter.isEnabled()) {
			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}

		// Initializes list view adapter.
		mLeDeviceListAdapter = new LeDeviceListAdapter();
		// Initializes and show the scan Device Dialog
		mScanDeviceDialog = new AlertDialog.Builder(this)
		.setTitle("BLE Device Scan...").setAdapter(mLeDeviceListAdapter, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				final BluetoothDevice device = mLeDeviceListAdapter.getDevice(which);
				if (device == null)
					return;
				if (mScanning) {
					scanLeDevice(false);
				}
				System.out.println("onListItemClick " + device.getName().toString());
				if (device.getName().equals("DFBLEduinoV1.0")) {
					System.out
					.println("Device Name:"+device.getName() + "   "
							+ "Device Name:"
							+ device.getAddress());
					
					mDeviceName=device.getName().toString();
					mDeviceAddress=device.getAddress().toString();
					
			        if(mDeviceName.equals("No Device Available") && mDeviceAddress.equals("No Address Available"))
			        {
			        	titleMode=titleModeEnum.isToScan;
			            titleImageView.setImageResource(R.drawable.title_scan);
			        }
			        else{
			        	titleMode=titleModeEnum.isConnecting;
			            titleImageView.setImageResource(R.drawable.title_connecting);

			            Intent gattServiceIntent = new Intent(BLUNOActivity.this, BluetoothLeService.class);
			            bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
			            isServiceBind=true;
			            

			        }
				} else {
					final Intent intent = new Intent(BLUNOActivity.this, DeviceControlActivity.class);
					System.out.println(DeviceControlActivity.EXTRAS_DEVICE_NAME
							+ device.getName() + "   "
							+ DeviceControlActivity.EXTRAS_DEVICE_ADDRESS
							+ device.getAddress());
					intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_NAME,
							device.getName());
					intent.putExtra(DeviceControlActivity.EXTRAS_DEVICE_ADDRESS,
							device.getAddress());
					startActivity(intent);
				}
			}
		})
		.setOnCancelListener(new DialogInterface.OnCancelListener(){

			@Override
			public void onCancel(DialogInterface arg0) {
				System.out.println("mBluetoothAdapter.stopLeScan");

	        	titleMode=titleModeEnum.isToScan;
                titleImageView.setImageResource(R.drawable.title_scan);
				mScanDeviceDialog.dismiss();
	            if(mScanning)
	            {
					scanLeDevice(false);
	            }
			}
		}).create();
		
		if(mConnected)
		{
			mScanDeviceDialog.dismiss();
            if(mScanning)
            {
				scanLeDevice(false);
            }
		}
		else
		{
			mScanDeviceDialog.show();
			if(!mScanning)
			{
				scanLeDevice(true);
			}

		}
		
		
	    registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		
	    if (mBluetoothLeService != null) {
	        final boolean result = mBluetoothLeService.connect(mDeviceAddress);
		    registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
	        Log.d(TAG, "Connect request result=" + result);
	    }
		
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// User chose not to enable Bluetooth.
		if (requestCode == REQUEST_ENABLE_BT
				&& resultCode == Activity.RESULT_CANCELED) {
			finish();
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("BLUNOActivity onPause");
		if(mScanning)
		{
			scanLeDevice(false);
		}
        unregisterReceiver(mGattUpdateReceiver);
		mLeDeviceListAdapter.clear();
	}
	
	protected void onStop() {
		super.onStop();


		System.out.println("MiUnoActivity onStop");
		
		receivedHandler.removeCallbacks(humidityRunnable); 
		receivedHandler.removeCallbacks(temperatureRunnable); 
		receivedHandler.removeCallbacks(colorRunnable); 
		receivedHandler.removeCallbacks(PotentiometerRunnable);
		
//		if(!(mDeviceName.equals("No Device Available") && mDeviceAddress.equals("No Address Available")))
		if(isServiceBind)
		{
	        unbindService(mServiceConnection);
	        isServiceBind=false;
		}
		
		if(mBluetoothLeService!=null)
		{
			mBluetoothLeService.disconnect();
		}
        mBluetoothLeService = null;
		mSCharacteristic=null;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("MiUnoActivity onDestroy");
		

	}
	
    private void getGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            System.out.println("displayGattServices + uuid="+uuid);
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                if(uuid.equals(SerialPortUUID)){
                	mSCharacteristic = gattCharacteristic;
                	mBluetoothLeService.setCharacteristicNotification(
                			mSCharacteristic, true);
                	System.out.println("mSCharacteristic  "+mSCharacteristic.getUuid().toString());
//                    updateConnectionState(R.string.comm_establish);
                }
                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }
    }
    
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
	
	
}
