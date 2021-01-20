package com.goodocom.gocsdk;

public class AppHelper {
    private volatile static AppHelper mSingleInstance = null;
    private AppHelper () {}

    public static final String ACTION_CLEAR_CALLINFO = "clear_calllog";
    public static int BT_ON_OR_OFF = 0;
    public String CURRENT_SPP_CONNECT_INDEX = "-1";
    public String CURRENT_SPP_CONNENCT_ADDRESS = "";
    public boolean  CURRENT_SPP_TEST_MODE = false;
    public String IMEI_INFO = "";
    public String PLAY_SOFT = "";

    public static AppHelper getInstance(){
        if (mSingleInstance == null) {
            synchronized (AppHelper.class) {
                if (mSingleInstance == null) {
                    mSingleInstance = new AppHelper();
                }
            }
        }
        return mSingleInstance;

    }

    public String[] mConnectAddress = new String[2];
    public String[] mConnectName = new String[2];
}
