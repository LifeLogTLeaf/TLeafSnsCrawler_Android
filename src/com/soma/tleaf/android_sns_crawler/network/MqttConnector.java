/**
 * 
 */
package com.soma.tleaf.android_sns_crawler.network;

import java.util.ArrayList;
import java.util.List;

import com.soma.tleaf.android_sns_crawler.core.AppContext;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import android.util.Log;

/**
 * Created with Eclipse IDE
 * Author : RichardJ
 * Date : Oct 27, 2014 1:19:59 PM
 * Description :
 */
public class MqttConnector implements Connector, MqttCallback {
    private static final String TAG = MqttConnector.class.getSimpleName();
    private static final String URL = "tcp://14.63.171.66:1883";
    private final static String TOPIC = "user/call";

    public List<Reciever> recievers;
	private volatile static MqttConnector connector;
	private MqttClient client;

	public MqttConnector() {
		recievers = new ArrayList<Reciever>();
	}

	public static MqttConnector getInstance() {
		if (connector == null) {
			synchronized (MqttConnector.class) {
				if (connector == null) {
					connector = new MqttConnector();
				}
			}
		}
		return connector;
	}

    /**
     * 커넥션 옵션 설정부분 생략됬습니다.
     * */
    @Deprecated
    public boolean connect() {
		try {
            Log.i(TAG, "브로커 서버 연결중");
            client = new MqttClient(URL, AppContext.getDeviceId(), new MemoryPersistence());
			client.connect();
			client.setCallback(this);
            Log.i(TAG, "브로커 서버 연결상태 : "+ client.isConnected());

            //연결과 동시에 토픽을 구독한다.
            setSubscribe();
            return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean subscribe(String topic) {
		if (client == null)
			return false;

		try {
			client.subscribe(topic);
			return true;
		} catch (MqttException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean publish(String message, String topic) {
		if (client == null)
			return false;
		MqttMessage mqttMessage = new MqttMessage(message.getBytes());

		try {
			client.publish(topic, mqttMessage);
			return true;
		} catch (MqttException e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean disconnect() {
		try {
			client.disconnect();
			return true;
		} catch (MqttException e) {
			e.printStackTrace();
			return false;
		}
	}

    public void setSubscribe(){
        subscribe(TOPIC);
    }

	@Override
	public boolean registerReciever(Reciever reciever) {
		Log.i(TAG, "리시버가 등록되었습니다 : " + reciever.getClass().getSimpleName());
		return recievers.add(reciever);
	}

	@Override
	public boolean unRegisterReciever(Reciever reciever) {
		Log.i(TAG, "리시버가 해제되었습니다. : " + reciever.getClass().getSimpleName());
		return recievers.remove(reciever);
	}

	@Override
	public void notifyReciever(String type, String data) {
		for (Reciever reciever : recievers) {
			reciever.onRecieve(type, data);
		}
	}

    @Override
	public void connectionLost(Throwable cause) {
	    Log.i(TAG, "연결 유실..");
    }

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {

	}

	@Override
	public void messageArrived(String topic, MqttMessage msg) throws Exception {
        notifyReciever(topic, msg.toString());
	}

}
