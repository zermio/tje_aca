<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- tag library 선언 : c tag --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<title>게시물 상세페이지</title>
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
	$('#btnGoUp').on('click',function(){
		if(confirm("바꾸실?")){
			var rf = document.readForm;
			rf.action = "<c:url value='/board/goUpdate.do'/>";
			rf.submit();
		}
	});
	$('#btnDel').on('click',function(){
		if(confirm("지우실?")){
			var rf = document.readForm;
			rf.action = "<c:url value='/board/delete.do'/>";
			rf.submit();
		}
	});
});


	/* var msg = '${msg}';
	function init(){
		if(msg!=''){
			alert(msg);
		}
	}

	function del(){
		if(confirm("지우실?")){
			var rf = document.readForm;
			rf.action = "<c:url value='/board/delete.do'/>";
			rf.submit();
		}
	}

	function goUp(){
		if(confirm("바꾸실?")){
			var rf = document.readForm;
			rf.action = "<c:url value='/board/goUpdate.do'/>";
			rf.submit();
		}
	} */

</script>

</head>
<body>

	<!-- wrap -->
	<div id="wrap">

		<!-- container -->
		<div id="container">

			<!-- content -->
			<div id="content">

				<!-- title board detail -->
				<div class="title_board_detail">게시물 보기</div>
				<!-- //title board detail -->

				<!-- board_area -->
				<div class="board_area">
					<form method="post" name="readForm">
					
						<input type="hidden" name="typeSeq" value="${board.typeSeq }"/>
						<input type="hidden" name="boardSeq" value="${board.boardSeq }"/>
						
						<fieldset>
							<legend>게시물 상세 내용</legend>

							<!-- board detail table -->
							<table summary="표 내용은 게시물의 상세 내용입니다." class="board_detail_table">
								<caption>게시물 상세 내용</caption>
								<colgroup>
									<col width="%" />
									<col width="%" />
									<col width="%" />
									<col width="%" />
									<col width="%" />
									<col width="%" />
								</colgroup>
								<tbody>
									<tr>
										<th class="tright">제목</th>
										<td colspan="5" class="tleft">${board.title }</td>
									</tr>
									<tr>
										<th class="tright">작성자</th>
										<td colspan="5" class="tleft">${board.memberNick }</td>
									</tr>
									<tr>
										<th class="tright">작성일</th>
										<td>${board.createDate }</td>
										<th class="tright">조회수</th>
										<td class="tright">${board.hits }</td>
										<th class="tright">추천</th>
										<td class="tright">0</td>
									</tr>
									<tr>
										<td colspan="6" class="tleft">
											<div class="body">
												<%-- <p><c:out value="${board.content }"/></p> --%>
												${board.content}
											</div>
										</td>
									</tr>
									<c:forEach items="${files}" var="file" varStatus="vs">
										<tr>
											<th class="tcenter">첨부파일${vs.count}</th>
											<td colspan="3" style="text-align: left">${file.filename}(${file.file_size} bytes)</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
							<!-- //board detail table -->

							<!-- bottom button -->
								<div class="btn_bottom_right">
								<c:if test="${sessionScope.memberId == board.memberId }">
									<%-- <a href="<c:url value='/board/update.do?boardSeq=${board.boardSeq}&typeSeq=${board.typeSeq }'/>"><input type="button" value="수정" title="수정" /></a> --%> 
									<input type="button" id="btnGoUp" value="수정" title="수정"/>
									<%-- <a href="<c:url value='/board/delete.do?boardSeq=${board.boardSeq}&typeSeq=${board.typeSeq }'/>"><input type="button" value="삭제" title="삭제" /></a> --%> 
									<input type="button" id="btnDel" value="삭제" title="삭제"/>
								</c:if>
									<input type="button" value="목록" title="목록" onclick="javascript:window.history.back();"/>
								</div>
							</div>
							<!-- //bottom button -->

						</fieldset>
					</form>
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