<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Renders any flash messages and then clears them via session-attribute-cleanup helper --%>
<div class="${empty sessionScope.flashSuccess ? 'hide' : 'toast toast-success'}">
    ${sessionScope.flashSuccess}
</div>
<div class="${empty sessionScope.flashError ? 'hide' : 'toast toast-error'}">
    ${sessionScope.flashError}
</div>
${sessionScope.remove('flashSuccess')}${sessionScope.remove('flashError')}
