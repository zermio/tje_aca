<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- tag library 선언 : c tag --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<title>게시물 목록페이지</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/common.css" />" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	var msg = '${msg}';
	if(msg!=''){
		alert(msg);
	}
	$('#btnSearch').on('click', function(){
		var searchType = $('#searchType option:selected').val();
		var searchText = $('#searchText').val();
		
		if(searchText==''){
			alert('검색어를 입력하세요');
			return;
		}
		if(searchText.length<2){
			alert('검색어를 2자 이상 입력하세요.');
			return;
		}
		var sf = document.searchForm;
		sf.action = '<c:url value="/board/list.do"/>';
		sf.submit();
		
	});
	
});
</script>
</head>
<body>

	<!-- wrap -->
	<div id="wrap">

		<!-- container -->
		<div id="container">

			<!-- content -->
			<div id="content">

				<!-- board_search -->
				<div class="board_search">
					<form name="searchForm" method="get">
					<input type="hidden" name="typeSeq" value="2"/>
					<select id="searchType" name="searchType" title="선택메뉴">
						<option selected="selected" value="1">전체</option>
						<option value="2">제목</option>
						<option value="3">내용</option>
					</select> 
					<input type="text" id="searchText" name="searchText" title="검색어 입력박스" class="input_100" value="${searchText}" /> 
					<input type="button" id="btnSearch" value="검색" title="검색버튼" class="btn_search" />
					</form>
				</div>
				<!-- //board_search -->

				<!-- board_area -->
				<div class="board_area">
						<fieldset>
							<legend>게시물 목록</legend>
							<!-- board list table -->
							<table summary="표 내용은 게시물의 목록입니다." class="board_list_table">
								<caption>게시물 목록</caption>
								<colgroup>
									<col width="5%" />
									<col width="40%" />
									<col width="15%" />
									<col width="25%" />
									<col width="10%" />
									<col width="5%" />
								</colgroup>
								<thead>
									<tr>
										<th scope="col">번호</th>
										<th scope="col">제목</th>
										<th scope="col">작성자</th>
										<th scope="col">작성일</th>
										<th scope="col">조회</th>
										<th scope="col">추천</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${list}" var="row">
									<tr>
										<td>${row.board_seq}</td>
										<td class="tleft">
											<span class="bold"><a href='<c:url value="/board/read.do?boardSeq=${row.board_seq}&typeSeq=2"/>'>${row.title}</a></span>
										</td>
										<td>${row.member_nick}</td>
										<td>${row.create_date}</td>
										<td class="tright">${row.hits}</td>
										<td class="tright">0</td>
									</tr>
									</c:forEach>
								</tbody>
							</table>
							<!-- //board list table -->

							<!--paginate start -->
							<div class="paginate">
								<c:if test="${startBlock!=1}">
									<a class="pre" href='<c:url value="/board/list.do?currentPage=${startBlock-1}&typeSeq=2&searchType=${searchType}&searchText=${searchText}"/>'>이전페이지</a> 
								</c:if>
								<c:forEach begin="${startBlock }" end="${endBlock }" step="1" var="pageNo">
									<c:choose>
										<c:when test="${pageNo==currentPage}">
											<strong>${pageNo}</strong>
										</c:when>
										<c:otherwise>
											<a href='<c:url value="/board/list.do?currentPage=${pageNo}&typeSeq=2&searchType=${searchType}&searchText=${searchText}"/>'>${pageNo}</a>
										</c:otherwise>
									</c:choose>
								</c:forEach>
								<c:if test="${endBlock!=totalPage}">
									<a class="next" href='<c:url value="/board/list.do?currentPage=${endBlock+1}&typeSeq=2&searchType=${searchType}&searchText=${searchText}"/>'>다음페이지</a>
								</c:if>
							</div>
							<!--//paginate end -->

							<!-- bottom button -->
							<c:choose>
								<c:when test="${sessionScope.memberId != null }">
									<div class="btn_bottom">
										<div class="btn_bottom_right">
										<a href="<c:url value='/board/goWritePage.do'/>">
											<input type="button" value="글쓰기" title="글쓰기" />
										</a>
										</div>
									</div>
								</c:when>
							</c:choose>
							<!-- //bottom button -->

						</fieldset>
				</div>
				<!-- //board_area -->

			</div>
			<!-- //content -->

		</div>
		<!-- //container -->

	</div>
	<!-- //wrap -->

</body>
</html>