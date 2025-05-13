<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Chọn màu nền</title>
    <style>
        body {
            background-color: ${sessionScope.color};
        }
    </style>
</head>
<body>
    <h1>Chọn màu nền</h1>
    
    <form action="ColorServlet" method="post">
        <select name="color">
            <option value="red">Đỏ</option>
            <option value="blue">Xanh dương</option>
            <option value="green">Xanh lá</option>
            <option value="yellow">Vàng</option>
            <option value="white">Trắng</option>
        </select>
        <input type="submit" value="Áp dụng">
    </form>
    
    <c:if test="${not empty sessionScope.color}">
        <c:choose>
            <c:when test="${sessionScope.color == 'red'}">
                <p>Bạn đã chọn màu đỏ</p>
            </c:when>
            <c:when test="${sessionScope.color == 'blue'}">
                <p>Bạn đã chọn màu xanh dương</p>
            </c:when>
            <c:when test="${sessionScope.color == 'green'}">
                <p>Bạn đã chọn màu xanh lá</p>
            </c:when>
            <c:when test="${sessionScope.color == 'yellow'}">
                <p>Bạn đã chọn màu vàng</p>
            </c:when>
            <c:otherwise>
                <p>Bạn đã chọn màu ${sessionScope.color}</p>
            </c:otherwise>
        </c:choose>
    </c:if>
</body>
</html>