package cn.kaer.multichat.mvp;

import java.util.List;

import cn.kaer.multichat.bean.CallOptBean;
import cn.kaer.multichat.enums.CallState;
import cn.kaer.multichat.helper.CallOptHelper;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * @author wanghx
 * @date 2020/8/12
 * @description
 */
public class CallModel implements CallContract.Model{
    @Override
    public Observable<List<CallOptBean>> getData(CallState callState, boolean isAddCall) {
        Observable<List<CallOptBean>>observable = Observable.create(new ObservableOnSubscribe<List<CallOptBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<CallOptBean>> e) throws Exception {
                List<CallOptBean>list = null;
                if (callState==CallState.INCALL){
                    list = CallOptHelper.getIncallData(isAddCall);
                }else if (callState == CallState.OUTGOING){
                    list = CallOptHelper.getOutgoingData(isAddCall);
                }else if (callState == CallState.CONFERENCE){
                    list = CallOptHelper.getConferenceData(isAddCall);
                }
                e.onNext(list);
                e.onComplete();
            }
        });
        return observable;
    }


}
