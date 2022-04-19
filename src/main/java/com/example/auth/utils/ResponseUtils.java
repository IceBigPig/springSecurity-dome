package com.example.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Date: 2022-04-11 20:13
 * version 1.0
 */
public class ResponseUtils {

    private  final static String RESPONSE_TYPE= "application/json;charset=utf-8";

    public static void result(HttpServletResponse response, int code, Object msg) throws IOException {
        response.setStatus(code);
        response.setContentType(RESPONSE_TYPE);
        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(msg));
    }

}
