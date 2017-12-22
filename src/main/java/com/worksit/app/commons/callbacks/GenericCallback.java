package com.worksit.app.commons.callbacks;

/**
 * Created by SKYNET-DEV01 on 19/12/2017.
 */

/**
 * Created by Everton on 13/06/2016.
 */
public interface GenericCallback<T> {

    void onSuccess(T data);

    void onError(Exception ex);

}