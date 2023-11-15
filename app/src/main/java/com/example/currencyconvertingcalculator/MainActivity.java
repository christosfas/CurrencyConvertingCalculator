package com.example.currencyconvertingcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements ConvertDialogFragment.OnInputListener {
    private final String TAG = this.getClass().getSimpleName();
    private static final String BASE_URL = "http://data.fixer.io/api"; //free Fixer.io users can't have encryption so http instead of https
    private static final String API_KEY = "?access_key=6df9ecb75171c747c04f56681e0a1924"; //free Fixer.io API key

    private final OkHttpClient client = new OkHttpClient();

    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private Button b5;
    private Button b6;
    private Button b7;
    private Button b8;
    private Button b9;
    private Button b0;
    private Button b_equal;
    private Button b_multi;
    private Button b_divide;
    private Button b_add;
    private Button b_sub;
    private Button b_clear;
    private Button b_erase;
    private Button b_dot;
    private Button b_rpar;
    private Button b_lpar;
    private Button b_para1;
    private Button b_para2;
    private Button b_conv;
    private TextView t1;
    private TextView t2;

    private Map<String, String> currenciesMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewSetup();

        try {             //get supported currencies from fixer.io
            getSymbols();
        } catch (IOException e) {
            e.printStackTrace();
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exceedLength();
                t1.setText(t1.getText().toString() + "1");
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exceedLength();
                t1.setText(t1.getText().toString() + "2");
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exceedLength();
                t1.setText(t1.getText().toString() + "3");
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exceedLength();
                t1.setText(t1.getText().toString() + "4");
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exceedLength();
                t1.setText(t1.getText().toString() + "5");
            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exceedLength();
                t1.setText(t1.getText().toString() + "6");
            }
        });

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exceedLength();
                t1.setText(t1.getText().toString() + "7");
            }
        });

        b8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exceedLength();
                t1.setText(t1.getText().toString() + "8");
            }
        });

        b9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exceedLength();
                t1.setText(t1.getText().toString() + "9");
            }
        });

        b0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exceedLength();
                t1.setText(t1.getText().toString() + "0");
            }
        });

        b_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exceedLength();
                t1.setText(t1.getText().toString() + ".");
            }
        });

        b_para1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exceedLength();
                t1.setText(t1.getText().toString() + "%");
            }
        });

        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exceedLength();
                t1.setText(t1.getText().toString() + "+");
            }
        });

        b_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exceedLength();
                t1.setText(t1.getText().toString() + "-");
            }
        });

        b_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exceedLength();
                t1.setText(t1.getText().toString() + "*");
            }
        });

        b_divide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exceedLength();
                t1.setText(t1.getText().toString() + "/");
            }
        });

        b_para2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exceedLength();
                t1.setText(t1.getText().toString() + "^");
            }
        });

        b_lpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exceedLength();
                t1.setText(t1.getText().toString() + "(");
            }
        });

        b_rpar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exceedLength();
                t1.setText(t1.getText().toString() + ")");
            }
        });

        b_equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = t1.getText().toString();

                if(!text.matches(".*[a-zA-Z].*")) {  //check whether the given expression contains letters before evaluating (can happen after currency conversion)

                    Scanner sc = new Scanner(text);
                    List<ScannedToken> scanExp = sc.scan();
                    Parser parser = new Parser(scanExp);
                    List<ScannedToken> parsed;
                    parsed = parser.parse();
                    //Log.e("MAIN", String.valueOf(sc.evaluate(parsed)));
                    t2.setText(text);
                    try {
                        t1.setText(String.valueOf(ifReallyDecimal(sc.evaluate(parsed))));
                        exceedLength();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show(); //Catch exceptions caused by invalid expressions and show in toast
                        t1.setText("");

                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Invalid symbols!", Toast.LENGTH_LONG).show();
                    t1.setText("");
                    t2.setText("");
                }
            }
        });

        b_conv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = t1.getText().toString();
                if (isNetworkConnected()) {             //check connection to internet first
                    if (!text.isEmpty() && !text.matches(".*[a-zA-Z].*") && !Double.valueOf(text).equals(0.0)) {  //check if displayed value is suitable for conversion
                        if(currenciesMap!=null){
                            Bundle bundle = new Bundle();
                            bundle.putStringArray("currencies", currenciesMap.values().toArray(new String[0]));
                            // set Fragmentclass Arguments
                            ConvertDialogFragment dialog = new ConvertDialogFragment();
                            dialog.setArguments(bundle);
                            dialog.show(getSupportFragmentManager(), "Dialog");
                        }else{
                            //currencies map may be null if there was no internet connection during onCreate
                            Toast.makeText(getApplicationContext(), "No currencies available. Reconnecting with server...\n Try again later", Toast.LENGTH_LONG).show();

                            try {       //try getting the supported currencies now
                                getSymbols();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Cannot convert empty value or invalid symbols!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Conversion won't be possible due to lack of internet connection!", Toast.LENGTH_LONG).show();
                }
            }
        });

        b_erase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (t1.getText().length() > 0) {
                    CharSequence name = t1.getText().toString();
                    t1.setText(name.subSequence(0, name.length() - 1));
                    exceedLength();
                } else {
                    t1.setText("");
                    t2.setText("");
                }
            }
        });


        b_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1.setText("");
                t2.setText("");
            }
        });
    }

    private void viewSetup() {  //get all the views
        b1 = findViewById(R.id.button1);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        b4 = findViewById(R.id.button4);
        b5 = findViewById(R.id.button5);
        b6 = findViewById(R.id.button6);
        b7 = findViewById(R.id.button7);
        b8 = findViewById(R.id.button8);
        b9 = findViewById(R.id.button9);
        b0 = findViewById(R.id.button0);
        b_equal = findViewById(R.id.button_equal);
        b_multi = findViewById(R.id.button_multi);
        b_divide = findViewById(R.id.button_divide);
        b_add = findViewById(R.id.button_add);
        b_sub = findViewById(R.id.button_sub);
        b_clear = findViewById(R.id.button_clear);
        b_erase = findViewById(R.id.button_erase);
        b_dot = findViewById(R.id.button_dot);
        b_lpar = findViewById(R.id.button_lpar);
        b_rpar = findViewById(R.id.button_rpar);
        b_para1 = findViewById(R.id.button_para1);
        b_para2 = findViewById(R.id.button_para2);
        b_conv = findViewById(R.id.button_conv);
        t1 = findViewById(R.id.input);
        t2 = findViewById(R.id.output);
    }


    // Whether value is really a double or not & change representation accordingly
    private Number ifReallyDecimal(double N) {
        if(N == (int) N){
            return (int) N;
        } else {
            return N;
        }
    }

    // Make text smaller if too many digits and vice versa.
    private void exceedLength() {
        int length = t1.getText().toString().length();
        if ( length > 20) {
            t1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }else if(length > 10){
            t1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
        }else{
            t1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
        }
    }

    //get available currency symbols from Fixer.io endpoint
    public void getSymbols() throws IOException {
        final String[] responseString = new String[1];
        if(isNetworkConnected()) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpUrl.Builder urlBuilder
                                = Objects.requireNonNull(HttpUrl.parse(BASE_URL + "/symbols" + API_KEY)).newBuilder();

                        String url = urlBuilder.build().toString();

                        Request request = new Request.Builder()
                                .url(url)
                                .build();
                        Call call = client.newCall(request);
                        Response response = call.execute();
                        responseString[0] = Objects.requireNonNull(response.body()).string();

                        Log.i("SYMBOLS RESPONSE", String.valueOf(response.code()));


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ObjectMapper objectMapper = new ObjectMapper();

                    SymbolsResponse symbolsResponse = new SymbolsResponse();
                    try {
                        if(responseString[0]!=null){
                            symbolsResponse = objectMapper.readValue(responseString[0], SymbolsResponse.class);
                        }else{
                            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Currency conversion won't be available,\n Fixer-io server not responding", Toast.LENGTH_LONG).show());
                            return;
                        }
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    currenciesMap = symbolsResponse.getSymbols();

                    Log.i("Mapper", "Get Symbols Success: " + symbolsResponse.isSuccess());
                    Log.i("Mapper", "Number of symbols: " + currenciesMap.size());
                }
            });

            thread.start();
        }else{
            Toast.makeText(getApplicationContext(), "Currency conversion won't be available due to lack of internet connection!", Toast.LENGTH_LONG).show();
        }
    }

    //get latest rates and convert a given amount from a base to a target currency
    public double convertCurrency(String from, String to, double amount) throws IOException {
        final String[] responseString = new String[1];
        final double[] result = new double[1];

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpUrl.Builder urlBuilder
                            = Objects.requireNonNull(HttpUrl.parse(BASE_URL + "/latest" + API_KEY)).newBuilder();

                    String url = urlBuilder.build().toString();

                    Request request = new Request.Builder()
                            .url(url)
                            .build();
                    Call call = client.newCall(request);
                    Response response = call.execute();
                    responseString[0] = Objects.requireNonNull(response.body()).string();

                    Log.i("CONVERSION RESPONSE", String.valueOf(response.code()));
//                    Log.e("CONVERSION RESPONSE", response.body().string());


                } catch (Exception e) {
                    e.printStackTrace();
                }
                ObjectMapper objectMapper = new ObjectMapper();

                JsonNode jsonNode = null;
                try {
                    jsonNode = objectMapper.readTree(responseString[0]);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                // Get the "success" value as a boolean
                assert jsonNode != null;
                boolean success = jsonNode.get("success").asBoolean();
                //Get the rates to the default base (EURO) for the conversion
                double targetCurrencyRate = jsonNode.get("rates").get(to).asDouble();
                double startingCurrencyRate = 1;
                if(!jsonNode.get("base").asText().equals(from)) startingCurrencyRate = jsonNode.get("rates").get(from).asDouble();

                result[0] = amount / startingCurrencyRate * targetCurrencyRate;
                // Example usage
                Log.i("Mapper", "Conversion Success: " + success);
                Log.i("Mapper", "Result: " + result[0]);
            }
        });

        thread.start();

        while(result[0] == 0){  //delay because conversion and api call are executed on different threads
            new CountDownTimer(500, 500) {
                public void onFinish() {
                    // When timer is finished
                    // Execute your code here
                }

                public void onTick(long millisUntilFinished) {
                    // millisUntilFinished    The amount of time until finished.
                }
            }.start();
        }
        return result[0];

    }

    @Override
    public void sendInput(String from, String to)  //receive user input from conversion dialog and proceed to conversion
    {
        final double[] values = new double[2];
        values[0] = Double.parseDouble(t1.getText().toString());
        Map<String, String> inverseCurrenciesMap = invertMap(currenciesMap);
        Log.e(TAG, "sendInput: got the input: " + from + " -> " + to);
        from = inverseCurrenciesMap.get(from);
        to = inverseCurrenciesMap.get(to);
        try {
             values[1] = convertCurrency(from, to, values[0]);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        t2.setText(from + " " + values[0]);
        t1.setText(to + " " + values[1]);
        exceedLength();
    }

    public static <K, V> Map<V, K> invertMap(Map<K, V> sourceMap) {  //invert keys and values into new map
        return sourceMap.entrySet()
                .stream().collect(
                        Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (oldValue, newValue) -> oldValue)
                );
    }

    private boolean isNetworkConnected() {  //check internet connection status
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}