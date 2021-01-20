package cn.kaer.multichat.mvp;

import cn.kaer.multichat.enums.CallState;
import cn.kaer.multichat.utils.rx.RxSchedulers;

/**
 * @author wanghx
 * @date 2020/8/12
 * @description
 */
public class CallPresenter extends CallContract.Presenter{

    @Override
    public void showHideHoldFm(boolean isShow) {
        mRxManage.post("showHideHoldFm",isShow);
    }

    @Override
    public void getData(CallState callState, boolean isAddCall) {
        mRxManage.add(mModel.getData(callState,isAddCall).compose(RxSchedulers.io_main())
        .subscribe(en->{
            mView.getDataSuc(en);
        },e->{
            e.printStackTrace();
        }));
    }
}
