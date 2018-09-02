package com.iisigroup.ris.${DOMAIN}.domain;

import java.util.Date;

@Deprecated
public class ${UPPER_ID}DTO {

    #foreach( $var in $DTO_FIELDS )
    /** $var.get(2) */
    private $var.get(1) $var.get(0);
    #end
}
