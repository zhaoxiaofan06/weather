package com.xiaofan.www.weather.common;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by think on 2017/10/14.
 */

public class RxBus {

    private static volatile RxBus defaultInstance;

    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());

    public void send(Object o) {
        _bus.onNext(o);
    }

    public Observable<Object> toObserverable() {
        return _bus;
    }

    public boolean hasObservers() {
        return _bus.hasObservers();
    }

    // 单例RxBus
    public static RxBus getDefault() {
        RxBus rxBus = defaultInstance;
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                rxBus = defaultInstance;
                if (defaultInstance == null) {
                    rxBus = new RxBus();
                    defaultInstance = rxBus;
                }
            }
        }
        return rxBus;
    }
}
