<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>멤버 목록</title>
</head>
<body>
    <%@ include file="/WEB-INF/views/fragments/header.jsp"%>
    <div class="container">
        <h1>멤버 목록</h1>
        <form class="row mb-3" action="${root }/auth/member-list" method="get" id="search-form">
            <input type="hidden" id="currentPage" name="currentPage" value="1" />
            <div class="d-flex justify-content-end">
                <select class="form-select w-25" name="key">
                    <option disabled ${empty param.key?'selected':'' }>검색항목 선택</option>
                    <option value="1" ${param.key=='1'?'selected':'' }>name</option>
                    <option value="2" ${param.key=='2'?'selected':'' }>email</option>
                </select>
                <input type="text" class="form-control w-25" name="word" value="${param.word}">
                <button type="submit" class="btn btn-primary">검색</button>
            </div>
        </form>
        <table class="table">
            <thead>
                <tr>
                    <th>no</th>
                    <th>name</th>
                    <th>email</th>
                    <th>gender</th>
                    <th>birth date</th>
                    <th>created at</th>
                    <th>updated at</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${page.list }" var="item">
                    <tr>
                        <td>${item.id }</td>
                        <td>${item.name }</td>
                        <td><a href="${root }/auth/member-detail?email=${item.email}">${item.email }</a></td>
                        <td>${item.gender }</td>
                        <td>${item.birthDate }</td>
                        <td>${item.createdAt }</td>
                        <td>${item.updatedAt }</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <nav class="d-flex justify-content-center">
            <ul class="pagination">
                <c:if test="${page.hasPre}">
                    <li class="page-item"><a class="page-link" href="#" data-page="${ page.startPage-1}">이전</a></li>
                </c:if>
                <c:forEach begin="${page.startPage}" end="${page.endPage}" var="item">
                    <li class="page-item ${page.condition.currentPage == item ? 'active' : ''}"><a
                            class="page-link" href="#" data-page="${ item}">${item}</a></li>
                </c:forEach>
                <c:if test="${page.hasNext}">
                    <li class="page-item"><a class="page-link" href="#" data-page="${ page.endPage+1}">다음</a></li>
                </c:if>
            </ul>
        </nav>
    </div>
    <%@ include file="/WEB-INF/views/fragments/footer.jsp"%>
</body>
<script>
const pageLinks = document.querySelectorAll(".pagination a");
pageLinks.forEach(link =>{
  link.addEventListener("click", (e)=>{
    e.preventDefault();
    document.querySelector("#currentPage").value=link.dataset.page;
    document.querySelector("#search-form").submit();
  })
})
</script>
</html>
