package cn.kaer.multichat.utils.rx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * @describe：TODO</br> Created by wanghuixiang on 2018/12/27.
 */

public class RxBus {
    private static RxBus instance;
    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = new ConcurrentHashMap();

    public static synchronized RxBus $() {
        if(null == instance) {
            instance = new RxBus();
        }

        return instance;
    }

    private RxBus() {
    }

    public RxBus OnEvent(Observable<?> mObservable, Consumer<Object> mAction1) {
        mObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(mAction1, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        });
        return $();
    }

    public <T> Observable<T> register(@io.reactivex.annotations.NonNull Object tag) {
        Object subjectList = (List)this.subjectMapper.get(tag);
        if(null == subjectList) {
            subjectList = new ArrayList();
            this.subjectMapper.put(tag, (List<Subject>) subjectList);
        }

        PublishSubject subject;
        ((List)subjectList).add(subject = PublishSubject.create());
        return subject;
    }

    public void unregister(@io.reactivex.annotations.NonNull Object tag) {
        List subjects = (List)this.subjectMapper.get(tag);
        if(null != subjects) {
            this.subjectMapper.remove(tag);
        }

    }

    public RxBus unregister(@io.reactivex.annotations.NonNull Object tag, @io.reactivex.annotations.NonNull Observable<?> observable) {
        if(null == observable) {
            return $();
        } else {
            List subjects = (List)this.subjectMapper.get(tag);
            if(null != subjects) {
                subjects.remove((Subject)observable);
                if(isEmpty(subjects)) {
                    this.subjectMapper.remove(tag);
                }
            }

            return $();
        }
    }

    public void post(@io.reactivex.annotations.NonNull Object content) {
        this.post(content.getClass().getName(), content);
    }

    public void post(@io.reactivex.annotations.NonNull Object tag, @io.reactivex.annotations.NonNull Object content) {
        List subjectList = (List)this.subjectMapper.get(tag);
        if(!isEmpty(subjectList)) {
            Iterator var4 = subjectList.iterator();

            while(var4.hasNext()) {
                Subject subject = (Subject)var4.next();
                subject.onNext(content);
            }
        }

    }

    public static boolean isEmpty(Collection<Subject> collection) {
        return null == collection || collection.isEmpty();
    }
}
