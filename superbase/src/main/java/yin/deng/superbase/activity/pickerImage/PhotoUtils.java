package yin.deng.superbase.activity.pickerImage;

import android.app.Activity;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import yin.deng.superbase.R;


/**
 * Created by Administrator on 2019/4/1 0001.
 */
public class PhotoUtils {
    /**
     * 选择单张图片，（可选）支持拍照
     * @param ac
     * @param isNeedTakePhoto 是否支持拍照
     * @param isCompress 是否压缩
     */
    public static void choosePhotoSingle(Activity ac,boolean isNeedTakePhoto,boolean isCompress){
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(ac)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageSpanCount(3)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(isNeedTakePhoto)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/DyPhotos")// 自定义拍照保存路径,可不填
                .compress(isCompress)// 是否压缩 true or false
                .theme(R.style.myPhotoChooseThem)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    /**
     * 选择多张图片，（可选）支持拍照
     * @param ac
     * @param isNeedTakePhoto 是否支持拍照
     * @param isCompress 是否压缩
     */
    public static void choosePhotoMuilt(Activity ac,int maxNum,boolean isNeedTakePhoto,boolean isCompress){
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(ac)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(maxNum)
                .imageSpanCount(3)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片 true or false
                .isCamera(isNeedTakePhoto)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.JPEG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/DyPhotos")// 自定义拍照保存路径,可不填
                .compress(isCompress)// 是否压缩 true or false
                .theme(R.style.myPhotoChooseThem)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }



    /**
     * 选择单张图片，（可选）支持拍照
     * @param ac
     * @param isNeedTakePhoto 是否支持拍照
     * @param isCompress 是否压缩
     */
    public static void chooseVideoSingle(Activity ac,boolean isNeedTakePhoto,boolean isCompress){
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(ac)
                .openGallery(PictureMimeType.ofVideo())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .imageSpanCount(3)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewVideo(true)// 是否可预览视频 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/DyPhotos")// 自定义拍照保存路径,可不填
                .compress(isCompress)// 是否压缩 true or false
                .theme(R.style.myPhotoChooseThem)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    /**
     * 选择多张图片，（可选）支持拍照
     * @param ac
     * @param isNeedTakePhoto 是否支持拍照
     * @param isCompress 是否压缩
     */
    public static void chooseVideoMuilt(Activity ac,int maxNum,boolean isNeedTakePhoto,boolean isCompress){
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(ac)
                .openGallery(PictureMimeType.ofVideo())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(maxNum)
                .imageSpanCount(3)// 每行显示个数 int
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewVideo(true)// 是否可预览视频 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/DyPhotos")// 自定义拍照保存路径,可不填
                .compress(isCompress)// 是否压缩 true or false
                .theme(R.style.myPhotoChooseThem)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    public static void openCamera(Activity ac){
        PictureSelector.create(ac)
                .openCamera(PictureMimeType.ofImage())
                .compress(true)
                .theme(R.style.myPhotoChooseThem)
                .forResult(PictureConfig.REQUEST_CAMERA);
    }


    public static void openVideoCamera(Activity ac){
        PictureSelector.create(ac)
                .openCamera(PictureMimeType.ofVideo())
                .compress(true)
                .theme(R.style.myPhotoChooseThem)
                .forResult(PictureConfig.REQUEST_CAMERA);
    }


    public static void previewPhotos(Activity ac,int position,List<LocalMedia> selectList){
        PictureSelector.create(ac).themeStyle(R.style.myPhotoChooseThem).openExternalPreview(position, selectList);
    }


}
