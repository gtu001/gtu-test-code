<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<%@include file="common/headCommon.jsp" %>
	<title>Login</title>
</head>
<body>
    <div id="content">
            <div class="mx-auto mt-5 row">
                <div class="col-md-4 offset-md-4">
                    <div class="account-wall">
                        <img class="profile-img" src="<c:url value='/static/jpg/test.gif' />" style='height: 100%; width: 100%; object-fit: contain'>
                        <form action="${loginUrl}" method="post" class="form-signin">
                        	<c:if test="${param.error != null}">
								<div class="alert alert-danger">
									<p>帳號或密碼錯誤</p>
								</div>
							</c:if>
							<c:if test="${param.logout != null}">
								<div class="alert alert-success">
									<p>您已成功登出</p>
								</div>
							</c:if>
                            <a>帳號:</a><br>
                            <input type="text" class="form-control" id="username" name="ssoId" placeholder="請輸入帳號" required>
                            <a>密碼:</a><br>
                            <input type="password" class="form-control" id="password" name="password" placeholder="請輸入密碼" required>
                            <div class="input-group input-sm">
                              <div class="checkbox">
                                <label><input type="checkbox" id="rememberme" name="remember-me"> Remember Me</label>  
                              </div>
                            </div>
                            <input type="hidden" name="${_csrf.parameterName}"  value="${_csrf.token}" />
                            <div class="form-actions">
								<input type="submit"
									class="btn btn-lg btn-primary btn-block mt-5" value="Sign in">
							</div>
                        </form>
                    </div>
                </div>
            </div>
    </div>

    <script type="text/javascript">
        $(document).ready(function () {
            $('#sidebarCollapse').on('click', function () {
                $('#sidebar').toggleClass('active');
                $(this).toggleClass('active');
            });
        });
    </script>

</body>
</html>
