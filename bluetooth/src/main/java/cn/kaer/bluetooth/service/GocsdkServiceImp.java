package cn.kaer.bluetooth.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.util.Log;

import com.goodocom.gocsdk.IGocsdkCallback;
import com.goodocom.gocsdk.IGocsdkService;


import cn.kaer.bluetooth.command.CommandSend;

//发送指令集合
public class GocsdkServiceImp extends IGocsdkService.Stub {
	private GocsdkService service;
	public  GocsdkServiceImp(GocsdkService service){
		this.service = service;
	}
	private void write(String str){
		service.write(str);
	}
	
	@Override
	public void restBluetooth() throws RemoteException {
		write(CommandSend.RESET_BLUE);
	}
	
	@Override
	public void getLocalName() throws RemoteException {
		write(CommandSend.MODIFY_LOCAL_NAME);
	}

	@Override
	public void setLocalName(String name) throws RemoteException {
		write(CommandSend.MODIFY_LOCAL_NAME+name);
	}

	@Override
	public void getPinCode() throws RemoteException {
		write(CommandSend.MODIFY_PIN_CODE);
	}

	@Override
	public void setPinCode(String pincode) throws RemoteException {
		write(CommandSend.MODIFY_PIN_CODE+pincode);
	}
	
	@Override
	public void getLocalAddress() throws RemoteException {
		write(CommandSend.LOCAL_ADDRESS);
	}
	
	@Override
	public void getAutoConnectAnswer() throws RemoteException {
		write(CommandSend.INQUIRY_AUTO_CONNECT_ACCETP);
	}
	
	@Override
	public void setAutoConnect() throws RemoteException {
		write(CommandSend.SET_AUTO_CONNECT_ON_POWER);
	}
	
	@Override
	public void cancelAutoConnect() throws RemoteException {
		write(CommandSend.UNSET_AUTO_CONNECT_ON_POWER);
	}
	
	@Override
	public void setAutoAnswer() throws RemoteException {
		write(CommandSend.SET_AUTO_ANSWER);
	}
	
	@Override
	public void cancelAutoAnswer() throws RemoteException {
		write(CommandSend.UNSET_AUTO_ANSWER);
	}
	
	@Override
	public void getVersion() throws RemoteException {
		write(CommandSend.INQUIRY_VERSION_DATE);
	}

//connect
	@Override
	public void setPairMode() throws RemoteException {
		write(CommandSend.PAIR_MODE);
	}
	
	@Override
	public void cancelPairMode() throws RemoteException {
		write(CommandSend.CANCEL_PAIR_MOD);
	}
	
	@Override
	public void connectLast() throws RemoteException {
		write(CommandSend.CONNECT_DEVICE);
	}
	
	@Override
	public void connectA2dp(String addr) throws RemoteException {
		write(CommandSend.CONNECT_A2DP+addr);
	}

	@Override
	public void connectHFP(String addr) throws RemoteException {
		write(CommandSend.CONNECT_HFP+addr);
	}
	
	@Override
	public void connectHid(String addr) throws RemoteException {
		write(CommandSend.CONNECT_HID);
	}
	
//	@Override
//	public void connectSpp(String addr) throws RemoteException{
//		write(Commands.CONNECT_SPP_ADDRESS);
//	}

	@Override
	public void disconnect(String addr) throws RemoteException {
		write(CommandSend.DISCONNECT_DEVICE + addr);
	}

	@Override
	public void disconnectA2DP() throws RemoteException {
		write(CommandSend.DISCONNECT_A2DP);
	}

	@Override
	public void disconnectHFP() throws RemoteException {
		write(CommandSend.DISCONNECT_HFP);
	}
	
	@Override
	public void disconnectHid(){
		write(CommandSend.DISCONNECT_HID);
	}

//devices list
	@Override
	public void deletePair(String addr) throws RemoteException {
		write(CommandSend.DELETE_PAIR_LIST+addr);
	}

	@Override
	public void startDiscovery() throws RemoteException {
		write(CommandSend.START_DISCOVERY);
	}

	@Override
	public void getPairList() throws RemoteException {
		write(CommandSend.INQUIRY_PAIR_RECORD);
	}

	@Override
	public void stopDiscovery() throws RemoteException {
		write(CommandSend.STOP_DISCOVERY);
	}

//hfp	
	@Override
	public void phoneAnswer() throws RemoteException {
		write(CommandSend.ACCEPT_INCOMMING);
	}

	@Override
	public void phoneHangUp() throws RemoteException {
		write(CommandSend.REJECT_INCOMMMING);
	}

	@Override
	public void phoneDail(String phonenum) throws RemoteException {
		write(CommandSend.DIAL+phonenum);
	}

	@Override
	public void phoneTransmitDTMFCode(char code) throws RemoteException {
		write(CommandSend.DTMF+code);
	}
	
