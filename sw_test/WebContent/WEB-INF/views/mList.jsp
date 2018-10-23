<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/jquery-ui/css/jquery-ui.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/jqgrid/css/ui.jqgrid.css" />" />
<style type="text/css">
.board_search {
	margin-bottom: 5px;
	text-align: right;
	margin-right: -7px;
}
.board_search .btn_search, #btnDel {
	height: 20px;
	line-height: 20px;
	padding: 0 10px;
	vertical-align: middle;
	border: 1px solid #e9e9e9;
	background-color: #f7f7f7;
	font-size: 12px;
	font-family: Dotum, "돋움";
	font-weight: bold;
	text-align: center;
	cursor: pointer;
}
select {
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	margin-left : 3px;
	padding: 0;
	font-size: 12px;
	height: 20px;
	line-height: 20px;
	border: 1px solid #d7d7d7;
	color: #7f7f7f;
	/* padding: 0 5px; */
	vertical-align: middle;
}
</style>
<script type="text/javascript" src="<c:url value="/resources/jquery/js/jquery-3.2.1.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/jqgrid/js/jquery.jqGrid.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/jqgrid/js/i18n/grid.locale-kr.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/jquery-ui/js/jquery-ui.js" />"></script>
<script type="text/javascript" src="<c:url value="/resources/jquery/js/jquery-migrate-1.4.1.js" />"></script>

<script type="text/javascript">

$(document).ready(function(){
	// Tab
	$( "#tabs" ).tabs();
	
	/* var myData = [{
			'CategoryName':'IT Device',
			'ProductName':'iP',
			'Country':'US',
			'Price':'2000',
			'Quantity':'10',
			},
			{'CategoryName':'IT Device',
			'ProductName':'sP',
			'Country':'KR',
			'Price':'1000',
			'Quantity':'20',
			}]; */
	
	$("#jqGrid").jqGrid({
		url: '<c:url value="/member/mList.do"/>',
		datatype: "json",
		/* datatype: "local",
		data : myData, */
		jsonReader: {id:"member_idx"},
		prmNames: {id:"member_idx"},
		colModel: [
			{ label: 'Member Idx', name: 'member_idx', width: 50, align:'center' },
			{ label: 'Member Id', name: 'member_id', width: 80 },
			{ label: 'Member Name', name: 'member_name', width: 80 },
			{ label: 'Member Nick', name: 'member_nick', width: 80 },
			// sorttype is used only if the data is loaded locally or loadonce is set to true
			{ label: 'Member Email', name: 'email', width: 80 },                   
			{ label: 'Create Date', name: 'create_date', width: 80 }                   
		],
		viewrecords: true, // show the current page, data rang and total records on the toolbar
		width: 740,
		height: 200,
		rowNum: 5,
		rowList:[5,10,15],
		caption:'회원목록',
		rownumbers: true,
		sortname: "member_idx",
		sortorder: "desc",
		scrollrows: true,
		viewrecords: true,
		pager: "#jqGridPager"
	});
	
	//navButtons
	$("#jqGrid").jqGrid('navGrid',"#jqGridPager",
		{
			edit: false,
			add: false,
			del: true,
			search: false,
			refresh: true,
			view: true
		},
	{},	//edit
	{},	//add
	{	//del
		caption: '회원정보 삭제',
		msg: '선택한 회원을 삭제 하시겠습니까?',
		width: 300,
		recreateForm: true,
		url: '<c:url value="/member/delMember.do"/>',
		beforeSubmit: function(post){
			//post : 지울 key 값
			console.log('beforeSubmit : '+post);
			var rowData = $("#jqGrid").jqGrid('getRowData',post);
			console.log(rowData);
			var loginId = '${sessionScope.memberId}';
			if(loginId==rowData.member_id){
				return [false, "본인을 삭제할 수 없습니다."];
			}
			return [true,""];
		},
		afterComplete: function(result, postdata){
			console.log('delOption - afterComplete');
			console.log(result);
			console.log(result.responseJSON);
			alert(result.responseJSON.msg);
		}
	}
	);
	
	$('#btnDel').on('click',function(){
		var rowId = $('#jqGrid').jqGrid('getGridParam','selrow');
		console.log(rowId);
		if(rowId==null){
			alert("삭제할 회원을 선택하세요.");
			return;
		}else{
			var rowData = $('#jqGrid').jqGrid('getRowData',rowId);
			console.log(rowData);
			$.ajax({
				url: '<c:url value="/member/delMember.do"/>',
				data: {member_idx:rowData.member_idx},
				success: function(result, textStatus, XMLHttpRequest){
					alert(result.msg);
					$('#jqGrid').jqGrid('setGridParam',{page: 1}).trigger("reloadGrid");
				},
				error : function(){
				}
			});
		}
		
	});
	
	$('#btnSearch').on('click', function(){
		var searchType = $('#searchType option:selected').val();
		var searchText = $('#searchText').val();
		
		$('#jqGrid').jqGrid('setGridParam',{
			postData: {searchType: searchType, searchText: searchText},
			page: 1
			}).trigger("reloadGrid");
		
		if(searchText==''){
			alert('검색어를 입력하세요');
			return;
		}
		
		$.ajax({
			url: '<c:url value="/member/mList.do"/>',
			data: postData,
			success: function(result, textStatus, XMLHttpRequest){
				alert("검색완료");
			},
			error : function(){
			}
		});
				
		var sf = document.searchForm;
		sf.action = '<c:url value="/member/mList.do"/>';
		sf.submit();
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
		<!-- content -->
		<div id="content">

			<!-- board_search -->
			<div class="board_search">
				<form name="searchForm" method="get">
					<select id="searchType" name="searchType" title="선택메뉴">
						<option value="1" selected="selected">전체</option>
						<option value="2">ID</option>
						<option value="3">Email</option>
					</select> 
					<input type="text" id="searchText" name="searchText" title="검색어 입력박스" class="input_100" /> 
					<input type="button" id="btnSearch" value="검색" title="검색버튼" class="btn_search" />
				</form>
			</div>
			
			<!-- //board_search -->

			<!-- board_area -->
			<div class="board_area board_search">
				<button id="btnDel" style="margin-bottom: 3px">삭제</button>
				<table id="jqGrid"></table>
  					<div id="jqGridPager"></div>
			</div>
			<!-- //board_area -->

		</div>
		<!-- //content -->
	</div>
</div>
</body>
</html>