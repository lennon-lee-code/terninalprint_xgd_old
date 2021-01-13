package com.jiuqi.dna.gams.jy03.printing.util;

public class Response {
	public static final int SUCCESS = 0;

	public static final int ERROR = -1;

	public static Response success() {
		return new Response();
	}

	public static Response success(Object content) {
		return new Response(content);
	}

	public static Response failed(Object content, String message) {
		return new Response(ERROR, content, message);
	}

	private int code = SUCCESS;

	private Object content;

	private String message;

	public Response() {
	}

	public Response(Object content) {
		this.content = content;
	}

	public Response(int code, Object content, String message) {
		this.code = code;
		this.content = content;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
