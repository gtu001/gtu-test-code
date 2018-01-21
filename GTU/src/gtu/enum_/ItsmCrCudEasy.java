package gtu.enum_;

import gtu.spring.steve.ItsmCrData;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class ItsmCrCudEasy {
	
    private final Long id;
    
	public Long getId() {
		return id;
	}
	
    private ItsmCrCudEasy(Builder builder) {
        this.id = builder.id;
    }

    
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
        private Builder() {}
        public ItsmCrCudEasy build() {
            return new ItsmCrCudEasy(this);
        }
        public Builder id(Long id) {
            this.id = id;
            return this;
        }
    }

	/**
	 * @author Steve Tien
	 * @version 1.0, Mar 13, 2009
	 */
	private static enum ColumnInfo {
		ID(args().name("ID").javaType(Number.class)) {
			@Override
			public <T> T getValue(Class<T> javaType, ItsmCrData itsmCrData) {
				Long value = ItsmCrCudEasy.getId();
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
}
