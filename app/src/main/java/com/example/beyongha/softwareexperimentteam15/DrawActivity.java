package com.example.beyongha.softwareexperimentteam15;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.Vector;


public class DrawActivity extends AppCompatActivity {
    ArrayList<Vertex> arVertex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new MyGraphicView(this));
        arVertex = new ArrayList<Vertex>();
    }

    public class Vertex {
        float x;
        float y;
        boolean draw;

        // 그리기 여부
        public Vertex(float x, float y, boolean draw) {
            this.x = x;
            this.y = y;
            this.draw = draw;
        }
    }

    private class MyGraphicView extends View {
        Paint mPaint;

        public MyGraphicView(Context context) {
            super(context);

            // 페인트 객체 생성 후 설정
            mPaint = new Paint();
            mPaint.setColor(Color.BLACK);
            mPaint.setStrokeWidth(3);
            mPaint.setAntiAlias(true);
            // 안티얼라이싱
        }

        /* 터치 이벤트를 받는 메소드 */
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    arVertex.add(new Vertex(event.getX(), event.getY(), false));
                    break;
                case MotionEvent.ACTION_MOVE:
                    arVertex.add(new Vertex(event.getX(), event.getY(), true));
            }

            invalidate();
            // onDraw() 호출
            return true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            //canvas.drawBitmap(); 이 메소드로 배경에 사진을 삽입
            // 캔버스 배경색깔 설정
            canvas.drawColor(Color.WHITE);

            // 그리기
            for (int i = 0; i < arVertex.size(); i++) {
                if (arVertex.get(i).draw) {
                    // 이어서 그리고 있는 중이라면
                    canvas.drawLine(arVertex.get(i - 1).x,
                            arVertex.get(i - 1).y, arVertex.get(i).x,
                            arVertex.get(i).y, mPaint);
                    // 이전 좌표에서 다음좌표까지 그린다.
                } else {
                    canvas.drawPoint(arVertex.get(i).x, arVertex.get(i).y,
                            mPaint);
                    // 점만 찍는다.
                }
            }
        }


    }

}
