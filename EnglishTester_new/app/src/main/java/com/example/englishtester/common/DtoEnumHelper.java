package com.example.englishtester.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.englishtester.MainActivityDTO;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class DtoEnumHelper {
    private static final String TAG = DtoEnumHelper.class.getSimpleName();

    public static void _createFromParcel_4enum(Class clz, Object dto, String name, Parcel paramParcel, ClassLoader loader) {
        try {
            Field field = clz.getDeclaredField(name);
            field.setAccessible(true);
            Object val = paramParcel.readValue(loader);
            field.set(dto, val);
        } catch (Exception e) {
            Log.e(TAG, "_createFromParcel_4enum ERR : " + e.getMessage(), e);
        }
    }

    public static void _writeToParcel_4enum(Class clz, Object dto, String name, Parcel paramParcel, int paramInt) {
        try {
            Field field = clz.getDeclaredField(name);
            field.setAccessible(true);
            Object val = field.get(dto);
            paramParcel.writeValue(val);
        } catch (Exception e) {
            Log.e(TAG, "_writeToParcel_4enum ERR : " + e.getMessage(), e);
        }
    }

    public static void writeToParcel(Object dto, Parcel paramParcel, int paramInt, IValueTransfer[] values) {
        for (IValueTransfer e : values) {
            e.writeToParcel(dto, paramParcel, paramInt);
        }
    }

    public static final <T> Parcelable.Creator<T> getCreator(final Class clz, final IValueTransfer[] values) {
        return new Parcelable.Creator<T>() {
            @Override
            public T createFromParcel(Parcel paramParcel) {
                ClassLoader loader = this.getClass().getClassLoader();
                T dto = (T) BeanUtils.instantiate(clz);
                for (IValueTransfer e : values) {
                    e.createFromParcel(dto, paramParcel, loader);
                }
                return dto;
            }

            @Override
            public T[] newArray(int paramInt) {
                return (T[]) Array.newInstance(clz, paramInt);
            }
        };
    }

    public interface IValueTransfer {
        void createFromParcel(Object dto, Parcel paramParcel, ClassLoader loader);

        void writeToParcel(Object dto, Parcel paramParcel, int paramInt);
    }
}