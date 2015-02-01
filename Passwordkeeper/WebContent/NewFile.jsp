<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<style type="text/css">
.mainMenu {
	height: 50px;
	width: 100%;
	/*  background-color: #A4A4A4; 
	border-width: 1px;
	border-style: solid;
	border-color: black; */
	margin-bottom: 2px;
	font-size: 30px;
	text-align: center;;
}

.content {
	width: 20%;
	height: 100%;
	display: inline-block;
	float: left;
	background-color: green;
	display: inline-block;
}

.operation {
	width: 20%;
	display: inline-block;
	float: right;
	background-color: orange;
	height: 100%;
}

#dataContainer {
	width: 100%;
}

.data {
	width: 100%;
	background-color: green;
	height: 50px;
	font-size: 30px;
}

.childData {
	width: 20%;
	float: left;
	height: 50px; display : inline-block;
	white-space: nowrap;
	display: inline-block
}

.childData.fltRight {
	float: right;
}

button {
	width: 100%;
	height: 100%;
}
</style>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		function populateScreen(dir) {
			$('#dataContainer').empty();
			$('.curDir.mainMenu').empty();
			$('.parentDir.mainMenu').empty();
			$.getJSON("rest/getResource/path/"+dir, function(data) {
				var a = data;
				var html="";
				for ( var prop in a) {
					var p= a[prop];
					var normalData=(prop!= ".") && (prop!= "..");
					html="";
					if (normalData ){
						html=html.concat('<div class="data">'+
						'<div class="childData fileName">'+ prop+'</div>');
					}
					if (normalData && (p.isFile == true)){
						html=html.concat(
								'<div class="childData fltRight download"><button> Download</button></div>'+
								'<div class="childData fltRight"><button> Edit</button> </div>'
						);
					}
					if (normalData ){
						html=html.concat('<div class="childData fltRight view"><button> View</button> </div>'+'</div>');
					}
					$('#dataContainer').append(html);
					
				}
				var cPath=a["."];
				if (cPath==""){
					cPath="abc";
				}else{
					cPath=cPath.substring(0,cPath.length-1);
					cPath=cPath.replace("/","-");
				}
				html="";
				html=html.concat('<div class="content">.</div>');
				html=html.concat('<div class="operation">View</div>');
				html=html.concat('<div class="relativePath" style="display: none;">'+cPath+'</div>');
				
				
				$('.curDir.mainMenu').append(html);
				if (a[".."] != "Not") {
					var pPath=a[".."];
					if (pPath==""){
						pPath="abc";
					}else{
						pPath=pPath.substring(0,pPath.length-1);
						pPath=pPath.replace("/","-");
					}
					html="";
					html=html.concat('<div class="content">..</div>');
					html=html.concat('<div class="operation">View</div>');
					html=html.concat('<div class="relativePath" style="display: none;">'+pPath+'</div>');
					$('.parentDir.mainMenu').append(html);
					$('.parentDir.mainMenu').css({display:'inherit'});
				} else {
					$('.parentDir.mainMenu').css({display:'none'});
				}
			});
		}
		
		function download(fileName){
			$.ajax(
					{
						url : "rest/getResource/"+fileName, 
						success :function (){
							
						} 
					}
			);
		}
			
		
		populateScreen('abc');
		//$download("Bank-en.jpg");
		//$.download('rest/getResource/Bank-en.jpg','','GET' );
		
		$(document).delegate(".childData button", "click", function(){
			var t= $(this).closest('.data').find('.fileName').text();
			if ($(this).closest(".childData").hasClass("view")){
				t=$('.curDir .relativePath').text().concat("-").concat(t);
				if (t.indexOf("abc-") == 0){
					t= $(this).closest('.data').find('.fileName').text();	
				}
				populateScreen(t);
			}else if ($(this).closest(".childData").hasClass("download")){
				t=$('.curDir .relativePath').text().concat("-").concat(t);
				if (t.indexOf("abc-") == 0){
					t= $(this).closest('.data').find('.fileName').text();	
				}
				$("#frame").attr("src", "rest/getResource/"+t);
			}
			
			//alert('asdasdas');
			//$(this).closest('.data').find('.fileName').text();
		});
		$('button').click();
// 		$("#button").click(function () { 
// 		      $("#frame").attr("src", "rest/getResource/Bank-en.jpg");
// 		});
	});
	
	
</script>

</head>
<body>
	<div class="curDir mainMenu"></div>
	<div class="parentDir mainMenu"></div>
	<div id="dataContainer"></div>
	 <div id="mydiv" style="display: none;"><iframe id="frame" src="" width="100%" height="300">
   </iframe></div>

</body>
</html>