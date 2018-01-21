package gtu.velocity.gemtek;

import java.util.Date;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ${poNameBig} extends BasePersistenceObject {

	/**
	 * serialVersionUID
	 */

	#foreach( $key in $db.keySet() )
	// column="$key"
	private String $db.get($key) = null;
	#end

	/**
	 * default constructor.
	 */
	public ${poNameBig}() {
		super();
	}
}
