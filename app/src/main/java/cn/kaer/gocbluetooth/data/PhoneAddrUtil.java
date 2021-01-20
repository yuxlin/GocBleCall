package cn.kaer.gocbluetooth.data;

import android.text.TextUtils;

import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author wanghx
 * @date 2020/12/16
 * @description
 */
public class PhoneAddrUtil {
    private static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
    private static PhoneNumberToCarrierMapper carrierMapper = PhoneNumberToCarrierMapper.getInstance();
    private static PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();
    /**
     * 根据国家代码和手机号  判断手机号是否有效
     * @param phoneNumber
     * @param countryCode
     * @return
     */
    public static boolean checkPhoneNumber(String phoneNumber, String countryCode){

        int ccode = Integer.valueOf(countryCode);
        long phone = Long.valueOf(phoneNumber);

        Phonenumber.PhoneNumber pn = new Phonenumber.PhoneNumber();
        pn.setCountryCode(ccode);
        pn.setNationalNumber(phone);

        return phoneNumberUtil.isValidNumber(pn);

    }

    /**
     * 根据国家代码和手机号  判断手机运营商
     * @param phoneNumber
     * @param countryCode
     * @return
     */
    public static String getCarrier(String phoneNumber, String countryCode){

        int ccode = Integer.valueOf(countryCode);
        long phone = Long.valueOf(phoneNumber);

        Phonenumber.PhoneNumber pn = new Phonenumber.PhoneNumber();
        pn.setCountryCode(ccode);
        pn.setNationalNumber(phone);
        //返回结果只有英文，自己转成成中文
        String carrierEn = carrierMapper.getNameForNumber(pn, Locale.ENGLISH);
        String carrierZh = "";
        carrierZh += geocoder.getDescriptionForNumber(pn, Locale.CHINESE);
        switch (carrierEn) {
            case "China Mobile":
                carrierZh += "移动";
                break;
            case "China Unicom":
                carrierZh += "联通";
                break;
            case "China Telecom":
                carrierZh += "电信";
                break;
            default:
                break;
        }
        return carrierZh;
    }


    /**
     *
     * @Description: 根据国家代码和手机号  手机归属地
     * @date 2015-7-13 上午11:33:18
     * @param @param phoneNumber
     * @param @param countryCode  86
     * @param @return 参数
     * @throws
     */
    public static String getGeo(String phoneNumber, int countryCode) {

        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length()>15){
            return "";
        }
        int ccode = countryCode;
        long phone = Long.valueOf(phoneNumber);

        Phonenumber.PhoneNumber pn = new Phonenumber.PhoneNumber();
        pn.setCountryCode(ccode);
        pn.setNationalNumber(phone);
        return geocoder.getDescriptionForNumber(pn, Locale.CHINA);

    }

}
