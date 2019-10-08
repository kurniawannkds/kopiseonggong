package com.androiddevnkds.kopiseong.module.jurnal;

import com.androiddevnkds.kopiseong.model.JurnalModel;

public interface JurnalContract {

    interface jurnalView{

        void showProgressBar();

        void hideProgressBar();

        void onFailed(String message);

        void showAllJurnalPerMonth(JurnalModel jurnalModel);
    }

    interface jurnalPresenter{

        void getAllJurnalPerMonth(String month);
    }
}
