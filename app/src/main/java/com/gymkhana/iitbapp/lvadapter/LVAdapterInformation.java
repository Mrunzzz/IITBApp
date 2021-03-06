package com.gymkhana.iitbapp.lvadapter;

/**
 * Created by Bijoy on 5/27/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gymkhana.iitbapp.R;
import com.gymkhana.iitbapp.items.InformationItem;
import com.gymkhana.iitbapp.util.Functions;

import java.util.List;

/*Listview adapter for the navigation drawer listview*/

public class LVAdapterInformation extends ArrayAdapter<InformationItem> {
    private final Context mContext;
    private final List<InformationItem> mValues;
    private final Integer mLayoutId;

    public LVAdapterInformation(Context context, List<InformationItem> values) {
        super(context, R.layout.information_list_item_layout, values);
        this.mLayoutId = R.layout.information_list_item_layout;
        this.mContext = context;
        this.mValues = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        InformationViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(mLayoutId, parent, false);
            viewHolder = new InformationViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.logo = (ImageView) convertView.findViewById(R.id.logo);
            viewHolder.phone = (ImageView) convertView.findViewById(R.id.phone_icon);
            viewHolder.email = (ImageView) convertView.findViewById(R.id.email_icon);
            viewHolder.website = (ImageView) convertView.findViewById(R.id.website_icon);
            viewHolder.facebook = (ImageView) convertView.findViewById(R.id.facebook_icon);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (InformationViewHolder) convertView.getTag();
        }

        View.OnClickListener unresponsiveClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
        };

        final InformationItem data = mValues.get(position);

        if (data != null) {
            viewHolder.logo.setImageResource(data.img_resource);
            viewHolder.title.setText(data.title);
            viewHolder.description.setText(data.description);
            if (!data.phone.contentEquals("")) {
                viewHolder.phone.setImageResource(R.drawable.info_icon_phone);
                viewHolder.phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
                        mBuilder.setTitle(R.string.make_call)
                                .setMessage(mContext.getString(R.string.make_call_message) + " " + data.title)
                                .setPositiveButton(R.string.call, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        try {
                                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + data.phone));
                                            mContext.startActivity(intent);
                                        } catch (Exception e) {
                                            Log.e("PHONE_CALL_INTENT", "Could make call", e);
                                        }
                                    }
                                }).create().show();
                    }
                });
            } else {
                viewHolder.phone.setImageResource(R.drawable.info_icon_phone_disabled);
                viewHolder.phone.setOnClickListener(unresponsiveClickListener);
            }

            if (!data.email.contentEquals("")) {
                viewHolder.email.setImageResource(R.drawable.info_icon_email);
                viewHolder.email.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("plain/text");
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{data.email});
                        intent.putExtra(Intent.EXTRA_SUBJECT, "");
                        intent.putExtra(Intent.EXTRA_TEXT, "");
                        mContext.startActivity(Intent.createChooser(intent, ""));
                    }
                });
            } else {
                viewHolder.email.setImageResource(R.drawable.info_icon_email_disabled);
                viewHolder.email.setOnClickListener(unresponsiveClickListener);
            }

            if (!data.website.contentEquals("")) {
                viewHolder.website.setImageResource(R.drawable.info_icon_website);
                viewHolder.website.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Functions.openWebsite(mContext, data.website);
                    }
                });
            } else {
                viewHolder.website.setImageResource(R.drawable.info_icon_website_disabled);
                viewHolder.website.setOnClickListener(unresponsiveClickListener);
            }

            if (!data.facebook.contentEquals("")) {
                viewHolder.facebook.setImageResource(R.drawable.info_icon_facebook);
                viewHolder.facebook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.facebook));
                        mContext.startActivity(browserIntent);
                    }
                });
            } else {
                viewHolder.facebook.setImageResource(R.drawable.info_icon_facebook_disabled);
                viewHolder.facebook.setOnClickListener(unresponsiveClickListener);
            }
        }

        return convertView;
    }

    public class InformationViewHolder {
        TextView title, description;
        ImageView logo, phone, email, website, facebook;
    }
}