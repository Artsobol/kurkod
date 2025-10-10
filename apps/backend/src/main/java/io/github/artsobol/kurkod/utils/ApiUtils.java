package io.github.artsobol.kurkod.utils;

import io.github.artsobol.kurkod.model.constants.ApiConstants;

public class ApiUtils {

    public static String getMethodName(){
        try{
            return Thread.currentThread().getStackTrace()[1].getMethodName();
        } catch(Exception e){
            return ApiConstants.UNDEFINED;
        }
    }
}
