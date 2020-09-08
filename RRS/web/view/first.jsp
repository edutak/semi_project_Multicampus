<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<!-- fmt�� ����ϱ����� �±� ���̺귯�� -->
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script type="text/javascript"
	src="//dapi.kakao.com/v2/maps/sdk.js?appkey=2348a50ce2bdcf2f6cf00ff8ad5e426d"></script>
<style>
#map {
	width: 800px;
	height: 400px;
	border: 2px solid blue;
}

section {
	width: 95%;
	height: auto;
	padding: 10px 0;
	background: #cbcbcb;
}

section>#first {
	width: 100%;
	height: 300px;
	background: white;
}

section>#second {
	width: 100%;
	height: 500px;
	overflow: auto;
	background: white;
}
      /*��� ����Ʈ CSS START*/
#commentdetails {
   width:95%;
   hight:auto;
   background:white;
   border: 1px solid #ccc;
   box-sizing: border-box;
   margin: 0 auto;
   text-align:center;
}

#commentdetails > h2{
   float : left;
}

#commentdetails > #comment_table{
   width:70%;
   border-collapse:collapse;
   border-right:none;
   border-left:none;
   border-bottom:none;
   margin:0 auto;
   font-size:20px;
   overflow:auto;
}

.no, .wr, .pt, .we {
   width:10%;
   height:40px;
}
.con {
   width:60%;
   height:40px;
}
      /*��� ����Ʈ CSS END*/
      
section>#third {
	width: 100%;
	height: 300px;
	overflow: auto;
	background: white;
}

.gap1 {
	height: 15px;
	clear: both;
	overflow: hidden;
	display: block;
}
</style>

<script>
/* ----------------------------------------------------------------------------------------- */
/*                                     �ּ� �˻� �ý���                                                                               */
/* ----------------------------------------------------------------------------------------- */
var latiA = new Array();
var longiA = new Array();
var latiK = new Array();
var longiK = new Array();
var latiS = new Array();
var longiS = new Array();
var sw = 0;
var arrs = null; // �� json
var arrs2 = null; // ��õ json
var arrsA = null; // ��������Ʈ json
var arrsK = null; // Ű���帮��Ʈ json

	
//�˻������� ǥ�� �� ����ǥ�� �����ϰ� �����浵 ���� ��ȯ(�ּҷ� �˻�)
function display(data) {
	
	$(data.documents).each(
			function(index, add) {
				var result = '';
				result += '<h4>' + "�ּ� : " + add.address_name + '<br>' + "x : " + add.x + '<br>' 
				          + "y : "+ add.y + '</h4>';
				$('#result').append(result);
				
				$('button').eq(index).append(add.address_name+' '+add.x+' '+add.y);
				
				var latit = parseFloat((parseFloat(add.x)).toFixed(6));
				var longit = parseFloat((parseFloat(add.y)).toFixed(6));
				
				latiA[index] = latit;
				longiA[index] = longit;
				
			}
	);
	//������� ������ ����ġ�� 1���ϰ� Ű����� ǥ��
	 if(typeof latiA[0] == "undefined"){
		 sw = 1;
	 }
};
//�˻������� ǥ�� �� ����ǥ�� �����ϰ� �����浵 ���� ��ȯ(Ű����� �˻�)
function display2(data) {
 	if(sw==1){
		$(data.documents).each(
				function(index, add) {
					var result = '';
					result += '<h4>' + "Ű���� : " + add.place_name + '<br>' + "x : " + add.x + '<br>' 
					          + "y : "+ add.y + '</h4>';
					$('#result').append(result);
							
					var latit = parseFloat((parseFloat(add.x)).toFixed(6));
					var longit = parseFloat((parseFloat(add.y)).toFixed(6));
					
					latiK[index] = latit;
					longiK[index] = longit;
	
				}
		);
 	}
};
// ��õ���� ǥ��
function display3(data) {
		$(data.documents).each(
				function(index, add) {
					var result = '';
					result += '<h4>' + "Ű���� : " + add.place_name + '<br>' + "x : " + add.x + '<br>' 
					          + "y : "+ add.y + '</h4>';
					$('#result').append(result);
							
					var latit = parseFloat((parseFloat(add.x)).toFixed(6));
					var longit = parseFloat((parseFloat(add.y)).toFixed(6));
					
					latiK[index] = latit;
					longiK[index] = longit;
	
				}
		);
};
//�ּ� �� Ű���� �˻���  ������ ��������
function getData() {
//�ּҰ˻�
		$.ajax({
					method : 'GET',
					url : 'https://dapi.kakao.com/v2/local/search/address.json',
					async : false,
					headers : {
						'Authorization' : 'KakaoAK 8bb4664642b1184f894533fe7edb0245'
					},
					data : {
						'query' : '${address}',
						'page' : 1,
						'AddressSize' : 3
					},
					success : function(data) {
						display(data);
						arrsA = data;
					},
					error : function() {
						alert('error');
					}
				});
		
//Ű����˻�		
		$.ajax({
					method : 'GET',
					url : 'https://dapi.kakao.com/v2/local/search/keyword.json',
					async : false,
					headers : {
						'Authorization' : 'KakaoAK 8bb4664642b1184f894533fe7edb0245'
					},
					data : {
						'query' : '${address}',
						'page' : 1,
						'size' : 3
					},
					success : function(data) {
						display2(data);
						arrsK = data;
					},
					error : function() {
						alert('error2');
					}
			});
};
// ���� ������ �޾ƿ��� �׽�Ʈ �Լ�
function getshopdata() {
	$.ajax({
		url : 'getshopdata.mc',
		async : false,
		success : function(result) {
			arrs = result;
			
			for(i=0; i<result.length; i++){
				var shopname = result[i].shopname;
				var shopid = result[i].shopid;
				var lat = result[i].lat;
				var lon = result[i].lon;
				var shopphonenumber = result[i].shopphonenumber;
				var resulthtml = '';
				resulthtml += shopname +' ' + shopid + ' ' + lat + ' ' + lon + ' ' + shopphonenumber + '<br>';
							
				var latiS = parseFloat((parseFloat(result[i].lat)).toFixed(6));
				var longiS = parseFloat((parseFloat(result[i].lon)).toFixed(6));
				$('#result2').append(i+' : '+resulthtml);
			}    
		},
		error : function() {
			alert('Error3');
		}
	});
	return arrs;
};
//���� ��õ ������ �޾ƿ��� �׽�Ʈ �Լ�
function getshopdata2() {
	$.ajax({
		url : 'getshopdata2.mc',
		async : false,
		success : function(result) {
			arrs2 = result;
			for(i=0; i<result.length; i++){
				var shopid = result[i].shopid;
				var up = result[i].up;
				var down = result[i].down;
				var resulthtml = '';
				resulthtml += shopid + ' ' + up + ' ' + down + '<br>';
				$('#result2').append(resulthtml);
			}    	
		},
		error : function() {
			alert('Error4');
		}
	});
	return arrs2;
};
/* ----------------------------------------------------------------------------------------- */
/*                                     �˻� �� ���� �ý���                                                                               */
/* ----------------------------------------------------------------------------------------- */
var map = null;
//���� ǥ�� ����
function mapDislapy() {
	var container = document.getElementById('map');
	var options = {
		center : new kakao.maps.LatLng(37.549530, 126.989045),
		level : 9
	};
	map = new kakao.maps.Map(container, options);
};
//���� ����Ʈ ������ ǥ��
function setMarkers(arrs,arrs2) {
	$(arrs).each(
					function(index, shop) {
						var marker = new kakao.maps.Marker({
							title : shop.shopname,
							position : new kakao.maps.LatLng(shop.lat,
									shop.lon)
						});
						marker.setMap(map);
						var iwContent = '<div style="padding:5px; width:100px; font-size:small;"><a href="shop_detail.mc?shopid='+shop.shopid+'" style="color:blue; font-size:large;" target="_blank">' + shop.shopname + '</a>'+ ' ��õ��(' + arrs2[index].up + ')' + '<br>'+shop.shopphonenumber+'</div>' 
										,iwRemoveable = true;
						var infowindow = new kakao.maps.InfoWindow({
							content : iwContent,
							removable : iwRemoveable
						});
						kakao.maps.event.addListener(marker, 'click',
								function() {
									infowindow.open(map, marker);
								});
				});
};
//���� ��ġ �̵�
function panTo(lat, lng) {
	var moveLatLon = new kakao.maps.LatLng(lng, lat);
	map.panTo(moveLatLon);
	setMarkers(arrs,arrs2);
}

