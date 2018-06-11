package com.example.boulocalix.a1click1leave.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.boulocalix.a1click1leave.R;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class UpdateNavHeaderUtil {

    static public void updateUI(LinearLayout activity, Context context) {
        SharePrefer sharePrefer = new SharePrefer(context) ;
        TextView userName = activity.findViewById(R.id.user_name_header);
        TextView userAddress = activity.findViewById(R.id.user_address);
        ImageView avatar = activity.findViewById(R.id.imageView);
            userName.setText(sharePrefer.getFullName());
            userAddress.setText(sharePrefer.getEmail());
            Transformation transformation = new RoundedTransformationBuilder()
                    .borderColor(Color.WHITE)
                    .borderWidthDp(2)
                    .oval(true)
                    .build();
            Picasso.get().load(sharePrefer.getPicture()).transform(transformation).into(avatar);
        }
}
