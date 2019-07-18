package yin.deng.superbase.activity.pictureUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yin.deng.superbase.activity.LogUtils;


/**
 * Created by Administrator on 2018/5/17.
 * 图片批量压缩工具
 * deng yin
 */
public class CompressPhotoUtils {
    private static final String COMPRESS_DIR = "mnt/sdcard/compressDir/";
    private List<String> fileList = new ArrayList<>();
//    private ProgressDialog progressDialog;

    public void CompressPhoto(Context context, List<String> list, CompressCallBack callBack) {
        deleteDir(COMPRESS_DIR);
        CompressTask task = new CompressTask(context, list, callBack);
        task.execute();
    }

    class CompressTask extends AsyncTask<Void, Integer, Integer> {
        private Context context;
        private List<String> list;
        private CompressCallBack callBack;

        CompressTask(Context context, List<String> list, CompressCallBack callBack) {
            this.context = context;
            this.list = list;
            this.callBack = callBack;
        }

        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
           // progressDialog = ProgressDialog.show(context, null, "处理中...");
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... params) {
            for (int i = 0; i < list.size(); i++) {
                Bitmap bitmap = createCoverThumbnail(list.get(i));
                String path = SaveBitmap(bitmap, i);
                fileList.add(path);
            }
            return null;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer integer) {
          //  progressDialog.dismiss();
            callBack.success(fileList);
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }

    /**
     * 从sd卡获取压缩图片bitmap
     */
    public static Bitmap getBitmap(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 1280f;
        float ww = 720f;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        newOpts.inSampleSize = be;// 设置缩放比例
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }


    public static Bitmap createCoverThumbnail(String filePath) {
        if(filePath==null){
            return null;
        }
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return null;
        }
        long size = getFileSize(file);
        LogUtils.i("file.length==压缩前" + size);
        Long l1 = new Long(204820);
        Long l2 = new Long(size);
        if (l2.longValue() < l1.longValue()) {
            Bitmap bm = BitmapFactory.decodeFile(filePath);
            int degree = readPictureDegree(filePath);
            if (degree != 0) {//旋转照片角度
                bm = rotateBitmap(bm, degree);
            }
            return bm;
        }
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);

        opts.inSampleSize = computeSampleSize(opts, -1, 800 * 800);
        opts.inJustDecodeBounds = false;

        try {
            bitmap = BitmapFactory.decodeFile(filePath, opts);
            int degree = readPictureDegree(filePath);
            if (degree != 0) {//旋转照片角度
                bitmap = rotateBitmap(bitmap, degree);
            }
        }catch (Exception e) {
            // TODO: handle exception
        }
        return bitmap;
    }


    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 :(int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }


    /**
     * 旋转图片，使图片保持正确的方向。
     *
     * @param bitmap  原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (null != bitmap) {
            bitmap.recycle();
        }
        return bmp;
    }


    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return degree;
        }
        return degree;
    }


    /**
     * 获取指定文件大小
     *
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            try {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }


    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 540, 960);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }



    /**
     * 保存bitmap到内存卡
     */
    public static String SaveBitmap(Bitmap bmp, int num) {
        File file = new File(COMPRESS_DIR);
        String path = null;
        if (!file.exists())
            file.mkdirs();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String picName = formatter.format(new Date());
            path = file.getPath() + "/" + picName + "_" + num + ".png";
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }


    //删除文件夹和文件夹里面的文件
    public static void deleteDir(final String pPath) {
        File dir = new File(pPath);
        deleteDirWihtFile(dir);
    }

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    public interface CompressCallBack {
        void success(List<String> list);
    }


}
