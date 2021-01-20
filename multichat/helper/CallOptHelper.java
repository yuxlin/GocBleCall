package cn.kaer.multichat.helper;

import android.telecom.Call;

import java.util.ArrayList;
import java.util.List;

import cn.kaer.multichat.R;
import cn.kaer.multichat.bean.CallOptBean;
import cn.kaer.multichat.enums.CallOptType;

/**
 * @author wanghx
 * @date 2020/8/12
 * @description
 */
public class CallOptHelper {
    private static final String[] IncallTitle = {"静音","拨号键盘","免提","添加通话","视频通话","保持","开始录音"};
    private static final int[] IncallIcons = {R.drawable.ic_mic,R.drawable.ic_dialpad,R.drawable.ic_volume,
            R.drawable.ic_addcall,R.drawable.ic_video,R.drawable.ic_pause,R.drawable.ic_radio};
    private static final int[] IncallTags = {CallOptType.MUTE.toInt(),CallOptType.DIALPAD.toInt(), CallOptType.SPEAKER_ON.toInt(),
            CallOptType.ADD_CALL.toInt(),CallOptType.VIDEO_CALL.toInt(),CallOptType.HOLD_ON.toInt(),CallOptType.RECORD.toInt()};

    private static final String[] IncallTitle_1 = {"静音","拨号键盘","免提","合并","切换","开始录音","挂断所有电话","挂断保持"};
    private static final int[] IncallIcons_1 = {R.drawable.ic_mic,R.drawable.ic_dialpad,R.drawable.ic_volume,
            R.drawable.quantum_ic_call_merge_white_36,R.drawable.quantum_ic_swap_calls_white_36,R.drawable.ic_radio,
            R.drawable.mtk_ic_toolbar_hangup_all,R.drawable.mtk_ic_toolbar_hangup_all_holding};
    private static final int[] IncallTags_1 = {CallOptType.MUTE.toInt(),CallOptType.DIALPAD.toInt(), CallOptType.SPEAKER_ON.toInt(),
            CallOptType.MERGE.toInt(),CallOptType.SWAP.toInt(),CallOptType.RECORD.toInt(),CallOptType.HANGUP_ALL.toInt(),CallOptType.HANGUP_HOLD.toInt()};

    private static final String[] OutgoingTitle = {"静音","拨号键盘","免提","添加通话"};
    private static final int[] OutgoingIcons = {R.drawable.ic_mic,R.drawable.ic_dialpad,R.drawable.ic_volume,
            R.drawable.ic_addcall,R.drawable.ic_video};
    private static final int[] OutgoingTags = {CallOptType.MUTE.toInt(),CallOptType.DIALPAD.toInt(), CallOptType.SPEAKER_ON.toInt(),
            CallOptType.VIDEO_CALL.toInt()};


    private static final String[] OutgoingTitle_1 = {"静音","拨号键盘","免提","添加通话","切换"};
    private static final int[] OutgoingIcons_1 = {R.drawable.ic_mic,R.drawable.ic_dialpad,R.drawable.ic_volume,
            R.drawable.ic_addcall,R.drawable.ic_swap};
    private static final int[] OutgoingTags_1 = {CallOptType.MUTE.toInt(),CallOptType.DIALPAD.toInt(), CallOptType.SPEAKER_ON.toInt(),
            CallOptType.ADD_CALL.toInt(),CallOptType.SWAP.toInt()};


    private static final String[] ConferenceTitle = {"静音","拨号键盘","免提","添加通话","管理","保持","开始录音"};
    private static final int[] ConferenceIcons = {R.drawable.ic_mic,R.drawable.ic_dialpad,R.drawable.ic_volume,
            R.drawable.ic_addcall,R.drawable.quantum_ic_group_white_36,R.drawable.ic_pause,R.drawable.ic_radio};
    private static final int[] ConferenceTags = {CallOptType.MUTE.toInt(),CallOptType.DIALPAD.toInt(), CallOptType.SPEAKER_ON.toInt(),
            CallOptType.ADD_CALL.toInt(),CallOptType.GROUP_MANAGE.toInt(),CallOptType.HOLD_ON.toInt(),CallOptType.RECORD.toInt()};
    public static List<CallOptBean> getConferenceData(boolean isAddCall) {
        List<CallOptBean>list = new ArrayList<>();
        if (isAddCall){

        }else{
            for (int i=0;i<ConferenceTitle.length;i++){
                CallOptBean bean = new CallOptBean(ConferenceTitle[i],ConferenceIcons[i],ConferenceTags[i]);
                if (i==1 || i==3 || i==4){
                    bean.setNeedBg(false);
                }
                list.add(bean);
            }
        }
        return list;
    }

    public static List<CallOptBean> getOutgoingData(boolean isAddCall) {
        List<CallOptBean>list = new ArrayList<>();
        if (isAddCall){
            for (int i=0;i<OutgoingTitle_1.length;i++){
                CallOptBean bean = new CallOptBean(OutgoingTitle_1[i],OutgoingIcons_1[i],OutgoingTags_1[i]);
                if (i>2){
                    bean.setEnable(false);
                }
                if (i==1 || i==3 || i==4){
                    bean.setNeedBg(false);
                }
                list.add(bean);
            }
        }else{
            for (int i=0;i<OutgoingTitle.length;i++){
                CallOptBean bean = new CallOptBean(OutgoingTitle[i],OutgoingIcons[i],OutgoingTags[i]);
                if (i>2){
                    bean.setEnable(false);
                }
                if (i==1 || i==3){
                    bean.setNeedBg(false);
                }
                list.add(bean);
            }
        }
        return list;
    }


    public static List<CallOptBean> getIncallData(boolean isAddCall) {
        List<CallOptBean>list = new ArrayList<>();
        if (isAddCall){
            for (int i=0;i<IncallTitle_1.length;i++){
                CallOptBean bean = new CallOptBean(IncallTitle_1[i],IncallIcons_1[i],IncallTags_1[i]);
                if (i==1 || i==3 || i==4 || i==6 || i==7){
                    bean.setNeedBg(false);
                }
                list.add(bean);
            }
        }else{
            for (int i=0;i<IncallTitle.length;i++){
                CallOptBean bean = new CallOptBean(IncallTitle[i],IncallIcons[i],IncallTags[i]);
                if (i==1||i==3||i==4){
                    bean.setNeedBg(false);
                }
                list.add(bean);
            }
        }
        return list;
    }
}
