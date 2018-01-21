package gtu.velocity.gemtek;

import org.apache.log4j.Logger;

import java.util.List;

import com.gemtek.ehs.model.po.RolePo;


public class ${po}DaoImpl extends BaseDataAccessObject implements ${po}Dao {
	
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(${po}DaoImpl.class);
	
	/**
	 * defalut constructor
	 */
	public ${po}DaoImpl() {
		super();
	}

	/**
	 * delete $poName , by $poName
	 * 
	 * @param $poName $poName
	 */
	public void deleteByPk(String $pk) {
		logger.info("deleteByPk(String) - start");

		this.getSqlMapClientTemplate().delete("${po}.deleteByPk", $pk);

		logger.info("deleteByPk(String) - end");
	}

	/**
	 * query all $poName
	 * 
	 *@return $poName compose List
	 */
	public List<$poNameBig> findAll() {
		logger.info("findAll() - start");

		List<$poNameBig> list = null;

		list = (List<$poNameBig>) this.getSqlMapClientTemplate().queryForList(
				"${po}.findAll");

		logger.info("findAll() - end");
		return list;
	}

	/**
	 * query $poName by $pk
	 * 
	 * @param $pk $pk
	 * @return query result $poName
	 */
	public $poNameBig findByPk(String $pk) {
		logger.info("findByPk(Integer) - start");

		$poNameBig $poName = null;
		${poName} = ($poNameBig) this.getSqlMapClientTemplate().queryForObject(
				"${po}.findByPk", $pk);

		logger.info("findByPk(String) - end");
		return ${poName};
	}

	/**
	 * insert $poName
	 * 
	 * @param $poName insert $poName
	 */
	public void insert($poNameBig $poName) {
		logger.info("insert($poNameBig) - start");

		this.getSqlMapClientTemplate().insert("${po}.insert", $poName);

		logger.info("insert($poNameBig) - end");
	}

	/**
	 * update $poName
	 * 
	 * @param $poName update $poName
	 */
	public void update($poNameBig $poName) {
		logger.info("update($poNameBig) - start");

		this.getSqlMapClientTemplate().update("${po}.update", $poName);

		logger.info("update($poNameBig) - end");
	}
}
