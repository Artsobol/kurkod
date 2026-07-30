package io.github.artsobol.kurkod.infrastructure.util;

import io.github.artsobol.kurkod.infrastructure.constants.CommonConstants;

public class LogUtils {
    public static String getMethodName(){
        try{
            return Thread.currentThread().getStackTrace()[2].getMethodName();
        } catch(Exception e){
            return CommonConstants.UNDEFINED;
        }
    }
}
