package cn.kaer.gocbluetooth.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.provider.Settings;
import android.util.Log;


public class SoundHelper {
    //  ToneGenerator.TONE_DTMF_0

    private static SoundHelper instance;
    private Context mContext;
    private AudioManager mAudioManager;
    private boolean isAllowPlay;
    private String TAG = getClass().getSimpleName();
    private static final int TONE_LENGTH_MS = 150; // 延迟时间
    private ToneGenerator mToneGenerator_out;
    private ToneGenerator mToneGenerator_inner;
    private ToneGenerator mToneGenerator;
    private static final int VOLUME = 100; //音量
    private static final int STERAM_TYPE_OUT = AudioManager.STREAM_DTMF;
    private static final int STERAM_TYPE_INNER = AudioManager.STREAM_VOICE_CALL;
    private boolean isPlaying = false;

    private SoundHelper() {
    }

    public static SoundHelper getInstance() {
        if (instance == null) {
            instance = new SoundHelper();
        }
        return instance;
    }

    public SoundHelper init(Context context) {
        if (mContext != null && mAudioManager!=null) {
            return this;
        }
        mContext = context;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        Log.d(TAG, "系统按键音开启：" + isAllowPlay);
        mToneGenerator_out = new ToneGenerator(STERAM_TYPE_OUT, VOLUME);
        mToneGenerator_inner = new ToneGenerator(STERAM_TYPE_INNER, VOLUME);
        mToneGenerator = mToneGenerator_out;
        return this;
    }

    public void playTone(int tone) {
        if (!isAllowPlay || mToneGenerator == null) {
            return;
        }
        mToneGenerator.startTone(tone, TONE_LENGTH_MS); // 发声
    }

    public void playWaitingTone() {
        // if (mToneGenerator != null)
        isPlaying = true;
        mToneGenerator.startTone(ToneGenerator.TONE_SUP_DIAL, -1);
    }

    public void stopWaitingTone() {
        if (isPlaying) {
            mToneGenerator.stopTone();
            isPlaying = false;
        }

    }

    //设置外放还是听筒
    public void setSoundChannel(boolean isHookoff) {
        SoundHelper.getInstance().stopWaitingTone();
        if (isHookoff) { //听筒
            mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            mAudioManager.setSpeakerphoneOn(false);
            Log.d(TAG, "切换为听筒");
        } else {//外放
            Log.d(TAG, "切换为外放");
            mAudioManager.setMode(AudioManager.MODE_NORMAL);
            mAudioManager.setSpeakerphoneOn(true);
        }
    }


    public void queryDTMFState() {
        isAllowPlay = Settings.System.getInt(mContext.getContentResolver(), Settings.System.DTMF_TONE_WHEN_DIALING, 1) == 1;
    }
}
