package cn.kaer.multichat.bean;

/**
 * @author wanghx
 * @date 2020/8/12
 * @description
 */
public class CallOptBean {
    private int drsInt;
    private String ttlStr;
    private int tag;
    private boolean enable;
    private boolean checked;
    private boolean needBg;

    public CallOptBean(String title, int icon, int tag) {
        this.ttlStr = title;
        this.drsInt = icon;
        this.tag = tag;
        this.enable = true;
        this.checked = false;
        this.needBg = true;
    }

    public CallOptBean(int drsInt, String ttlStr, int tag, boolean enable, boolean checked) {
        this.drsInt = drsInt;
        this.ttlStr = ttlStr;
        this.tag = tag;
        this.enable = enable;
        this.checked = checked;
    }

    public int getDrsInt() {
        return drsInt;
    }

    public void setDrsInt(int drsInt) {
        this.drsInt = drsInt;
    }

    public String getTtlStr() {
        return ttlStr;
    }

    public void setTtlStr(String ttlStr) {
        this.ttlStr = ttlStr;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isNeedBg() {
        return needBg;
    }

    public void setNeedBg(boolean needBg) {
        this.needBg = needBg;
    }
}
