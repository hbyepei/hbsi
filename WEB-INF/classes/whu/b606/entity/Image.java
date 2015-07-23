package whu.b606.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import whu.b606.dto.File;
import whu.b606.dto.FileType;

/**
 * 
 * @author Yepei,Wuhan University
 * @version Create time:2014年11月14日
 * 
 **/
@Entity
public class Image extends File implements Serializable {
	private static final long serialVersionUID = -3042742109188123522L;
	private static final FileType filetype = FileType.IMAGE;
	private Integer id;

	public Image() {
		super();
	}

	public Image(String filename, String pathname, String description) {
		super(filename, pathname, description);
	}

	@Transient
	public FileType getFiletype() {
		return filetype;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Image other = (Image) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
