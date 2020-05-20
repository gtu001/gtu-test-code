package com.staffchannel.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="T_AUDITTRAIL_LOG")
public class AuditTrailLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6741270566065262308L;

	@Id @GeneratedValue(strategy =GenerationType.IDENTITY)
	private Integer id;

	@Column(name="ACCOUNT" , nullable=true)
	private String Account;

	@Column(name="IP" , nullable=false)
	private String IP;

	@Column(name="USEFUNCTION" , nullable=true)
	private Integer useFunction;

	@Column(name="MEMO" , nullable=false)
	private String Memo;

	@Column(name="CREATE_DATETIME" , nullable=true)
	private Timestamp Create_DateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccount() {
		return Account;
	}

	public void setAccount(String account) {
		Account = account;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public Integer getUseFunction() {
		return useFunction;
	}

	public void setUseFunction(Integer useFunction) {
		this.useFunction = useFunction;
	}

	public String getMemo() {
		return Memo;
	}

	public void setMemo(String memo) {
		Memo = memo;
	}	

	public Timestamp getCreate_DateTime() {
		return Create_DateTime;
	}

	public void setCreate_DateTime(Timestamp create_DateTime) {
		Create_DateTime = create_DateTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Account == null) ? 0 : Account.hashCode());
		result = prime * result + ((IP == null) ? 0 : IP.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		AuditTrailLog other = (AuditTrailLog) obj;
		if (Account == null) {
			if (other.Account != null)
				return false;
		} else if (!Account.equals(other.Account))
			return false;
		if (IP == null) {
			if (other.IP != null)
				return false;
		} else if (!IP.equals(other.IP))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AuditTrailLog [id=" + id + ", Account=" + Account + ", IP=" + IP + ", useFunction=" + useFunction
				+ ", Memo=" + Memo + ", Create_DateTime=" + Create_DateTime + ", getId()=" + getId() + ", getAccount()="
				+ getAccount() + ", getIP()=" + getIP() + ", getUseFunction()=" + getUseFunction() + ", getMemo()="
				+ getMemo() + ", getCreate_DateTime()=" + getCreate_DateTime() + "]";
	}
	
}
