<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- tag library 선언 : c tag --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html lang="kr">
<head>
<meta charset="UTF-8">
<title>로그인 & 회원가입</title>
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/login.css" />" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
<script type="text/javascript">

	$(document).ready(function() {
		var panelOne = $('.form-panel.two').height(), 
			panelTwo = $('.form-panel.two')[0].scrollHeight;
	
		$('.form-panel.two').not('.form-panel.two.active').on('click', function(e) {
			e.preventDefault();
	
		    $('.form-toggle').addClass('visible');
		    $('.form-panel.one').addClass('hidden');
		    $('.form-panel.two').addClass('active');
		    $('.form').animate({
		      'height': panelTwo
		    }, 200);
		});
	
		$('.form-toggle').on('click', function(e) {
		    e.preventDefault();
		    $(this).removeClass('visible');
		    $('.form-panel.one').removeClass('hidden');
		    $('.form-panel.two').removeClass('active');
		    $('.form').animate({
		      'height': panelOne
		    }, 200);
		});
		
		$('#btnRegister').on('click', function(){
			var jf = document.joinForm;
			/* var ui = jf.ui.value;
			if(ui.length==0){
				alert("ID를 입력해주세요.");
				jf.ui.focus();
				return;
			}
			if(ui.length<7||ui.length>12){
				alert("ID는 7자이상 12자이하만 가능합니다.");
				jf.ui.focus();
				return;
			}
			var up = jf.up.value;
			if(up.length==0){
				alert("PW를 입력해주세요.");
				jf.up.focus();
				return;
			}
			var una = jf.una.value;
			if(una.length==0){
				alert("NAME를 입력해주세요.");
				jf.una.focus();
				return;
			}
			var uni = jf.uni.value;
			if(uni.length==0){
				alert("NickName을 입력해주세요.");
				jf.uni.focus();
				return;
			}
			var uni = jf.uni.value;
			if(uni.length>24){
				alert("Nickname는 한글 12자, 영문,숫자 24자이하만 가능합니다.");
				jf.uni.focus();
				return;
			}
			var em = jf.em.value;
			if(em.length==0){
				alert("email를 입력해주세요.");
				jf.em.focus();
				return;
			}
			var bd = jf.bd.value;
			if(bd.length==0){
				alert("birth-date를 입력해주세요.");
				jf.bd.focus();
				return;
			}
			if(bd.charAt(4)!='-'||bd.charAt(7)!='-'){
				alert("올바른 날짜형태가 아닙니다.");
				jf.bd.focus();
				return;
			} */
			
			// 회원가입 요청 전 비동기 통신으로 ID 중복검사
			$.ajax({
				url : '<c:url value="/member/checkId.do"/>',
				//url에 보낼 데이터(파라미터){맵}
				data : { ui : $('#ui').val() },
				//통신이 성공하면!
				success : function(result, textStatus, XMLHttpRequest){
					//alert('result arrived');
					if(result==0){
						jf.action = "/s06/member/join.do";
						jf.method = "POST";
						jf.submit();
					}else{
						alert("중복된 ID 입니다.")
					}
				},
				//success 끝
				error : function(e){
					alert(e)
				}
				//error 끝
			});
			//alert('비동기 통신 호출함.');
		});
	});
	
	/* function doJoin(){
		var jf = document.joinForm;
		var ui = jf.ui.value;
		if(ui.length==0){
			alert("ID를 입력해주세요.");
			jf.ui.focus();
			return;
		}
		if(ui.length<7||ui.length>12){
			alert("ID는 7자이상 12자이하만 가능합니다.");
			jf.ui.focus();
			return;
		}
		var up = jf.up.value;
		if(up.length==0){
			alert("PW를 입력해주세요.");
			jf.up.focus();
			return;
		}
		var una = jf.una.value;
		if(una.length==0){
			alert("NAME를 입력해주세요.");
			jf.una.focus();
			return;
		}
		var uni = jf.uni.value;
		if(uni.length==0){
			alert("NickName을 입력해주세요.");
			jf.uni.focus();
			return;
		}
		var uni = jf.uni.value;
		if(uni.length>24){
			alert("Nickname는 한글 12자, 영문,숫자 24자이하만 가능합니다.");
			jf.uni.focus();
			return;
		}
		var em = jf.em.value;
		if(em.length==0){
			alert("email를 입력해주세요.");
			jf.em.focus();
			return;
		}
		var bd = jf.bd.value;
		if(bd.length==0){
			alert("birth-date를 입력해주세요.");
			jf.bd.focus();
			return;
		}
		if(bd.charAt(4)!='-'||bd.charAt(7)!='-'){
			alert("올바른 날짜형태가 아닙니다.");
			jf.bd.focus();
			return;
		}
		jf.action = "/s06/member/join.do";
		jf.method = "POST";
		jf.submit();
	} */
</script>
</head>
<body>
	<!-- 디자인 출처 : http://www.blueb.co.kr/?c=2/34&where=subject%7Ctag&keyword=%EB%A1%9C%EA%B7%B8%EC%9D%B8&uid=4050 -->
	<!-- Form-->
	<div class="form">
		<div class="form-toggle"></div>
		<div class="form-panel one">
			<div class="form-header">
				<h1>Account Login</h1>
				<c:if test="${msg!=null }"><font color="red"><b>${msg }</b></font>
				</c:if>
			</div>
			<!-- 로그인 -->
			<div class="form-content">
				<form action="/s06/member/login.do" method="post">
					<div class="form-group">
						<label for="username">User Id</label> 
						<input type="text" id="" name="ui" required="required" />
					</div>
					<div class="form-group">
						<label for="password">Password</label> 
						<input type="password" id="" name="up" required="required" />
					</div>
					<div class="form-group">
						<label class="form-remember"> 
							<input type="checkbox" />Remember Me
						</label>
						<a href="#" class="form-recovery">Forgot Password?</a>
					</div>
					<div class="form-group">
						<button type="submit">Log In</button>
					</div>
					<%-- <div class="form-group">
						<button type="button" onclick="<c:url value='/home.do' />">Home</button>
					</div> --%>
				</form>
			</div>
		</div>
		
		<!-- 회원가입 -->
		<div class="form-panel two">
			<div class="form-header">
				<h1>Register Account</h1>
			</div>
			<div class="form-content">
				<form name="joinForm">
					<div class="form-group">
						<label for="username">User Id</label> 
						<input type="text" id="ui" name="ui" required="required" />
					</div>
					<div class="form-group">
						<label for="password">Password</label> 
						<input type="password" id="" name="up" required="required" />
					</div>
					<div class="form-group">
						<label for="name">Name</label> 
						<input type="text" id="" name="una" required="required" />
					</div>
					<div class="form-group">
						<label for="nick">nick</label> 
						<input type="text" id="" name="uni" required="required" />
					</div>
					<div class="form-group">
						<label for="email">e-mail</label> 
						<input type="text" id="" name="em" required="required" />
					</div>
					<div class="form-group">
						<label for="birth_date">birth(yyyy-mm-dd)</label> 
						<input type="text" id="" name="bd" required="required" />
					</div>
					<div class="form-group">
						<button type="button" id="btnRegister">Register</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>