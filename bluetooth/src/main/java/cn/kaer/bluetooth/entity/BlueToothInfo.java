package cn.kaer.bluetooth.entity;

public class BlueToothInfo {
    public String address;
    public String name;
    public int status;//0~初始化 1~待机状态 2~连接中 3~连接成功 4~电话拨出 5~电话打入 6~通话中


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String toString(String address, String name) {
        return "address: " + address + "name: " + name;
    }
}
