package cn.kaer.bluetooth.command;

public class CommandSend {
    public static final String COMMAND_HEAD = "AT#";

    /**
     *  AT#DB[bt_addr:12]\r\n 配对 ODB 设备
     */
    public static final String START_PAIR = "DB";
    /**
     * 进入配对模式:::CA
     */
    public static final String PAIR_MODE = "CA";
    /**
     * 取消配对模式:::CB
     */
    public static final String CANCEL_PAIR_MOD = "CB";
    /**
     * 连接到HFP:::SC[index:配对记录索引号:1]
     */
    public static final String CONNECT_HFP = "SC";
    /**
     * 断开HFP:::SE
     */
    public static final String DISCONNECT_HFP = "SE";
    /**
     * 连接设备:::CC[addr:12] hfp+a2dp
     */
    public static final String CONNECT_DEVICE = "CC";
    /**
     * 断开设备:::CD hfp+a2dp
     */
    public static final String DISCONNECT_DEVICE = "CD";
    /**
     * 接听来电:::CE
     */
    public static final String ACCEPT_INCOMMING = "CE";
    /**
     * 拒接来电:::CF
     */
    public static final String REJECT_INCOMMMING = "CF";
    /**
     * 结束通话:::CG
     */
    public static final String FINISH_PHONE = "CG";
    /**
     * 重拨:::CH
     */
    public static final String REDIAL = "CH";
    /**
     * 语音拨号:::CI
     */
    public static final String VOICE_DIAL = "CI";
    /**
     * 取消语音拨号:::CJ
     */

    public static final String CANCEL_VOID_DIAL = "CJ";
    /**
     * 音量增加:::CK
     */
    public static final String VOLUME_UP = "CK";
    /**
     * 音量减少:::CL
     */
    public static final String VOLUME_DOWN = "CL";
    /**
     * 麦克风打开 关闭:::CM
     */
    public static final String MIC_OPEN_CLOSE = "CM";
    /**
     * 语音切换到手机:::TF
     */
    public static final String VOICE_TO_PHONE = "TF";
    /**
     * 语音切换到蓝牙:::CP
     */
    public static final String VOICE_TO_BLUE = "CP";
    /**
     * 语音在蓝也和手机之间切换:::CO
     */
    public static final String VOICE_TRANSFER = "CO";
    /**
     * 挂断等待来电
     */
    public static final String HANG_UP_WAIT_PHONE = "CQ";
    /**
     * 挂断当前通话,接听等待来电
     */
    public static final String HANG_UP_CURRENT_ACCEPT_WAIT = "CR";
    /**
     * 保持当前通话接听等待来电
     */
    public static final String HOLD_CURRENT_ACCEPT_WAIT = "CS";
    /**
     * 会议电话
     */
    public static final String MEETING_PHONE = "CT";
    /**
     * 删除配对记录:::CV
     */
    public static final String DELETE_PAIR_LIST = "CV";
    /**
     * 拨打电话:::CW[number]
     */
    public static final String DIAL = "CW";
    /**
     * 拨打分机号:::CX[DTMF:1]
     */
    public static final String DTMF = "CX";
    /**
     * 查询HFP状态:::CY
     */
    public static final String INQUIRY_HFP_STATUS = "CY";
    /**
     * 复位蓝牙模块:::CZ
     */
    public static final String RESET_BLUE = "CZ";
    /**
     * 连接A2Dp:::DC[index:配对记录索引号:1]
     */
    public static final String CONNECT_A2DP = "DC";
    /**
     * 断开A2DP:::DA
     */
    public static final String DISCONNECT_A2DP = "DA";
    /**
     * 播放,暂停音乐:::MA
     */
    public static final String PLAY_PAUSE_MUSIC = "MA";
    /**
     * 停止音乐:::MC
     */
    public static final String STOP_MUSIC = "MB";
    /**
     * 下一曲:::MD
     */
    public static final String NEXT_SOUND = "MD";
    /**
     * 上一曲:::ME
     */
    public static final String PREV_SOUND = "ME";
    /**
     * 查询自动接听和上电自动连接配置:::MF
     */
    public static final String INQUIRY_AUTO_CONNECT_ACCETP = "MF";
    /**
     * 设置上电自动连接:::MG
     */
    public static final String SET_AUTO_CONNECT_ON_POWER = "MG";
    /**
     * 取消上电自动连接:::MH
     */
    public static final String UNSET_AUTO_CONNECT_ON_POWER = "MH";
    /**
     * 连接最后一个AV设备:::MI
     */
    public static final String CONNECT_LAST_AV_DEVICE = "MI";
    /**
     * 更改与获取LOCAL Name:::MM[name]
     */
    public static final String MODIFY_LOCAL_NAME = "MM";
    /**
     * 更改与获取PIN Code:::MN[code]
     */
    public static final String MODIFY_PIN_CODE = "MN";
    /**
     * 查询AVRCP状态:::MO
     */
    public static final String INQUIRY_AVRCP_STATUS = "MO";
    /**
     * 设定自动接听:::MP
     */
    public static final String SET_AUTO_ANSWER = "MP";
    /**
     * 取消自动接听:::MQ
     */
    public static final String UNSET_AUTO_ANSWER = "MQ";
    /**
     * 快进:::MQ
     */
    public static final String FAST_FORWARD = "MR1";
    /**
     * 停止快进:::MS
     */
    public static final String STOP_FAST_FORWARD = "MR0";
    /**
     * 快退:::MT
     */
    public static final String FAST_BACK = "MT1";
    /**
     * 停止快退:::MU
     */
    public static final String STOP_FAST_BACK = "MT0";
    /**
     * 查询A2DP状态:::MV
     */
    public static final String INQUIRY_A2DP_STATUS = "MV";
    /**
     * 查询配对记录:::MX
     */
    public static final String INQUIRY_PAIR_RECORD = "MX";
    /**
     * 查询版本日期:::MY
     */
    public static final String INQUIRY_VERSION_DATE = "MY";
    /**
     * 读取SIM电话本:::PA
     */
    public static final String SET_SIM_PHONE_BOOK = "PA";
    /**
     * 读取手机电话本:::PB
     */
    public static final String SET_PHONE_PHONE_BOOK = "PB";

