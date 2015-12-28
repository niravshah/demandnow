package com.demandnow.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.demandnow.GDNApiHelper;
import com.demandnow.GDNBaseActivity;
import com.demandnow.GDNSharedPrefrences;
import com.demandnow.GDNVolleySingleton;
import com.demandnow.R;
import com.devmarvel.creditcardentry.library.CreditCardForm;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.AuthenticationException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nirav on 28/12/2015.
 */
public class AddPaymentMethodActivity extends GDNBaseActivity implements View.OnClickListener {

    private CreditCardForm mCreditCardForm;
    private Button mAddPayment;
    private TextView mErrorMessages;
    private ProgressDialog mProgressDialog;
    private String TAG = "AddPaymentMethod";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment_method);
        renderNavigationDrawer();
        renderChildActivityToolbar();
        mCreditCardForm = (CreditCardForm) findViewById(R.id.credit_card_form);
        mAddPayment = (Button) findViewById(R.id.add_payment_method);
        mErrorMessages = (TextView) findViewById(R.id.validation_messages);
        mErrorMessages.setVisibility(View.INVISIBLE);
        mAddPayment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_payment_method:
                validateCreditCardEntry();
                break;
        }
    }

    private void validateCreditCardEntry() {


        Card card = new Card(mCreditCardForm.getCreditCard().getCardNumber(), mCreditCardForm.getCreditCard().getExpMonth(), mCreditCardForm.getCreditCard().getExpYear(), mCreditCardForm.getCreditCard().getSecurityCode());

        if ( !card.validateCard() ) {
            mErrorMessages.setText("Card Number Not Valid");
            mErrorMessages.setVisibility(View.VISIBLE);
        }else if(!card.validateCVC()){
            mErrorMessages.setText("Card CVC Not Valid");
            mErrorMessages.setVisibility(View.VISIBLE);
        }else{
            showProgress();
            try {
                Stripe stripe = new Stripe("pk_test_vVoeLIOZx6T1bAsdhogwJLyG");
                stripe.createToken(
                        card,
                        new TokenCallback() {
                            public void onSuccess(Token token) {
                                mProgressDialog.setMessage("Payment Method Validated. Saving to Server. Please Wait...");
                                mProgressDialog.setProgress(75);
                                sendTokenToServer(token.getId());
                            }


                            public void onError(Exception error) {
                                hideProgress();
                                Toast.makeText(getApplicationContext(),error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                );
            } catch (AuthenticationException e) {
                e.printStackTrace();
            }

        }
    }

    private void sendTokenToServer(String token) {

        JSONObject data = new JSONObject();
        try {
            data.put("stripeToken", token);
            data.put("token", GDNSharedPrefrences.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, GDNApiHelper.ADD_PAYMENT_METHOD_URL, data, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideProgress();
                        Toast.makeText(getApplicationContext(),"Payment Method Validated. Thanks.",Toast.LENGTH_LONG).show();
                        GDNSharedPrefrences.setPaymentVerified(true);
                        NavUtils.navigateUpFromSameTask(AddPaymentMethodActivity.this);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgress();
                        Toast.makeText(getApplicationContext(),"Error Adding Payment Method.",Toast.LENGTH_LONG).show();
                        Log.e(TAG, error.getLocalizedMessage() + error.getMessage());
                    }
                });

        GDNVolleySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }


    private void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgress(25);
            mProgressDialog.setMessage("Validating Payment Method. Please Wait...");
        }

        mProgressDialog.show();
    }

    private void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
