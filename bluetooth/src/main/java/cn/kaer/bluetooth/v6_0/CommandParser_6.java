package cn.kaer.bluetooth.v6_0;

import android.content.Context;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import cn.kaer.bluetooth.callback.GocsdkCallbackImp;
import cn.kaer.bluetooth.callback.OnGocsdkCallback;
import cn.kaer.bluetooth.service.GocsdkService;

public class CommandParser_6 {
    private static final String TAG = CommandParser_6.class.getSimpleName();

    private GocsdkCallbackImp mGocsdkCallbackImp;
    private Context mContext;

    public CommandParser_6(GocsdkService gocsdkService) {
        mGocsdkCallbackImp = new GocsdkCallbackImp(gocsdkService);
        mContext = gocsdkService;
    }

    private byte[] serialBuffer = new byte[1024];
    private int count = 0;

    public void setOnDeviceSearchCallback(OnGocsdkCallback.OnDeviceSearchCallback onDeviceSearchCallback, boolean isRegister) {

        mGocsdkCallbackImp.setOnDeviceSearchCallback(onDeviceSearchCallback, isRegister);
    }

    public void setonDeviceConnectCallback(OnGocsdkCallback.OnDeviceConnectCallback onDeviceConnCallback, boolean isRegister) {
        mGocsdkCallbackImp.setonDeviceConnectCallback(onDeviceConnCallback, isRegister);
    }

    public void setOnCallStateCallback(OnGocsdkCallback.OnBleCallStateListen onBleCallStateListen, boolean isRegister) {
        mGocsdkCallbackImp.setOnCallStateCallback(onBleCallStateListen, isRegister);

    }

    public void setOnContactSyncListen(OnGocsdkCallback.OnContactSyncListen onContactSyncListen, boolean isRegister) {
        mGocsdkCallbackImp.setOnContactSyncListen(onContactSyncListen, isRegister);
    }

