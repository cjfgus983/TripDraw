<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>유저 상세 정보</title>
<style type="text/css">
#profile {
 max-width: 150px;
}

th:nth-child(1) {
 width: 80px;
}

th:nth-child(2) {
 width: 50%;
}

input[type='text'] {
 width: 100%;
}

#img-profile {
 width: 150px;
}
</style>
</head>
<body>
    <%@ include file="/WEB-INF/views/fragments/header.jsp"%>
    <div class="container">
        <div class="container mt-5">
            <div class="row">
                <div class="col-md-8 offset-md-2">
                    <div class="card mb-4">
                        <div class="card-header">
                            <h4>유저 정보</h4>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6">
                                    <p><strong>ID:</strong> ${user.id}</p>
                                    <p><strong>이름:</strong> ${user.name}</p>
                                    <p><strong>이메일:</strong> ${user.email}</p>
                                    <p><strong>이메일 인증:</strong> ${user.emailVerified}</p>
                                    <p><strong>성별:</strong> ${user.gender}</p>
                                    <p><strong>생년월일:</strong> ${user.birthDate}</p>
                                    <p><strong>가입일:</strong> ${user.createdAt}</p>
                                    <p><strong>최근수정일:</strong> ${user.updatedAt}</p>
                                </div>
                                <div class="mt-3 col-md-6 text-center">
                                    <img alt="프로필 이미지" src="${user.profileImg}" id="img-profile">
                                </div>
                            </div>

                            <c:if test="${loginUser.email.equals(user.email)}">
                                <div class="d-flex mt-4 justify-content-center">
                                    <form method="post" action="${root}/auth/user-delete" id="form-delete">
                                        <input type="hidden" name="id" value="${user.id}">
                                        <input type="hidden" name="email" value="${user.email}">
                                        <button type="button" class="btn btn-primary mx-3" onclick="updateUser()">수정</button>
                                        <button type="submit" class="btn btn-danger mx-3" onclick="deleteUser()">삭제</button>
                                        <button type="button" class="btn btn-secondary mx-3" onclick="updateUserProfile()">프로필 변경</button>
                                    </form>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <c:if test="${!empty error}">
            <div class="alert alert-danger" role="alert">${error}</div>
        </c:if>

        <dialog id="profileDialog">
            <input type="file" name="profile" id="profile">
            <button class="btn btn-primary" id="btn-profile-regist">등록</button>
            <button class="btn btn-secondary" id="btn-dialog-close">닫기</button>
        </dialog>
    </div>
    <%@ include file="/WEB-INF/views/fragments/footer.jsp"%>
</body>
<script>
const updateUser = () => {
    location.href = "${root}/auth/user-modify-form?email=${user.email}";
};

const deleteUser = () => {
    if (confirm("삭제하시겠습니까?")) {
        document.querySelector("#form-delete").submit();
    }
};

const updateUserProfile = () => {
    const dialog = document.querySelector("#profileDialog");
    if (!dialog.open) {
        dialog.showModal();
    }
};

// 닫기 버튼
document.querySelector("#btn-dialog-close").addEventListener("click", () => {
    document.querySelector("#profileDialog").close();
});

// 프로필 등록
document.querySelector("#btn-profile-regist").addEventListener("click", async () => {
    const formData = new FormData();
    const file = document.querySelector("#profile").files[0];
    formData.append("file", file);
    formData.append("email", "${user.email}");

    try {
        const response = await fetch("${root}/auth/profileajax", {
            method: "post",
            body: formData
        });
        const json = await response.json();
        if (json.img) {
            document.querySelector("#img-profile").src = json.img;
        } else {
            alert(json.error);
        }
        document.querySelector("#btn-dialog-close").click();
    } catch (e) {
        console.log("profile upload fail", e);
    }
});
</script>
</html>
