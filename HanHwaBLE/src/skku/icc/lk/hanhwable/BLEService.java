/**
 * made my lk.kim in skku
 */
package skku.icc.lk.hanhwable;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class BLEService extends Service {
	///////////////////
	/* Action String */
	///////////////////
	/**스마트폰이 부팅되었음을 알립니다.
	 */
	public static final String ACTION_BOOT_COMPLETED	= "android.intent.action.BOOT_COMPLETED";
	/**beacon에서 announcement하길 요청합니다.
	 */
	public static final String ACTION_ANNOUNCEMENT		= "skku.icc.lk.HanHwaBeacon.announcement";
	/**BLE와 연결되었음을 알립니다.
	 */
	public final static String ACTION_GATT_CONNECTED = "skku.icc.lk.HanHwaBeacon.ACTION_GATT_CONNECTED";
	/**BLE와 단절되었음을 알립니다.
	 */
	public final static String ACTION_GATT_DISCONNECTED = "skku.icc.lk.HanHwaBeacon.ACTION_GATT_DISCONNECTED";
	/**BLE의 service를 찾았음을 알립니다.
	 */
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "skku.icc.lk.HanHwaBeacon.ACTION_GATT_SERVICES_DISCOVERED";
	/**BLE가 data를 전송했음을 알립니다.
	 */
	public final static String ACTION_DATA_AVAILABLE = "skku.icc.lk.HanHwaBeacon.ACTION_DATA_AVAILABLE";
	/**이건 뭐지...?
	 */
	public final static String ACTION_EXTRA_DATA = "skku.icc.lk.HanHwaBeacon.EXTRA_DATA";

	/* Bluetooth Variables */
	private BluetoothManager bluetoothManager;
	private BluetoothAdapter bluetoothAdapter;
	private BluetoothGatt bthGatt;

	/** 현재 또는 latest BLE기기의 주소
	 * 아마도 이건 나중에 배열로 바뀌지 않을까 생각합니다...
	 */
	public String bluetoothDeviceAddress;

	/**Thread를 돌리기 위한 handler입니다.
	 */
	private Handler handler;
	/**이 service가 무슨 짓을 하는 지를 표시하는 건데...이것도 아마 없어도 될 겁니다.
	 */
	public enum connectionStateEnum{isNull, isScanning, isToScan, isConnecting , isConnected, isDisconnecting};
	public connectionStateEnum connectionState = connectionStateEnum.isNull;

	/**
	 * 연결을 시도할 때, 허용된 시간이 초과되면 강제로 서비스를 꺼버립니다.
	 */
	private Runnable connectTimeLimitRunnable	= new Runnable(){

		@Override
		public void run() {
			if(connectionState == connectionStateEnum.isConnecting)
				connectionState	= connectionStateEnum.isToScan;

			onConnectionStateChange(connectionState);

			System.out.println("connect time out...service will be shutdown");
			shutdownService();//TODO service로 intent를 보내서 종료시켜야 합니다.
		}
	};

	/**
	 * 연결을 해제할 때, 허용된 시간이 초과되면 강제로 서비스를 꺼버립니다.
	 */
	private Runnable disonnectTimeLimitRunnable	= new Runnable(){

		@Override
		public void run() {
			if(connectionState == connectionStateEnum.isDisconnecting)
				connectionState	= connectionStateEnum.isToScan;

			onConnectionStateChange(connectionState);
			System.out.println("disconnect time out...service will be shutdown");
			shutdownService();
		}
	};


	/* RingBuffer */
	/**To tell the onCharacteristicWrite call back function that this is a new characteristic,
	 * not the Write Characteristic to the device successfully. 
	 */
	private static final int WRITE_NEW_CHARACTERISTIC = -1;
	/**define the limited length of the characteristic.
	 */
	private static final int MAX_CHARACTERISTIC_LENGTH = 17;
	/**Show that Characteristic is writing or not.
	 */
	private boolean isDataRemained	= false;
	private RingBuffer<BluetoothModel> bthModelRBuffer = new RingBuffer<BluetoothModel>(8);


	/**
	 * bluethooth Generic Attribute Call back function
	 * BLE 서버 입니다. 이 변수가 모든 BLE기기를 관리합니다.
	 */
	private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			System.out.println("BluetoothGattCallback----onConnectionStateChange"+newState);

			switch (newState) {
			case BluetoothProfile.STATE_CONNECTED :
				sendBroadcast(ACTION_GATT_CONNECTED);

				System.out.println("Connected to GATT server.");

				// Attempts to discover services after successful connection.
				if(bthGatt.discoverServices())
					System.out.println("Attempting to start service discovery:");
				else
					System.out.println("Attempting to start service discovery:not success");
				break;

			case BluetoothProfile.STATE_DISCONNECTED :
				System.out.println("Disconnected from GATT server.");
				sendBroadcast(ACTION_GATT_DISCONNECTED);
				break;
			}
		}

		/**
		 * BLE의 service를 확인하면 실행됩니다.
		 */
		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			System.out.println("BLE의 기능을 찾았습니다 onServicesDiscovered "+status);

			if (status == BluetoothGatt.GATT_SUCCESS)
				sendBroadcast(ACTION_GATT_SERVICES_DISCOVERED);
			else 
				System.out.println("GATT를 못 받았다...onServicesDiscovered received: " + status);
		}

		/**
		 * BLE 정보를 write합니다.
		 */
		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status){
			//this block should be synchronized to prevent the function overloading
			synchronized(this){
				//CharacteristicWrite success
				if(status == BluetoothGatt.GATT_SUCCESS){
					System.out.println("onCharacteristicWrite success:"+ new String(characteristic.getValue()));

					if(bthModelRBuffer.isEmpty()){
						isDataRemained = false;
					}else{
						BluetoothModel bthModel = bthModelRBuffer.next();

						//characteristic에 쓸 내용이 17byte를 초과하면 나눠서 앞의 내용만 먼저 전송합니다. 
						if(bthModel.characteristicBuffer.length() > MAX_CHARACTERISTIC_LENGTH){
							try {
								bthModel.characteristic.setValue(bthModel.characteristicBuffer.substring(0, MAX_CHARACTERISTIC_LENGTH).getBytes("ISO-8859-1"));

							} catch (UnsupportedEncodingException e) {
								// this should never happen because "US-ASCII" is hard-coded.
								throw new IllegalStateException(e);
							}

							if(bthGatt.writeCharacteristic(bthModel.characteristic))
								System.out.println("writeCharacteristic init "+new String(bthModel.characteristic.getValue())+ ":success");
							else
								System.out.println("writeCharacteristic init "+new String(bthModel.characteristic.getValue())+ ":failure");

							bthModel.characteristicBuffer = bthModel.characteristicBuffer.substring(MAX_CHARACTERISTIC_LENGTH);
							//characteristic에 쓸 내일이 17byte 이내면 모두 전송하고 해당 BluetoothModel을 pop합니다.
						}else{
							try {
								bthModel.characteristic.setValue(bthModel.characteristicBuffer.getBytes("ISO-8859-1"));
							} catch (UnsupportedEncodingException e) {
								// this should never happen because "US-ASCII" is hard-coded.
								throw new IllegalStateException(e);
							}

							if(bthGatt.writeCharacteristic(bthModel.characteristic))
								System.out.println("writeCharacteristic init "+new String(bthModel.characteristic.getValue())+ ":success");
							else
								System.out.println("writeCharacteristic init "+new String(bthModel.characteristic.getValue())+ ":failure");

							bthModel.characteristicBuffer = "";

							bthModelRBuffer.pop();
						}
					}
					//WRITE a NEW CHARACTERISTIC
				}else if(status == WRITE_NEW_CHARACTERISTIC){
					if((!bthModelRBuffer.isEmpty()) && isDataRemained == false){
						BluetoothModel bthModel = bthModelRBuffer.next();

						if(bthModel.characteristicBuffer.length() > MAX_CHARACTERISTIC_LENGTH){

							try {
								bthModel.characteristic.setValue(bthModel.characteristicBuffer.substring(0, MAX_CHARACTERISTIC_LENGTH).getBytes("ISO-8859-1"));
							} catch (UnsupportedEncodingException e) {
								// this should never happen because "US-ASCII" is hard-coded.
								throw new IllegalStateException(e);
							}

							if(bthGatt.writeCharacteristic(bthModel.characteristic)){
								System.out.println("writeCharacteristic init "+new String(bthModel.characteristic.getValue())+ ":success");
							}else{
								System.out.println("writeCharacteristic init "+new String(bthModel.characteristic.getValue())+ ":failure");
							}
							bthModel.characteristicBuffer = bthModel.characteristicBuffer.substring(MAX_CHARACTERISTIC_LENGTH);
						}else{
							try {
								bthModel.characteristic.setValue(bthModel.characteristicBuffer.getBytes("ISO-8859-1"));
							} catch (UnsupportedEncodingException e) {
								// this should never happen because "US-ASCII" is hard-coded.
								throw new IllegalStateException(e);
							}

							if(bthGatt.writeCharacteristic(bthModel.characteristic)){
								System.out.println("writeCharacteristic init "+new String(bthModel.characteristic.getValue())+ ":success");
							}else{
								System.out.println("writeCharacteristic init "+new String(bthModel.characteristic.getValue())+ ":failure");
							}

							bthModel.characteristicBuffer = "";

							bthModelRBuffer.pop();
						}
					}

					isDataRemained = true;

					//clear the buffer to prevent the lock of the mIsWritingCharacteristic
					if(bthModelRBuffer.isFull()){
						bthModelRBuffer.clear();
						isDataRemained = false;
					}
				}else{
					//CharacteristicWrite fail
					bthModelRBuffer.clear();
					System.out.println("onCharacteristicWrite fail:"+ new String(characteristic.getValue()));
					System.out.println(status);
				}
			}
		}

		/**
		 * BLE기기가 전송한 데이터를 수신합니다.
		 */
		@Override
		public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				System.out.println("onCharacteristicRead  "+characteristic.getUuid().toString());
				broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
			}
		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor characteristic, int status){
			System.out.println("onDescriptorWrite  "+characteristic.getUuid().toString()+" "+status);
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
			System.out.println("onCharacteristicChanged  "+new String(characteristic.getValue()));
			broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
		}
	};

	/**Broadcast action을 업데이트합니다. 
	 * @param action 명시적 broadcast용 action입니다.
	 */
	private void sendBroadcast(final String action) {
		sendBroadcast(new Intent(action));
	}

	/**Broadcast action을 업데이트합니다.<br>
	 * BluetoothGattCharacteristic이 가지고 있는 값을 intent에 포함시키고, 그 intent를 broadcast합니다.
	 * XXX 이건 추후에 없어질 수도 있습니다.
	 * @param action
	 * @param characteristic
	 */
	private void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic) {

		System.out.println("BluetoothLeService broadcastUpdate");

		final byte[] data = characteristic.getValue();

		if (data != null && data.length > 0) 
			sendBroadcast(new Intent(action).putExtra(ACTION_EXTRA_DATA, new String(data)));
	}

	/**BluetoothGattCharacteristic과 거기에 실어서 보낼 data buffer를 보관하는 class입니다.<br>
	 * characteristic은 17byte가 한계인데 이보다 긴 경우가 다반사라서 해당 class를 만들었습니다.
	 * @author lk.kim
	 *
	 */
	private class BluetoothModel{
		private BluetoothGattCharacteristic characteristic;
		private String characteristicBuffer;

		BluetoothModel(BluetoothGattCharacteristic chatic, String chaticVal){
			characteristic			= chatic;
			characteristicBuffer	= chaticVal;
		}
	}
	private int[] loginBuffer = new int[10];
	private int loginCounter;
	/** scan하기 위한 callback함수입니다.
	 */
	private BluetoothAdapter.LeScanCallback scanCallback	= new BluetoothAdapter.LeScanCallback() {
		
		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			System.out.println("device : " + device);
			System.out.println("RSSI : " + rssi);
			System.out.println("name : " + device.getName());
			System.out.println("address : " + device.getAddress());
			System.out.println("bondrate" + device.getBondState());
			System.out.println("type : " + device.getType());
			System.out.println("scanRecord : " + scanRecord);
			
//			if(device.getAddress().equals("D0:FF:50:67:6F:96")){
//				//TODO 연결을 진행합니다. 그런데 연결을 해야 되나?
////				connect(device.getAddress());
//			}
//			
			loginBuffer[loginCounter]	= rssi;
			
			int j	= 0;
			
			if(loginCounter == 9){
				for(int i = 0 ; i < loginBuffer.length ; i++){
					j += loginBuffer[i];
				}
				
				if(Math.abs(j / 10 ) < 72)
					System.out.println("loginloginloginlogin\nloginloginloginlogin\nloginloginloginlogin\nloginloginloginlogin\n");
			}
			
			loginCounter	= loginCounter == 9 ? 0 : loginCounter + 1;
		}
	};
	
	private Runnable scanRunnable	= new Runnable() {
		
		@Override
		public void run() {
				System.out.println("start scan");
//				//XXX 흐잉 껏다 키면 복구가 안대 ㅠㅠㅠ
				if(bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON)
					bluetoothAdapter.startLeScan(scanCallback);
				else
					System.out.println("bluetoothAdater state off");
		}
	};

	/*////////// bluetooth functions //////////*/
	/**
	 * Initializes the local Bluetooth manager and adapter.
	 *
	 * @return Return true if the initialization is successful.
	 */
	public boolean initializeBluetooth() {
		// For API level 18 and above, get a reference to BluetoothAdapter through
		// BluetoothManager.
		System.out.println("BluetoothLeService initialize"+bluetoothManager);

		if(handler == null)
			handler	= new Handler();
		
		if(scanHandler == null)
			scanHandler	= new Handler();
		
		if (bluetoothManager == null) {
			bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

			if (bluetoothManager == null) {
				System.out.println("Unable to initialize BluetoothManager.");

				return false;
			}
		}

		bluetoothAdapter = bluetoothManager.getAdapter();

		if (bluetoothAdapter == null) {
			System.out.println("Unable to obtain a BluetoothAdapter.");

			return false;
		}
		
		//BLE를 scan하는 thread입니다.
		scanHandler.post(scanRunnable);

		return true;
	}

	/**
	 * Connects to the GATT server hosted on the Bluetooth LE device.
	 *
	 * @param address The device address of the destination device.
	 *
	 * @return Return true if the connection is initiated successfully. The connection result
	 *         is reported asynchronously through the
	 *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 *         callback.
	 */
	public boolean connect(final String address) {
		System.out.println("BluetoothLeService connect"+address+bthGatt);

		if (bluetoothAdapter == null || address == null) {
			System.out.println("BluetoothAdapter not initialized or unspecified address.");

			return false;
		}

		// Previously connected device.  Try to reconnect.
		if (bluetoothDeviceAddress != null && address.equals(bluetoothDeviceAddress) && bthGatt != null) {
			System.out.println("Trying to use an existing mBluetoothGatt for connection.");

			if (bthGatt.connect()) {
				System.out.println("mBluetoothGatt connect");
				return true;
			} else {
				System.out.println("mBluetoothGatt else connect");
				return false;
			}
		}

		//XXX 처음 BLE에 대한 정보를 받는 곳
		final BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);

		if (device == null) {
			System.out.println("Device not found.  Unable to connect.");
			return false;
		}
		// We want to directly connect to the device, so we are setting the autoConnect
		// parameter to false.
		System.out.println("device.connectGatt connect");

		synchronized(this){
			/*device.connectGatt (Context context, boolean autoConnect, BluetoothGattCallback callback)
			 * context : 연결을 시도하는 현재 context
			 * autoConnect : 자동으로 연결할 건지 아닌지를 결정
			 * callback : bluetooth gatt callback 변수 
			 * 
			 */
			bthGatt = device.connectGatt(this, false, gattCallback);
		}
		System.out.println("Trying to create a new connection.");

		return true;
	}

	/**
	 * Disconnects an existing connection or cancel a pending connection. The disconnection result
	 * is reported asynchronously through the
	 * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 * callback.
	 */
	public void disconnect() {
		System.out.println("BluetoothLeService disconnect");

		if (bluetoothAdapter == null || bthGatt == null) {
			System.out.println("BluetoothAdapter not initialized");

			return;
		}

		bthGatt.disconnect();
	}

	/**
	 * After using a given BLE device, the app must call this method<br> 
	 * to ensure resources are released properly.<br>
	 * disconnect -> close 순으로 호출할 것.
	 */
	public void shutdownService() {
		System.out.println("Bluetooth Service shutdown");

		if (bthGatt != null){
			bthGatt.close();
			bthGatt = null;
		}
	}

	/**
	 * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
	 * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
	 * callback.
	 *
	 * @param characteristic The characteristic to read from.
	 */
	public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (bluetoothAdapter == null || bthGatt == null) {
			System.out.println("BluetoothAdapter not initialized");
			return;
		}

		bthGatt.readCharacteristic(characteristic);
	}


	/**
	 * Write information to the device on a given {@code BluetoothGattCharacteristic}. The content string and characteristic is 
	 * only pushed into a ring buffer. All the transmission is based on the {@code onCharacteristicWrite} call back function, 
	 * which is called directly in this function
	 * @param characteristic The characteristic to write to.
	 */
	public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (bluetoothAdapter == null || bthGatt == null) {
			System.out.println("BluetoothAdapter not initialized");
			return;
		}

		//The character size of TI CC2540 is limited to 17 bytes, otherwise characteristic can not be sent properly,
		//so String should be cut to comply this restriction. And something should be done here:
		String stringifiedValue;

		try {
			stringifiedValue = new String(characteristic.getValue(),"ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			// this should never happen because "US-ASCII" is hard-coded.
			throw new IllegalStateException(e);
		}
		System.out.println("stringifiedBthChaticValue:"+stringifiedValue);

		//As the communication is asynchronous content string and characteristic should be pushed into an ring buffer for further transmission
		bthModelRBuffer.push(new BluetoothModel(characteristic, stringifiedValue) );
		System.out.println("bthChaticRBuffer:"+bthModelRBuffer.size());


		//The progress of onCharacteristicWrite and writeCharacteristic is almost the same. So callback function is called directly here
		//for details see the onCharacteristicWrite function
		gattCallback.onCharacteristicWrite(bthGatt, characteristic, WRITE_NEW_CHARACTERISTIC);

	}    

	/**
	 * Enables or disables notification on a give characteristic.
	 *
	 * @param characteristic Characteristic to act on.
	 * @param enabled If true, enable notification.  False otherwise.
	 */
	public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (bluetoothAdapter == null || bthGatt == null) {
			System.out.println("BluetoothAdapter not initialized");
			return;
		}

		bthGatt.setCharacteristicNotification(characteristic, enabled);
	}

	/**
	 * Retrieves a list of supported GATT services on the connected device. This should be
	 * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
	 *
	 * @return A {@code List} of supported services.
	 */
	public List<BluetoothGattService> getSupportedGattServices() {
		return bthGatt == null ? null :  bthGatt.getServices();
	}

	public static final String SerialPortUUID="0000dfb1-0000-1000-8000-00805f9b34fb";
	public static final String CommandUUID="0000dfb2-0000-1000-8000-00805f9b34fb";
	public static final String ModelNumberStringUUID="00002a24-0000-1000-8000-00805f9b34fb";

	/**gattCharacteristic.getUuid().toString()에서 읽어온 UUID를 저장합니다.
	 * <br>각 UUID는 전용 characteristic별로 나눠서 저장됩니다. 
	 * @param uuid
	 * @param gattCharacteristic
	 */
	private void distributeUUIDtoCharacteristic(String uuid, BluetoothGattCharacteristic gattCharacteristic){
		if(uuid.equals(ModelNumberStringUUID)){
			modelNoCharacteristic	= gattCharacteristic;
			System.out.println("mModelNumberCharacteristic  "+modelNoCharacteristic.getUuid().toString());
		}else if(uuid.equals(SerialPortUUID)){
			serialPortCharacteristic = gattCharacteristic;
			System.out.println("mSerialPortCharacteristic  "+serialPortCharacteristic.getUuid().toString());
		}
	}

	private static BluetoothGattCharacteristic StaticCharacteristic;
	private static BluetoothGattCharacteristic modelNoCharacteristic;
	private static BluetoothGattCharacteristic serialPortCharacteristic;

	private ArrayList<ArrayList<BluetoothGattCharacteristic>> gattCharacteristicListList =
			new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

	/**bluetooth 장치로 읽은 charateristic을 분석합니다.
	 * <br>
	 * @param gattServices
	 */
	private void getGattServices(List<BluetoothGattService> gattServices) {
		if (gattServices == null) return;

		String uuid = null;
		modelNoCharacteristic	= null;
		serialPortCharacteristic	= null;
		gattCharacteristicListList		= new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

		// Loops through available GATT Services.
		for (BluetoothGattService gattService : gattServices) {
			uuid = gattService.getUuid().toString();
			System.out.println("displayGattServices + uuid="+uuid);

			List<BluetoothGattCharacteristic> sysGattCharacteristicList = gattService.getCharacteristics();
			ArrayList<BluetoothGattCharacteristic> gattCharacteristicsList = new ArrayList<BluetoothGattCharacteristic>();

			// Loops through available Characteristics.
			for (BluetoothGattCharacteristic sysGattCharacteristic : sysGattCharacteristicList) {
				gattCharacteristicsList.add(sysGattCharacteristic);

				uuid = sysGattCharacteristic.getUuid().toString();

				distributeUUIDtoCharacteristic(uuid, sysGattCharacteristic);
			}

			gattCharacteristicListList.add(gattCharacteristicsList);
		}

		if (modelNoCharacteristic == null || serialPortCharacteristic == null) {
			//			Toast.makeText(mainContext, "Please select DFRobot devices",Toast.LENGTH_SHORT).show();
			connectionState = connectionStateEnum.isToScan;
			onConnectionStateChange(connectionState);
		} else {
			StaticCharacteristic=modelNoCharacteristic;
			setCharacteristicNotification(StaticCharacteristic, true);
			readCharacteristic(StaticCharacteristic);
		}
	}

	/*////////// bluetooth functions //////////*/

