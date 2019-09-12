package com.androiddevnkds.kopiseong.module.login;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.model.UserInfoModel;
import com.androiddevnkds.kopiseong.module.login.model.LoginCredential;
import com.androiddevnkds.kopiseong.module.register.RegisterFragment;
import com.androiddevnkds.kopiseong.module.register.model.RegisterInteractor;
import com.androiddevnkds.kopiseong.utils.FragmentHelper;
import com.androiddevnkds.kopiseong.utils.K;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

public class LoginPresenter implements LoginContract.loginPresenter {

    private LoginContract.loginView loginView;

    public LoginPresenter(LoginContract.loginView loginView){
        this.loginView = loginView;
    }

    @Override
    public void onSuccess() {

        loginView.hideProgressBar();
        loginView.onSuccess();
    }

    @Override
    public void onFailed(int tipe,String message) {

        loginView.hideProgressBar();
        loginView.onFailed(tipe,message);
    }

    //method login
    public void login(LoginCredential loginCredential){

        loginView.showProgressBar();
        if(hasError(loginCredential)){

        }
        else {
            //hit api
            AndroidNetworking.post(K.URL_LOGIN)
                    .addBodyParameter("user_email",loginCredential.getUserName())
                    .addBodyParameter("user_pass",loginCredential.getUserPassword())
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(RegisterInteractor.class, new ParsedRequestListener<RegisterInteractor>() {
                        @Override
                        public void onResponse(RegisterInteractor user) {
                            // do anything with response
                            if(user.getErrorMessage()!=null){
                                onFailed(3,user.getErrorMessage());
                            }
                            else {
                                DataManager.can().setUserStatusToStorage(true);
                                DataManager.can().setUserInfoToStorage(user);
                                onSuccess();
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
                            // handle error
                            onFailed(3,"ERROR");
                            Log.e("base",anError.getErrorDetail());
                        }
                    });
        }

    }

    //validasi
    private boolean hasError(LoginCredential loginCredential){

        String userName = loginCredential.getUserName();
        String password = loginCredential.getUserPassword();
        if(TextUtils.isEmpty(userName)){


            onFailed(1,"Email cannot be empty");
            return true;
        }
        else if(TextUtils.isEmpty(password)){

            onFailed(2,"Password cannot be empty");
            return true;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(userName).matches()){

            onFailed(1,"Email wrong format");
            return true;
        }

        else {

            return false;
        }


    }
}
