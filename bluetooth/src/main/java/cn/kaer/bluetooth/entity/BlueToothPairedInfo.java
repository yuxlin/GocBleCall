package cn.kaer.bluetooth.entity;

public class BlueToothPairedInfo extends BlueToothInfo {
    public int index;


    public boolean equals(Object obj) {
        if (obj instanceof BlueToothPairedInfo) {
            BlueToothPairedInfo pairedInfo = (BlueToothPairedInfo) obj;
            return this.index == pairedInfo.index
                    && this.address.equals(pairedInfo.address)
                    && this.name.equals(pairedInfo.name);
        }
        return super.equals(obj);
    }
}
