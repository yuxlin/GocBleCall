package cn.kaer.bluetooth.command;

public class CommandIND {


    /**
     * ERROR
     * */
    public static final String IND_ERROR = "ERROR";
    /**
     * OK
     * */
    public static final String IND_OK = "OK";

    public static final String IND_HEAD = "\r\n";

    /**
     * HFP已断开:::IA
     */
    public static final String IND_HFP_DISCONNECTED = "IA";

    /**
     * HFP已连接:::IB
     */
    public static final String IND_HFP_CONNECTED = "IB";

    /**
     * 去电:::IC[number]
     */
    public static final String IND_CALL_SUCCEED = "IC";

    /**
     * 打出电话或通话中号码
     */
    public static final String IND_OUTGOING_TALKING_NUMBER = "IR";


    /**
     * 来电:::ID[number]
     */
    public static final String IND_INCOMING = "ID";

    /**
     * 通话中的来电::IE[number]
     */
    public static final String IND_SECOND_INCOMING = "IE";

    /**
     * 挂机:::IF[numberlen:2][number]
     */
    public static final String IND_HANG_UP = "IF";

    /**
     * 通话中:::IG[number]
     */
    public static final String IND_TALKING = "IG";
    public static final String IND_RING_START = "VR1";
    public static final String IND_RING_STOP = "VR0";

    /**
     * 手机接听
     */
    public static final String IND_HF_LOCAL = "T1";

    /**
     * 蓝牙接听
     */
    public static final String IND_HF_REMOTE = "T0";

    /**
     * 进入配对模式:::II
     */
    public static final String IND_IN_PAIR_MODE = "II";

    /**
     * 退出配对模式:::IJ
     */
    public static final String IND_EXIT_PAIR_MODE = "IJ";

    /**
     * 来电名字显示
     */
    public static final String IND_INCOMING_NAME = "IQ";


    /**
     * 上电初始化成功:::IS
     */
    public static final String IND_INIT_SUCCEED = "IS";

    /**
     * 连接中
     */
    public static  final String IND_CONNECTING = "IV";



    /**
     * 音乐停止:MB
     */
    public static final String IND_MUSIC_STOPPED = "MB";

    /**
     * 语音连接建立
     */
    public static final String IND_VOICE_CONNECTED = "MC";

    /**
     * 语音连接断开
     */
    public static final String IND_VOICE_DISCONNECTED = "MD";

    /**
     * 开机自动连接,来电自动接听当前配置:::MF[auto_connect:1][auto_answer:1]
     */
    public static final String IND_AUTO_CONNECT_ACCEPT = "MF";

    /**
     * 当前连接设备地址:::JH[addr:12]
     */
    public static final String IND_CURRENT_ADDR = "JH";

    /**
     * 当前连接设备名称:::SA[name]
     */
    public static final String IND_CURRENT_NAME = "SA";

    /**
     * 当前HFP状态:::S[hf_state:1] 1:未连接 3:已连接 4：电话拨出 5：电话打入 6：通话中
     */
    public static final String IND_HFP_STATUS = "MG";

    /**
     * 当a2dp状态:::S[av_state:1] 1:未连接 3:已连接d
     */
    public static final String IND_AV_STATUS = "MU";

    /**
     * 当前版本号
     */
    public static final String IND_VERSION_DATE = "SY";

    /**
     * 当前AVRCP状态
     */
    public static final String IND_AVRCP_STATUS = "ML";

    /**
     * 当前设备名称:::MM[name]
     */
    public static final String IND_CURRENT_DEVICE_NAME = "MM";

    /**
     * 当前配对密码:::MN[code]
     */
    public static final String IND_CURRENT_PIN_CODE = "MN";

    /**
     * A2DP connected
     */
    public static final String IND_A2DP_CONNECTED = "MH";

    /**
     * 当前设备名称和配对记录
     */
    public static final String IND_CURRENT_AND_PAIR_LIST = "XM";

