package com.panther.takeaway.Utils;

import java.util.UUID;

public class UUIDUtils {

    public static String UUID(){
        return UUID.randomUUID()
                .toString()
                .replaceAll("-","")
                .substring(0,15);
    }

}
