package com.rxjava2.android.samples.ui.operators;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rxjava2.android.samples.R;
import com.rxjava2.android.samples.utils.AppConstant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * Created by amitshekhar on 28/08/16.
 */
public class MergeExampleActivity extends AppCompatActivity {

    private static final String TAG = MergeExampleActivity.class.getSimpleName();
    Button btn;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        btn = (Button) findViewById(R.id.btn);
        textView = (TextView) findViewById(R.id.textView);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSomeWork();
            }
        });
    }

	/*
     * Using merge operator to combine Observable : merge does not maintain
	 * the order of Observable.
	 * It will emit all the 7 values may not be in order
	 * Ex - "A1", "B1", "A2", "A3", "A4", "B2", "B3" - may be anything
	 */


    private void doSomeWork() {
        final String[] aStrings = {"A1", "A2", "A3", "A4"};
        Observable.fromArray(aStrings).flatMap(new Function<String, ObservableSource<List<String>>>() {
            @Override
            public ObservableSource<List<String>> apply(String s) throws Exception {
                return getKeysV2(s);
            }
        }).reduce(new BiFunction<List<String>, List<String>, List<String>>() {
            @Override
            public List<String> apply(List<String> strings, List<String> strings2) throws Exception {
                List<String> result = new ArrayList<String>(strings.size() + strings2.size());
                result.addAll(strings);
                result.addAll(strings2);
                return result;
            }
        }).toSingle().toObservable().subscribe(getListObserver1());


    }

    private void doSomeWorkV555() {
        final String[] aStrings = {"A1", "A2", "A3", "A4"};


        Observable.combineLatest(getIterableObservable1(aStrings), new Function<Object[], List<String>>() {
            @Override
            public List<String> apply(Object[] objects) throws Exception {
                List<String> total = new ArrayList<String>();
                for (int i = 0; i < objects.length; i++) {
                    total.addAll((Collection<? extends String>) objects[i]);
                }
                return total;
            }
        }).subscribe(getListObserver1());
    }

    private void doSomeWorkV33() {
        final String[] aStrings = {"A1", "A2", "A3", "A4"};
        Observable.combineLatest(getIterableObservable1(aStrings), new Function<Object[], List<String>>() {
            @Override
            public List<String> apply(Object[] objects) throws Exception {
                List<String> total = new ArrayList<String>();
                for (int i = 0; i < objects.length; i++) {
                    total.addAll((Collection<? extends String>) objects[i]);
                }
                return total;
            }
        }).subscribe(getListObserver1());
    }


    private List<Observable<List<String>>> getIterableObservable1(String[] aStrings) {
        List<Observable<List<String>>> result = new ArrayList<>();
        for (String aString : aStrings) {
            result.add(getKeysV2(aString));
        }

        return result;
    }

    private Observable<List<String>> getKeysV2(String badgeId) {
        List<String> keyList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            keyList.add(i + ":" + badgeId);

        }
        return Observable.just(keyList);
    }


    private void doSomeWorkV1() {
        final String[] aStrings = {"A1", "A2", "A3", "A4"};

        Observable.fromArray(aStrings).flatMap(new Function<String, ObservableSource<List<String>>>() {
            @Override
            public ObservableSource<List<String>> apply(String s) throws Exception {
                return getKeys(s);
            }
        }).toList().toObservable().map(new Function<List<List<String>>, List<String>>() {
            @Override
            public List<String> apply(List<List<String>> lists) throws Exception {
                List<String> result = new ArrayList<String>();
                for (List<String> list : lists) {
                    result.addAll(list);
                }
                return result;
            }
        }).subscribe(getListObserver1());
    }

    private Observer<? super List<Object>> getListObserver2() {
        return null;
    }


    private Observable<List<String>> getKeys(String badgeId) {
        List<String> keyList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            keyList.add(i + ":" + badgeId);

        }
        return Observable.just(keyList);
    }


    private void doSomeWorkV0() {
        final String[] aStrings = {"A1", "A2", "A3", "A4"};
        final String[] bStrings = {"B1", "B2", "B3"};

        final Observable<String> aObservable = Observable.fromArray(aStrings);
        final Observable<String> bObservable = Observable.fromArray(bStrings);

        Observable.merge(aObservable, bObservable).toList().toObservable()
                .subscribe(getListObserver1());
    }

    private Observer<? super List<String>> getListObserver1() {
        return new Observer<List<String>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<String> value) {
                textView.append(" onNext : value size : " + value.size());
                textView.append(AppConstant.LINE_SEPARATOR);


                for (String raw : value) {
                    textView.append("   item: " + raw);
                    textView.append(AppConstant.LINE_SEPARATOR);

                }
            }

            @Override
            public void onError(Throwable e) {
                textView.append(" onError : " + e.getMessage());
                textView.append(AppConstant.LINE_SEPARATOR);
            }

            @Override
            public void onComplete() {
                textView.append(" onComplete");
                textView.append(AppConstant.LINE_SEPARATOR);
            }
        };
    }

//    private Observable<? super List<String>> getListObserver() {
//        return new Observable<List<String>>(){
//
//            @Override
//            protected void subscribeActual(Observer<? super List<String>> observer) {
//
//            }
//        };
//    }


    private Observer<List<String>> getObserver() {
        return new Observer<List<String>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, " onSubscribe : " + d.isDisposed());
            }

            @Override
            public void onNext(List<String> value) {
                textView.append(" onNext : value size : " + value.size());
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " onNext : value : " + value);
            }

            @Override
            public void onError(Throwable e) {
                textView.append(" onError : " + e.getMessage());
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " onError : " + e.getMessage());
            }

            @Override
            public void onComplete() {
                textView.append(" onComplete");
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " onComplete");
            }
        };
    }


}