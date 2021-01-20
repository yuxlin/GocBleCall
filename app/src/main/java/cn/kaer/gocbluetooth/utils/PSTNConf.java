package cn.kaer.gocbluetooth.utils;

/**
 * User: yxl
 * Date: 2020/10/31
 */
public class PSTNConf {
    public static final int WIRELESS = 0;
    public static final int PSTN = 1;
    public static final int BLUETOOTH = 2;
    public static final int CALLSTATE_IDLE = 0;
    public static final int CALLSTATE_RINGING = 1;
    public static final int CALLSTATE_INCALL = 2;
    public static boolean SYSTEM_OFFHOOK_ENABLE = true;
    public static final boolean writeLogFile = false;
    public static final boolean DEBUG = true;
    public static final String PSTN_GPIO_ONLINE = "cn.kaer.pstn_online";
    public static final String PSTN_GPIO_OFFLINE = "cn.kaer.pstn_offline";
    public static final String PSTN_GPIO_LINECHANGE = "cn.kaer.pstn_change";
    public static final String PSTN_GPIO_ACCEPTCALL = "cn.kaer.pstn_acceptcall";
    public static final String PSTN_GPIO_HANGUP = "cn.kaer.pstn_hangup";
    public static boolean isReadytoHangup = true;
    public static boolean isSendnumReadyState = false;
    public static boolean isReadytoFlash = true;
    public static boolean needSendNumberSwitchAudiopath = false;
    public static boolean closeAudioInterupped = false;
    public static boolean isMutestate = false;
    public static int DEFAULT_VOICECALL_VOLUME = 7;
    public static boolean hookpathOnHangup = false;

    public PSTNConf() {
    }
}
