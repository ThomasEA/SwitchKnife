package com.worksit.app.commons.net;

import java.util.Map;

/**
 * Created by SKYNET-DEV01 on 16/02/2017.
 */

public class RequestParams
{
    private String baseUrl;
    private String endPoint;
    private String name;
    private Map<String, Object> parametersMap;
    private Map<String, Object> queriesMap;
    private String url;

    public String getBaseUrl()
    {
        return this.baseUrl;
    }

    public String getEndPoint()
    {
        return this.endPoint;
    }

    public String getName()
    {
        return this.name;
    }

    public Map<String, Object> getParametersMap()
    {
        return this.parametersMap;
    }

    public Map<String, Object> getQueriesMap()
    {
        return this.queriesMap;
    }

    public String getUrl()
    {
        return this.url;
    }

    public void setBaseUrl(String paramString)
    {
        this.baseUrl = paramString;
    }

    public void setEndPoint(String paramString)
    {
        this.endPoint = paramString;
    }

    public void setName(String paramString)
    {
        this.name = paramString;
    }

    public void setParametersMap(Map<String, Object> paramMap)
    {
        this.parametersMap = paramMap;
    }

    public void setQueriesMap(Map<String, Object> paramMap)
    {
        this.queriesMap = paramMap;
    }

    public void setUrl(String paramString)
    {
        this.url = paramString;
    }
}