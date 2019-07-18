package yin.deng.superbase.activity;

import com.luck.picture.lib.entity.LocalMedia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2019/4/1 0001.
 */
public class MediaListInfo implements Serializable {
    private List<LocalMedia> medias=new ArrayList<>();
    private int requestCode;

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public List<LocalMedia> getMedias() {
        return medias;
    }

    public void setMedias(List<LocalMedia> medias) {
        this.medias = medias;
    }
}
