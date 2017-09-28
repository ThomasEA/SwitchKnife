package com.worksit.app.commons.switchknife;

/**
 * Created by SKYNET-DEV01 on 08/09/2017.
 */

public class Utils {

    public static <T1> boolean checkTypeOf(T1 object, Class... types){
        return checkTypeOf(object, true, types);
    }

    public static <T1> boolean checkTypeOf(T1 object, boolean throwError, Class... types){
        for (Class c : types) {
            if (!c.isInstance(object)) {
                if (throwError)
                    throw  new RuntimeException(object.toString() + " must implement " + c.getCanonicalName());
                else
                    return false;
            }
        }
        return true;
    }

}
