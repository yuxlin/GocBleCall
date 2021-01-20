package cn.kaer.multichat.enums;

/**
 * @author wanghx
 * @date 2020/8/12
 * @description
 */
public enum CallState {
    INCALL(1),
    INCOMING(2),
    OUTGOING(3),
    CONFERENCE(4);
    private int callType;

    CallState(int callType) {
        this.callType = callType;
    }

    public int toInt() {
        return this.callType;
    }
}
