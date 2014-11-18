/**
 * 
 */
package com.soma.tleaf.android_sns_crawler.network;

/**
 * Created with Eclipse IDE
 * Author : RichardJ
 * Date : Oct 27, 2014 1:17:43 PM
 * Description :
 */
public interface Connector {
	public boolean registerReciever(Reciever reciever);
	public boolean unRegisterReciever(Reciever reciever);
	public void notifyReciever(String topic, String data);
}
