package cn.kaer.multichat.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * @author wanghx
 * @date 2020/8/13
 * @description
 */
public class CallHelper {
    public static synchronized void endCall(Context ctx) {
        Log.e("InCallPresenter", "endCall");
        Intent endCallIntent = new Intent();
        endCallIntent.setAction("kaer.intent.action.END_WIRELESS_CALL");
        endCallIntent.putExtra("isSecondCall", true);
        ctx.sendBroadcast(endCallIntent);
    }
    public static synchronized void rejectCall(Context ctx) {
        Log.e("InCallPresenter", "rejectCall");
        Intent endCallIntent = new Intent();
        endCallIntent.setAction("kaer.intent.action.END_WIRELESS_CALL");
        endCallIntent.putExtra("isSecondCall", true);
        ctx.sendBroadcast(endCallIntent);
    }
    public static synchronized void acceptComingCall(Context ctx) {
        Log.e("InCallPresenter", "acceptComingCall");
        Intent answerCallIntent = new Intent();
        answerCallIntent.setAction("kaer.intent.action.ANSWER_WIRELESS_CALL");
        ctx.sendBroadcast(answerCallIntent);
    }

    @SuppressLint("MissingPermission")
    public static synchronized void starCall(Context ctx,String number,int type) {
        Log.e("InCallPresenter", "starCall");
        Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        phoneIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        phoneIntent.putExtra("android.telecom.extra.START_CALL_WITH_VIDEO_STATE", type);
        ctx.startActivity(phoneIntent);
    }
    public static synchronized void silentComingRing(Context ctx) {
        Log.e("InCallPresenter", "silentComingRing");
        Intent silenceRingerIntent = new Intent();
        silenceRingerIntent.setAction("kaer.intent.action.SILENCE_RINGER");
        ctx.sendBroadcast(silenceRingerIntent);
    }
    public static synchronized void audioModeSwitch(Context ctx,boolean isOutSide) {
        Log.e("InCallPresenter", "audioModeSwitch");
        Intent intent = new Intent("cn.kaer.wireless.audio_switch");
        intent.putExtra("mode", isOutSide ? "outside" : "inner");
        ctx.sendBroadcast(intent);
    }
    public static synchronized void silentIncallVoice(Context ctx,boolean isMute) {
        Log.e("InCallPresenter", "silentIncallVoice");
        Intent intent = new Intent("cn.kaer.wireless.incall_voice_mute");
        intent.putExtra("isMuted", isMute);
        ctx.sendBroadcast(intent);
    }
    public static synchronized void sendDtmfNUmberFroWireless(Context ctx,String number) {
        Log.e("InCallPresenter", "sendDtmfNUmberFroWireless");
        Intent intent = new Intent("cn.kaer.wireless.sendDtmf");
        intent.putExtra("dialNum", number);
        ctx.sendBroadcast(intent);
    }

    public static void multimeeting(Context ctx) {
        Log.e("InCallPresenter", "multimeeting");
        Intent intent = new Intent("cn.kaer.wireless.multimeeting");
        intent.putExtra("mode", "addCall");
        ctx.sendBroadcast(intent);
    }

    public static synchronized void endAllCall(Context ctx) {
        Log.e("InCallPresenter", "endAllCall");
        Intent intent = new Intent("cn.kaer.wireless.multimeeting");
        intent.putExtra("mode", "endAllCall");
        ctx.sendBroadcast(intent);
    }

    public static synchronized void swapCall(Context ctx) {
        Log.e("InCallPresenter", "swapCall");
        Intent intent = new Intent("cn.kaer.wireless.multimeeting");
        intent.putExtra("mode", "swapCall");
        ctx.sendBroadcast(intent);
    }

    public static synchronized void endHoldCall(Context ctx) {
        Log.e("InCallPresenter", "endHoldCall");
        Intent intent = new Intent("cn.kaer.wireless.multimeeting");
        intent.putExtra("mode", "endHoldCall");
        ctx.sendBroadcast(intent);
    }

    public static synchronized void mergeCall(Context ctx) {
        Log.e("InCallPresenter", "mergeCall");
        Intent intent = new Intent("cn.kaer.wireless.multimeeting");
        intent.putExtra("mode", "mergeCall");
        ctx.sendBroadcast(intent);
    }

    public static synchronized void disCall(Context ctx,String num) {
        Log.e("InCallPresenter", "disCall");
        Intent intent = new Intent("cn.kaer.wireless.multimeeting");
        intent.putExtra("mode", "disCall");
        intent.putExtra("number", "num");
        ctx.sendBroadcast(intent);
    }
}