    /**
     * 读取拨通话记录:::PH
     */
    public static final String SET_OUT_GOING_CALLLOG = "PH";
    /**
     * 读取已接通话记录:::PI
     */
    public static final String SET_INCOMING_CALLLOG = "PI";
    /**
     * 读取未接通话记录:::PJ
     */
    public static final String SET_MISSED_CALLLOG = "PJ";
    /**
     * 开始查找设备:::SD
     */
    public static final String START_DISCOVERY = "SD";
    /**
     * 停止查找设备:::ST
     */
    public static final String STOP_DISCOVERY = "ST";
    /**
     * 禁止蓝牙音乐:::VA
     */
    public static final String MUSIC_MUTE = "VA";
    /**
     * 启用蓝牙音乐:::VB
     */
    public static final String MUSIC_UNMUTE = "VB";
    /**
     * 蓝牙音乐作为背景音，音量减半:::VC
     */
    public static final String MUSIC_BACKGROUND = "VC";
    /**
     * 正常播放:::VD
     */
    public static final String MUSIC_NORMAL = "VD";
    /**
     * 本机蓝牙地址:::VE
     */
    public static final String LOCAL_ADDRESS = "VE";
    /**
     * 通过OPP发送文件给手机:::OS[path]
     */
    public static final String OPP_SEND_FILE = "OS";

