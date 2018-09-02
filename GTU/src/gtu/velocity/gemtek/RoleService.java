package gtu.velocity.gemtek;

import java.util.List;

import com.gemtek.ehs.model.po.RolePo;

public interface ${po}Service {


	/**
	 * insert $poName
	 * 
	 * @param $poName insert $poName
	 */
	public void insert($poNameBig $poName);
		
	/**
	 * delete $poName , by $poName
	 * 
	 * @param $poName $poName
	 */
	public void deleteByPk(String $pk);
	
	/**
	 * update $poName
	 * 
	 * @param $poName update $poName
	 */
	public void update($poNameBig $poName);
	
	/**
	 * query all $poName
	 * 
	 *@return $poName compose List
	 */
	public List<$poNameBig> findAll();
	
	/**
	 * query $poName by $pk
	 * 
	 * @param $pk $pk
	 * @return query result $poName
	 */
	public $poNameBig findByPk(String $pk);
}
