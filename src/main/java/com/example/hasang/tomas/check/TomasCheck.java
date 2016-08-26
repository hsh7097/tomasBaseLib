package com.example.hasang.tomas.check;

/**
 * Created by hasang on 16. 8. 2..
 */
public class TomasCheck {

    /**
     * 업데이트 버전비교
     *
     * @param clientVersion 클라이언트 버전
     * @param serverVersion 서버 버전
     * @return
     */
    public static boolean isUpdate(String clientVersion, String serverVersion) {

        clientVersion = clientVersion.replaceAll("\\.", "");
        serverVersion = serverVersion.replaceAll("\\.", "");

        try {
            if ((Integer.parseInt(clientVersion) < Integer.parseInt(serverVersion))) {
                return true;
            }
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }


}
