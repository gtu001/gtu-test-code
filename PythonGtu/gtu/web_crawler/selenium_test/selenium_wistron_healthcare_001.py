from selenium import webdriver
from selenium.webdriver.support.ui import Select
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import asyncio

login_account_name = "employeeId"
login_phone_name = "mobileNo"
form_one_submit_css_selector = ''

acct_id_data = 'tp1806005'
phone_num_data = '0954006788' #0905137990

login_url = "https://wits-rms.wistronits.cn/healthy-tp/login";
login_url_2 = "https://wits-rms.wistronits.cn/healthy-tp/im011a";
driver = webdriver.Chrome()
work_country_id = 'workProvince'
work_city_id = 'workCity'
temperature_id = 'temperatureType0'
symptomList0_id = 'symptomList0'
attentionKb0_id = 'attentionKb0'
familyStatusKb0_id = 'familyStatusKb0'
familyStatusKb1_id = 'familyStatusKb1'

#login and pad data tp_id && phone_number

async def login():
    driver.get(login_url)
    print('open chrome with url ' + login_url)
    login_acct_id_input = driver.find_element_by_id(login_account_name)
    login_acct_phone_input = driver.find_element_by_id(login_phone_name)
    page_one_submit_btn = driver.find_element_by_css_selector("div.col-12 > button")
    login_acct_id_input.send_keys(acct_id_data)
    login_acct_phone_input.send_keys(phone_num_data)
    page_one_submit_btn.click()

async def login2():
    team_project = driver.find_element_by_id("projectTeam")
    team_project.clear()
    team_project.send_keys("變數寶典")
    Select(driver.find_element_by_id(work_country_id)).select_by_visible_text('台湾')
    Select(driver.find_element_by_id(work_city_id)).select_by_visible_text('台北')
    #溫度 預設通過紅外線
    driver.find_element_by_id(temperature_id).click()
    #症狀 預設無症狀
    ele1 = driver.find_element_by_id(symptomList0_id)
    driver.execute_script("arguments[0].click();", ele1)
    #如有以上症狀 是否就醫 預設否
    if_go_hospital = driver.find_element_by_id(attentionKb0_id)
    driver.execute_script("arguments[0].click();", if_go_hospital)
    #家屬是否有異常 預設無,test預設無
    family_status = driver.find_element_by_id(familyStatusKb0_id)
    driver.execute_script("arguments[0].click();", family_status)
    #button 提交
    #submitBtn = driver.find_element_by_css_selector('div.col-12 > button')
    #driver.execute_script("arguments[0].click();", submitBtn)

async def main():
    await login()
    await login2()


asyncio.run(main())


