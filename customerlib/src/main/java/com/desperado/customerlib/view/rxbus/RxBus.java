package com.desperado.customerlib.view.rxbus;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/11/15.
 */
public class RxBus {
    private static final String TAG = RxBus.class.getSimpleName();
    private static RxBus instance;
    public static boolean DEBUG = false;

    public static synchronized RxBus get() {
        if (null == instance) {
            instance = new RxBus();
        }
        return instance;
    }

    private RxBus() {
    }

    private ConcurrentHashMap<Object, List<Subject>> subjectMapper = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> Observable<T> register(@NonNull Object tag, @NonNull Class<T> clazz) {
        List<Subject> subjectList = subjectMapper.get(tag);
        if (null == subjectList) {
            subjectList = new ArrayList<>();
            subjectMapper.put(tag, subjectList);
        }

        Subject<T, T> subject;
        subjectList.add(subject = PublishSubject.create());
        if (DEBUG) Log.d(TAG, "[register]subjectMapper: " + subjectMapper);
        return subject;
    }

    public void unregister(@NonNull Object tag, @NonNull Observable observable) {
        List<Subject> subjects = subjectMapper.get(tag);
        if (null != subjects) {
            subjects.remove((Subject) observable);
            if (subjects == null || subjects.isEmpty()) {
                subjectMapper.remove(tag);
            }
        }

        if (DEBUG) Log.d(TAG, "[unregister]subjectMapper: " + subjectMapper);
    }

    public void post(@NonNull Object content) {
        post(content.getClass().getName(), content);
    }

    @SuppressWarnings("unchecked")
    public void post(@NonNull Object tag, @NonNull Object content) {
        List<Subject> subjectList = subjectMapper.get(tag);

        if (!(subjectList==null||subjectList.isEmpty())) {
            for (Subject subject : subjectList) {
                subject.onNext(content);
            }
        }
        if (DEBUG) Log.d(TAG, "[send]subjectMapper: " + subjectMapper);
    }


}
