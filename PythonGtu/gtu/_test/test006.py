import io
from gtu.io import stringIoUtil
from gtu.string import stringUtil
from gtu.io import LogWriter




def main() :
    # strVal = stringIoUtil.multiInput()
    # print(strVal)

    

    strVal = "INSERT INTO rcdf0m57 (apply_transaction_id, apply_date, apply_time, person_id, person_name, birth_yyymmdd, apply_email, apply_phone, apply_cell_phone, finger_mark_l, finger_mark_r, capture_status, passport_id, case_no, accept_site_id, accept_admin_office_code, settle_site_id, area_code, village, neighbor, street_doorplate, rl_update_status, rl_update_name, rl_update_id, rl_update_date, rl_update_time, foreign_office_mark, rd_process_date, rd_process_time, imi_process_date, imi_process_time, ctime, mtime) VALUES ('{txid}', '{dateVal}', '111236', 'S151582600', '顏辌幨', '0620602', 'adam@yahoo.com', '', '0957486754', '3', '4', '1', '', '10865000010000001', '65000010', '65000010', '65000010', '65000030', '中興里', '005', '中興路○段○○號○樓之○', 'A', '測試自然人5', 'RF0300123', '1081018', '111457', 'Y', '1081112', '210510', '       ', '      ', null, null);"

    for d in range(0, 11) :
        dateStr = str(19 + d).rjust(2, "0")
        dateVal = "10801" + str(19 + d).rjust(2, "0")
        log = LogWriter.LogWriter(filename="janna-sql-" + dateVal + ".txt")
        for i in range(1,100) :
            # TXFLRL1901191112360001
            txid = "TXFLRL1901" + dateStr + "1112360" + str(i).rjust(3, "0")
            line = strVal.format(txid=txid, dateVal=dateVal)
            log.writeline(line)
        log.close()


if __name__ == '__main__' :
    main()
    print("done...")
