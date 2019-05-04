<html>

<h1>登录页面</h1>
<div>
<#if errorMsg ??>
	<p style="color:red"> ${errorMsg}</p>
</#if>
</div>
<form action="/login" method="post">
	<input placeholder="用户名" name="username"/>
	<input placeholder="密码" name="password" type="password"/>
	<input type="submit" value="登录"/>
</form>
</html>