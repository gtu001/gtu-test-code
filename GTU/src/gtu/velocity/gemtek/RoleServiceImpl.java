package gtu.velocity.gemtek;

import org.apache.log4j.Logger;

import java.util.List;

import com.gemtek.ehs.model.dao.RoleDao;
import com.gemtek.ehs.model.po.RolePo;

public class ${po}ServiceImpl implements ${po}Service {
	
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(${po}ServiceImpl.class);

	private ${po}Dao ${dbName}Dao = null;
	
	/**
	 * default constructor.
	 */
	public ${po}ServiceImpl() {
		super();
	}
	
	/**
	 * getter $dbNameDao
	 * 
	 * @return $dbNameDao
	 */
	public ${po}Dao get${po}Dao() {
		return ${dbName}Dao;
	}

	/**
	 * setter $dbNameDao
	 * 
	 * @param $dbNameDao $dbNameDao
	 */
	public void set${po}Dao(${po}Dao ${dbName}Dao) {
		this.${dbName}Dao = ${dbName}Dao;
	}
	
	

	/**
	 * insert $poName
	 * 
	 * @param $poName insert $poName
	 */
	public void insert($poNameBig $poName){
		logger.info("insert($poNameBig) - start");
		this.${dbName}Dao.insert($poName);
		logger.info("insert($poNameBig) - end");
	}
		
	/**
	 * delete $poName , by $poName
	 * 
	 * @param $poName $poName
	 */
	public void deleteByPk(String $pk){
		logger.info("deleteByPk(String) - start"); //$NON-NLS-1$
		this.${dbName}Dao.deleteByPk($pk);
		logger.info("deleteByPk(String) - end"); //$NON-NLS-1$
	}
	
	/**
	 * update $poName
	 * 
	 * @param $poName update $poName
	 */
	public void update($poNameBig $poName){
		logger.info("update(RolePo) - start");
		this.${dbName}Dao.update($poName);
		logger.info("update(RolePo) - end");
	}
	
	/**
	 * query all $poName
	 * 
	 *@return $poName compose List
	 */
	public List<$poNameBig> findAll(){
		logger.info("findAll() - start");
		List<$poNameBig> list = null;
		list = this.${dbName}Dao.findAll();
		logger.info("findAll() - end");
		return list;
	}
	
	/**
	 * query $poName by $pk
	 * 
	 * @param $pk $pk
	 * @return query result $poName
	 */
	public $poNameBig findByPk(String $pk){
		logger.info("findByPk(Integer) - start");
		$poNameBig $poName = null;
		$poName = this.${dbName}Dao.findByPk(${pk});
		logger.info("findByPk(Integer) - end");
		return $poName;
	}
}
