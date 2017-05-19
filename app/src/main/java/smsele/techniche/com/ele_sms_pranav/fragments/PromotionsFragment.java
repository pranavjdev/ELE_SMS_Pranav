package smsele.techniche.com.ele_sms_pranav.fragments;

import android.support.v4.app.Fragment;

/**
 * @author Pranav J.Dev E-mail : pranav@techniche.co
 *         Date : 18/5/17
 *         Module : ELE_SMS_Pranav.
 */

public class PromotionsFragment extends Fragment{

    public void setTitle(String title) {
        this.title = title;
    }

    private String title = null;

    public static PromotionsFragment newInstance(String title) {
        PromotionsFragment fragment = new PromotionsFragment();
        return new PromotionsFragment();
    }
}