    /**
     * A2DP已断开
     */
    public static final String IND_A2DP_DISCONNECTED = "MY";

    /**
     * 设定电话本状态
     */
    public static final String IND_SET_PHONE_BOOK = "PA";

    /**
     * 电话本记录显示:::PB[namelen:2][numlen:2][name][number]
     */
    public static final String IND_PHONE_BOOK = "PB";

    /**
     * SIM卡电话本记录显示:::PB[namelen:2][numlen:2][name][number]
     */
    public static final String IND_SIM_BOOK = "PB";

    /**
     * 下载电话本结束:::PC
     */
    public static final String IND_PHONE_BOOK_DONE = "PC";

    /**
     * SIM卡结束
     */
    public static final String IND_SIM_DONE = "PC";

    /**
     * 下载通话记录结束:::PE
     */
    public static final String IND_CALLLOG_DONE = "PE";

    /**
     * 通话记录显示:::PD[type:1][number]
     */
    public static final String IND_CALLLOG = "PD";

    /**
     * 查找到的设备:::SF[type][addr:12][name]
     */
    public static final String IND_DISCOVERY = "SF";

    /**
     * 查找结束:::IY
     */
    public static final String IND_DISCOVERY_DONE = "SH";

    /**
     * 本机蓝牙地址:::IZ[addr:12]
     */
    public static final String IND_LOCAL_ADDRESS = "DB";


    /**
     * spp连接:::SPC[index:1]
     */
    public static final String IND_SPP_CONNECT = "SPC";

    /**
     * spp断开:::SPS[index:1]
     */
    public static final String IND_SPP_DISCONNECT = "SPS";

    /**
     * IND_SPP_STATUS = SR[index:1][status:1] SPP状态
     */
    public static  final String IND_SPP_STATUS = "SR";

    /**
     * OPP收到文件
     */
    public static final String IND_OPP_RECEIVED_FILE = "OR";

    /**
     * OPP发送文件成功
     */
    public static final String IND_OPP_PUSH_SUCCEED = "OC";

    /**
     * OPP发送文件失败
     */
    public static final String IND_OPP_PUSH_FAILED = "OF";

    /**
     * hid连接成功
     */
    public static  final String IND_HID_CONNECTED = "HB";

    /**
     * hid断开连接
     */
    public static final String IND_HID_DISCONNECTED = "HA";

    /**
     * ID3信息
     */
    public static final String IND_MUSIC_INFO = "MI";

    /**
     * 歌曲进度
     */
    public static final String IND_MUSIC_POS = "MP";

    /**
     * 协议开关
     */
    public static final String IND_PROFILE_ENABLED = "SX";

    /**
     * 短信列表
     */
    public static final String IND_MESSAGE_LIST = "YL";

    /**
     * 短信内容
     */
    public static final String IND_MESSAGE_TEXT = "YT";

    /**
     * 配对状态  P[状态] 1:成功，2:失败
     */
    public static final String IND_PAIR_STATE = "P";

    /**
     * IND_SHUTDOWN = ST
     * 关闭蓝牙回复
     */
    public static final String IND_SHUTDOWN = "ST";

    /**
     * IND_UPDATE_SUCCESS = US
     * pskey升级完成
     */
    public static final String IND_UPDATE_SUCCESS = "US";

    /**
     * IND_SIGNAL_BATTERY_VAL = PS[signal:2][battery:2]
     * 手机信号强度/电池电量
     */
    public static final String IND_SIGNAL_BATTERY_VAL = "PS";

    /**
     * IND_PAN_STATUS = NS[status:1]          PAN状态
     */
    public static  final String IND_PAN_STATUS = "NS";

    /**
     * IND_PAN_DISCONNECT = NA
     * pan断开
     */
    public static final String IND_PAN_DISCONNECT = "NA";

