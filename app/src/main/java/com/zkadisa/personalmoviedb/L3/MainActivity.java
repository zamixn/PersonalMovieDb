package com.zkadisa.personalmoviedb.L3;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.zkadisa.personalmoviedb.BaseActivityClass;
import com.zkadisa.personalmoviedb.Misc.Utilities;
import com.zkadisa.personalmoviedb.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity  extends BaseActivityClass implements SensorEventListener {

    private Context context = this;
    private static final String CAM_TAG = "Camera";

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private Sensor senOrientation;
    private LocationManager locationManager;

    private Button startAndStop;
    private TextView xValue;
    private TextView yValue;
    private TextView zValue;
    private TextView orientation;
    private TextView longitude_gps;
    private TextView latitude_gps;
    private TextView longitude_network;
    private TextView latitude_network;

    private boolean InformationObtained;

    private double changeThreshold = 0.15;
    double x = Double.MAX_VALUE, y = Double.MAX_VALUE, z = Double.MAX_VALUE;

    private ImageView image;
    private float currentDegree = 0f;
    private TextView tvHeading;

    private Button takePictureButton;
    private TextureView textureView;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    private String cameraID;
    protected CameraDevice cameraDevice;
    protected CameraCaptureSession cameraCaptireSessions;
    protected CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader imageReader;
    private File file;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    private boolean photo_took = false;
    private boolean stopped = false;
    private boolean sos_sent = false;
    private boolean flashLightStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab3_mainactivity);

        InformationObtained = false;
        startAndStop = (Button) findViewById(R.id.start_and_stop);
        startAndStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(senAccelerometer == null)
                    return;
                if(InformationObtained){
                    senSensorManager.unregisterListener(MainActivity.this, senAccelerometer);
                    senSensorManager.unregisterListener(MainActivity.this, senOrientation);
                    InformationObtained = false;
                }else {
                    senSensorManager.registerListener(MainActivity.this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                    senSensorManager.registerListener(MainActivity.this, senOrientation, SensorManager.SENSOR_DELAY_GAME);
                    InformationObtained = true;
                }
            }
        });
        xValue = findViewById(R.id.x_value);
        yValue = findViewById(R.id.y_value);
        zValue = findViewById(R.id.z_value);
        orientation = findViewById(R.id.orientation);
        longitude_gps = findViewById(R.id.longitude_gps);
        latitude_gps = findViewById(R.id.latitude_gps);
        longitude_network = findViewById(R.id.longitude_network);
        latitude_network = findViewById(R.id.latitude_network);

        senSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senOrientation = senSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        image = (ImageView) findViewById(R.id.imageViewCompass);
        tvHeading = (TextView) findViewById(R.id.tvHeading);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(context)) {
                Log.i("Requesting_permission", "asd");
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                startActivity(intent);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.INTERNET},
                        10);
            }
            else
                requestLocationUpdates();
        }
        else
            requestLocationUpdates();


        textureView = findViewById(R.id.texture_view);
        assert textureView != null;
        textureView.setSurfaceTextureListener(textureListener);
        takePictureButton = findViewById(R.id.take_photo);
        assert takePictureButton != null;
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("location", "granted " + requestCode + " " + grantResults[0]);
        switch (requestCode){
            case 10:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    requestLocationUpdates();
                break;
            case REQUEST_CAMERA_PERMISSION:
                if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                    Utilities.ShowCustomToast(context, "Sorry!!!, you can't use this feature without granting permission");
                    finish();
                }

        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        // accelerometer tasks
        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER){
            double n_x = event.values[0];
            double n_y = event.values[1];
            double n_z = event.values[2];
            if(Math.abs(x - n_x) > changeThreshold)
                x = n_x;
            if(Math.abs(y - n_y) > changeThreshold)
                y = n_y;
            if(Math.abs(z - n_z) > changeThreshold)
                z = n_z;
            xValue.setText(String.valueOf(x));
            yValue.setText(String.valueOf(y));
            zValue.setText(String.valueOf(z));

            orientation.setText(getAccelerometerString(x, y, z));

            if(z < -9) {
                Log.i("APP_EXIT", "exiting");
                moveTaskToBack(true);
            }
        }

        // compass
        if(mySensor.getType() == Sensor.TYPE_ORIENTATION) {
            float degree = Math.round(event.values[0]);
            tvHeading.setText("Heading: " + Float.toString(degree) + " degrees");

            // create a rotation animation (reverse turn degree degrees)
            RotateAnimation ra = new RotateAnimation(
                    currentDegree,
                    -degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);

            // how long the animation will take place
            ra.setDuration(210);

            // set the animation after the end of the reservation status
            ra.setFillAfter(true);

            // Start the animation
            image.startAnimation(ra);
            currentDegree = -degree;

            // take photo if heading is north-ish
            if(!photo_took &&(degree > 359 || degree < 1)){
                Log.i(CAM_TAG, "taking photo " + degree);
                takePicture();
                Log.i(CAM_TAG, "photo took" + degree);
                photo_took = true;
            }else if(degree < 358 && degree > 2)
                photo_took = false;

            // sos flash if heading south-ish
            if(!sos_sent && (degree > 179 && degree < 181)){
                closeCamera();
                Log.i("SOS", "sending flash");
                send_sos();
                sos_sent = true;
            }
        }

        //brightness
        if(mySensor.getType() == Sensor.TYPE_ORIENTATION) {
            double degree = event.values[1] % 90;
            int brightness = (int)Math.round(Math.abs(degree) / 90.0 * 255.0);
            if(Math.abs(event.values[1]) > 90)
                brightness = 255 - brightness;
            boolean writeAllowed = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(Settings.System.canWrite(context))
                    writeAllowed = true;
            }else
                writeAllowed = true;
            if(writeAllowed) {
                Settings.System.putInt(context.getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS, brightness);
            }
        }
    }

    private void send_sos(){
        final long short_flash_time = 150;
        final long long_flash_time = 300;
        final long between_flash_time = 150;
        final long short_flash_wait_time = short_flash_time + between_flash_time;
        final long long_flash_wait_time = long_flash_time + between_flash_time;

        final Handler handler = new Handler();
        Runnable shortFlash = new Runnable() {
            @Override
            public void run() {
                flashLightOn();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        flashLightOff();
                    }
                }, short_flash_time);

            }
        };
        Runnable longFlash = new Runnable() {
            @Override
            public void run() {
                flashLightOn();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        flashLightOff();
                    }
                }, long_flash_time);
            }
        };

        Runnable short_triple_flash = new Runnable() {
            @Override
            public void run() {
                handler.post(shortFlash);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(shortFlash);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                handler.post(shortFlash);
                            }
                        }, short_flash_wait_time);
                    }
                }, short_flash_wait_time);
            }
        };
        Runnable long_triple_flash = new Runnable() {
            @Override
            public void run() {
                handler.post(longFlash);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(longFlash);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                handler.post(longFlash);
                            }
                        }, long_flash_wait_time);
                    }
                }, long_flash_wait_time);
            }
        };

        handler.postDelayed(short_triple_flash, 0);
        handler.postDelayed(long_triple_flash,
           short_flash_wait_time * 3 + between_flash_time);
        handler.postDelayed(short_triple_flash,
           short_flash_wait_time * 3 + long_flash_wait_time * 3 + between_flash_time * 2);
        handler.postDelayed(() -> sos_sent = false,
           short_flash_wait_time * 3 + long_flash_wait_time * 3 + between_flash_time * 2);
    }
    private void flashLightOn() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        Log.i("Flashlight", "turning on");

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
            flashLightStatus = true;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void flashLightOff() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        Log.i("Flashlight", "turning off");
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
            flashLightStatus = false;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private String getAccelerometerString (double x, double y, double z){
        double threshold = 7;

        if(x > threshold)
            return "horizontal, left";
        if(x < -threshold)
            return "horizontal, right";
        if(z > threshold)
            return "face up";
        if(z < -threshold)
            return "face down";
        if(y > threshold)
            return "vertical, rightside up";
        if(y < -threshold)
            return "vertical, upside down";

        return "in between";
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
            stopBackgroundThread();
        }
        if(senAccelerometer != null){
            senSensorManager.unregisterListener(MainActivity.this, senAccelerometer);
            senSensorManager.unregisterListener(MainActivity.this, senOrientation);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        this.locationManager.removeUpdates(gps_location_listener);
        this.locationManager.removeUpdates(network_location_listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(stopped){
            startBackgroundThread();
            if (textureView.isAvailable()) {
                openCamera();
            } else {
                textureView.setSurfaceTextureListener(textureListener);
            }
        }

        if(senAccelerometer != null) {
            senSensorManager.registerListener(MainActivity.this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            senSensorManager.registerListener(MainActivity.this, senOrientation, SensorManager.SENSOR_DELAY_GAME);
        }
        requestLocationUpdates();
    }

    private void requestLocationUpdates(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 0, gps_location_listener);
        this.locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 0, network_location_listener);

    }
    LocationListener gps_location_listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.i("location", location == null ? "null" : location.getProvider());
            if (location != null) {
                longitude_gps.setText(String.valueOf(location.getLongitude()));
                latitude_gps.setText(String.valueOf(location.getLatitude()));
            }

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.i("location", "status changed");
        }

        @Override
        public void onProviderEnabled(String s) {
            Log.i("location", "enabled");

        }

        @Override
        public void onProviderDisabled(String s) {
            Log.i("location", "disabled");
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    };
    LocationListener network_location_listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.i("location", location == null ? "null" : location.getProvider());
            if(location != null){
                longitude_network.setText(String.valueOf(location.getLongitude()));
                latitude_network.setText(String.valueOf(location.getLatitude()));
            }

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.i("location", "status changed");
        }

        @Override
        public void onProviderEnabled(String s) {
            Log.i("location", "enabled");

        }

        @Override
        public void onProviderDisabled(String s) {
            Log.i("location", "disabled");
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
    };

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            Log.i(CAM_TAG, "opened");
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int i) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    final CameraCaptureSession.CaptureCallback captureCallbackListener = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
            Utilities.ShowCustomToast(context, "Saved: " + file);
            createCameraPreview();
        }
    };

    protected void startBackgroundThread(){
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread(){
        mBackgroundThread.quitSafely();
        try{
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    protected void takePicture(){
        if(cameraDevice == null){
            Log.e(CAM_TAG, "camera device is null");
            return;
        }

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try{
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            if(characteristics != null){
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640;
            int height = 480;
            if(jpegSizes != null && 0 < jpegSizes.length){
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));

            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);

            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));

            final File file = new File(Environment.getExternalStorageDirectory() + "/pic.jpg");

            ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader imageReader) {
                    Image image = null;
                    try{
                        image = reader.acquireLatestImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }catch (IOException e){
                        e.printStackTrace();
                    }finally {
                        if(image != null)
                            image.close();
                    }
                }
                private void save(byte[] bytes)throws  IOException{
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(file);
                        output.write(bytes);
                    }finally {
                        if(null != output) {
                            output.close();
                            closeCamera();
                            Intent intent = new Intent(context, ImageFromFileActivity.class);
                            intent.putExtra("path", file.getAbsolutePath());
                            startActivity(intent);
                        }
                    }
                }
            };

            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);

            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Utilities.ShowCustomToast(context, "Saved: " + file);
                    createCameraPreview();
                }
            };

            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                    }catch (CameraAccessException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, mBackgroundHandler);
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    protected void createCameraPreview(){
        try{
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if(null == cameraDevice)
                        return;
                    cameraCaptireSessions = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Utilities.ShowCustomToast(context, "Configuration change");
                }
            }, null);
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
    }

    private void openCamera(){
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        Log.e(CAM_TAG, "is camera open");

        try{
            cameraID = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraID);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraID, stateCallback, null);
        }catch (CameraAccessException e){
            e.printStackTrace();
        }
        Log.e(CAM_TAG, "open camera X");
    }

    protected void updatePreview(){
        if(null == cameraDevice){
            Log.e(CAM_TAG, "update preview error, return");
        }

        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try{
            cameraCaptireSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        }catch (CameraAccessException e){
            e.printStackTrace();;
        }
    }

    private void closeCamera(){
        stopped = true;
        if(null != cameraDevice){
            cameraDevice.close();
            cameraDevice = null;
        }
        if(null != imageReader){
            imageReader.close();
            imageReader = null;
        }
    }


}