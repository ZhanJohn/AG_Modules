package com.ag.modules;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tencent.qcload.playersdk.ui.PlayerActionInterface;
import com.tencent.qcload.playersdk.ui.TitleMenu;
import com.tencent.qcload.playersdk.ui.UiChangeInterface;
import com.tencent.qcload.playersdk.ui.VideoRootFrame;
import com.tencent.qcload.playersdk.util.PlayerListener;
import com.tencent.qcload.playersdk.util.VideoInfo;

import java.util.ArrayList;
import java.util.List;

public class VideoActivity extends Activity {

    private final String TAG = "VideoActivity";
    private VideoRootFrame player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video);

        player=(VideoRootFrame)findViewById(R.id.player);
        if(player==null){
            Toast.makeText(getApplicationContext(),"player is null!!!",Toast.LENGTH_LONG).show();
            return;
        }
        List<TitleMenu> videoTitleMenus=new ArrayList<TitleMenu>();
        TitleMenu icon1=new TitleMenu();
        icon1.iconId=R.mipmap.ic_share;
        icon1.action=new PlayerActionInterface(){
            @Override
            public void action() {
                Toast.makeText(VideoActivity.this,"share icon taped",Toast.LENGTH_SHORT).show();
            }
        };
        videoTitleMenus.add(icon1);
        TitleMenu icon2=new TitleMenu();
        icon2.iconId=R.mipmap.ic_favorite;
        videoTitleMenus.add(icon2);
        TitleMenu icon3=new TitleMenu();
        icon3.iconId=R.mipmap.ic_perm_identity;
        videoTitleMenus.add(icon3);
        player.setMenu(videoTitleMenus);
        List<VideoInfo> videos=new ArrayList<VideoInfo>();
        VideoInfo v1=new VideoInfo();
        v1.description="111";
        v1.type=VideoInfo.VideoType.MP4;
        v1.url="http://4500.vod.myqcloud.com/4500_d754e448e74c11e4ad9e37e079c2b389.f20.mp4?vkey=693D66AF23164CA4741745A2FE9675DCC4493BF10CF724CBE3769CB237121DAB55F3D494AC2C6DB7&ocid=12345";
//        v1.url="http://player.youku.com/player.php/sid/XMTQ1MDc1NDkxNg==/v.swf";
        videos.add(v1);
        VideoInfo v2=new VideoInfo();
        v2.description="222";
        v2.type=VideoInfo.VideoType.MP4;
        v2.url="http://4500.vod.myqcloud.com/4500_d754e448e74c11e4ad9e37e079c2b389.f0.mp4?vkey=77F279B72A3788656E0A14837DA6C89AA57D5CA46FBAD14A81FE3B63FE2DE92C5668CBD27304071B&ocid=12345";
        videos.add(v2);
        player.setListener(new PlayerListener(){

            @Override
            public void onError(Exception arg0) {
                arg0.printStackTrace();

            }

            @Override
            public void onStateChanged(int arg0) {
                Log.d(TAG, "player states:"+arg0);

            }

        });
        player.play(videos);
        player.setToggleFullScreenHandler(new UiChangeInterface() {
            @Override
            public void OnChange() {
                if (player.isFullScreen()) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        });

    }

}
