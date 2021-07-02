package fr.frazew.virtualgyroscope;

public class Util {
    public static boolean checkSensorResolution(float[] prevValues, float[] values, float resolution) {
        if (Math.abs(prevValues[0] - values[0]) <= resolution && Math.abs(prevValues[1] - values[1]) <= resolution && Math.abs(prevValues[2] - values[2]) <= resolution) {
            return false;
        }
        return true;
    }

    public static float[] normalizeQuaternion(float[] quaternion) {
        returnQuat = new float[4];
        float sqrt = (float) Math.sqrt((double) ((((quaternion[0] * quaternion[0]) + (quaternion[1] * quaternion[1])) + (quaternion[2] * quaternion[2])) + (quaternion[3] * quaternion[3])));
        returnQuat[0] = quaternion[0] / sqrt;
        returnQuat[1] = quaternion[1] / sqrt;
        returnQuat[2] = quaternion[2] / sqrt;
        returnQuat[3] = quaternion[3] / sqrt;
        return returnQuat;
    }

    public static float[] normalizeVector(float[] vector) {
        newVec = new float[3];
        float sqrt = (float) Math.sqrt((double) (((vector[0] * vector[0]) + (vector[1] * vector[1])) + (vector[2] * vector[2])));
        newVec[0] = vector[0] / sqrt;
        newVec[1] = vector[1] / sqrt;
        newVec[2] = vector[2] / sqrt;
        return newVec;
    }

    public static float[] subtractQuaternionbyQuaternion(float[] quat1, float[] quat2) {
        return new float[]{quat1[0] - quat2[0], quat1[1] - quat2[1], quat1[2] - quat2[2], quat1[3] - quat2[3]};
    }

    public static float[] rotateVectorByQuaternion(float[] vector, float[] quaternion) {
        float a = vector[0];
        float b = vector[1];
        float c = vector[2];
        float d = vector[3];
        float A = quaternion[0];
        float B = quaternion[1];
        float C = quaternion[2];
        float D = quaternion[3];
        float newQuaternionReal = (((a * A) - (b * B)) - (c * C)) - (d * D);
        float newQuaternioni = (((a * B) + (b * A)) + (c * D)) - (d * C);
        float newQuaternionj = (((a * C) - (b * D)) + (c * A)) + (d * B);
        float newQuaternionk = (((a * D) + (b * C)) - (c * B)) + (d * A);
        return new float[]{newQuaternionReal, newQuaternioni, newQuaternionj, newQuaternionk};
    }

    public static float[] quaternionToRotationMatrix(float[] quaternion) {
        float[] rotationMatrix = new float[9];
        float w = quaternion[0];
        float x = quaternion[1];
        float y = quaternion[2];
        float z = quaternion[3];
        float n = (((w * w) + (x * x)) + (y * y)) + (z * z);
        float s = n == 0.0f ? 0.0f : 2.0f / n;
        float wx = (s * w) * x;
        float wy = (s * w) * y;
        float wz = (s * w) * z;
        float xx = (s * x) * x;
        float xy = (s * x) * y;
        float xz = (s * x) * z;
        float yy = (s * y) * y;
        float yz = (s * y) * z;
        float zz = (s * z) * z;
        rotationMatrix[0] = 1.0f - (yy + zz);
        rotationMatrix[1] = xy - wz;
        rotationMatrix[2] = xz + wy;
        rotationMatrix[3] = xy + wz;
        rotationMatrix[4] = 1.0f - (xx + zz);
        rotationMatrix[5] = yz - wx;
        rotationMatrix[6] = xz - wy;
        rotationMatrix[7] = yz + wx;
        rotationMatrix[8] = 1.0f - (xx + yy);
        return rotationMatrix;
    }

    public static float[] rotationMatrixToQuaternion(float[] rotationMatrix) {
        float qw;
        float qx;
        float qy;
        float qz;
        float m00 = rotationMatrix[0];
        float m01 = rotationMatrix[1];
        float m02 = rotationMatrix[2];
        float m10 = rotationMatrix[3];
        float m11 = rotationMatrix[4];
        float m12 = rotationMatrix[5];
        float m20 = rotationMatrix[6];
        float m21 = rotationMatrix[7];
        float m22 = rotationMatrix[8];
        float tr = (m00 + m11) + m22;
        float S;
        if (tr > 0.0f) {
            S = ((float) Math.sqrt(((double) tr) + 1.0d)) * 2.0f;
            qw = 0.25f * S;
            qx = (m21 - m12) / S;
            qy = (m02 - m20) / S;
            qz = (m10 - m01) / S;
        } else {
            if (((m00 > m22 ? 1 : 0) & (m00 > m11 ? 1 : 0)) != 0) {
                S = ((float) Math.sqrt(((1.0d + ((double) m00)) - ((double) m11)) - ((double) m22))) * 2.0f;
                qw = (m21 - m12) / S;
                qx = 0.25f * S;
                qy = (m01 + m10) / S;
                qz = (m02 + m20) / S;
            } else if (m11 > m22) {
                S = ((float) Math.sqrt(((1.0d + ((double) m11)) - ((double) m00)) - ((double) m22))) * 2.0f;
                qw = (m02 - m20) / S;
                qx = (m01 + m10) / S;
                qy = 0.25f * S;
                qz = (m12 + m21) / S;
            } else {
                S = ((float) Math.sqrt(((1.0d + ((double) m22)) - ((double) m00)) - ((double) m11))) * 2.0f;
                qw = (m10 - m01) / S;
                qx = (m02 + m20) / S;
                qy = (m12 + m21) / S;
                qz = 0.25f * S;
            }
        }
        return new float[]{qw, qx, qy, qz};
    }
}