    /**
     * 查询a2dp播放状态:::VI
     */
    public static final String INQUIRY_PLAY_STATUS = "VI";
    /**
     * 连接hid:::HC[addr:12]
     */
    public static final String CONNECT_HID = "HC";
    /**
     * 连接最后一个设备的HID:::HE
     */
    public static final String CONNECT_HID_LAST = "HE";
    /**
     * 断开hid:::HD
     */
    public static final String DISCONNECT_HID = "HD";
    /**
     * hid 菜单:::HK
     */
    public static final String MOUSE_MENU = "HG";
    /**
     * hid home:::HH
     */
    public static final String MOUSE_HOME = "HH";
    /**
     * hid 返回:::HI
     */
    public static final String MOUSE_BACK = "HI";
    /**
     * 发送鼠标移动:::HM[x:5][y:5] -9999,+9999
     */
    public static final String MOUSE_MOVE = "HM";
    /**
     * 发送鼠标点击:::HL
     */
    public static final String MOUSE_CLICK = "HL";
    /**
     * 发送鼠标按下:::HO[x:5][y:5]
     */
    public static final String MOUSE_DOWN = "HO";
    /**
     * 发送鼠标弹起:::HP[x:5][y:5]
     */
    public static final String MOUSE_UP = "HP";
    /**
     * 发送触摸屏按下:::HQ[x:5][y:5] +0000,+8195
     */
    public static final String SEND_TOUCH_DOWN = "HQ";
    /**
     * 发送触摸屏弹起:::HR[x:5][y:5] +0000,+8195
     */
    public static final String SEND_TOUCH_MOVE = "HR";
    /**
     * 发送触摸屏移动:::HS[x:5][y:5] +0000,+8195
     */
    public static final String SEND_TOUCH_UP = "HS";
    /**
     * 发送HF命令:::HF[cmd]
     */
    public static final String HF_CMD = "HF";

    /**
     * 获取音乐播放ID3:::MK
     */
    public static final String INQUIRY_MUSIC_INFO = "MK";

    /**
     * 查询当前连接设备地址:::QA
     */
    public static final String INQUIRY_CUR_BT_ADDR = "QA";

    /**
     * 查询当前连接设备名字:::QB
     */
    public static final String INQUIRY_CUR_BT_NAME = "QB";

    /**
     * 查询或设置协议开关
     * */
    public static final String SET_PROFILE_ENABLED = "SZ";

    public static final String GET_MESSAGE_INBOX_LIST = "YI";
    public static final String GET_MESSAGE_SENT_LIST = "YS";
    public static final String GET_MESSAGE_DELETED_LIST = "YD";

    public static final String GET_MESSAGE_TEXT = "YG";

    /**
     * OPEN_BT = P1                   打开蓝牙
     */
    public static final String OPEN_BT = "P1";

    /**
     *CLOSE_BT = P0                  关闭蓝牙
     */
    public static final String CLOSE_BT = "P0";

    /**
     * VOICE_SIRI = VO 				一键语音
     */
    public static final String VOICE_SIRI = "VO";

    /**
     * ENTER_TESTMODE = TE             测试指令
     */
    public static final String ENTER_TESTMODE = "TE";

    /**
     * SET_OPP_PATH = OP
     * 设置opp接收文件保存路径
     */
    public static final String SET_OPP_PATH = "OP";

    /**
     * PLAY_MUSIC = MS                 强制播放音乐
     */
    public static final String PLAY_MUSIC = "MS";

    /**
     * UPDATE_PSKEY = UP               pskey升级
     */
    public static final String UPDATE_PSKEY = "UP";

    /**
     * VOICE_MIC_GAIN = VM
     * 设置通话音量大小以及音质效果
     */
    public static final String VOICE_MIC_GAIN = "VM";

    /**
     * MUSIC_VOL_SET = VF              设置蓝牙音乐音量
     */
    public static final String MUSIC_VOL_SET = "VF";

    /**
     * INQUIRY_SPP_STATUS = SY         查询SPP状态
     */
    public static final String INQUIRY_SPP_STATUS = "SY";

    /**
     * INQUIRY_SIGNEL_BATTERY_VAL = QD 查询电池/信号量
     */
    public static final String INQUIRY_SIGNEL_BATTERY_VAL = "QD";

    /**
     * INQUIRY_SPK_MIC_VAL = QC        查询SPK及MIC音量
     */
    public static final String INQUIRY_SPK_MIC_VAL = "QC";

    /**
     * INQUIRY_DB_ADDR = DF           查询本地蓝牙地址
     */
    public static final String INQUIRY_DB_ADDR = "DF";

    /**
     * INQUIRY_PAN_STATUS = NY        查询pan状态
     */
    public static final String INQUIRY_PAN_STATUS = "NY";

    /**
     * PAN_CONNECT = NC               PAN连接
     */
    public static final String PAN_CONNECT = "NC";

    /**
     * PAN_DISCONNECT = ND            断开PAN
     */
    public static final String PAN_DISCONNECT = "ND";

