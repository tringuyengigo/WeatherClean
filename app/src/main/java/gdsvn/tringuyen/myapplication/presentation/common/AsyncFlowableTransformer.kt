package gdsvn.tringuyen.myapplication.presentation.common

import gdsvn.tringuyen.myapplication.domain.common.FlowableRxTransformer
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher

class AsyncFlowableTransformer<T> : FlowableRxTransformer<T>() {
    override fun apply(upstream: Flowable<T>): Publisher<T> {
        return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}