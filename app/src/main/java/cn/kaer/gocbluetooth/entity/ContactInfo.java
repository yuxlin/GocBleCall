package cn.kaer.gocbluetooth.entity;

import android.text.TextUtils;

/**
 * User: yxl
 * Date: 2021/1/19
 */
public class ContactInfo {
    private String name;
    private String num;

    public String getName() {
        return name;
    }

    public void setName(String name) {


        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {

        try {
            if (!TextUtils.isEmpty(num)) {
                num = num.replaceAll(" ", "")
                        .replaceAll("-", "");
            }
        } catch (Exception ignore) {}

        this.num = num;
    }


}