    private void handleWithoutCallback(String cmd) {
        try {
            Log.d(TAG, "callbacks.getRegisteredCallbackCount() == 0");
            if (cmd.startsWith(Commands.IND_INCOMING)) {
                Log.d(TAG, "IND_INCOMING fromBehind!");
                mGocsdkCallbackImp.onIncoming(cmd.substring(2));
            } else if (cmd.startsWith(Commands.IND_CALL_SUCCEED)) {
                mGocsdkCallbackImp.onCallSucceed(cmd.substring(2));
            } else if (cmd.startsWith(Commands.IND_TALKING)) {
                Log.d(TAG, "IND_CALL_SUCCEED fromBehind!");
                mGocsdkCallbackImp.onTalking(cmd.substring(2));
            } else if (cmd.startsWith(Commands.IND_HFP_STATUS)) {
                Log.d(TAG, "IND_HFP_STATUS fromBehind!");
                int status = Integer.parseInt(cmd.substring(Commands.IND_HFP_STATUS.length()));
                mGocsdkCallbackImp.onHfpStatus(status);

            } else if (cmd.startsWith(Commands.IND_OUTGOING_TALKING_NUMBER)) {
                Log.d(TAG, "IND_CALL_SUCCEED fromBehind!");
                mGocsdkCallbackImp.onOutGoingOrTalkingNumber(cmd.substring(Commands.IND_OUTGOING_TALKING_NUMBER.length()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onSerialCommand(String cmd) {
	/*	if (callbacks.getRegisteredCallbackCount() == 0) {
			handleWithoutCallback(cmd);
			return;
		}
*/
        try {

            if (cmd.startsWith(Commands.IND_HFP_CONNECTED)) {
                Log.e(TAG, "IND_HFP_CONNECTED:"+cmd);
                mGocsdkCallbackImp.onHfpConnected("");
            } else if (cmd.startsWith(Commands.IND_HFP_DISCONNECTED)) {
                mGocsdkCallbackImp.onHfpDisconnected("");
            } else if (cmd.startsWith(Commands.IND_CALL_SUCCEED)) {
                Log.e(TAG,"callout___CALL SUCCESS:"+cmd);
                if (cmd.length() < 4) {
                    mGocsdkCallbackImp.onCallSucceed("");
                } else {
                    mGocsdkCallbackImp.onCallSucceed(cmd.substring(2));
                }
            } else if (cmd.startsWith(Commands.IND_INCOMING)) {
                if (cmd.length() <= 2) {
                    mGocsdkCallbackImp.onIncoming("");
                } else {
                    mGocsdkCallbackImp.onIncoming(cmd.substring(2));
                }
            } else if (cmd.startsWith(Commands.IND_HANG_UP)) {
                mGocsdkCallbackImp.onHangUp();
            } else if (cmd.startsWith(Commands.IND_TALKING)) {// 通话中:::IG[number]
                if (cmd.length() <= 2) {
                    mGocsdkCallbackImp.onTalking("");
                } else {
                    mGocsdkCallbackImp.onTalking(cmd.substring(2));
                }
            } else if (cmd.startsWith(Commands.IND_RING_START)) {// 开始响铃
                mGocsdkCallbackImp.onRingStart();
            } else if (cmd.startsWith(Commands.IND_RING_STOP)) {// 停止响铃
                mGocsdkCallbackImp.onRingStop();
            } else if (cmd.startsWith(Commands.IND_HF_LOCAL)) {//
                mGocsdkCallbackImp.onHfpLocal();
            } else if (cmd.startsWith(Commands.IND_HF_REMOTE)) {// 蓝牙接听
                mGocsdkCallbackImp.onHfpRemote();
            } else if (cmd.startsWith(Commands.IND_IN_PAIR_MODE)) {// 进入配对模式:::II
                mGocsdkCallbackImp.onInPairMode();
            } else if (cmd.startsWith(Commands.IND_EXIT_PAIR_MODE)) {// 退出配对模式
                mGocsdkCallbackImp.onExitPairMode();
            } else if (cmd.startsWith(Commands.IND_INIT_SUCCEED)) {// 上电初始化成功:::IS
                mGocsdkCallbackImp.onInitSucceed("1");
            } else if (cmd.startsWith(Commands.IND_MUSIC_PLAYING)) {// 音乐播放
                Log.d(TAG, "callback Commands playing" + cmd);
                mGocsdkCallbackImp.onMusicPlaying();
            } else if (cmd.startsWith(Commands.IND_MUSIC_STOPPED)) {// 音乐停止
                Log.d(TAG, "callback Commands stoped" + cmd);
                mGocsdkCallbackImp.onMusicStopped();
            } else if (cmd.startsWith(Commands.IND_VOICE_CONNECTED)) {
                // mGocsdkCallbackImp.onVoiceConnected();
            } else if (cmd.startsWith(Commands.IND_VOICE_DISCONNECTED)) {
                // mGocsdkCallbackImp.onVoiceDisconnected();
            } else if (cmd.startsWith(Commands.IND_AUTO_CONNECT_ACCEPT)) {
                if (cmd.length() < 4) {
                    Log.e(TAG, cmd + "=====error command");
                } else {
                    mGocsdkCallbackImp.onAutoConnectAccept(cmd.substring(2, 4));
                }
            } else if (cmd.startsWith(Commands.IND_CURRENT_ADDR)) {
                if (cmd.length() < 3) {
                    Log.e(TAG, cmd + "==== error command");
                } else {
                    mGocsdkCallbackImp.onCurrentAddr(cmd.substring(2));
                }
            } else if (cmd.startsWith(Commands.IND_CURRENT_NAME)) {
                if (cmd.length() < 3) {
                    Log.e(TAG, cmd + "==== error command");
                } else {
                    mGocsdkCallbackImp.onCurrentName(cmd.substring(2));
                }
            } else if (cmd.startsWith(Commands.IND_AV_STATUS)) {
                if (cmd.length() < 3) {
                    Log.e(TAG, cmd + "=====error");
                } else {
                    mGocsdkCallbackImp.onAvStatus(Integer.parseInt(cmd.substring(2, 3)));
                }
            } else if (cmd.startsWith(Commands.IND_HFP_STATUS)) {
                if (cmd.length() < 3) {
                    Log.e(TAG, cmd + " ==== error");
                } else {
                    int status = Integer.parseInt(cmd.substring(2, 3));
                    mGocsdkCallbackImp.onHfpStatus(status);
                }
            } else if (cmd.startsWith(Commands.IND_VERSION_DATE)) {
                if (cmd.length() < 3) {
                    Log.e(TAG, cmd + "====error");
                } else {
                    mGocsdkCallbackImp.onVersionDate(cmd.substring(2));
                }
            } else if (cmd.startsWith(Commands.IND_CURRENT_DEVICE_NAME)) {
                if (cmd.length() < 3) {
                    Log.e(TAG, cmd + "====error");
                } else {
                    mGocsdkCallbackImp.onCurrentDeviceName(cmd.substring(2));
                }
            } else if (cmd.startsWith(Commands.IND_CURRENT_PIN_CODE)) {
                if (cmd.length() < 3) {
                    Log.e(TAG, cmd + "====error");
                } else {
                    mGocsdkCallbackImp.onCurrentPinCode(cmd.substring(2));
                }
            } else if (cmd.startsWith(Commands.IND_A2DP_CONNECTED)) {
                mGocsdkCallbackImp.onA2dpConnected();
            } else if (cmd.startsWith(Commands.IND_A2DP_DISCONNECTED)) {
                mGocsdkCallbackImp.onA2dpDisconnected();
            } else if (cmd.startsWith(Commands.IND_CURRENT_AND_PAIR_LIST)) {
                if (cmd.length() < 15) {
                    Log.e(TAG, cmd + "====error");
                } else if (cmd.length() == 15) {
                    mGocsdkCallbackImp.onCurrentAndPairList(Integer.parseInt(cmd.substring(2, 3)), "", cmd.substring(3, 15));
                } else {
                    mGocsdkCallbackImp.onCurrentAndPairList(Integer.parseInt(cmd.substring(2, 3)), cmd.substring(15),
                            cmd.substring(3, 15));
                }
            } else if (cmd.startsWith(Commands.IND_PHONE_BOOK)) {// 联系人信息
                if (cmd.length() < 6) {
                    Log.e(TAG, cmd + "====error");
                } else {

                    String name = null;
                    String number = null;
                    if (cmd.contains("[FF]")) {
                        String[] split = cmd.split("\\[FF\\]");
                        if (split.length == 2) {
                            name = split[0].substring(2);
                            number = split[1];
                        }
                    } else {
                        int nameLen = Integer.parseInt(cmd.substring(2, 4));
                        int numLen = Integer.parseInt(cmd.substring(4, 6));
                        byte[] bytes = cmd.getBytes();
                        if (nameLen > 0) {
                            byte[] buffer = new byte[nameLen];
                            System.arraycopy(bytes, 6, buffer, 0, nameLen);
                            name = new String(buffer);
                        } else {
                            name = "";
                        }
                        if (numLen > 0) {
                            if ((6 + nameLen + numLen) == bytes.length) {
                                byte[] buffer = new byte[numLen];
                                System.arraycopy(bytes, 6 + nameLen, buffer, 0, numLen);
                                number = new String(buffer);
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
            } else if (cmd.startsWith(Commands.IND_PHONE_BOOK_DONE)) {
                mGocsdkCallbackImp.onPhoneBookDone();
            } else if (cmd.startsWith(Commands.IND_SIM_DONE)) {
                mGocsdkCallbackImp.onSimDone();
            } else if (cmd.startsWith(Commands.IND_CALLLOG_DONE)) {
                mGocsdkCallbackImp.onCalllogDone();
            } else if (cmd.startsWith(Commands.IND_CALLLOG)) {
                if (cmd.length() < 4) {
                    Log.e(TAG, cmd + "====error");
                } else {
                    String[] split = cmd.substring(3).split("\\[FF\\]");
                    mGocsdkCallbackImp.onCalllog(Integer.parseInt(cmd.substring(2, 3)), split[0], split[1]);
                }
            } else if (cmd.startsWith(Commands.IND_DISCOVERY)) {
                if (cmd.length() < 14) {
                    Log.e(TAG, cmd + "===error");
                } else if (cmd.length() == 14) {
                    mGocsdkCallbackImp.onDiscovery("", "", cmd.substring(2));
                } else {
                    mGocsdkCallbackImp.onDiscovery("", cmd.substring(14), cmd.substring(2, 14));
                }
            } else if (cmd.startsWith(Commands.IND_DISCOVERY_DONE)) {
                mGocsdkCallbackImp.onDiscoveryDone();
            } else if (cmd.startsWith(Commands.IND_LOCAL_ADDRESS)) {
                if (cmd.length() != 14) {
                }
                mGocsdkCallbackImp.onLocalAddress(cmd.substring(2));
            } else if (cmd.startsWith(Commands.IND_OUTGOING_TALKING_NUMBER)) {
                Log.e(TAG,"callout___IND_OUTGOING_TALKING_NUMBER:"+cmd);
                if (cmd.length() <= 2) {
                    mGocsdkCallbackImp.onOutGoingOrTalkingNumber("");
                } else {
                    mGocsdkCallbackImp.onOutGoingOrTalkingNumber(cmd.substring(2));
                }
            } else if (cmd.startsWith(Commands.IND_MUSIC_INFO)) {
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
            } else if (cmd.startsWith(Commands.IND_MUSIC_POS)) {
                if (cmd.length() != 10) {
                    Log.e(TAG, cmd + "====error");
                } else {
                    mGocsdkCallbackImp.onMusicPos(Integer.parseInt(cmd.substring(2, 6), 16), Integer.parseInt(cmd.substring(6, 10), 16));
                }
            } else if (cmd.startsWith(Commands.IND_PROFILE_ENABLED)) {
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
            } else if (cmd.startsWith(Commands.IND_MESSAGE_LIST)) {
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
            } else if (cmd.startsWith(Commands.IND_MESSAGE_TEXT)) {
                mGocsdkCallbackImp.onMessageContent(cmd.substring(2));
            } else if (cmd.startsWith(Commands.IND_OK)) {
            } else if (cmd.startsWith(Commands.IND_ERROR)) {
            } else {
            }
        } catch (RemoteException e) {
        }


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
