package com.iisigroup.ris.${DOMAIN}.${PACKAGE}.service;

import java.util.List;

import com.iisigroup.ris.ae.domain.ExecutantType;
import com.iisigroup.ris.exception.RisBusinessException;
import com.iisigroup.ris.rc.domain.Rc0c130DTO;

/**
 * ${chineseShortName}服務介面
 * 
 * @author ${yourName}
 */
public interface ${UPPER_ID}Service {
    
    #foreach( $key in $SERVICE_METHODS.keySet() )
    public $SERVICE_METHODS.get($key) $key(${UPPER_ID}DTO ${LOWER_ID}DTO) throws RisBusinessException;
    #end
    
    public String doPrint(${UPPER_ID}DTO ${LOWER_ID}dto, ExecutantType executantType) throws RisBusinessException;
}
