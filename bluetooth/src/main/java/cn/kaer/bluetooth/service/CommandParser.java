package cn.kaer.bluetooth.service;

import android.content.Context;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.goodocom.gocsdk.AppHelper;

import cn.kaer.bluetooth.GocSdkController;
import cn.kaer.bluetooth.R;
import cn.kaer.bluetooth.callback.GocsdkCallbackImp;
import cn.kaer.bluetooth.callback.OnGocsdkCallback;
import cn.kaer.bluetooth.command.CommandIND;

//接收指令合集
public class CommandParser {
    private static final String TAG = "CommandParser";

    //	private RemoteCallbackList<IGocsdkCallback> callbacks;
    private GocsdkCallbackImp mGocsdkCallbackImp;
    private Context mContext;

    private byte[] serialBuffer = new byte[1024];
    private int count = 0;


    public CommandParser(/*RemoteCallbackList<IGocsdkCallback> callbacks,*/ GocsdkService gocsdkService) {
//		this.callbacks = callbacks;
        mGocsdkCallbackImp = new GocsdkCallbackImp(gocsdkService);
        mContext = gocsdkService;

    }

    public GocsdkCallbackImp getGocsdkCallbackImp() {
        return mGocsdkCallbackImp;
    }


    public void setOnDeviceSearchCallback(OnGocsdkCallback.OnDeviceSearchCallback onDeviceSearchCallback, boolean isRegister) {

        mGocsdkCallbackImp.setOnDeviceSearchCallback(onDeviceSearchCallback, isRegister);
    }

    public void setonDeviceConnectCallback(OnGocsdkCallback.OnDeviceConnectCallback onDeviceConnCallback, boolean isRegister) {
        mGocsdkCallbackImp.setonDeviceConnectCallback(onDeviceConnCallback, isRegister);
    }

    public void setOnCallStateCallback(OnGocsdkCallback.OnBleCallStateListen onBleCallStateListen, boolean isRegister) {
        mGocsdkCallbackImp.setOnCallStateCallback(onBleCallStateListen, isRegister);

    }

