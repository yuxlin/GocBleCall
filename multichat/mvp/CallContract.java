package cn.kaer.multichat.mvp;

import java.util.List;

import cn.kaer.multichat.base.BaseModelCreate;
import cn.kaer.multichat.base.BasePresenter;
import cn.kaer.multichat.base.BaseView;
import cn.kaer.multichat.bean.CallOptBean;
import cn.kaer.multichat.enums.CallState;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * @author wanghx
 * @date 2020/8/12
 * @description
 */
public interface CallContract {
    interface Model extends BaseModelCreate{
        Observable<List<CallOptBean>> getData(CallState callState, boolean isAddCall);
    }
    interface View extends BaseView{
        void getDataSuc(List<CallOptBean> en);
    }
    abstract class Presenter extends BasePresenter<Model,View>{

        public abstract void showHideHoldFm(boolean isShow);

        public abstract void getData(CallState incall, boolean isAddCall);
    }
}
