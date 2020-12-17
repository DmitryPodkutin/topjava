package ru.javawebinar.topjava.util.exception;

import java.util.List;

public class ErrorInfo {
    private final String url;
    private final ErrorType type;
    private final String detail;
    private final String[] listDetail;


    public ErrorInfo(CharSequence url, ErrorType type, String detail) {
        this(url, type, detail, null);
    }

    public ErrorInfo(CharSequence url, ErrorType type, String[] listDetail) {
        this(url, type, null, listDetail);
    }

    public ErrorInfo(CharSequence url, ErrorType type, String detail, String[] listDetail) {
        this.url = url.toString();
        this.type = type;
        this.detail = detail;
        this.listDetail = listDetail;
    }

    @Override
    public String toString() {
        return "ErrorInfo{" +
                "url='" + url + '\'' +
                ", type=" + type +
                ", detail='" + detail + '\'' +
                ", listDetail=" + listDetail +
                '}';
    }
}