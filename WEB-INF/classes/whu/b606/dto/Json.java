package whu.b606.dto;

import java.io.Serializable;

/**
 * 定义一个用于向前台传递处理结果消息的Json模型
 * 
 * @author yepei
 */
public class Json implements Serializable {
	private static final long serialVersionUID = 2349403481467709607L;
	private boolean ok = false;// 是否成功
	private String msg = "";// 提示信息
	private Object object = null;// 其它信息

	public Json(boolean ok, String msg, Object object) {
		super();
		this.ok = ok;
		this.msg = msg;
		this.object = object;
	}

	public Json() {
		super();
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}
}
