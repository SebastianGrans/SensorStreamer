package no.grans.sensorstreamer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.nio.ByteBuffer;
import java.util.Set;
import java.util.TreeSet;

import no.grans.sensorstreamer.models.Packet;



public class BinaryPacketComposer implements PacketComposer, SensorEventListener {
    private PacketComposerListener mListener;
    private SensorManager mManager;
    private Packet mPacket;
    private int mTargetCount;
    private ByteBuffer mData;
    Set<Integer> mSeenTypes;

    private int accelerometerOffset,
        ambientTemperatureOffset,
        gravityOffset,
        gyroscopeOffset,
        lightOffset,
        linearAccelerationOffset,
        magneticFieldOffset,
        pressureOffset,
        proximityOffset,
        relativeHumidityOffset,
        rotationVectorOffset,
        poseVectorOffset;

    public BinaryPacketComposer(SensorManager manager, Packet packet) {
        mManager = manager;
        mPacket = packet;
    }


    @Override
    public void start(int period) {
        mTargetCount = 0;
        // Packet size in bytes
        // The first packet is 0x80
        // For some reason, the received packet is pre-padded with 4 zero bytes and post-padded
        // with 3 zero bytes. ??
        int size = 1;
        if (mPacket.timeStamp)
            // a timestamp of type long which is  2 bytes.
            // https://developer.android.com/reference/android/hardware/SensorEvent
            size += 8;

        if (mPacket.accelerometer) {
            mTargetCount++;
            accelerometerOffset = size;
            // Sensor value are given as a list of floats.
            // A Java float is 4 bytes and Sensor.TYPE_ACCELEROMETER has three values.
            // 3*4 = 12
            size += 12;
            Sensor s = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mManager.registerListener(this, s, period);
        }

        if (mPacket.ambientTemperature) {
            mTargetCount++;
            ambientTemperatureOffset = size;
            size += 4;
            Sensor s = mManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            mManager.registerListener(this, s, period);
        }

        if (mPacket.gravity) {
            mTargetCount++;
            gravityOffset = size;
            size += 12;
            Sensor s = mManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            mManager.registerListener(this, s, period);
        }

        if (mPacket.gyroscope) {
            mTargetCount++;
            gyroscopeOffset = size;
            size += 12;
            Sensor s = mManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            mManager.registerListener(this, s, period);
        }

        if (mPacket.light) {
            mTargetCount++;
            lightOffset = size;
            size += 4;
            Sensor s = mManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            mManager.registerListener(this, s, period);
        }

        if (mPacket.linearAcceleration) {
            mTargetCount++;
            linearAccelerationOffset = size;
            size += 12;
            Sensor s = mManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            mManager.registerListener(this, s, period);
        }

        if (mPacket.magneticField) {
            mTargetCount++;
            magneticFieldOffset = size;
            size += 12;
            Sensor s = mManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            mManager.registerListener(this, s, period);
        }

        if (mPacket.pressure) {
            mTargetCount++;
            pressureOffset = size;
            size += 4;
            Sensor s = mManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
            mManager.registerListener(this, s, period);
        }

        if (mPacket.proximity) {
            mTargetCount++;
            proximityOffset = size;
            size += 4;
            Sensor s = mManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            mManager.registerListener(this, s, period);
        }

        if (mPacket.relativeHumidity) {
            mTargetCount++;
            relativeHumidityOffset = size;
            size += 4;
            Sensor s = mManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
            mManager.registerListener(this, s, period);
        }

        if (mPacket.rotationVector) {
            mTargetCount++;
            rotationVectorOffset = size;

            // 5 values
            size += 20;
            Sensor s = mManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            mManager.registerListener(this, s, period);
        }

        if (mPacket.poseVector) {
            mTargetCount++;
            rotationVectorOffset = size;
            // 15 values. 15 * 4 = 60
            size += 60;
            Sensor s = mManager.getDefaultSensor(Sensor.TYPE_POSE_6DOF);
            mManager.registerListener(this, s, period);
        }

        mSeenTypes = new TreeSet<>();
        mData = ByteBuffer.allocateDirect(size);
        mData.put((byte) 0x80);
    }

    @Override
    public void stop() {
        mManager.unregisterListener(this);
    }

    @Override
    public void setListener(PacketComposerListener listener) {
        mListener = listener;
    }

    private void putN(int offset, int count, float[] data) {
        for (int i = 0; i != count; i++)
            mData.putFloat(offset + 4 * i, data[i]);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                putN(accelerometerOffset, 3, event.values);
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                putN(ambientTemperatureOffset, 1, event.values);
                break;
            case Sensor.TYPE_GRAVITY:
                putN(gravityOffset, 3, event.values);
                break;
            case Sensor.TYPE_GYROSCOPE:
                putN(gyroscopeOffset, 3, event.values);
                break;
            case Sensor.TYPE_LIGHT:
                putN(lightOffset, 1, event.values);
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                putN(linearAccelerationOffset, 3, event.values);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                putN(magneticFieldOffset, 3, event.values);
                break;
            case Sensor.TYPE_PRESSURE:
                putN(pressureOffset, 1, event.values);
                break;
            case Sensor.TYPE_PROXIMITY:
                putN(proximityOffset, 1, event.values);
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                putN(relativeHumidityOffset, 1, event.values);
                break;
            case Sensor.TYPE_ROTATION_VECTOR:
                putN(rotationVectorOffset, 5, event.values);
                break;
            case Sensor.TYPE_POSE_6DOF:
                putN(poseVectorOffset, 15, event.values);
            default:
                return;
        }
        mSeenTypes.add(new Integer(event.sensor.getType()));
        if (mSeenTypes.size() == mTargetCount) {
            if (mPacket.timeStamp)
                mData.putLong(1, event.timestamp);
            mSeenTypes.clear();
            mListener.onPacketComplete(mData.array());
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
