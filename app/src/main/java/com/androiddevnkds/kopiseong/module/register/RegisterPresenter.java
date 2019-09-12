package com.androiddevnkds.kopiseong.module.register;

import android.text.TextUtils;
import android.util.Patterns;

import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.model.UserInfoModel;
import com.androiddevnkds.kopiseong.module.register.model.RegisterInteractor;
import com.androiddevnkds.kopiseong.utils.K;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

public class RegisterPresenter implements RegisterContract.registerPresenter {

    private RegisterContract.registerView registerView;

    public RegisterPresenter(RegisterContract.registerView registerView){
        this.registerView = registerView;
    }

    @Override
    public void onSuccess() {

        registerView.hideProgressBar();
        registerView.onSuccess();
    }

    @Override
    public void onFailed(int tipe, String message) {

        registerView.hideProgressBar();
        registerView.onFailed(tipe,message);
    }

    public void registerUser(final RegisterInteractor registerInteractor){

        if(validateInput(registerInteractor)){

        }
        else {
            //hit API
            AndroidNetworking.post(K.URL_REGISTER_USER)

                    .addBodyParameter(registerInteractor)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsObject(RegisterInteractor.class, new ParsedRequestListener<RegisterInteractor>() {
                        @Override
                        public void onResponse(RegisterInteractor user) {
                            // do anything with response
                            if(user.getErrorMessage()!=null){
                                onFailed(7,user.getErrorMessage());
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
                            onFailed(7,"ERROR");
                        }
                    });
        }
    }

    private boolean validateInput(RegisterInteractor registerInteractor){

        String userEmail = registerInteractor.getUserEmail();
        String password = registerInteractor.getUserPass();
        String confirmPass = registerInteractor.getUserPassConfirmation();
        String nameUser = registerInteractor.getNameUser();
        String userBranch = registerInteractor.getUserBranch();
        String userRole = registerInteractor.getUserRole();
        String userMembership = registerInteractor.getUserMembership();
        if(TextUtils.isEmpty(userEmail)){


            onFailed(1,"Email cannot be empty");
            return true;
        }
        else if(TextUtils.isEmpty(password)){

            onFailed(2,"Password cannot be empty");
            return true;
        }

        else if(TextUtils.isEmpty(nameUser)){

            onFailed(3,"Username cannot be empty");
            return true;
        }

        else if(TextUtils.isEmpty(userBranch)){

            onFailed(4,"Userbranch cannot be empty");
            return true;
        }

        else if(TextUtils.isEmpty(userRole)){

            onFailed(7,"UserRole cannot be empty");
            return true;
        }

        else if(TextUtils.isEmpty(userMembership)){

            onFailed(7,"UserMembership cannot be empty");
            return true;
        }

        else if(TextUtils.isEmpty(confirmPass)){

            onFailed(5,"Confirm password cannot be empty");
            return true;
        }

        else if(!confirmPass.equalsIgnoreCase(password)){

            onFailed(6,"Password and confirm password didn't match");
            return true;
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){

            onFailed(1,"Email wrong format");
            return true;
        }

        else {

            return false;
        }
    }
}
