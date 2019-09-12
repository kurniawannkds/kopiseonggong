package com.androiddevnkds.kopiseong.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androiddevnkds.kopiseong.R;

public class HeaderHelper {

    private static View parentView;

    public static TextView tvHeaderLabel, tvHeaderLabelContent;
    public static LinearLayout linearLayoutAtas, linearReport;
    public static RelativeLayout relativeLayoutAwal;

    public static void initialize(View parentView) {
        generateView(parentView);
    }

    public static void initialize(Context context, View parentView) {
        generateView(parentView);
    }

    public static void initialize(Context context, int resId) {
        LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resId, null);
        generateView(view);
    }

    private static void generateView(View mParentView) {
        parentView = mParentView;

        tvHeaderLabel = (TextView) parentView.findViewById(R.id.tv_header_label);
        tvHeaderLabelContent = (TextView) parentView.findViewById(R.id.tv_header_label_content);
        linearLayoutAtas = (LinearLayout) parentView.findViewById(R.id.linear_atas);
        linearReport = (LinearLayout) parentView.findViewById(R.id.reportLinearTitle2);
        relativeLayoutAwal = (RelativeLayout) parentView.findViewById(R.id.relative_awal);

    }

    //Title region
    public static void setLabelVisible(boolean visible) {
        if (tvHeaderLabel != null)
            tvHeaderLabel.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public static void setLabelText(String title) {
        if (title != null && tvHeaderLabel != null)
            tvHeaderLabel.setText(title);
    }

    //Title region
    public static void setLabelContentVisible(boolean visible) {
        if (tvHeaderLabelContent != null)
            tvHeaderLabelContent.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public static void setLabelContentText(String title) {
        if (title != null && tvHeaderLabelContent != null)
            tvHeaderLabelContent.setText(title);
    }

    //linear
    public static void setLinearContentVisible(boolean visible) {
        if (linearLayoutAtas != null)
            linearLayoutAtas.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public static void setRelativeContentVisible(boolean visible) {
        if (relativeLayoutAwal != null)
            relativeLayoutAwal.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public static void setLinearReportVisible(boolean visible) {
        if (linearReport != null)
            linearReport.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
