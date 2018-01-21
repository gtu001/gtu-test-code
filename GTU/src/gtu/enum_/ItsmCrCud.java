package gtu.enum_;

import gtu.spring.steve.ItsmCrData;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class ItsmCrCud {
	
    private final Long id;
    private final String csimsId;
    private final String crType;
    private final String mainCategory;
    private final String category;
    private final String subCategory;
    private final Date planStartDate;
    private final Date planEndDate;
    private final String ciCode;
    private final String description;
    private final String information;
    private final String maintainOrPlaning;
    private final String impactRisk;
    private final String impactDescription;
    private final String riskDescription;
    private final String concernCustomer;
    private final Date issueDate;
    private final String stateFlag;
    private final String cancelMsg;
    private final Date cancelDate;
    private final Long crId;
    private final Long crStatus;
    private final String neStatus;
    private final String sysErr;
    private final String admGroup;
    private final String creator;
    private final Long crCatId;
    private final Long impactRiskId;
    private final String workGroup;
    private final String lineManager;
    private final String specialist;
    private final Long newStatus;
    private final Date crCloseDate;
    private final String sysErrMsg;
    private final String crStatusText;
    

    /**
     * @author Steve Tien
     * @version 1.0, Feb 18, 2009
     */
    public static class Builder {
        public static Builder newInstance() {
            return new Builder();
        }
        /* ↓↓↓ Optional Parameters ↓↓↓ */
        private Long id = null;
        private String csimsId = null;
        private String crType = null;
        private String mainCategory = null;
        private String category = null;
        private String subCategory = null;
        private Date planStartDate = null;
        private Date planEndDate = null;
        private String ciCode = null;
        private String description = null;
        private String information = null;
        private String maintainOrPlaning = null;
        private String impactRisk = null;
        private String impactDescription = null;
        private String riskDescription = null;
        private String concernCustomer = null;
        private Date issueDate = null;
        private String stateFlag = null;
        private String cancelMsg = null;
        private Date cancelDate = null;
        private Long crId = null;
        private Long crStatus = null;
        private String neStatus = null;
        private String sysErr = null;
        private String admGroup = null;
        private String creator = null;
        private Long crCatId = null;
        private Long impactRiskId = null;
        private String workGroup = null;
        private String lineManager = null;
        private String specialist = null;
        private Long newStatus = null;
        private Date crCloseDate = null;
        private String sysErrMsg = null;
        private String crStatusText = null;
        private Builder() {}
        public Builder admGroup(String admGroup) {
            this.admGroup = admGroup;
            return this;
        }
        public ItsmCrCud build() {
            return new ItsmCrCud(this);
        }
        public Builder cancelDate(Date cancelDate) {
            if (null == cancelDate) {
                this.cancelDate = null;
            } else {
                // Make Defensive Copy of Parameter
                this.cancelDate = new Date(cancelDate.getTime());
            }
            return this;
        }
        public Builder cancelMsg(String cancelMsg) {
            this.cancelMsg = cancelMsg;
            return this;
        }
        public Builder category(String category) {
            this.category = category;
            return this;
        }
        public Builder ciCode(String ciCode) {
            this.ciCode = ciCode;
            return this;
        }
        public Builder concernCustomer(String concernCustomer) {
            this.concernCustomer = concernCustomer;
            return this;
        }
        public Builder crCatId(Long crCatId) {
            this.crCatId = crCatId;
            return this;
        }
        public Builder crCloseDate(Date crCloseDate) {
            if (null == crCloseDate) {
                this.crCloseDate = null;
            } else {
                // Make Defensive Copy of Parameter
                this.crCloseDate = new Date(crCloseDate.getTime());
            }
            return this;
        }
        public Builder creator(String creator) {
            this.creator = creator;
            return this;
        }
        public Builder crId(Long crId) {
            this.crId = crId;
            return this;
        }
        public Builder crStatus(Long crStatus) {
            this.crStatus = crStatus;
            return this;
        }
        public Builder crStatusText(String crStatusText) {
            this.crStatusText = crStatusText;
            return this;
        }
        public Builder crType(String crType) {
            this.crType = crType;
            return this;
        }
        public Builder csimsId(String csimsId) {
            this.csimsId = csimsId;
            return this;
        }
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder impactDescription(String impactDescription) {
            this.impactDescription = impactDescription;
            return this;
        }
        public Builder impactRisk(String impactRisk) {
            this.impactRisk = impactRisk;
            return this;
        }
        public Builder impactRiskId(Long impactRiskId) {
            this.impactRiskId = impactRiskId;
            return this;
        }
        public Builder information(String information) {
            this.information = information;
            return this;
        }
        public Builder issueDate(Date issueDate) {
            if (null == issueDate) {
                this.issueDate = new Date();
            } else {
                // Make Defensive Copy of Parameter
                this.issueDate = new Date(issueDate.getTime());
            }
            return this;
        }
        public Builder lineManager(String lineManager) {
            this.lineManager = lineManager;
            return this;
        }
        public Builder mainCategory(String mainCategory) {
            this.mainCategory = mainCategory;
            return this;
        }
        public Builder maintainOrPlaning(String maintainOrPlaning) {
            this.maintainOrPlaning = maintainOrPlaning;
            return this;
        }
        public Builder neStatus(String neStatus) {
            this.neStatus = neStatus;
            return this;
        }
        public Builder newStatus(Long newStatus) {
            this.newStatus = newStatus;
            return this;
        }
        public Builder planEndDate(Date planEndDate) {
            if (null == planEndDate) {
                this.planEndDate = null;
            } else {
                // Make Defensive Copy of Parameter
                this.planEndDate = new Date(planEndDate.getTime());
            }
            return this;
        }
        public Builder planStartDate(Date planStartDate) {
            if (null == planStartDate) {
                this.planStartDate = null;
            } else {
                // Make Defensive Copy of Parameter
                this.planStartDate = new Date(planStartDate.getTime());
            }
            return this;
        }
        public Builder riskDescription(String riskDescription) {
            this.riskDescription = riskDescription;
            return this;
        }
        public Builder specialist(String specialist) {
            this.specialist = specialist;
            return this;
        }
        public Builder stateFlag(String stateFlag) {
            this.stateFlag = stateFlag;
            return this;
        }
        public Builder subCategory(String subCategory) {
            this.subCategory = subCategory;
            return this;
        }
        public Builder sysErr(String sysErr) {
            this.sysErr = sysErr;
            return this;
        }
        public Builder sysErrMsg(String sysErrMsg) {
            this.sysErrMsg = sysErrMsg;
            return this;
        }
        public Builder workGroup(String workGroup) {
            this.workGroup = workGroup;
            return this;
        }
    }

    
    private ItsmCrCud(Builder builder) {
        this.id = builder.id;
        this.csimsId = builder.csimsId;
        this.crType = builder.crType;
        this.mainCategory = builder.mainCategory;
        this.category = builder.category;
        this.subCategory = builder.subCategory;

        if (null == builder.planStartDate) {
            this.planStartDate = null;
        } else {
            // Make Defensive Copy of Parameter
            this.planStartDate = new Date(builder.planStartDate.getTime());
        }

        if (null == builder.planEndDate) {
            this.planEndDate = null;
        } else {
            // Make Defensive Copy of Parameter
            this.planEndDate = new Date(builder.planEndDate.getTime());
        }

        this.ciCode = builder.ciCode;
        this.description = builder.description;
        this.information = builder.information;
        this.maintainOrPlaning = builder.maintainOrPlaning;
        this.impactRisk = builder.impactRisk;
        this.impactDescription = builder.impactDescription;
        this.riskDescription = builder.riskDescription;
        this.concernCustomer = builder.concernCustomer;

        if (null == builder.issueDate) {
            this.issueDate = new Date();
        } else {
            // Make Defensive Copy of Parameter
            this.issueDate = new Date(builder.issueDate.getTime());
        }

        this.stateFlag = builder.stateFlag;
        this.cancelMsg = builder.cancelMsg;

        if (null == builder.cancelDate) {
            this.cancelDate = null;
        } else {
            // Make Defensive Copy of Parameter
            this.cancelDate = new Date(builder.cancelDate.getTime());
        }

        this.crId = builder.crId;
        this.crStatus = builder.crStatus;
        this.neStatus = builder.neStatus;
        this.sysErr = builder.sysErr;
        this.admGroup = builder.admGroup;
        this.creator = builder.creator;
        this.crCatId = builder.crCatId;
        this.impactRiskId = builder.impactRiskId;
        this.workGroup = builder.workGroup;
        this.lineManager = builder.lineManager;
        this.specialist = builder.specialist;
        this.newStatus = builder.newStatus;

        if (null == builder.crCloseDate) {
            this.crCloseDate = null;
        } else {
            // Make Defensive Copy of Parameter
            this.crCloseDate = new Date(builder.crCloseDate.getTime());
        }

        this.sysErrMsg = builder.sysErrMsg;
        this.crStatusText = builder.crStatusText;
    }

	

	/**
	 * @author Steve Tien
	 * @version 1.0, Mar 13, 2009
	 */
	private static enum ColumnInfo {
		ID(args().name("ID").javaType(Number.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				Long value = ItsmCrCud.getId();
				return javaType.cast(value);
			}
		},
		CSIMS_ID(args().name("CSIMS_ID").javaType(String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getCsimsId();
				return javaType.cast(value);
			}
		},
		CR_TYPE(args().name("CR_TYPE").javaType(String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getCrType();
				return javaType.cast(value);
			}
		},
		MAIN_CATEGORY(args().name("MAIN_CATEGORY").javaType(String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getMainCategory();
				return javaType.cast(value);
			}
		},
		CATEGORY(args().name("CATEGORY").javaType(String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getCategory();
				return javaType.cast(value);
			}
		},
		SUB_CATEGORY(args().name("SUB_CATEGORY").javaType(String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getSubCategory();
				return javaType.cast(value);
			}
		},
		PLAN_START_DATE(args().name("PLAN_START_DATE").javaType(Date.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				Date value = ItsmCrCud.getPlanStartDate();
				return javaType.cast(value);
			}
		},
		PLAN_END_DATE(args().name("PLAN_END_DATE").javaType(Date.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				Date value = ItsmCrCud.getPlanEndDate();
				return javaType.cast(value);
			}
		},
		CI_CODE(args().name("CI_CODE").javaType(String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getCiCode();
				return javaType.cast(value);
			}
		},
		DESCRIPTION(args().name("DESCRIPTION").javaType(String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getDescription();
				return javaType.cast(value);
			}
		},
		INFORMATION(args().name("INFORMATION").javaType(String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getInformation();
				return javaType.cast(value);
			}
		},
		MAINTAIN_OR_PLANNING(args().name("MAINTAIN_OR_PLANNING").javaType(
				String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getMaintainOrPlaning();
				return javaType.cast(value);
			}
		},
		IMPACT_RISK(args().name("IMPACT_RISK").javaType(String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getImpactRisk();
				return javaType.cast(value);
			}
		},
		IMPACT_DESCRIPTION(args().name("IMPACT_DESCRIPTION").javaType(
				String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getImpactDescription();
				return javaType.cast(value);
			}
		},
		RISK_DESCRIPTION(args().name("RISK_DESCRIPTION").javaType(String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getRiskDescription();
				return javaType.cast(value);
			}
		},
		CONCERN_CUSTOMER(args().name("CONCERN_CUSTOMER").javaType(String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getConcernCustomer();
				return javaType.cast(value);
			}
		},
		ISSUE_DATE(args().name("ISSUE_DATE").javaType(Date.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				Date value = ItsmCrCud.getIssueDate();
				return javaType.cast(value);
			}
		},
		STATE_FLAG(args().name("STATE_FLAG").javaType(String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getStateFlag();
				return javaType.cast(value);
			}
		},
		CANCEL_MSG(args().name("CANCEL_MSG").javaType(String.class)) {

			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getCancelMsg();
				return javaType.cast(value);
			}
		},
		CANCEL_DATE(args().name("CANCEL_DATE").javaType(Date.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				Date value = ItsmCrCud.getCancelDate();
				return javaType.cast(value);
			}
		},
		CR_ID(args().name("CR_ID").javaType(Number.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				Long value = ItsmCrCud.getCrId();
				return javaType.cast(value);
			}
		},
		CR_STATUS(args().name("CR_STATUS").javaType(Number.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				Long value = ItsmCrCud.getCrStatus();
				return javaType.cast(value);
			}
		},
		NE_STATUS(args().name("NE_STATUS").javaType(String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getNeStatus();
				return javaType.cast(value);
			}
		},
		SYS_ERR(args().name("SYS_ERR").javaType(String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getSysErr();
				return javaType.cast(value);
			}
		},
		ADMGROUP(args().name("ADMGROUP").javaType(String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getAdmGroup();
				return javaType.cast(value);
			}
		},
		CREATOR(args().name("CREATOR").javaType(String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getCreator();
				return javaType.cast(value);
			}
		},
		CR_CAT_ID(args().name("CR_CAT_ID").javaType(Number.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				Long value = ItsmCrCud.getCrCatId();
				return javaType.cast(value);
			}
		},
		IMPACT_RISK_ID(args().name("IMPACT_RISK_ID").javaType(Number.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				Long value = ItsmCrCud.getImpactRiskId();
				return javaType.cast(value);
			}
		},
		WORKGROUP(args().name("WORKGROUP").javaType(String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getWorkGroup();
				return javaType.cast(value);
			}
		},
		LINE_MANAGER(args().name("LINE_MANAGER").javaType(String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getLineManager();
				return javaType.cast(value);
			}
		},
		SPECIALIST(args().name("SPECIALIST").javaType(String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getSpecialist();
				return javaType.cast(value);
			}
		},
		NEW_STATUS(args().name("NEW_STATUS").javaType(Number.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				Long value = ItsmCrCud.getNewStatus();
				return javaType.cast(value);
			}
		},
		CR_CLOSE_DATE(args().name("CR_CLOSE_DATE").javaType(Date.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				Date value = ItsmCrCud.getCrCloseDate();
				return javaType.cast(value);
			}
		},
		SYS_ERR_MSG(args().name("SYS_ERR_MSG").javaType(String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getSysErrMsg();
				return javaType.cast(value);
			}
		},
		CR_STATUS_TEXT(args().name("CR_STATUS_TEXT").javaType(String.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				String value = ItsmCrCud.getCrStatusText();
				return javaType.cast(value);
			}
		};

		private static class Arguments {
			private String name = "";
			private boolean nullable = true;
			private Class<?> javaType = Object.class;

			private Arguments() {}
			public Arguments javaType(Class<?> javaType) {
				this.javaType = javaType;
				return this;
			}
			public Arguments name(String name) {
				this.name = name;
				return this;
			}
			public Arguments nullable(boolean nullable) {
				this.nullable = nullable;
				return this;
			}
		}

		private static Arguments args() {
			return new Arguments();
		}

		private final String name;

		private final boolean nullable;

		private final Class<?> javaType;

		ColumnInfo(Arguments args) {
			this.name = args.name;
			this.nullable = args.nullable;
			this.javaType = args.javaType;
		}

		public Class<?> getJavaType() {
			return javaType;
		}

		private String getName() {
			return name;
		}

		abstract public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData);

		public boolean isNullable() {
			return nullable;
		}
	}



	public Long getId() {
		return id;
	}



	public String getCsimsId() {
		return csimsId;
	}



	public String getCrType() {
		return crType;
	}



	public String getMainCategory() {
		return mainCategory;
	}



	public String getCategory() {
		return category;
	}



	public String getSubCategory() {
		return subCategory;
	}



	public Date getPlanStartDate() {
		return planStartDate;
	}



	public Date getPlanEndDate() {
		return planEndDate;
	}



	public String getCiCode() {
		return ciCode;
	}



	public String getDescription() {
		return description;
	}



	public String getInformation() {
		return information;
	}



	public String getMaintainOrPlaning() {
		return maintainOrPlaning;
	}



	public String getImpactRisk() {
		return impactRisk;
	}



	public String getImpactDescription() {
		return impactDescription;
	}



	public String getRiskDescription() {
		return riskDescription;
	}



	public String getConcernCustomer() {
		return concernCustomer;
	}



	public Date getIssueDate() {
		return issueDate;
	}



	public String getStateFlag() {
		return stateFlag;
	}



	public String getCancelMsg() {
		return cancelMsg;
	}



	public Date getCancelDate() {
		return cancelDate;
	}



	public Long getCrId() {
		return crId;
	}



	public Long getCrStatus() {
		return crStatus;
	}



	public String getNeStatus() {
		return neStatus;
	}



	public String getSysErr() {
		return sysErr;
	}



	public String getAdmGroup() {
		return admGroup;
	}



	public String getCreator() {
		return creator;
	}



	public Long getCrCatId() {
		return crCatId;
	}



	public Long getImpactRiskId() {
		return impactRiskId;
	}



	public String getWorkGroup() {
		return workGroup;
	}



	public String getLineManager() {
		return lineManager;
	}



	public String getSpecialist() {
		return specialist;
	}



	public Long getNewStatus() {
		return newStatus;
	}



	public Date getCrCloseDate() {
		return crCloseDate;
	}



	public String getSysErrMsg() {
		return sysErrMsg;
	}



	public String getCrStatusText() {
		return crStatusText;
	}
}