////////////////////activity에서 구현되었어야 할 함수들////////////////////////////

	/**이 service의 작동 state를 update합니다.
	 * @param message
	 */
	public void onConnectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
		switch (theConnectionState) {											//Four connection state
		case isConnected:
			//			scanBtn.setText("Connected");
			//			sendMessage(INITIALIZE_CONNECTION);
			break;
		case isConnecting:
			//			scanBtn.setText("Connecting");
			break;
		case isToScan:
			//			scanBtn.setText("Scan");
			break;
		case isScanning:
			//			scanBtn.setText("Scanning");
			break;
		case isDisconnecting:
			//			scanBtn.setText("isDisconnecting");
			break;
		default:
			break;
		}
	}
	
	public void onReceiveMessage(String message){
		System.out.println("message : " + message);
	}
	
	private Handler scanHandler;

////////////////////activity에서 구현되었어야 할 함수들 끝////////////////////////////
	private static final String ACTION_STR	= "android.bluetooth.adapter.action.DISCOVERY_STARTED";
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		System.out.println("dhowy?");
		
		pendingService(intent, startId);

		final String action	= intent.getAction();

		//실행할 때
		if(action == null){
			System.out.println("start");
			initializeBluetooth();
		//안드로이드가 부팅되었을 때
		}else if(action.equals(ACTION_BOOT_COMPLETED)){
			initializeBluetooth();
			//connect를 성공하면 시간제한을 해제합니다.
		}else if (action.equals(ACTION_GATT_CONNECTED)) {
			handler.removeCallbacks(connectTimeLimitRunnable);
			//disconnect를 성공하면 시간제한을 해제합니다.
		} else if (action.equals(ACTION_GATT_DISCONNECTED)) {
			connectionState = connectionStateEnum.isToScan;
			onConnectionStateChange(connectionState);
			handler.removeCallbacks(disonnectTimeLimitRunnable);
			shutdownService();
			//BLE에서 service가 있음이 확인되면 서비스 내역을 수신받아서 저장합니다.
		} else if (action.equals(ACTION_GATT_SERVICES_DISCOVERED)) {
			// Show all the supported services and characteristics on the user interface.
			for (BluetoothGattService gattService : getSupportedGattServices()) 
				System.out.println("ACTION_GATT_SERVICES_DISCOVERED  " + gattService.getUuid().toString());

			getGattServices(getSupportedGattServices());
			//BLE에서 data를 송신하면 알맞게 수신합니다. 
		} else if (action.equals(ACTION_DATA_AVAILABLE)) {
			if(StaticCharacteristic == modelNoCharacteristic){
				if (intent.getStringExtra(ACTION_EXTRA_DATA).toUpperCase().startsWith("DF BLUNO")) {
					setCharacteristicNotification(StaticCharacteristic, false);

					StaticCharacteristic	= serialPortCharacteristic;

					setCharacteristicNotification(StaticCharacteristic, true);

					connectionState = connectionStateEnum.isConnected;

					onConnectionStateChange(connectionState);
				}else {
					connectionState = connectionStateEnum.isToScan;

					onConnectionStateChange(connectionState);
				}
			} else if (StaticCharacteristic == serialPortCharacteristic) {
				onReceiveMessage(intent.getStringExtra(ACTION_EXTRA_DATA));
			}

			System.out.println("displayData "+intent.getStringExtra(ACTION_EXTRA_DATA));
		}else if(action.equals(ACTION_STR)){
			System.out.println("bluetooth ready");
			initializeBluetooth();
		}else{
			System.out.println("이거라는데? " + action);
		}

		//서비스가 강제로 종료되어도 이전의 intent를 그대로 유지하여 재시작합니다.
		return START_REDELIVER_INTENT;
	}
	
	/**service가 System Kernel에 의해 죽지 않도록 pending합니다.
	 * @param itt service가 마지막으로 받은 intent
	 * @param startID service에게 보낸 intent id...일종의 PID와 비슷한 건데, intent마다 부여되는 id입니다.
	 */
	private void pendingService(Intent itt, int startID){
		PendingIntent pendingIntent	= PendingIntent.getBroadcast(this, 1, itt, 0);
		Notification notification	= new Notification();
		startForeground(startID, notification);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}