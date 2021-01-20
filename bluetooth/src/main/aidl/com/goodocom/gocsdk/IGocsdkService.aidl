package com.goodocom.gocsdk;

import com.goodocom.gocsdk.IGocsdkCallback;

interface IGocsdkService {
	

	void registerCallback(IGocsdkCallback callback);

	void unregisterCallback(IGocsdkCallback callback);

	void restBluetooth();
	

	void getLocalName();
	

	void setLocalName(String name);
	

	void getPinCode();
	

	void setPinCode(String pincode);
	

	void getLocalAddress();
	

	void getAutoConnectAnswer();
	

	void setAutoConnect();
	

	void cancelAutoConnect();
	

	void setAutoAnswer();
	

	void cancelAutoAnswer();
	

	void getVersion();


	void setPairMode();
	

	void cancelPairMode();


	void connectLast();

	void pairedDevice(String addr);

	void connectDevice(String addr);
	

	void connectA2dp(String addr);
	void connectA2dpp();


	void connectHFP(String addr);
	

	void connectHid(String addr);


	void disconnect(String address);
	

	void disconnectA2DP();


	void disconnectHFP();
	

	void disconnectHid();
	

	void deletePair(String addr);


	void startDiscovery();


	void getPairList();


	void stopDiscovery();
	

	void phoneAnswer();


	void phoneHangUp();


	void phoneDail(String phonenum);


	void phoneTransmitDTMFCode(char code);
	

	void phoneTransfer();


	void phoneTransferBack();
	

	void phoneVoiceDail();
	

	void cancelPhoneVoiceDail();
	

	void phoneBookStartUpdate();
	

	void callLogstartUpdate(int type);
	

	void musicPlayOrPause();



	void musicStop();


	void musicPrevious();


	void musicNext();
	

	void musicMute();
	

	void musicUnmute();
	

	void musicBackground();
	

	void musicNormal();
	

	void hidMouseMove(String point);
	

	void hidMouseUp(String point);
	

	void hidMousDown(String point);
	

	void hidHomeClick();
	

	void hidBackClick();
	

	void hidMenuClick(); 	
	

//	void sppSendData(String addr ,String data);
	void connenctSpp(String address);

	void sendSppData(String  index,String data);

	void disconnectSpp(String index);

	void getMusicInfo();
	

	void inqueryHfpStatus();
	
	
	void inqueryA2dpStatus();

	void getCurrentDeviceAddr();

	void getCurrentDeviceName();
	

	void pauseDownLoadContact();
	
	void musicPlay();
	void musicPause();
	void muteOpenAndClose(int status);
	
	

	void setProfileEnabled(in boolean[] enabled);
	void getProfileEnabled();
	
	void getMessageSentList();
	void getMessageDeletedList();
	void getMessageInboxList();
	void getMessageText(String handle);
	
	void openBlueTooth();
	void closeBlueTooth();


    void musiclist();


    void musicIntoList(String index);


    void musicIntoPre();


    void musicPlayNow(String index);


    void getOnMusicCover(String index);

    void getBtXchange(String addr);

    void setTestMode(String index);

    void testModeFinish(String index);

    void getImeiInfo();

    void getPlaySoft();


}