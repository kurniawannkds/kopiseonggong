package com.androiddevnkds.kopiseong.module.login;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androiddevnkds.kopiseong.R;

import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.databinding.FragmentLoginBinding;
import com.androiddevnkds.kopiseong.model.UserInfoModel;
import com.androiddevnkds.kopiseong.module.login.model.LoginCredential;
import com.androiddevnkds.kopiseong.utils.FragmentHelper;
import com.androiddevnkds.kopiseong.utils.HeaderHelper;
import com.androiddevnkds.kopiseong.utils.K;
import com.androiddevnkds.kopiseong.module.home.HomeActivity;
import com.androiddevnkds.kopiseong.BaseFragment;
import com.androiddevnkds.kopiseong.module.register.RegisterFragment;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment implements LoginContract.loginView{

    private FragmentLoginBinding mBinding;
    private Context mContext;
    private String email = "", password = "";
    private LoginPresenter loginPresenter;

    public LoginFragment() {
        // Required empty public constructor
        setArguments(new Bundle());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (getArguments() != null) {
            if (getArguments().containsKey(K.KEY_MAIN_FIRST_TIME)){

            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        loginPresenter = new LoginPresenter(this);

        initUI();
        initEvent();

        return mBinding.getRoot();
    }

    @Override
    public void initUI() {

        HeaderHelper.initialize(mBinding.getRoot());
        HeaderHelper.setLabelText("Login");
        HeaderHelper.setLabelVisible(true);
    }

    @Override
    public void initEvent() {

        mBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                email = mBinding.etUserName.getText().toString().trim();
                password = mBinding.etUserPassword.getText().toString().trim();

                LoginCredential loginCredential = new LoginCredential(email,password);
                loginPresenter.login(loginCredential);

            }
        });

//            mBinding.tvRegister.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    FragmentHelper.fragmentChanger(R.id.fl_fragment_container,
//                            ((AppCompatActivity) mContext).getSupportFragmentManager(),
//                            new RegisterFragment(), null, false);
//                }
//            });

    }

//    @Override
//    protected void successLogin(UserInfoModel userResponseModel) {
//        super.successLogin(userResponseModel);
//
//
//
//
//
//    }

//    @Override
//    protected void failedLogin(String message) {
//        super.failedLogin(message);
//
//        Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
//
//    }

    @Override
    public void showProgressBar() {

        mBinding.viewBackground.setVisibility(View.VISIBLE);
        mBinding.pbBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {

        mBinding.viewBackground.setVisibility(View.GONE);
        mBinding.pbBar.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess() {

        Intent intent = new Intent(mContext, HomeActivity.class);
        startActivity(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getActivity()).finish();
        }
    }

    @Override
    public void onFailed(int tipe,String message) {

        //email
        if(tipe==1){

            mBinding.etUserName.setError(message);
        }
        else if(tipe==2){
            mBinding.etUserPassword.setError(message);
        }
        else {
            Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
        }

        hideProgressBar();
    }
}
