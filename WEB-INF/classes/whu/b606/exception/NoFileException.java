package whu.b606.exception;

public class NoFileException extends Exception {
	private static final long serialVersionUID = -1995399871198397359L;

	@Override
	public String getMessage() {
		return "给定目录下没有要删除的文件！";
	}
}
