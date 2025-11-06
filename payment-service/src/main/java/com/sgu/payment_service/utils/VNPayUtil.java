package com.sgu.payment_service.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class VNPayUtil {

    public static String getPaymentUrl(Map<String, String> requestData, String vnpayPaymentUrl, String secretKey) {
        try {
            // Sắp xếp key tăng dần
            List<String> fieldNames = new ArrayList<>(requestData.keySet());
            Collections.sort(fieldNames);

            StringBuilder queryString = new StringBuilder();
            boolean first = true;


            for (String key : fieldNames) {
                String value = requestData.get(key);
                if (value != null && !value.isEmpty()) {
                    if (!first) queryString.append("&");
                    queryString.append(key).append("=")
                            .append(URLEncoder.encode(value, StandardCharsets.UTF_8.toString())); // quote_plus tương đương (spaces -> '+')
                    first = false;
                }
            }

            // Hash theo queryString đã encode
            String hashValue = hmacSHA512(secretKey.trim(), queryString.toString());
            String fullUrl = vnpayPaymentUrl + "?" + queryString + "&vnp_SecureHash=" + hashValue;

            log.debug("[VNPAY] RawData (for hash): {}", queryString);
            log.debug("[VNPAY] Hash: {}", hashValue);
            log.debug("[VNPAY] URL: {}", fullUrl);

            return fullUrl;
        } catch (Exception e) {
            log.error("Error building VNPay URL", e);
            return null;
        }
    }


    public static boolean validateResponse(Map<String, String> responseData, String secretKey) {
        String vnp_SecureHash = responseData.get("vnp_SecureHash");

        // Xóa hash params
        responseData.remove("vnp_SecureHash");
        responseData.remove("vnp_SecureHashType");

        // Sắp xếp
        List<String> fieldNames = new ArrayList<>(responseData.keySet());
        Collections.sort(fieldNames);

        StringBuilder hasData = new StringBuilder();
        boolean first = true;
        for (String key : fieldNames) {
            if (key.startsWith("vnp_")) {
                String value = responseData.get(key);
                if (value != null && !value.isEmpty()) {
                    if (!first) hasData.append("&");
                    hasData.append(key).append("=")
                .append(URLEncoder.encode(value, StandardCharsets.UTF_8));
                    first = false;
                }
            }
        }

        String hashValue = hmacSHA512(secretKey.trim(), hasData.toString());

        log.debug("🧩 [VNPAY VALIDATE] RawData: {}", hasData);
        log.debug("🧩 [VNPAY VALIDATE] LocalHash: {}", hashValue);
        log.debug("🧩 [VNPAY VALIDATE] InputHash: {}", vnp_SecureHash);

        return vnp_SecureHash != null && vnp_SecureHash.equalsIgnoreCase(hashValue);
    }


    public static String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            log.error("❌ Error in hmacSHA512", ex);
            return "";
        }
    }

    /**
     * Lấy IP client
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress;
        try {
            ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (ipAddress == null || ipAddress.isEmpty()) ipAddress = request.getRemoteAddr();
            if ("0:0:0:0:0:0:0:1".equals(ipAddress)) ipAddress = "127.0.0.1";
        } catch (Exception e) {
            ipAddress = "Invalid IP:" + e.getMessage();
        }
        return ipAddress;
    }
}
