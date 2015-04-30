package ru.terekhov.book2read.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "CATALOG")
@NamedQuery(name="Catalog.getLastUpdated",
			query="SELECT c.dateUpdated " + 
				  "FROM Catalog c")			
public class Catalog implements Serializable {
	private static final long serialVersionUID = -4796720242338042828L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_UPDATED")
	private Date dateUpdated;

	@Lob
	@Column(name = "CONTENT")
	private byte[] content;

	public Catalog() {
	}

	public Catalog(byte[] content) {
		this.setDateUpdated(new Date());
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long fsPk) {
		this.id = fsPk;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Catalog)) {
			return false;
		}
		Catalog other = (Catalog) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ru.terekhov.book2read.Catalog[ ID=" + id + " ]";
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
}
