package app.sunshine.android.example.com.carplay;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by janny on 2016-10-09.
 */

public class EventAgreeDialog extends Dialog
{
    private Context mContext = null;
    protected static final String TAG = "EventAgreeDialog";
    private TextView mTvContents = null;
    private Button mBtn_ok = null;
    private View.OnClickListener mClickListener;


    public EventAgreeDialog(Context context, View.OnClickListener clicklistener)
    {
        super(context);

//        	super(context, android.R.style.Theme_Translucent_NoTitleBar);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_event_agree);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.setCanceledOnTouchOutside(false);		// 다이알로그 바깥영역 터치시, 다이알로그 닫히지 않기
        this.setCancelable(true); // 백키로 다이알로그 닫기

        mContext = context;
        mClickListener = clicklistener;
        initComponent();
        componentSetValue();
    }

    private void initComponent(){
        mBtn_ok = (Button)findViewById(R.id.btn_event_ok);
        mBtn_ok.setOnClickListener(mClickListener);	// 클릭 이벤트 등록
        mTvContents = (TextView) findViewById(R.id.tvContents);
    }

    private void componentSetValue(){
        String eventbody = "고객님, 홍대점 방문을 환영합니다.";
        mTvContents.setText(Html.fromHtml(eventbody));
    }

    @Override
    public void show()
    {
        super.show();
    }

    @Override
    public void dismiss()
    {
        super.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}