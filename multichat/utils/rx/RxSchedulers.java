package cn.kaer.multichat.utils.rx;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @describe：TODO</br> Created by wanghuixiang on 2018/12/27.
 */

public class RxSchedulers {
    public static <T> ObservableTransformer<T, T> io_main() {
       return upstream->upstream.subscribeOn(Schedulers.io())
               .unsubscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread());
    }
    public static <T> ObservableTransformer<T, T> io_io() {
        return upstream->upstream.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }
    public static <T> ObservableTransformer<T,T> io_thread(){
        return upstream -> upstream.subscribeOn(Schedulers.trampoline());
    }
}
