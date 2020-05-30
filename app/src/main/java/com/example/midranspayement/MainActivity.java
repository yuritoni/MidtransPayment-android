package com.example.midranspayement;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.midtrans.sdk.corekit.BuildConfig;
import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentMethod;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.BillInfoModel;
import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ExpiryModel;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.PaymentMethods;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.midtrans.sdk.uikit.scancard.ExternalScanner;
import com.midtrans.sdk.uikit.scancard.ScannerModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TransactionFinishedCallback{
    private static final int SCAN_REQUEST_CODE = 10004;
    private Button buttonUiKit, buttonIndomaret,buttonDirectCreditCard, buttonDirectBcaVa, buttonDirectMandiriVa,
            buttonDirectBniVa, buttonDirectAtmBersamaVa, buttonDirectPermataVa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        // SDK init for UIflow

        initSDK();
        initActionButtons();
    }

    private TransactionRequest initTransactionRequest(){
        // create new transaction
        TransactionRequest transactionRequestNew = new TransactionRequest(System.currentTimeMillis() +"", 20000);

        // set custom details
        transactionRequestNew.setCustomerDetails(initCustomerDetails());

        // set items details
        ItemDetails itemDetails = new ItemDetails("1", 25000, 1, "Trekking Shoes");

        // Add item details into item details list
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        transactionRequestNew.setItemDetails(itemDetailsArrayList);

        // Create creaditcard options for payment
        CreditCard creditCard = new CreditCard();
        creditCard.setSaveCard(false); // when using one/two click set to true and if normal set false

        // this method deprecated use setAuthentication instead
        // creditCard.setChannel(CreditCard.MIGS); // set chanel migs

        creditCard.setBank(BankType.BCA); // set spesific acquiring bank

        transactionRequestNew.setCreditCard(creditCard);
        BillInfoModel billInfoModel = new BillInfoModel("demo_label", "demo_value");
        transactionRequestNew.setBillInfoModel(billInfoModel);
        ExpiryModel expiryModel = new ExpiryModel();
        expiryModel.setStartTime(Utils.getFormattedTime(System.currentTimeMillis()));
        expiryModel.setDuration(1);
        expiryModel.setUnit(ExpiryModel.UNIT_MINUTE);
        transactionRequestNew.setExpiry(expiryModel);
        return transactionRequestNew;
    }

    private CustomerDetails initCustomerDetails(){

        //define customer detail (mandatory for coreflow)
        CustomerDetails mCustomerDetails = new CustomerDetails();
        mCustomerDetails.setPhone("082249167841");
        mCustomerDetails.setFirstName("user fullname");
        mCustomerDetails.setEmail("mail@mail.com");

        return mCustomerDetails;
    }

    private void initActionButtons() {

        buttonUiKit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this);
            }
        });

        buttonIndomaret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show progress dialog
                //  dialog.show();
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.INDOMARET);
             /*   MidtransSDK.getInstance().paymentUsingIndomaret(
                        MidtransSDK.getInstance().readAuthenticationToken(), new TransactionCallback() {

                            @Override
                            public void onSuccess(TransactionResponse response) {
                               actionTransactionSuccess(response);
                            }

                            @Override
                            public void onFailure(TransactionResponse response, String reason) {
                               // actionTransactionFailure(response, reason);
                            }

                            @Override
                            public void onError(Throwable error) {
                                //actionTransactionError(error);
                            }
                        });
            }*/
            }
        });

        buttonDirectCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().UiCardRegistration(MainActivity.this, new CardRegistrationCallback() {
                    @Override
                    public void onSuccess(CardRegistrationResponse cardRegistrationResponse) {
                        Toast.makeText(MainActivity.this, "Register card toke success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(CardRegistrationResponse cardRegistrationResponse, String s) {
                        Toast.makeText(MainActivity.this, "Register card toke failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                });
            }
        });

        buttonDirectBcaVa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.BANK_TRANSFER_BCA);
            }
        });

        buttonDirectBniVa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.BANK_TRANSFER_BNI);
            }
        });

        buttonDirectMandiriVa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.BANK_TRANSFER_MANDIRI);
            }
        });

        buttonDirectPermataVa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.BANK_TRANSFER_PERMATA);
            }
        });

        buttonDirectAtmBersamaVa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(MainActivity.this, PaymentMethod.BCA_KLIKPAY);
            }
        });

    }



    private void finishWithResult(int resultCode, Intent data) {
        setResult(resultCode, data);
        finish();
    }

    private void initSDK() {
        String client_key = "CLIENT_KEY_ANDA";
        String base_url = "https://api.sandbox.midtrans.com/v2/";
        String MERCHANT_NAME = "";

        ExternalScanner  externalScanner =new ExternalScanner() {
            @Override
            public void startScan(Activity activity, int i) {


            }
        };
        SdkUIFlowBuilder.init()
                .setClientKey(client_key) // client_key is mandatory
                .setContext(this) // context is mandatory
                .setTransactionFinishedCallback( this) // set transaction finish callback (sdk callback)
                .setMerchantBaseUrl(base_url) //set merchant url
                .enableLog(true) // enable sdk log
                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // will replace theme on snap theme on MAP
                .buildSDK();
        MidtransSDK.getInstance().setClientKey(client_key);
        MidtransSDK.getInstance().setBoldText("open_sans_bold.ttf");
        MidtransSDK.getInstance().setDefaultText("open_sans_regular.ttf");
        MidtransSDK.getInstance().setMerchantName("Pembayaran");
        //MidtransSDK.getInstance().setMerchantLogo("");



        //        .enableLog(true)
        //        .setMerchantName(MERCHANT_NAME)
        //        .setExternalScanner(new ScanCard());
       // veritransBuilder.buildSDK();

        //VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
       // veritransSDK.setDefaultText("open_sans_regular.ttf");
        //veritransSDK.setSemiBoldText("open_sans_semibold.ttf");
        //veritransSDK.setBoldText("open_sans_bold.ttf");

      //  veritransSDK.setSelectedPaymentMethods(PaymentMethods.getAllPaymentMethods(this));
    }

    private UIKitCustomSetting customUI(boolean showStatus){
        UIKitCustomSetting uicustom = new UIKitCustomSetting();
        uicustom.setShowPaymentStatus(showStatus);
        return uicustom;
    }

    private void bindViews() {
        buttonUiKit = (Button) findViewById(R.id.btn_uikit);
        buttonIndomaret = (Button) findViewById(R.id.btn_indomaret);
        buttonDirectCreditCard = (Button) findViewById(R.id.btn_direct_credit_card);
        buttonDirectBcaVa = (Button) findViewById(R.id.btn_direct_bca_va);
        buttonDirectMandiriVa = (Button) findViewById(R.id.btn_direct_mandiri_va);
        buttonDirectBniVa = (Button) findViewById(R.id.btn_direct_bni_va);
        buttonDirectPermataVa = (Button) findViewById(R.id.btn_direct_permata_va);
        buttonDirectAtmBersamaVa = (Button) findViewById(R.id.btn_direct_atm_bersama_va);

    }


    @Override
    public void onTransactionFinished(TransactionResult result) {
        Log.d("finalx", "rsultd:" + result.getResponse());

        if (result.getResponse() != null) {
            Log.d("finalx", "result:" + result.getResponse().getStatusMessage());
            Log.d("finalx", "result>fraud:" + result.getResponse().getFraudStatus());
            switch (result.getStatus()) {
                case TransactionResult.STATUS_SUCCESS:
                    Toast.makeText(this, "Transaction Finished. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_PENDING:
                    Toast.makeText(this, "Transaction Pending. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(this, "Transaction Failed. ID: " + result.getResponse().getTransactionId() + ". Message: " + result.getResponse().getStatusMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
            result.getResponse().getValidationMessages();
        } else if (result.isTransactionCanceled()) {
            Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show();
        } else {
            if (result.getStatus().equalsIgnoreCase(TransactionResult.STATUS_INVALID)) {
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show();
            }
        }

    }
    private void actionTransactionSuccess(TransactionResponse response) {
        // Handle success transaction
      //  dialog.dismiss();
        Toast.makeText(this, "transaction successfull (" + response.getStatusMessage() + ")", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }


}
