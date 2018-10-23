<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/jquery-ui/css/jquery-ui.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/jqgrid/css/ui.jqgrid.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/common.css" />" />

<script type="text/javascript" src="<c:url value="/resources/jquery/js/jquery-3.2.1.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/jqgrid/js/jquery.jqGrid.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/jqgrid/js/i18n/grid.locale-kr.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/jquery-ui/js/jquery-ui.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/jquery/js/jquery-migrate-1.4.1.js" />"></script>

<script type="text/javascript">

$(document).ready(function(){
	// Tab
	$( "#tabs" ).tabs();
	
	$("#jqGrid").jqGrid({
		url: 'data.json',
		datatype: "json",
		 colModel: [
			{ label: 'Category Name', name: 'CategoryName', width: 75 },
			{ label: 'Product Name', name: 'ProductName', width: 90 },
			{ label: 'Country', name: 'Country', width: 100 },
			{ label: 'Price', name: 'Price', width: 80, sorttype: 'integer' },
			// sorttype is used only if the data is loaded locally or loadonce is set to true
			{ label: 'Quantity', name: 'Quantity', width: 80, sorttype: 'number' }                   
		],
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: 700,
		height: 200,
		rowNum: 30,
		pager: "#jqGridPager"
	});
	
});
</script>
<title></title>
</head>
<body>

<div id="tabs">
	<ul>
		<li><a href="#tabs-1">회원 목록</a></li>
	</ul>
	<div id="tabs-1">
		<!-- wrap -->
		<div id="wrap">
	
			<!-- container -->
			<div id="container">
	
				<!-- content -->
				<div id="content">
	
					<!-- board_search -->
					<div class="board_search">
						<form name="searchForm" method="get">
							<select id="searchType" name="searchType" title="선택메뉴">
								<option value="1" selected="selected">전체</option>
								<option value="2">제목</option>
								<option value="3">내용</option>
							</select> 
							<input type="text" id="searchText" name="searchText" title="검색어 입력박스" class="input_100" /> 
							<input type="button" id="btnSearch" value="검색" title="검색버튼" class="btn_search" />
						</form>
					</div>
					
					<!-- //board_search -->
	
					<!-- board_area -->
					<div class="board_area">
						<table id="jqGrid"></table>
 						<div id="jqGridPager"></div>
					</div>
					<!-- //board_area -->
	
				</div>
				<!-- //content -->
	
			</div>
			<!-- //container -->
	
		</div>
		<!-- //wrap -->
		</div>
	</div>
	
	</body>
	</html>