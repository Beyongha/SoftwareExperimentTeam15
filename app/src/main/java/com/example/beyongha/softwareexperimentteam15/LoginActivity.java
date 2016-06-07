package com.example.beyongha.softwareexperimentteam15;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/* naver library */
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginDefine;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


/* Java Libarary */
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;


public class LoginActivity extends AppCompatActivity {
    String email;
    String nickName;
    String encId;
    String profileImage;
    String age;
    String gender;
    String id;
    String name;
    String birthday;

    final static int ACT_EDIT = 0;

    private static final String TAG = "LoginActivity";

    /**
     * 네이버에서 제공하는 key 할당
     * @param savedInstanceState
     */
    private static String OAUTH_CLIENT_ID = "1yP82fQOQ2a1FhsyFPH6";
    private static String OAUTH_CLIENT_SECRET = "vRHd8kCPmb";
    private static String OAUTH_CLIENT_NAME = "MainActivity";

    private static OAuthLogin mOAuthLoginInstance;
    private static Context mContext;

    /* UI 요소 */
    private TextView mApiResultText;

    private OAuthLoginButton mOAuthLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        /* 로그인 api를 이용하는 앱의 개발자 버전 */
        OAuthLoginDefine.DEVELOPER_VERSION = true;

        mContext = this;

        /* Login 객체의 초기화 */
        initData();

        /*  */
        initView();


        this.setTitle("OAuthLoginSample Ver." + OAuthLogin.getVersion());
    }

    private void initData() {
        mOAuthLoginInstance = OAuthLogin.getInstance();

        /* login API에 등록된 사람들의 아이디중 하나에 로그인 가능 */
        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);
    }


    private void initView() {
        mApiResultText = (TextView) findViewById(R.id.api_result_text);

        mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
    }


    @Override
    protected void onResume() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }

    /* login 핸들러 */
    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if( success ) {
                /* 회원정보를 불러옴 */
                new RequestApiTask().execute();

                /*
                String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
                String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
                String tokenType = mOAuthLoginInstance.getTokenType(mContext);
                */


            } else {
                /**
                 * 에러가 발생한다면 에러메시지를 Toast.
                 */
                String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
                Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_LONG).show();
            }
        }
    };

    /**
     * 접근 토큰과 갱신 토큰을 삭제해서 연동 해제
     */
    private class DeleteTokenTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            boolean isSuccessDeleteToken = mOAuthLoginInstance.logoutAndDeleteToken(mContext);

            /* token을 삭제하는데 실패한 경우 */
            if (!isSuccessDeleteToken) {
                /* 서버에서 token삭제에 실패하더라도 클라이언트의 token은 삭제된 상태이므로
                 * 클라이언트에서 할 것은 없음
                 * 에러 코드를 log에 출력함
                 */
                Log.d(TAG, "errorCode:" + mOAuthLoginInstance.getLastErrorCode(mContext));
                Log.d(TAG, "errorCode:" + mOAuthLoginInstance.getLastErrorDesc(mContext));
            }

            return null;
        }

        protected void onPostExecute(Void v) {
            // updateView;
        }
    }

    /**
     * get methoud로 api를 호출함 성공시 결과 반환
     */
    private class RequestApiTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() { mApiResultText.setText((String) ""); }

        @Override
        protected String doInBackground(Void... params) {
            String url = "https://openapi.naver.com/v1/nid/getUserProfile.xml";
            String at = mOAuthLoginInstance.getAccessToken(mContext);

            /* */
            ParsingVersiondata(mOAuthLoginInstance.requestApi(mContext, at, url));

            return mOAuthLoginInstance.requestApi(mContext, at, url);
        }

        /* */
        protected void onPostExecute(String content) {
            mApiResultText.setText((String) content);
        }

        public void ParsingVersiondata(String data) {
            String f_array[] = new String[9];

            try {
                XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();

                XmlPullParser parser = parserCreator.newPullParser();
                InputStream input = new ByteArrayInputStream(data.getBytes("utf-8"));
                parser.setInput(input, "utf-8");

                int parserEvent = parser.getEventType();
                String tag;
                boolean inText = false;
                boolean lastMatTag = false;

                int colIdx = 0;


                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG:
                            tag = parser.getName();
                            if (tag.compareTo("xml") == 0) {
                                inText = false;
                            } else if (tag.compareTo("data") == 0) {
                                inText = false;
                            } else if (tag.compareTo("result") == 0) {
                                inText = false;
                            } else if (tag.compareTo("resultcode") == 0) {
                                inText = false;
                            } else if (tag.compareTo("message") == 0) {
                                inText = false;
                            } else if (tag.compareTo("response") == 0) {
                                inText = false;
                            } else {
                                inText = true;

                            }
                            break;
                        case XmlPullParser.TEXT:
                            tag = parser.getName();
                            if (inText) {
                                if (parser.getText() == null) {
                                    f_array[colIdx] = "";
                                } else {
                                    f_array[colIdx] = parser.getText().trim();
                                }

                                colIdx++;
                            }
                            inText = false;
                            break;
                        case XmlPullParser.END_TAG:
                            tag = parser.getName();
                            inText = false;
                            break;

                    }

                    parserEvent = parser.next();
                }


            } catch(XmlPullParserException xe) {
                Log.e("XmlPullParserException", "Error in network call", xe);
            } catch(UnsupportedEncodingException ue) {
                Log.e("EncodingException", "Error in encoding", ue);
            } catch(IOException ie) {
                Log.e("IOException", "Error in I/O", ie);
            } catch(Exception e) {
                Log.e("Exception", "Unknown Exception", e);
            }

            email = f_array[0];
            nickName = f_array[1];
            encId = f_array[2];
            profileImage = f_array[3];
            age = f_array[4];
            gender = f_array[5];
            id = f_array[6];
            name = f_array[7];
            birthday = f_array[8];



            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("ID", id);
            startActivity(intent);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        new DeleteTokenTask().execute();
    }
}