    /**
     * HID_ADJUST = HP                触摸屏校屏指令
     */
    public static final String HID_ADJUST = "HP";

    /**
     * SET_TOUCH_RESOLUTION = HJ      设置车机触摸屏分辨率
     */
    public static final String SET_TOUCH_RESOLUTION = "HJ";

    /**
     * INQUIRY_HID_STATUS = HY        查询HID状态
     */
    public static final String INQUIRY_HID_STATUS = "HY";

    /**
     * STOP_PHONEBOOK_DOWN = PS       停止电话本下载
     */
    public static final String STOP_PHONEBOOK_DOWN = "PS";

    /**
     * PAUSE_PHONEBOOK_DOWN = PO      暂停电话本下载
     */
    public static final String PAUSE_PHONEBOOK_DOWN = "PO";

    /**
     * PLAY_PHONEBOOK_DOWN = PQ       继续电话本下载
     */
    public static final String PLAY_PHONEBOOK_DOWN = "PQ";

    /**
     * READ_NEXT_PHONEBOOK_COUNT = PC 向下读取n个条目（电话本）
     */
    public static final String READ_NEXT_PHONEBOOK_COUNT = "PC";

    /**
     * READ_LAST_PHONEBOOK_COUNT = PD 向上读取n个条目（电话本）
     */
    public static final String INQUIRY_READ_LAST_PHONEBOOK_COUNT = "PD";

    /**
     * READ_ALL_PHONEBOOK = PX        读取全部条目（电话本）
     */
    public static final String READ_ALL_PHONEBOOK = "PX";

    /**
     * PAUSE_MUSIC = MJ           强制暂停音乐
     */
    public static final String PAUSE_MUSIC = "MJ";

    /**
     * SEND_KEY = HK
     * 发送hid按键:::HK[key]  key:HOME,MENU,BACK,A...Z
     */
    public static final String SEND_KEY = "HK";

    /**
     * SEND_KEY_DOWN = HKD
     * hid按键按下:::HKD[key]
     */
    public static final String SEND_KEY_DOWN = "HKD";

    /**
     * SEND_KEY_UP = HKU
     *  hid按键弹起:::HKU[key]
     */
    public static final String SEND_KEY_UP = "HKU";

    /**
     * SET_LOCAL_PHONE_BOOK = PN
     * 读取手机本机号码:::PN
     */
    public static final String SET_LOCAL_PHONE_BOOK = "PN";

    /**
     * PAIR_DEVICE = DP    //连接OBD::[addr:12]
     */
    public static final String PAIR_DEVICE = "DP";

    //双链接切换状态
    public static final String INQUIRY_BT_XCHANGE = "DR";


    //获取音乐播放的 封面 成功
    public static final String SEND_MUSIC_COVER_SUCEESS = "FV";

    //播放指定歌曲
    public static final String INQUIRY_MUSIC_IN = "FP";

    /**
     CMD:
     获取当前列表 AT#FR
     返回上一层：AT#FU
     进入指定列表：AT#FD[iteam]
     播放指定歌曲：AT#FP[iteam]
     */
    //获取当前列表
    public static final String INQUIRY_MUSIC_LIST = "FR";

    //返回上一层
    public static final String INQUIRY_MUSIC_PRE= "FU";

    //进入指定列表
    public static final String INQUIRY_MUSIC_INTER = "FD";

    /**
     * 音乐 播放:::MS
     */
    public static final String QUIRY_MUSIC_PLAYING = "MS";

    /**
     * SP[addr:12]
     */
    public static final String CONNECT_SPP = "SP";

    /**
     * SG[index:1][data]
     */
    public static final String SEND_SPP_DATA= "SG";

    /**
     * 断开  SH[index:1]

     */
    public static final String DISCONNECT_SPP= "SH";

    /**
     * spp test mode
     */
    public static  final String TEST_MODEL = "SGF";

    /**
     * spp test mode finish
     */
    public static  final String TEST_MODEL_FINISH = "SG";

    /**
     * IMEI INFO
     */
    public static  final String QUERY_IMEI_INFO= "MY";

    /**
     * 查询当前播放软件
     */
    public static  final String QUERY_CURRENT_PLAY_SOFT= "FQ";


}
