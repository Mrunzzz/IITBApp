package com.iitblive.iitblive.internet;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.iitblive.iitblive.MainActivity;
import com.iitblive.iitblive.R;
import com.iitblive.iitblive.util.Constants;
import com.iitblive.iitblive.util.Functions;
import com.iitblive.iitblive.util.LoginFunctions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;


/**
 * Created by Bijoy on 5/25/2015.
 */

public class FormTask extends AsyncTask<String, Integer, String> {

  public static int FORM_MODE_LDAP_LOGIN = 0;

  private Context mContext;
  private List<NameValuePair> mFormData;
  private String mLink;
  private Integer mMode;

  public FormTask(
      Context context,
      String link,
      int mode,
      List<NameValuePair> nameValuePairs) {
    this.mContext = context;
    this.mFormData = nameValuePairs;
    this.mLink = link;
    this.mMode = mode;
  }

  @Override
  protected String doInBackground(String... params) {
    return getData();
  }

  public String getData() {
    HttpClient httpClient = new DefaultHttpClient();
    HttpContext localContext = new BasicHttpContext();

    HttpPost httpPost = new HttpPost(mLink);
    httpPost.setHeader(Constants.SERVER_REFERER, Constants.BASE_URL + mLink);

    try {
      httpPost.setEntity(new UrlEncodedFormEntity(mFormData));
      HttpResponse httpResponse = httpClient.execute(httpPost, localContext);
      try {
        int status = httpResponse.getStatusLine().getStatusCode();
        if (status == 200) {
          HttpEntity e = httpResponse.getEntity();
          String data = EntityUtils.toString(e);
          return data;
        } else {
          HttpEntity e = httpResponse.getEntity();
          String data = EntityUtils.toString(e);
          Functions.makeToast(mContext, R.string.toast_no_connection);
          return data;
        }
      } catch (Exception e) {
        Functions.makeToast(mContext, R.string.toast_no_data);
      }
    } catch (ClientProtocolException e) {
      Functions.makeToast(mContext, R.string.toast_no_data);
    } catch (IOException e) {
      Functions.makeToast(mContext, R.string.toast_no_data);
    } catch (Exception e) {
      Functions.makeToast(mContext, R.string.toast_no_data);
    }
    return null;
  }

  protected void onPostExecute(String data) {
    if (data == null || data.contentEquals("")) {
      return;
    }

    if (mMode == FORM_MODE_LDAP_LOGIN) {
      if (!data.equals("FALSE")) {
        String[] lines = data.split("\n");
        if (lines[0].equals("TRUE")) {
          String ldap = lines[1];
          String name = lines[2];

          Functions.saveSharedPreference(mContext, Constants.SP_USERNAME, ldap);
          Functions.saveSharedPreference(mContext, Constants.SP_NAME, name);
          LoginFunctions.userLoggedIn(mContext);

          Intent intent = new Intent(mContext, MainActivity.class);
          mContext.startActivity(intent);
        }
      }
    }

  }

}