    /**
     * IND_PAN_CONNECT = NC                   pan连接成功
     */
    public static final String IND_PAN_CONNECT = "NC";

    /**
     * IND_SPK_MIC_VAL = KI[spk:1][mic:1]
     * 当前spk, mic音量
     */
    public static final String IND_SPK_MIC_VAL = "KI";

    /**
     * IND_MIC_STATUS = IO[index:1]
     * 打开或关闭咪头
     */
    public static final String IND_MIC_STATUS = "IO";

    /**
     * IND_HID_STATUS = HS[status:1]          hid状态
     */
    public static final String IND_HID_STATUS = "HS";

    /**
     * IND_HID_ADJUST = HP[key:1][x:4][y:4]   HID校屏
     */
    public static final String IND_HID_ADJUST = "HP";

    /**
     * IND_PAIR_LIST_DONE = PL
     * 配对列表发送结束
     */
    public static final String IND_PAIR_LIST_DONE = "PL";

    /**
     * IND_CURRENT_ADDR_NAME=MX0[addr][name]  当前连接设备地址
     */
    public static final String IND_CURRENT_ADDR_NAME = "MX0";

    /**
     * IND_MUSIC_LIST=MX0[addr][name]  获取蓝牙音乐列表
     */

    //进入指定列表
    public static final String INQUIRY_MUSIC_INTER = "FD";


    /**
     RSP:
     设置成功：FC
     设置失败：FD
     上报单个列表：FI[TYPE:1][iteam_hex:16][name]
     TYPE:D表示目录
     F表示单曲
     列表上报完成：FJ
     列表上报失败：FK
     列表播放成功：FS
     列表播放失败：FT
     当前歌曲iteam：FU[iteam_hex:16]*/

//收到的回调
//获取指定音乐类型列表成功
    public static final String IND_MUSIC_TYPE_SUCCESS= "FC";

    //获取指定音乐列表失败
    public static final String IND_MUSIC_TYPE_FAIL = "FD";

    //音乐类型列表
    public static final String IND_MUSIC_LIST_TYPE ="FI";

    //音乐列表上报成功
    public static final String IND_MUSIC_LIST_SUCESS = "FJ";

    //音乐列表上报失败
    public static final String IND_MUSIC_LIST_FAIL = "FK";

    //播放音乐列表成功
    public static final String IND_MUSIC_PLAY_SUCESSS = "FS";

    //播放音乐列表失败
    public static final String IND_MUSIC_PLAY_FAIL = "FT";

    //获取音乐播放的 封面 成功
    public static final String IND_MUSIC_COVER_SUCEESS = "FV";

    //获取音乐播放的 封面 失败
    public static  final String IND_MUSIC_COVER_FAIL = "FW";


    //双链接切换状态
    public static final String IND_CHANGE_BT = "DR";

    //应答指令 MB
    public static  final String IND_MUSIC_PLAYED = "MB";

    //应答指令 MA
    public static final String IN_MUSIC_PAUSE = "MA";

    //应答指令 联系人指令 图标
    public static final String IND_CONTACT_ICON = "VD";

    //应答指令 联系人下表
    public static final String IND_CONTACT_ID = "VS";

    /**
     * 设置上电自动连接:::MG
     */
    public static  final String IND_AUTO_CONNECT_ON_POWER = "MG";

    /**
     * :SV[addr:12][index:1]
     */

    public static final String IND_CONNECT_SPP_SUCESS = "SV";

    /**
     *SPP收到数据 SI[index:1][data]
     */
    public static final String IND_SPP_DATA = "SI";

    /**
     * 断开spp SS[addr:12][index:1]
     */

    public static final String IND_DISCONNECT_SPP = "SI";

    /**
     * IMEI INFO
     */
    public static final String IND_IMEI_INFO = "MW";

    /**
     * 当前播放器名称
     */
    public static final String IND_CURRENT_PLAY_SOFT = "FQ";

}