	@Override
	public void phoneTransfer() throws RemoteException {
		write(CommandSend.VOICE_TRANSFER);
	}

	@Override
	public void phoneTransferBack() throws RemoteException {
		write(CommandSend.VOICE_TO_BLUE);
	}
	
	@Override
	public void phoneVoiceDail() throws RemoteException {
		write(CommandSend.VOICE_DIAL);
	}
	
	@Override
	public void cancelPhoneVoiceDail() throws RemoteException {
		write(CommandSend.CANCEL_VOID_DIAL);
	}

//CONTACTS
	@Override
	public void phoneBookStartUpdate() throws RemoteException {
		write(CommandSend.SET_PHONE_PHONE_BOOK);
	}

	@Override
	public void callLogstartUpdate(int type) throws RemoteException {
		switch (type) {
		case 1:
			write(CommandSend.SET_OUT_GOING_CALLLOG);
			Log.e("app","callLogstartUpdate+IND_SET_OUT_GOING_CALLLOG:"+CommandSend.SET_OUT_GOING_CALLLOG);
			Log.e("fjasmin:","--------------------"+CommandSend.SET_OUT_GOING_CALLLOG);
			break;
		case 2:			
			write(CommandSend.SET_MISSED_CALLLOG);
			Log.e("app","callLogstartUpdate+IND_SET_MISSED_CALLLOG:"+CommandSend.SET_MISSED_CALLLOG);
			Log.e("fjasmin:","--------------------"+CommandSend.SET_MISSED_CALLLOG);
			break;
		case 3:
			write(CommandSend.SET_INCOMING_CALLLOG);
			Log.e("app","callLogstartUpdate+IND_SET_MISSED_CALLLOG:"+CommandSend.SET_INCOMING_CALLLOG);
			Log.e("fjasmin:","--------------------"+CommandSend.SET_INCOMING_CALLLOG);
		break;
		default:
			break;
		}
	}

	@Override
	public void musicPlayOrPause() throws RemoteException {
		write(CommandSend.QUIRY_MUSIC_PLAYING);
	}

    @Override
    public void musiclist() throws RemoteException {
        write(CommandSend.INQUIRY_MUSIC_LIST);
    }

    //獲取指定列表
	@Override
	public void musicIntoList(String index) throws RemoteException {
		Log.e(CommandSend.INQUIRY_MUSIC_INTER,index);
		write(CommandSend.INQUIRY_MUSIC_INTER+index);
	}

    @Override
    public void musicIntoPre() throws RemoteException {
        Log.e(" fjasmin 前进列表",CommandSend.INQUIRY_MUSIC_PRE);
        write(CommandSend.INQUIRY_MUSIC_PRE);
    }

	@Override
	public void musicPlayNow(String index) throws RemoteException {
		Log.e("fjasmin",CommandSend.INQUIRY_MUSIC_IN );
		write(CommandSend.INQUIRY_MUSIC_IN+(index));
	}


	@Override
	public void getOnMusicCover(String index) throws RemoteException {
		Log.e("fjasmin",CommandSend.SEND_MUSIC_COVER_SUCEESS);
		write(CommandSend.SEND_MUSIC_COVER_SUCEESS+(index));
	}

