/**
 * 
 */
package com.staffchannel.model;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * @author Walalala
 *
 */
public class CaseFilter {
	private Date startCWebApplyDateTime;
	private Date endCWebApplyDateTime;
	private String cWebApplyNo;
	private Integer cWebApplyStatus;
	private String cIdNumber;
	private String cChName;

	public void addFilters(Criteria criteria) {
		
		if (startCWebApplyDateTime != null && endCWebApplyDateTime!= null) {
			criteria.add(Restrictions.ge("cWebApplyDateTime", this.startCWebApplyDateTime));
			criteria.add(Restrictions.le("cWebApplyDateTime", this.endCWebApplyDateTime));
		}
		
		if (cWebApplyNo != null) {
			criteria.add(Restrictions.eq("cWebApplyNo", this.cWebApplyNo));
		}
		
		if (cWebApplyStatus != null) {
			criteria.add(Restrictions.eq("cWebApplyStatus", this.cWebApplyStatus));
		}
		
		if (cIdNumber != null) {
			criteria.add(Restrictions.eq("cIdNumber", this.cIdNumber));
		}
		
		if (cChName != null) {
			criteria.add(Restrictions.like("cChName", this.cChName));
		}
		
		criteria.addOrder(Order.desc("cWebApplyDateTime"));
	}

	public Date getStartCWebApplyDateTime() {
		return startCWebApplyDateTime;
	}

	public void setStartCWebApplyDateTime(Date startCWebApplyDateTime) {
		this.startCWebApplyDateTime = startCWebApplyDateTime;
	}

	public Date getEndCWebApplyDateTime() {
		return endCWebApplyDateTime;
	}

	public void setEndCWebApplyDateTime(Date endCWebApplyDateTime) {
		this.endCWebApplyDateTime = endCWebApplyDateTime;
	}

	public String getcWebApplyNo() {
		return cWebApplyNo;
	}

	public void setcWebApplyNo(String cWebApplyNo) {
		this.cWebApplyNo = cWebApplyNo;
	}

	public Integer getcWebApplyStatus() {
		return cWebApplyStatus;
	}

	public void setcWebApplyStatus(Integer cWebApplyStatus) {
		this.cWebApplyStatus = cWebApplyStatus;
	}

	public String getcIdNumber() {
		return cIdNumber;
	}

	public void setcIdNumber(String cIdNumber) {
		this.cIdNumber = cIdNumber;
	}

	public String getcChName() {
		return cChName;
	}

	public void setcChName(String cChName) {
		this.cChName = cChName;
	}

}
