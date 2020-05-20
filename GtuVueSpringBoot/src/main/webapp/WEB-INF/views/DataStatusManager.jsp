<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">

    <title>DataStatuManager</title>
</head>
<body>
        <div class="wrapper">

            <div id="content">
                <h2>案件查詢 > 案件狀態(作業主管)</h2>
                <div class="line"></div>
                <form:form method="POST" modelAttribute="caseFilter" class="form-horizontal">
                    <div class="form-row">
                        <h4 class="pr-3 pt-1">案件申請時間:</h4>
                        <div class="form-group col-3 pt-1">
                        	<form:input type="date" path="startCWebApplyDateTime" id="startCWebApplyDateTime" class="form-control"/>
                        </div>
                        <h4 class="pt-1">~</h4>
                        <div class="form-group col-3 pt-1">
                        	<form:input type="date" path="endCWebApplyDateTime" id="endCWebApplyDateTime" class="form-control"/>
                        </div>
                    </div>
<!--                     <div class="form-row"> -->
<!--                         <div class="form-check mr-2"> -->
<!--                             <input type="checkbox" class="form-control form-check-input" id="inputCity"> -->
<!--                         </div> -->
<!--                         <h4 class="pr-3 pt-2">案件分派日期:</h4> -->
<!--                         <div class="form-group col-3 mr-2 pt-1"> -->
<!--                             <input type="date" class="form-control" id="startdate"> -->
<!--                         </div> -->
<!--                         <h4 class="pt-1">~</h4> -->
<!--                         <div class="form-group col-3 pt-1"> -->
<!--                             <input type="date" class="form-control" id="startdate"> -->
<!--                         </div> -->
<!--                     </div> -->
                    <div class="form-row">
                        <h4 class="pr-3">申請人ID:</h4>
                        <div class="form-group col-3">
                        	<form:input type="text" path="cIdNumber" id="cIdNumber" class="form-control" placeholder="請輸入申請人ID"/>
                        </div>
                    </div>
                    <div class="form-row">
                        <h4 class="pr-3">案件狀態:</h4>
                        <div class="form-group col-3">
                            <select class="custom-select mr-sm-2" id="inlineFormCustomSelect">
                                <option selected>申請書待補全</option>
                                <option value="1">Example1</option>
                                <option value="2">Example2</option>
                                <option value="3">Example3</option>
                            </select>
                        </div>
                        <h4 class="pl-3 pr-3">Sales Code:</h4>
                        <div class="form-group col-3">
                            <select class="custom-select mr-sm-2" id="inlineFormCustomSelectstatus">
                                <option selected>0000123-Tony Stark</option>
                                <option value="1">Example1</option>
                                <option value="2">Example2</option>
                                <option value="3">Example3</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <button type="button" onclick="SalesCodedisplay()"  class="btn btn-primary" id="DataStatuManagerSearch">查詢</button>
                        </div>
                        <div class="form-group pl-2">
                            <button type="submit" class="btn btn-primary">匯出</button>
                        </div>
                    </div>

                    <div id="SalesCode" class="form-row pt-2" hidden>
                        <h4 class="pr-3 pt-2">重新指派Sales Code(勾選以下清單，以重新指派):</h4>
                        <div class="form-group col-3 pt-1">
                            <select class="custom-select mr-sm-2" id="inlineFormCustomSelectstatus">
                                <option selected>無</option>
                                <option value="1">Example1</option>
                                <option value="2">Example2</option>
                                <option value="3">Example3</option>
                            </select>
                        </div>
                    </div>
                </form:form>
                <hr>
                <div id="divtabledata" hidden>
                    <table id="tabledata" class="table table-striped table-bordered" cellspacing="0" width="100%">
                        <thead>
                            <tr>
                                <th>
                                    <div class="custom-control custom-checkbox">
                                        <input type="checkbox" class="custom-control-input" id="customCheck1">
                                        <label class="custom-control-label" for="customCheck1">指派勾選</label>
                                    </div>
                                </th>
                                <th>NO.</th>
                                <th>備註</th>
                                <th>檢視案件</th>
                                <th>發查(JCIC/NC)</th>
                                <th>發查狀態</th>
                                <th>個案審查</th>
                                <th>Sales Code</th>
                                <th>申請時間</th>
                                <th>申請步驟</th>
                                <th>申請類型</th>
                                <th>申請人ID</th>
                                <th>申請人姓名</th>
                                <th>申請人手機號碼</th>
                                <th>案件審查狀態</th>
                                <th>下次聯絡時間</th>
                                <th>分派日期</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>
                                    <div class="custom-control custom-checkbox">
                                        <input class="form-check-input position-static" type="checkbox" id="blankCheckbox" value="option1" aria-label="...">
                                    </div>
                                </td>
                                <td>1</td>
                                <td><a type="button" class="btn btn-outline-dark" href="CaseRearchRemarks.jsp">備註</a></td>
                                <td><a type="button" class="btn btn-outline-dark" href="CheckCase.jsp">檢視</a></td>
                                <td><button type="button" class="btn btn-outline-dark">發查</button></td>
                                <td>已發查</td>
                                <td><a type="button" class="btn btn-outline-dark" href="Check.jsp">審查</a></td>
                                <td>999836</td>
                                <td>2019-01-15 14:11:20</td>
                                <td>2</td>
                                <td>帳戶</td>
                                <td>A123123123</td>
                                <td>Tom</td>
                                <td>10801156035</td>
                                <td>尚未聯絡</td>
                                <td>10:00 - 13:00</td>
                                <td>2019-01-17</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
</body>
</html>