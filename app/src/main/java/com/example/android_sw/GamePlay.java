package com.example.android_sw;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class GamePlay extends SurfaceView implements SurfaceHolder.Callback {
    MainThread mainThread;
    public GamePlay(Context context) {
        super(context); // Context의 부모 클래스 상속
        SurfaceHolder myHolder = getHolder(); // SurfaceHolder 타입의 myHolder 객체 생성
        myHolder.addCallback(this); // 객체에 대한 콜백을 등록 → 이벤트 수신
        mainThread = new MainThread(myHolder);
    }
    
    /*
     MainThread의 실행 상태 설정 및 스레드 시작 → (SurfaceView가 생성 되었을 시 호출)
     */
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) { 
        mainThread.setIsRunning(true);
        mainThread.start();
    }
    
    /*
    SurfaceView의 크기나 형식이 변경 되었을 시 호출
     */
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}
    
    /*
    SurfaceView가 파괴되기 전 호출 → 스레드 실행 중단 및 종료될 때까지 대기
     */
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

        boolean retry = true;
        while (retry) {
            try {
                mainThread.setIsRunning(false);
                mainThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;

        }
    }
    
    /*
    <터치 이벤트 처리 메소드>
    1. gameState == 0일 시 → 게임 시작 & 소리 재생
    2. gameState != 0일 시 → 플레이어 캐릭터의 속도 설정 & 점프 동작 수행
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (AppHolder.getGameManager().gameState == 0) {
            AppHolder.getGameManager().gameState = 1;
            AppHolder.getSoundPlay().playSwoosh();
        } else {
            AppHolder.getSoundPlay().playArm();
        }

        AppHolder.getGameManager().swimmingChar.setVelocity(AppHolder.JUMP_VELOCITY);
        return true;
    }
}