	@Override
	public void getBtXchange(String addr) throws RemoteException {
		Log.e("fjasmin",CommandSend.INQUIRY_BT_XCHANGE+":"+addr);

		write(CommandSend.INQUIRY_BT_XCHANGE + addr);
		SharedPreferences sharedPreferences = service.getSharedPreferences("main", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("address",addr);
		editor.commit();
	}

	@Override
	public void setTestMode(String index) throws RemoteException {
		write(CommandSend.TEST_MODEL + index);
	}

	@Override
	public void testModeFinish(String index) throws RemoteException {
		write(CommandSend.TEST_MODEL_FINISH + index + "OVER");
	}

	@Override
	public void getImeiInfo() throws RemoteException {
		write(CommandSend.QUERY_IMEI_INFO);
	}

	@Override
	public void getPlaySoft() throws RemoteException {
		write(CommandSend.QUERY_CURRENT_PLAY_SOFT);
	}


	@Override
	public void musicStop() throws RemoteException {
		write(CommandSend.STOP_MUSIC);
	}

	@Override
	public void musicPrevious() throws RemoteException {
		Log.d("app","write musicprevious");
		write(CommandSend.PREV_SOUND);
	}

	@Override
	public void musicNext() throws RemoteException {
		write(CommandSend.NEXT_SOUND);
	}

	@Override
	public void musicMute() throws RemoteException {
		write(CommandSend.MUSIC_MUTE);
	}
	
	@Override
	public void musicUnmute() throws RemoteException {
		write(CommandSend.MUSIC_UNMUTE);
	}

	@Override
	public void musicBackground() throws RemoteException {
		write(CommandSend.MUSIC_BACKGROUND);
	}

	@Override
	public void musicNormal() throws RemoteException {
		write(CommandSend.MUSIC_NORMAL);
	}	
	
	@Override
	public void registerCallback(IGocsdkCallback callback)
			throws RemoteException {
		service.registerCallback(callback);
	}

	@Override
	public void unregisterCallback(IGocsdkCallback callback)
			throws RemoteException {
		service.unregisterCallback(callback);
	}
	@Override
	public void hidMouseMove(String point) throws RemoteException {
		write(CommandSend.MOUSE_MOVE+ point);
	}
	@Override
	public void hidMouseUp(String point) throws RemoteException {
		write(CommandSend.MOUSE_MOVE +point);
	}
	@Override
	public void hidMousDown(String point) throws RemoteException {
		write(CommandSend.MOUSE_DOWN +point);
	}
	@Override
	public void hidHomeClick() throws RemoteException {
		write(CommandSend.MOUSE_HOME);
	}
	@Override
	public void hidBackClick() throws RemoteException {
		write(CommandSend.MOUSE_BACK);
	}
	@Override
	public void hidMenuClick() throws RemoteException {
		write(CommandSend.MOUSE_MENU);
	}

	@Override
	public void connenctSpp(String address) throws RemoteException {
		write(CommandSend.CONNECT_SPP + address);
	}

	@Override
	public void sendSppData(String index, String data) throws RemoteException {
		write(CommandSend.SEND_SPP_DATA+index + data);
	}

	@Override
	public void disconnectSpp(String index) throws RemoteException {
		write(CommandSend.DISCONNECT_SPP+index);
	}
	
	@Override
	public void getMusicInfo(){
		write(CommandSend.INQUIRY_MUSIC_INFO);
	}
	
	@Override
	public void inqueryHfpStatus() {
		write(CommandSend.INQUIRY_HFP_STATUS);
	}
	
	@Override
	public void getCurrentDeviceAddr(){
		write(CommandSend.INQUIRY_CUR_BT_ADDR);
	}
	@Override
	public void getCurrentDeviceName() {
		write(CommandSend.INQUIRY_CUR_BT_NAME);
	}
	@Override
	public void connectDevice(String addr) throws RemoteException {
		write(CommandSend.CONNECT_DEVICE + addr);
	}
	@Override
	public void setProfileEnabled(boolean[] enabled) throws RemoteException {
		String str = "";
		for(int i=0;(i<enabled.length) && (i<10);i++){
			if(enabled[i])str += "1";
			else str += "0";
		}
		int len = str.length();
		for (int i = 0; i < 10-len; i++) {
			str += "0";
		}
		write(CommandSend.SET_PROFILE_ENABLED+str);
	}
	@Override
	public void getProfileEnabled() throws RemoteException {
		write(CommandSend.SET_PROFILE_ENABLED);
	}
	
	@Override
	public void getMessageInboxList() throws RemoteException {
		write(CommandSend.GET_MESSAGE_INBOX_LIST);
	}
	
	@Override
	public void getMessageText(String handle) throws RemoteException {
		write(CommandSend.GET_MESSAGE_TEXT + handle);
	}
	@Override
	public void getMessageSentList() throws RemoteException {
		write(CommandSend.GET_MESSAGE_SENT_LIST);
	}
	@Override
	public void getMessageDeletedList() throws RemoteException {
		write(CommandSend.GET_MESSAGE_DELETED_LIST);
	}
	@Override
	public void pauseDownLoadContact() throws RemoteException {
		write(CommandSend.PAUSE_PHONEBOOK_DOWN);
	}
	@Override
	public void connectA2dpp() throws RemoteException {
		write(CommandSend.CONNECT_A2DP);
	}
	@Override
	public void musicPlay() throws RemoteException {
		write(CommandSend.PLAY_MUSIC);
	}
	@Override
	public void musicPause() throws RemoteException {
		write(CommandSend.PAUSE_MUSIC);
	}
	@Override
	public void pairedDevice(String addr) throws RemoteException {
		write(CommandSend.START_PAIR+addr);
	}
	@Override
	public void muteOpenAndClose(int status) throws RemoteException {
		write(CommandSend.MIC_OPEN_CLOSE+status);
	}
	@Override
	public void openBlueTooth() throws RemoteException {
		write(CommandSend.OPEN_BT);
	}
	@Override
	public void closeBlueTooth() throws RemoteException {
		write(CommandSend.CLOSE_BT);
	}
	@Override
	public void inqueryA2dpStatus() throws RemoteException {
		write(CommandSend.INQUIRY_A2DP_STATUS);
	}


}