/* ----------------------------------------------------------------------------------------- */
/*                                    shop ��Ͻ� �ּҷ� ���� �浵 ����                                                                              */
/* ----------------------------------------------------------------------------------------- */


function setshopxy() {

	var shopxy = {};
	
			$.ajax({
				method : 'GET',
				url : 'https://dapi.kakao.com/v2/local/search/address.json',
				async : false,
				headers : {
					'Authorization' : 'KakaoAK 8bb4664642b1184f894533fe7edb0245'
				},
				data : {
					'query' : '${registshop.address}',
					'page' : 1,
					'AddressSize' : 3
				},
				success : function(data) {
					shopxy = {x:parseFloat((parseFloat(data.documents[0].x)).toFixed(6)) , y:parseFloat((parseFloat(data.documents[0].y)).toFixed(6))};
				},
				error : function() {
					
				}
			});

			
			$.ajax({
				url: 'shopxyupdate.mc',
	 		    async : false,
				type: "POST",
				data: shopxy,				
				success : function() {
					
				},
				error : function() {

				}
			});

			
}


//���� �����߸� ��Ŭ���� ������ (������)
$(document).ready(function() {
	if(${registshop == null}){
		
	}else{
		setshopxy();
		alert("OK");
	}
	arrs = getshopdata(); // DB������
	arrs2 = getshopdata2(); // DB����õ���� 
	mapDislapy();
	setMarkers(arrs,arrs2);
});


</script>

<div id="center">
	<h1>����� ���� ����</h1>
	<div id="map"></div>
	<section>
		<p class="gap1"></p>

		<div id="second">
			<div class="list">
				<p>��Ʈ �� ����Ʈ</p>
			</div>
		</div>

		<p class="gap1"></p>


		<div id="third">
			<!-- ��۸���Ʈ div -->
			<div id="commentdetails">
				<h2>���</h2>
				<table id="comment_table" border="1">
					<tr>
						<th class="no">��ȣ</th>
						<th class="wr">�ۼ���</th>
						<th class="con">����</th>
						<th class="pt">�̹���</th>
						<th class="we">��¥</th>
					</tr>
					<!-- forEach ���� Ȱ���Ͽ� comment list �����ֱ� -->
					<c:forEach var="c" items="${shop_comment }">
						<tr>
							<!-- ��Ʈ�ѷ����� �Ѱ��� list �� ��ü�� ���� ����� �� �������� -->
							<td>${c.commentid}</td>
							<td>${c.userid}</td>
							<td>${c.commentcontents}</td>
							<td><img src="img/comment/${c.comment_img }"
								style="width: 100px; height: 100px;"></td>
							<td><fmt:formatDate value="${c.commentdate}"
									pattern="yyyy-MM-dd HH:mm:ss" /></td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
	</section>
</div>