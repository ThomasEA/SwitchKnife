package com.worksit.app.commons.switchknife;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.SupportActivity;
import android.view.View;

import com.worksit.app.commons.switchknife.annotations.BindLoadBanner;
import com.worksit.app.commons.switchknife.annotations.BindOnClick;
import com.worksit.app.commons.switchknife.annotations.BindView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by SKYNET-DEV01 on 26/07/2017.
 */

public class SwitchKnife {

    public static <T1 extends Activity> void bind(T1 ctx)  {
        bindLocal(ctx, ctx);
    }

    public static <T1> void bind(T1 viewHolder, Activity view) {
        bindLocal(view, viewHolder);
    }

    private static <T1> void bindLocal(Activity ctx, T1 obj) {
        Class<?> classe = obj.getClass();

        for (Class<?> c : classe.getClasses()) {
            bindLocal(ctx, c);
        }

        bindFields(ctx, obj, classe);
        bindListeners(ctx, obj, classe);
    }

    private static <T1> void bindFields(Activity ctx, T1 obj, Class<?> classe) {
        for (Field f : classe.getDeclaredFields()) {
            if (f.isAnnotationPresent(BindView.class)) {
                BindView bv = f.getAnnotation(BindView.class);
                try {
                    f.setAccessible(true);
                    f.set(obj, findById(ctx, bv.value()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static <T1> void bindListeners(Activity ctx, final T1 obj, Class<?> classe) {
        for (final Method m : classe.getDeclaredMethods()) {
            bindOnClickListener(ctx, obj, m);
            loadBanners(ctx, obj, m);
        }
    }

    private static <T1> void bindOnClickListener(Activity ctx, final T1 obj, final Method m) {
        if (m.isAnnotationPresent(BindOnClick.class)) {
            BindOnClick bv = m.getAnnotation(BindOnClick.class);

            for (int id : bv.value()) {
                try {
                    View vw = findById(ctx, id);
                    vw.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                m.setAccessible(true);
                                m.invoke(obj, null);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static <T1> void loadBanners(Activity ctx, final T1 obj, final Method m) {
        if (m.isAnnotationPresent(BindLoadBanner.class)) {
            try {
                m.setAccessible(true);
                m.invoke(obj, null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T1> T1 findById(Activity view, int id) {
        return (T1) view.findViewById(id);
    }


}
