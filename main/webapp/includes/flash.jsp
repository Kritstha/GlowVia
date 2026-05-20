<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!--
    This file handles flash messages that appear after an action
    Flash messages are temporary and disappear after being shown once
    Green toast is shown for success and red toast for error messages
-->

<!-- This shows the success flash message in a green toast -->
<c:if test="${not empty sessionScope.success}">
    <div class="toast toast-success">${sessionScope.success}</div>
    <c:remove var="success" scope="session"/>
</c:if>

<!-- Error flash message in a red toast -->
<c:if test="${not empty sessionScope.error}">
    <div class="toast toast-error">${sessionScope.error}</div>
    <c:remove var="error" scope="session"/>
</c:if>