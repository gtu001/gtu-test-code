SET TERMOUT ON
SET FEEDBACK OFF
SET VERIFY OFF
SET SCAN ON
SET LINESIZE 256
SET PAGESIZE 999

UNDEFINE sql_id

SET ECHO ON

REM
REM Setup test environment
REM

ALTER SESSION SET current_schema = &schema_name;

spool &outputFileName

PAUSE

REM
REM Display execution statistics
REM

--execute dbms_stats.gather_table_stats(ownname=>'&schema_name', tabname=>'t_contract_product');

SELECT /*+ gather_plan_statistics */ *
FROM (
	SELECT
	T.*
	FROM
	T_PREM_ARAP T, T_NOCLAIM_DISCNT_PREM D
	WHERE
		T.OFFSET_STATUS = 1
		AND D.FEE_STATUS = 0
		AND T.POLICY_ID = D.POLICY_ID
		AND T.LIST_ID = D.ARAP_ID
		AND T.POLICY_ID = 20714899
		AND T.CHANGE_ID = 133498729
);

UNDEFINE sql_id
UNDEFINE child_number

SELECT * FROM table(dbms_xplan.display_cursor(null, null,'iostats last'));

spool off;

REM
REM Cleanup
REM

UNDEFINE sql_id
UNDEFINE child_number