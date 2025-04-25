<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>유저 정보 수정</title>
</head>
<body>
    <%@ include file="/WEB-INF/views/fragments/header.jsp"%>
    <div class="container">
        <h1>회원 정보 수정</h1>
        <form action="${root}/auth/user-modify" method="post" class="m-3">
            <input type="hidden" name="id" value="${user.id}" />

            <div class="mb-3 row">
                <label for="name" class="col-sm-2 col-form-label">이름</label>
                <div class="col-sm-10">
                    <input type="text" name="name" id="name" class="form-control" required value="${user.name}" />
                </div>
            </div>

            <div class="mb-3 row">
                <label for="email" class="col-sm-2 col-form-label">이메일</label>
                <div class="col-sm-10">
                    <input type="email" name="email" id="email" class="form-control" readonly value="${user.email}" />
                </div>
            </div>

            <div class="mb-3 row">
                <label for="password" class="col-sm-2 col-form-label">비밀번호</label>
                <div class="col-sm-10">
                    <input type="password" name="password" id="password" class="form-control" required value="${user.password}" />
                </div>
            </div>

            <div class="mb-3 row">
                <label for="gender" class="col-sm-2 col-form-label">성별</label>
                <div class="col-sm-10">
                    <select name="gender" id="gender" class="form-control">
                        <option value="M" ${user.gender == 'M' ? 'selected' : ''}>남성</option>
                        <option value="F" ${user.gender == 'F' ? 'selected' : ''}>여성</option>
                    </select>
                </div>
            </div>

            <div class="mb-3 row">
                <label for="birthDate" class="col-sm-2 col-form-label">생년월일</label>
                <div class="col-sm-10">
                    <input type="date" name="birthDate" id="birthDate" class="form-control" value="${user.birthDate}" />
                </div>
            </div>

            <button type="submit" class="btn btn-primary">수정</button>
        </form>

        <c:if test="${!empty error}">
            <div class="alert alert-danger" role="alert">${error}</div>
        </c:if>
    </div>
    <%@ include file="/WEB-INF/views/fragments/footer.jsp"%>
</body>
</html>
