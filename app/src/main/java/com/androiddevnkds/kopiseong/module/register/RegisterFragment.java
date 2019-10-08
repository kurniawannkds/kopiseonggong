package com.androiddevnkds.kopiseong.module.register;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.androiddevnkds.kopiseong.R;
import com.androiddevnkds.kopiseong.data.DataManager;
import com.androiddevnkds.kopiseong.databinding.FragmentRegisterBinding;
import com.androiddevnkds.kopiseong.model.ListUserRoleModel;
import com.androiddevnkds.kopiseong.model.UserRoleModel;
import com.androiddevnkds.kopiseong.module.login.LoginFragment;
import com.androiddevnkds.kopiseong.model.UserInfoModel;
import com.androiddevnkds.kopiseong.module.register.model.RegisterInteractor;
import com.androiddevnkds.kopiseong.module.stock.StockActivity;
import com.androiddevnkds.kopiseong.module.transaction.TransactionActivity;
import com.androiddevnkds.kopiseong.utils.FragmentHelper;
import com.androiddevnkds.kopiseong.utils.HeaderHelper;
import com.androiddevnkds.kopiseong.utils.K;
import com.androiddevnkds.kopiseong.module.home.HomeActivity;
import com.androiddevnkds.kopiseong.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends BaseFragment implements RegisterContract.registerView{

    private FragmentRegisterBinding mBinding;
    private Context mContext;
    private String name ="", email ="", password = "", confirmPass = "", branch = "PUSAT", mUserRole = "";
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
        HeaderHelper.setLabelContentText("Regis New User");
        HeaderHelper.setRelativeContentVisible(true);
        HeaderHelper.setLabelContentVisible(true);
        registerPresenter.getAllUserRole();
    }

    @Override
    public void initEvent() {

        mBinding.lyBottomNav.navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.home_menu:
                        Intent intent = new Intent(mContext, HomeActivity.class);
                        startActivity(intent);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Objects.requireNonNull(getActivity()).finish();
                        }
                        return true;
                    case R.id.transaction_menu:
                        Intent intentTrans = new Intent(mContext, TransactionActivity.class);
                        intentTrans.putExtra(K.KEY_STOCK, K.VALUE_KEY_STOCK_WAREHOUSE);
                        startActivity(intentTrans);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Objects.requireNonNull(getActivity()).finish();
                        }

                        return true;
                    case R.id.stock_menu:
                        Intent intentStock = new Intent(mContext, StockActivity.class);
                        intentStock.putExtra(K.KEY_STOCK, K.VALUE_KEY_STOCK_WAREHOUSE);
                        startActivity(intentStock);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Objects.requireNonNull(getActivity()).finish();
                        }

                        return true;
                }

                return false;
            }
        });

        mBinding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = mBinding.etUserName.getText().toString().trim();
                email = mBinding.etEmail.getText().toString().trim();
                password = mBinding.etUserPassword.getText().toString().trim();
                confirmPass = mBinding.etUserConfirmPassword.getText().toString().trim();


                registerInteractor.setUserEmail(email);
                registerInteractor.setUserPass(password);
                registerInteractor.setUserPassConfirmation(confirmPass);
                registerInteractor.setUserBranch(branch);
                registerInteractor.setUserRole(mUserRole);
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

    @Override
    public void showUserRole(List userRole) {

        ArrayAdapter<ListUserRoleModel> userRoleAdapter = new ArrayAdapter<ListUserRoleModel>(mContext,
                android.R.layout.simple_spinner_item, userRole);
        userRoleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spRole.setAdapter(userRoleAdapter);

        mBinding.spRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ListUserRoleModel listUserRoleModel =(ListUserRoleModel) parent.getSelectedItem();
                mUserRole = listUserRoleModel.getUserRole();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //end
}
