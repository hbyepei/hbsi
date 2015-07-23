package whu.b606.dto;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年11月15日
 * 
 **/
@MappedSuperclass
public class File {
	private String filename;// 文件自然名
	private String pathname;// 文件实际名
	private String description;// 文件描述

	public File() {
		super();
	}

	public File(String filename, String pathname, String description) {
		super();
		this.filename = filename;
		this.pathname = pathname;
		this.description = description;
	}

	@Column(length = 100)
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Column(length = 50, nullable = false, unique = true)
	public String getPathname() {
		return pathname;
	}

	public void setPathname(String pathname) {
		this.pathname = pathname;
	}

	@Column(length = 255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pathname == null) ? 0 : pathname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		File other = (File) obj;
		if (pathname == null) {
			if (other.pathname != null)
				return false;
		} else if (!pathname.equals(other.pathname))
			return false;
		return true;
	}
}
