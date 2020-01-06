package com.tysq.ty_android.feature.aboutUs;

import com.bit.presenter.BasePresenter;
import com.tysq.ty_android.net.RetrofitFactory;
import com.tysq.ty_android.net.config.NetConfig;
import com.tysq.ty_android.net.rx.RxObservableSubscriber;
import com.tysq.ty_android.utils.TyUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import response.UpdateResp;

public final class AboutUsPresenter extends BasePresenter<AboutUsView> {
    @Inject
    public AboutUsPresenter(AboutUsView view) {
        super(view);
    }

    public void getUpdateInfo() {
        RetrofitFactory
                .getApiService()
                .getUpdateInfo(NetConfig.getUpdateUrl())
                .doOnNext(updateResp -> {
                    int type = TyUtils.checkUpdateInfo(updateResp.getMinimumSupportVersion(),
                            updateResp.getLatestVersion());
                    updateResp.setUpdateType(type);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxObservableSubscriber<UpdateResp>(mySelf) {
                    @Override
                    protected void onError(int code, String message) {
                        showErrorMsg(code,message);
                        mView.onGetUpdateInfoError();
                    }

                    @Override
                    protected void onSuccessRes(UpdateResp value) {
                        mView.onGetUpdateInfo(value);
                    }
                });
    }
}
