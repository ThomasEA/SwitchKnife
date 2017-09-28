package com.worksit.app.commons.switchknife;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.SupportActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.worksit.app.commons.switchknife.annotations.BindLoadBanner;
import com.worksit.app.commons.switchknife.annotations.BindOnClick;
import com.worksit.app.commons.switchknife.annotations.BindOnEditorAction;
import com.worksit.app.commons.switchknife.annotations.BindView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by SKYNET-DEV01 on 26/07/2017.
 */

public class SwitchKnife {

    public static void bind(Activity activity)  {
        View sourceView = activity.getWindow().getDecorView();
        bindLocal(activity, sourceView);
    }

    public static <T1 extends View> void bind(T1 view)  {
        bindLocal(view, view);
    }

    public static <T1> void bind(T1 viewHolder, Activity activity) {
        View sourceView = activity.getWindow().getDecorView();
        bindLocal(viewHolder, sourceView);
    }

    public static <T1> void bind(T1 viewHolder, View view) {
        bindLocal(viewHolder, view);
    }

    private static <T1> void bindLocal(Object obj, View view) {
        Class<?> classe = obj.getClass();

        for (Class<?> c : classe.getClasses()) {
            bindLocal(c, view);
        }

        bindFields(view, obj, classe);
        bindListeners(view, obj, classe);
    }

    private static <T1> void bindFields(View view, T1 obj, Class<?> classe) {
        for (Field f : classe.getDeclaredFields()) {
            if (f.isAnnotationPresent(BindView.class)) {
                BindView bv = f.getAnnotation(BindView.class);
                try {
                    f.setAccessible(true);
                    f.set(obj, findById(view, bv.value()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static <T1> void bindListeners(View view, final T1 obj, Class<?> classe) {
        for (final Method m : classe.getDeclaredMethods()) {
            bindOnClickListener(view, obj, m);
            loadBanners(view, obj, m);
        }
    }

    private static <T1> void bindOnClickListener(View view, final T1 obj, final Method m) {
        if (m.isAnnotationPresent(BindOnClick.class)) {
            BindOnClick bv = m.getAnnotation(BindOnClick.class);

            for (int id : bv.value()) {
                try {
                    View vw = findById(view, id);
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

        if (m.isAnnotationPresent(BindOnEditorAction.class)) {
            bindOnEditorActionListener(view, obj, m);
        }
    }

    private static <T1> void bindOnEditorActionListener(View view, final T1 obj, final Method m) {
        BindOnEditorAction bv = m.getAnnotation(BindOnEditorAction.class);

        if (bv == null) return;

        for (int id : bv.value()) {
            try {
                EditText editText = findById(view, id);
                editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        try {
                            m.setAccessible(true);
                            m.invoke(obj, null);
                            return true;
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static <T1> void loadBanners(View view, final T1 obj, final Method m) {
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
        return findById(view, id);
    }

    public static <T1> T1 findById(View view, int id) {
        return (T1) view.findViewById(id);
    }

}
