package nickgao.com.meiyousample;

import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.meetyou.media.player.client.MeetyouPlayerEngine;
import com.meetyou.media.player.client.ui.MeetyouPlayerTextureView;
import com.meetyou.media.player.client.ui.MeetyouPlayerView;
import com.meetyou.media.player.client.ui.UISeekBarManager;
import com.meiyou.media.player.tv.MeetyouPlayer;
import com.meiyou.media.player.tv.playengine.IPlayerCallback;

import tv.danmaku.ijk.media.player.pragma.DebugLog;

public class VideoTestActivity extends AppCompatActivity{

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
//    }
    ProgressBar progressBar ;
    float m_volume = 0;
    int i = 1;
    MeetyouPlayer meetyouPlayerCore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        MeetyouPlayerEngine.Instance().formatActivity(this);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar) findViewById(R.id.pb);

        meetyouPlayerCore = MeetyouPlayerEngine.Instance().bindPlayer("meetyou_demo");

        meetyouPlayerCore.setFetcher(true);
        meetyouPlayerCore.setOnVideoSizeChangeListener(new IPlayerCallback.OnVideoSizeChangeListener() {
            @Override
            public void onVideoSizeChange(MeetyouPlayerView view, int width, int height) {
                DebugLog.d("ijk-activity","onSetVideoViewLayout:"+height + ","+width);
                view.setVideoSize(width, height);//如果需要更新显示区域请设置这个
            }
        });

        meetyouPlayerCore.setOnLoadListener(new IPlayerCallback.OnLoadListener() {
            @Override
            public void onLoad(boolean loading) {
                if(loading){
                    progressBar.setVisibility(View.VISIBLE);
                }else{
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        meetyouPlayerCore.setOnStartListener(new IPlayerCallback.OnStartListener() {
            @Override
            public void onStart() {
                progressBar.setVisibility(View.GONE);
                DebugLog.d("ijk-activity","onPlayerStart");
            }
        });

        meetyouPlayerCore.addOnBufferingListener(new IPlayerCallback.OnBufferingListener() {
            @Override
            public void onBuffering(int currentBufferPercentage) {
                DebugLog.d("ijk-activity","onBufferingback:" + currentBufferPercentage);
            }
        });

        meetyouPlayerCore.setOnStartListener(new IPlayerCallback.OnStartListener() {
            @Override
            public void onStart() {
                progressBar.setVisibility(View.GONE);
                DebugLog.d("ijk-activity","onPlayerStart");
            }
        });

        meetyouPlayerCore.setOnCompleteListener(new IPlayerCallback.OnCompleteListener() {
            @Override
            public void onComplete() {
                findViewById(R.id.play).setVisibility(View.VISIBLE);
            }
        });

        meetyouPlayerCore.addOnProgressListener(new IPlayerCallback.OnProgressListener() {
            @Override
            public void onPorgress(long cur, long total) {
                DebugLog.d("ijk-activity","onPorgress:" +cur + ","+total);
            }
        });

        UISeekBarManager playerUIManager = new UISeekBarManager();
        playerUIManager.setView(((SeekBar) findViewById(R.id.progressBarl)));
        playerUIManager.setPlayer(meetyouPlayerCore);

        final MeetyouPlayerTextureView sv = (MeetyouPlayerTextureView) findViewById(R.id.sv);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                i ++;
                meetyouPlayerCore.setPlaySource("http://sc.seeyouyima.com/pregnancy_baby/960BABY" + i + ".mp4");
                meetyouPlayerCore.prepare();
                meetyouPlayerCore.play();

            }
        });

        sv.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                meetyouPlayerCore.setMeetyouPlayerView(sv);
                meetyouPlayerCore.setPlaySource("http://i.l.inmobicdn.net/adtools/videoads/prod/1701d05de0294e57be0bb1e009e34566/videos/54c5473f-9ce1-4720-8de5-a83feae2e0fa/video.720p.mp4");

//                meetyouPlayerCore.setPlaySource("http://fms.ipinyou.com/5/07/1A/38/F001Nl1OsprH000B5Cfk.mp4");
//                PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionsResultAction() {
//                    @Override
//                    public void onGranted() {
//                        meetyouPlayerCore.prepare();
//                        meetyouPlayerCore.play();
//                    }
//
//                    @Override
//                    public void onDenied(String permission) {
//
//                    }
//                });

                DebugLog.d("ijk-activity","onSurfaceCreate");

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });

        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meetyouPlayerCore.stop();
            }
        });

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(meetyouPlayerCore.isPlaying()){
                    return;
                }
                if(meetyouPlayerCore.isStopped()) {
                    meetyouPlayerCore.prepare();
                }
                meetyouPlayerCore.play();
            }
        });

        findViewById(R.id.v_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meetyouPlayerCore.pause();
            }
        });

        findViewById(R.id.v_volume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m_volume == 0){
                    m_volume = 1.0f;
                }else{
                    m_volume = 0;
                }

            }
        });

        findViewById(R.id.v_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        MeetyouPlayerEngine.Instance().clearCache(new Utils.PlayerCacheFilter() {
//                            @Override
//                            public boolean onCache(File file) {
//                                if(file.getName().startsWith("cache")){
//                                    return true;
//                                }
//                                return false;
//                            }
//                        });
                        MeetyouPlayerEngine.Instance().clearCacheBeforeTime(System.currentTimeMillis() - 5000000);
//                        MeetyouPlayerEngine.Instance().clearCache();
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MeetyouPlayerEngine.Instance().unbindPlayer("meetyou_demo");
    }
}
