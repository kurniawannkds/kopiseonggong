package com.androiddevnkds.kopiseong.module.register;


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
import com.androiddevnkds.kopiseong.databinding.FragmentRegisterBinding;
import com.androiddevnkds.kopiseong.module.login.LoginFragment;
import com.androiddevnkds.kopiseong.model.UserInfoModel;
import com.androiddevnkds.kopiseong.module.register.model.RegisterInteractor;
import com.androiddevnkds.kopiseong.utils.FragmentHelper;
import com.androiddevnkds.kopiseong.utils.HeaderHelper;
import com.androiddevnkds.kopiseong.utils.K;
import com.androiddevnkds.kopiseong.module.home.HomeActivity;
import com.androiddevnkds.kopiseong.BaseFragment;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends BaseFragment implements RegisterContract.registerView{

    private FragmentRegisterBinding mBinding;
    private Context mContext;
    private String name ="", email ="", password = "", confirmPass = "", branch = "";
    private RegisterPresenter registerPresenter;
    private RegisterInteractor registerInteractor;

    public RegisterFragment() {
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        registerPresenter = new RegisterPresenter(this);
        registerInteractor = new RegisterInteractor();

        initUI();
        initEvent();

        return mBinding.getRoot();
    }

    @Override
    public void initUI() {

        HeaderHelper.initialize(mBinding.getRoot());
        HeaderHelper.setLabelText("Register");
        HeaderHelper.setLabelVisible(true);
    }

    @Override
    public void initEvent() {

        mBinding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentHelper.fragmentChanger(R.id.fl_fragment_container,
                        ((AppCompatActivity) mContext).getSupportFragmentManager(),
                        new LoginFragment(), null, false);
            }
        });

        mBinding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = mBinding.etUserName.getText().toString().trim();
                email = mBinding.etEmail.getText().toString().trim();
                password = mBinding.etUserPassword.getText().toString().trim();
                confirmPass = mBinding.etUserConfirmPassword.getText().toString().trim();
                branch = mBinding.etUserBranch.getText().toString().trim();

                registerInteractor.setUserEmail(email);
                registerInteractor.setUserPass(password);
                registerInteractor.setUserPassConfirmation(confirmPass);
                registerInteractor.setUserBranch(branch);
                registerInteractor.setUserRole(K.KEY_ROLE_ADMIN);
                registerInteractor.setUserMembership(K.KEY_NOT_MEMBERSHIP);

                registerPresenter.registerUser(registerInteractor);
            }
        });

        //end init
    }


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
    public void onFailed(int tipe, String message) {

        //email
        if(tipe==1){
            mBinding.etEmail.setError(message);
        }
        else if(tipe==2){
            mBinding.etUserPassword.setError(message);
        }

        else if(tipe==3){
            mBinding.etUserName.setError(message);
        }

        else if(tipe==4){
            mBinding.etUserBranch.setError(message);
        }

        else if(tipe==5){

            mBinding.etUserConfirmPassword.setError(message);
        }

        else if(tipe==6){

            mBinding.etUserPassword.setError(message);
            mBinding.etUserConfirmPassword.setError(message);
        }

        else if(tipe==7){

            Toast.makeText(mContext,message,Toast.LENGTH_SHORT).show();
        }
    }

    //end
}