    private void onSerialCommand(String cmd) {
        Log.e("serial", "onSerialCommand: " + cmd);
//		if (callbacks.getRegisteredCallbackCount() == 0) {
//			handleWithoutCallback(cmd);
//			return;
//		}//todo

//		int i = callbacks.beginBroadcast();
////		//强制赋值 不让 callback 多次注册
//		if ( i > 1 ){
//			i = 1;
//		}
//		Log.e("app","onSerialCommand callbacks.getRegisteredCallbackCount() :" + callbacks.getRegisteredCallbackCount());
//		Log.e("app","onSerialCommand callbacks.beginBroadcast() :" + i);


//		while (i > 0) {
//			i--;
//			IGocsdkCallback cbk = callbacks.getBroadcastItem(i);
        try {
            if (cmd.startsWith(CommandIND.IND_HFP_CONNECTED)) {
              /*  if (cmd.length() < 2) return;
                String address = cmd.substring(2);
                if (!TextUtils.isEmpty(AppHelper.getInstance().mConnectAddress[0]) &&
                        !TextUtils.isEmpty(AppHelper.getInstance().mConnectAddress[1]) &&
                        !address.equals(AppHelper.getInstance().mConnectAddress[0])
                        && !address.equals(AppHelper.getInstance().mConnectAddress[1])) {
                    Toast.makeText(mContext, R.string.connect_full, Toast.LENGTH_SHORT).show();
                    return;
                }*/
                mGocsdkCallbackImp.onHfpConnected("");//hfp连接成功
            } else if (cmd.startsWith(CommandIND.IND_HFP_DISCONNECTED)) {
                String address = null;
                if (cmd.length() >= 14) {
                    address = cmd.substring(2, 14);
                }
                mGocsdkCallbackImp.onHfpDisconnected(address);
            } else if (cmd.startsWith(CommandIND.IND_CALL_SUCCEED)) {
                if (cmd.length() < 4) {
                    mGocsdkCallbackImp.onCallSucceed("");
                } else {
                    mGocsdkCallbackImp.onCallSucceed(cmd.substring(2));
                }
            } else if (cmd.startsWith(CommandIND.IND_CHANGE_BT)) {
                if (cmd.length() < 3) {
                    return;
                }
                String address = cmd.substring(2);
                mGocsdkCallbackImp.onChangeBt(address);

            } else if (cmd.startsWith(CommandIND.IND_INCOMING)) {

                if (cmd.length() <= 2) {
                    mGocsdkCallbackImp.onIncoming("");
                } else {
                    mGocsdkCallbackImp.onIncoming(cmd.substring(2));
                }
            } else if (cmd.startsWith(CommandIND.IND_HANG_UP)) {
                mGocsdkCallbackImp.onHangUp();
            } else if (cmd.startsWith(CommandIND.IND_TALKING)) {// 通话中:::IG[number]
                Log.d("app", "IND_TALKING--------" + cmd);
                if (cmd.length() <= 2) {
                    mGocsdkCallbackImp.onTalking("");
                } else {
                    mGocsdkCallbackImp.onTalking(cmd.substring(2));
                }
            } else if (cmd.startsWith(CommandIND.IND_RING_START)) {// 开始响铃
                mGocsdkCallbackImp.onRingStart();
            } else if (cmd.startsWith(CommandIND.IND_RING_STOP)) {// 停止响铃
                mGocsdkCallbackImp.onRingStop();
            } else if (cmd.startsWith(CommandIND.IND_HF_LOCAL)) {//
                mGocsdkCallbackImp.onHfpLocal();
            } else if (cmd.startsWith(CommandIND.IND_HF_REMOTE)) {// 蓝牙接听
                mGocsdkCallbackImp.onHfpRemote();
            } else if (cmd.startsWith(CommandIND.IND_IN_PAIR_MODE)) {// 进入配对模式:::II
                mGocsdkCallbackImp.onInPairMode();
            } else if (cmd.startsWith(CommandIND.IND_EXIT_PAIR_MODE)) {// 退出配对模式
                mGocsdkCallbackImp.onExitPairMode();
            } else if (cmd.startsWith(CommandIND.IND_INIT_SUCCEED)) {// 上电初始化成功:::IS
                String init = cmd.substring(2);
                mGocsdkCallbackImp.onInitSucceed(init);
            } else if (cmd.startsWith(CommandIND.IND_MUSIC_PLAYED)) {// 音乐播放
                Log.d(TAG, "callback Commands playing" + cmd);
                mGocsdkCallbackImp.onMusicPlaying();


                if (GocSdkController.get().getGocSdkService() != null) {
                    GocSdkController.get().getGocSdkService().getPlaySoft();
                }

            } else if (cmd.startsWith(CommandIND.IN_MUSIC_PAUSE)) {// 音乐停止
                Log.d(TAG, "callback Commands stoped" + cmd);
                mGocsdkCallbackImp.onMusicStopped();
            } else if (cmd.startsWith(CommandIND.IND_VOICE_CONNECTED)) {//语音连接建立
                // cbk.onVoiceConnected();
            } else if (cmd.startsWith(CommandIND.IND_VOICE_DISCONNECTED)) {//语音连接断开
                // cbk.onVoiceDisconnected();
            } else if (cmd.startsWith(CommandIND.IND_AUTO_CONNECT_ACCEPT)) {//开机自动连接,来电自动接听当前配置
                if (cmd.length() < 4) {
                    Log.e(TAG, cmd + "=====error command");
                } else {
                    mGocsdkCallbackImp.onAutoConnectAccept(cmd.substring(2, 4));
                }
            } else if (cmd.startsWith(CommandIND.IND_CURRENT_ADDR)) {
                if (cmd.length() < 3) {
                    Log.e(TAG, cmd + "==== error command");
                } else {
                    mGocsdkCallbackImp.onCurrentAddr(cmd.substring(2));
                }
            } else if (cmd.startsWith(CommandIND.IND_CURRENT_NAME)) {
                //获取已经连接蓝牙设备的名称。
                if (cmd.length() < 3) {
                    Log.e(TAG, cmd + "==== error command");
                } else {
                    mGocsdkCallbackImp.onCurrentName(cmd.substring(2));
                }
            } else if (cmd.startsWith(CommandIND.IND_AV_STATUS)) {
                if (cmd.length() < 3) {
                    Log.e(TAG, cmd + "=====error");
                } else {
                    mGocsdkCallbackImp.onAvStatus(Integer.parseInt(cmd.substring(2, 3)));
                }
            } else if (cmd.startsWith(CommandIND.IND_HFP_STATUS)) {
                if (cmd.length() < 3) {
                    Log.e(TAG, cmd + " ==== error");
                } else {
                    int status = Integer.parseInt(cmd.substring(2, 3));
                    mGocsdkCallbackImp.onHfpStatus(status);
                }
            } else if (cmd.startsWith(CommandIND.IND_VERSION_DATE)) {
                Log.e(TAG,"rec version date");
                if (cmd.length() < 3) {
                    Log.e(TAG, cmd + "====error");
                } else {
                    mGocsdkCallbackImp.onVersionDate(cmd.substring(2));
                }
            } else if (cmd.startsWith(CommandIND.IND_CURRENT_DEVICE_NAME)) {
                if (cmd.length() < 3) {
                    Log.e(TAG, cmd + "====error");
                } else {
                    mGocsdkCallbackImp.onCurrentDeviceName(cmd.substring(2));
                }
            } else if (cmd.startsWith(CommandIND.IND_CURRENT_PIN_CODE)) {
                if (cmd.length() < 3) {
                    Log.e(TAG, cmd + "====error");
                } else {
                    mGocsdkCallbackImp.onCurrentPinCode(cmd.substring(2));
                }
            } else if (cmd.startsWith(CommandIND.IND_A2DP_CONNECTED)) {
                mGocsdkCallbackImp.onA2dpConnected();
            } else if (cmd.startsWith(CommandIND.IND_A2DP_DISCONNECTED)) {
                mGocsdkCallbackImp.onA2dpDisconnected();
            } else if (cmd.startsWith(CommandIND.IND_CURRENT_AND_PAIR_LIST)) {


                if (cmd.length() < 15) {
                    Log.e(TAG, cmd + "====error");
                } else if (cmd.length() == 15) {
                    mGocsdkCallbackImp.onCurrentAndPairList(Integer.parseInt(cmd.substring(2, 3)), "", cmd.substring(3, 15));
                } else {
                    mGocsdkCallbackImp.onCurrentAndPairList(Integer.parseInt(cmd.substring(2, 3)), cmd.substring(15),
                            cmd.substring(3, 15));
                }
            /*    *//**
                 * 当前设备名称和配对记录
                 *//*


                if (cmd.length() < 20) {
                    Log.e(TAG, cmd + "====error");
                } else {
                    int cmd_index = Integer.parseInt(cmd.substring(2, 3));
                    String cmd_addr = cmd.substring(3, 15);
                    String cmd_cod = cmd.substring(15, 21);
                    int hex_uuid = Integer.parseInt(cmd.substring(21, 23));
                    *//*String cmd_name = cmd.substring((hex_uuid*4+23));*//*
                    int begin_index = hex_uuid * 4 + 23;
                    int end_index = hex_uuid * 4 + 31;
                    String cmd_type = cmd.substring(begin_index, end_index);
                    String cmd_name = cmd.substring(end_index);
                    *//*Log.e("fjasmin",cmd_addr +" :"+cmd_cod+":"+cmd_name+":"+cmd_type);*//*

                    // 数据cbk.onCurrentAndPairList(cmd_index,cmd_addr,cmd_name);
                    mGocsdkCallbackImp.onCurrentAndPairList(cmd_index, cmd_name, cmd_addr);
                }*/
            } else if (cmd.startsWith(CommandIND.IND_PHONE_BOOK)) {// 联系人信息
                if (cmd.length() < 6) {
                    Log.e(TAG, cmd + "====error");
                } else {
                    String type = null;
                    String name = null;
                    String number = null;
                    if (cmd.contains("[PB]")) {
                        String[] split = cmd.split("\\[FF\\]");
                        if (split.length == 2) {
                            name = split[0].substring(2);
                            number = split[1];
                        }
                    } else {
                        int typeint = Integer.parseInt(cmd.substring(2, 3));

                        int nameLen = Integer.parseInt(cmd.substring(3, 5));//联系人姓名的长度

                        int numLen = Integer.parseInt(cmd.substring(5, 7));//联系人电话号码的长度

                        byte[] bytes = cmd.getBytes();
                        if (nameLen > 0) {
                            byte[] buffer = new byte[nameLen];
                            System.arraycopy(bytes, 7, buffer, 0, nameLen);
                            name = new String(buffer);
                            /*Log.e("fjasmin name",name);*/
                        } else {
                            name = "";
                        }
                        if (numLen > 0) {
                            if ((7 + nameLen + numLen) == bytes.length) {
                                byte[] buffer = new byte[numLen];
                                System.arraycopy(bytes, 7 + nameLen, buffer, 0, numLen);
                                number = new String(buffer);
                                /*Log.e("fjasmin number",number)*/
                                ;

                            } else {
                                Log.e("goc", "PhoneBook bytes length is err!");
                            }

                        } else {
                            number = "";
                        }
                    }
                    if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(number)) {
                        mGocsdkCallbackImp.onPhoneBook(name, number);
                    }
                }
            } else if (cmd.startsWith(CommandIND.IND_PHONE_BOOK_DONE)) {
                mGocsdkCallbackImp.onPhoneBookDone();
            } else if (cmd.startsWith(CommandIND.IND_SIM_DONE)) {
                mGocsdkCallbackImp.onSimDone();
            } else if (cmd.startsWith(CommandIND.IND_CALLLOG_DONE)) {
                mGocsdkCallbackImp.onCalllogDone();
            } else if (cmd.startsWith(CommandIND.IND_CALLLOG)) {
                if (cmd.length() < 4) {
                    Log.e(TAG, cmd + "====error");
                } else {
                    String[] split = cmd.substring(3).split("\\[FF\\]");
                    String phone_name = split[0];
                    String phone_number = split[1];
                    Log.e("cmd", "IND_CALLLOG: " + cmd);
                    int phone_type = Integer.parseInt(cmd.substring(2, 3));

                    mGocsdkCallbackImp.onCalllog(phone_type, phone_name, phone_number);
                    Log.e("app", "返回的通话记录: phone_number:" + phone_name + ":" + "phone_number" + phone_name + "phone_type:" + phone_type);
                }
            } else if (cmd.startsWith(CommandIND.IND_DISCOVERY)) {
                Log.e("fjasmin", cmd);

                //uuid的位数
                // int hex_uuid = Integer.parseInt(cmd.substring(22, 24));
                //        String name_hex = cmd.substring(hex_uuid * 4 + 24);
                /*Log.e("fjasmin+name_hex : ",name_hex);*/
                if (cmd.length() < 14) {
                    Log.e(TAG, cmd + "===error");
                } else if (cmd.length() == 14) {
                    mGocsdkCallbackImp.onDiscovery("", "", cmd.substring(2, 14));
                } else {
                    mGocsdkCallbackImp.onDiscovery("", cmd.substring(14), cmd.substring(2, 14));
                }
            } else if (cmd.startsWith(CommandIND.IND_DISCOVERY_DONE)) {
                mGocsdkCallbackImp.onDiscoveryDone();
            } else if (cmd.startsWith(CommandIND.IND_LOCAL_ADDRESS)) {
                if (cmd.length() != 14) {
                }
                mGocsdkCallbackImp.onLocalAddress(cmd.substring(2));
            } else if (cmd.startsWith(CommandIND.IND_OUTGOING_TALKING_NUMBER)) {
                if (cmd.length() <= 2) {
                    mGocsdkCallbackImp.onOutGoingOrTalkingNumber("");
                } else {
                    String test_name = cmd.substring(2);
                    mGocsdkCallbackImp.onOutGoingOrTalkingNumber(cmd.substring(2));
                    Log.e("app", "IND_OUTGOING_TALKING_NUMBER: " + test_name);
                }
            } else if (cmd.startsWith(CommandIND.IND_MUSIC_INFO)) {
                if (cmd.length() <= 2) {
                    Log.e(TAG, cmd + "===error");
                } else {
                    String info = cmd.substring(2);
                    String[] arr = info.split("\\[FF\\]");
                    if (arr.length == 5) {
                        mGocsdkCallbackImp.onMusicInfo(arr[0], arr[1], "none", Integer.parseInt(arr[2]), Integer.parseInt(arr[3]),
                                Integer.parseInt(arr[4]));

                    } else if (arr.length == 6) {
                        mGocsdkCallbackImp.onMusicInfo(arr[0], arr[1], arr[2], Integer.parseInt(arr[3]), Integer.parseInt(arr[4]),
                                Integer.parseInt(arr[5]));
                    } else {
                        Log.e(TAG, cmd + "===error");
                    }
                }
            } else if (cmd.startsWith(CommandIND.IND_MUSIC_POS)) {
                if (cmd.length() != 14) {
                    Log.e(TAG, cmd + "====error");
                } else {
                    mGocsdkCallbackImp.onMusicPos(Integer.parseInt(cmd.substring(2, 8), 16), Integer.parseInt(cmd.substring(8, 14), 16));
                }
            } else if (cmd.startsWith(CommandIND.IND_PROFILE_ENABLED)) {
                if (cmd.length() < 12) {
                    Log.e(TAG, cmd + "====error");
                } else {
                    boolean[] enabled = new boolean[10];
                    for (int ii = 0; ii < 10; ii++) {
                        if (cmd.charAt(ii + 2) == '0') {
                            enabled[ii] = false;
                        } else {
                            enabled[ii] = true;
                        }
                    }
                    mGocsdkCallbackImp.onProfileEnbled(enabled);
                }
            } else if (cmd.startsWith(CommandIND.IND_MESSAGE_LIST)) {
                String text = cmd.substring(2);
                if (text.length() == 0) {
                    Log.e("goc", "cmd error:param==0" + cmd);
                } else {
                    String[] arr = text.split("\\[FF\\]", -1);
                    if (arr.length != 6) {
                        Log.e("goc", "cmd error:arr.length=" + arr.length + ";" + cmd);
                    } else {
                        mGocsdkCallbackImp.onMessageInfo(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5]);
                    }
                }
            } else if (cmd.startsWith(CommandIND.IND_MESSAGE_TEXT)) {
                mGocsdkCallbackImp.onMessageContent(cmd.substring(2));
            } else if (cmd.startsWith(CommandIND.IND_OK)) {
            } else if (cmd.startsWith(CommandIND.IND_ERROR)) {
                //音乐类型列表
            } else if (cmd.startsWith(CommandIND.IND_MUSIC_LIST_TYPE)) {
                //FID0000000000000001我的音乐
                if (cmd.length() < 19) {
                    Log.e(TAG, cmd + "=====error IND_MUSIC_LIST_TYPE command");
                } else {
                    //onMusicListState(String type,int index.String name)
                    String type = cmd.substring(2, 3);
                    String index = cmd.substring(3, 19);
                    String name = cmd.substring(19);
                    mGocsdkCallbackImp.onMusicListState(type, index, name);
                    //獲取狀態
                    Log.e("fjasmin+ 音乐列表", type + " : " + index + " : " + name);
                }
            } else if (cmd.startsWith(CommandIND.INQUIRY_MUSIC_INTER)) {
            } else if (cmd.startsWith(CommandIND.IND_MUSIC_TYPE_SUCCESS)) {//获取指定音乐类型列表成功
                Log.e("fjasmin: ", CommandIND.IND_MUSIC_TYPE_SUCCESS + "===success");
                //获取列表成功
                mGocsdkCallbackImp.onMusicListSucess();

            }
            //获取指定音乐列表失败
            else if (cmd.startsWith(CommandIND.IND_MUSIC_TYPE_FAIL)) {

                //获取列表成功
                mGocsdkCallbackImp.onMusicListFail();
            }
            //设置音乐列表成功
            else if (cmd.startsWith(CommandIND.IND_MUSIC_LIST_SUCESS)) {
                Log.e(TAG, cmd + CommandIND.IND_MUSIC_LIST_SUCESS + "===success");
                mGocsdkCallbackImp.onMusicListSettingSuccess();
            }
            //音乐列表上报失败
            else if (cmd.startsWith(CommandIND.IND_MUSIC_LIST_FAIL)) {
                Log.e(TAG, cmd + "===fail");
                mGocsdkCallbackImp.onMusicListSettingFail();
            }
            //播放音乐列表成功
            else if (cmd.startsWith(CommandIND.IND_MUSIC_PLAY_SUCESSS)) {
                Log.e(TAG, cmd + CommandIND.IND_MUSIC_PLAY_SUCESSS + "===success");
                mGocsdkCallbackImp.onMusicPlaySuccess();
            } else if (cmd.startsWith(CommandIND.IND_MUSIC_PLAY_FAIL)) {

                Log.e(TAG, cmd + "===fail");
                mGocsdkCallbackImp.onMusicListSettingFail();
            } else if (cmd.startsWith(CommandIND.IND_MUSIC_COVER_SUCEESS)) {

                Log.e(TAG, cmd + "===封面success");
                mGocsdkCallbackImp.onMusicCoverSuccess(cmd.substring(2));
            }
            //如果当前歌曲没有音乐封面，FW
            else if (cmd.startsWith(CommandIND.IND_MUSIC_COVER_SUCEESS)) {
                Log.e("fjasmin", CommandIND.IND_MUSIC_COVER_FAIL + "===fail");
                Log.e(TAG, cmd + "===封面fail");
                mGocsdkCallbackImp.onMusicCoverFail();
            } else if (cmd.startsWith(CommandIND.IND_CONTACT_ICON)) {
//					Log.e(TAG, "INQUIRY_CONTACT_ICON:" + cmd );
                if (cmd.length() < 2) {
                    Log.e("app", "INQUIRY_CONTACT_ICON == fail");
                } else {
                    String icon_path = cmd.substring(2);
                    mGocsdkCallbackImp.onContactIcon(icon_path);
//						Log.e("app"," icon_path== path: "+ icon_path);
                }

            } else if (cmd.startsWith(CommandIND.IND_CONTACT_ID)) {
//					Log.e(TAG, "INQUIRY_CONTACT_ID:"+cmd );
                if (cmd.length() < 2) {
                    Log.e("app", "INQUIRY_CONTACT_ID == fail");
                } else {
                    String icon_id = cmd.substring(2);
                    /*Log.e("app"," icon_id== id: "+ icon_id);*/
                    mGocsdkCallbackImp.onContactId(icon_id);
                }
            } else if (cmd.startsWith(CommandIND.IND_AUTO_CONNECT_ON_POWER)) {
                Log.e(TAG, "SET_AUTO_CONNECT_ON_POWER:" + cmd);
            } else if (cmd.startsWith(CommandIND.IND_CONNECT_SPP_SUCESS)) {
                Log.e("spp", "IND_CONNECT_SPP_SUCESS:: " + cmd);
                if (cmd.length() < 3) return;
                String address = cmd.substring(2, 14);
                String index = cmd.substring(14);

                mGocsdkCallbackImp.onSppConnect(index, address);
            } else if (cmd.startsWith(CommandIND.IND_SPP_DATA)) {
                Log.e("spp", "IND_SPP_DATA:: " + cmd);
                if (cmd.length() < 3) return;
                String index = cmd.substring(2, 3);
                String data = cmd.substring(3);
                mGocsdkCallbackImp.onSppData(index, data);

            } else if (cmd.startsWith(CommandIND.IND_DISCONNECT_SPP)) {
                Log.e("spp", "IND_DISCONNECT_SPP:: " + cmd);
                if (cmd.length() < 3) return;
                String address = cmd.substring(2, 14);
                String index = cmd.substring(14);
                mGocsdkCallbackImp.onSppDisconnect(index, address);
            } else if (cmd.startsWith(CommandIND.IND_IMEI_INFO)) {
                if (cmd.length() < 3) return;
                String imei = cmd.substring(2);
                if (TextUtils.isEmpty(imei)) return;
                Log.e("imei", "imei::: " + imei);
                mGocsdkCallbackImp.onIMEIInfo(imei);
            } else if (cmd.startsWith(CommandIND.IND_AVRCP_STATUS)) {

            } else if (cmd.startsWith(CommandIND.IND_CURRENT_PLAY_SOFT)) {
                if (cmd.length() < 3) return;
                String player = cmd.substring(2);
                mGocsdkCallbackImp.onPlayerSoft(player);

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
//		}

//		callbacks.finishBroadcast();
    }

    private void onByte(byte b) {
        if ('\n' == b)
            return;
        if (count >= 1000)
            count = 0;
        if ('\r' == b) {
            if (count > 0) {
                byte[] buf = new byte[count];
                System.arraycopy(serialBuffer, 0, buf, 0, count);
                onSerialCommand(new String(buf));
                count = 0;
            }
            return;
        }
        if ((b & 0xFF) == 0xFF) {
            serialBuffer[count++] = '[';
            serialBuffer[count++] = 'F';
            serialBuffer[count++] = 'F';
            serialBuffer[count++] = ']';
        } else {
            serialBuffer[count++] = b;
        }
    }

    public void onBytes(byte[] data) {
        for (byte b : data) {
            onByte(b);
        }
    }


}
