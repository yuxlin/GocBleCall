package cn.kaer.multichat.enums;

/**
 * @author wanghx
 * @date 2020/8/12
 * @description
 */
public enum CallOptType {
    MUTE(1),
    DIALPAD(2),
    SPEAKER_ON(3),
    ADD_CALL(4),
    VIDEO_CALL(5),
    HOLD_ON(6),
    RECORD(7),
    MERGE(8),
    SWAP(9),
    HANGUP_ALL(10),
    HANGUP_HOLD(11),
    GROUP_MANAGE(12);
    private int optType;

    CallOptType(int optType) {
        this.optType = optType;
    }

    public int toInt() {
        return this.optType;
    }
}
