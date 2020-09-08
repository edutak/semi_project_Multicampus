<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<style>
section{
	width:95%;
	height:auto;
	padding:10px 0;
	background: #cbcbcb;
}
section> #first{
	width:100%;
	height:300px;
	background:white;
}
section> #second{
	width:100%;
	height:500px;
	overflow:auto;
	background:white;
}
section> #third{
	width:100%;
	height:300px;
	overflow:auto;
	background:white;
}

.gap1{height:15px; clear:both; overflow:hidden; display:block;}
</style>

<script>
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
	}
	

});


</script>

<div id="center">
<h1>����� ���� ����</h1>
<section>
			<div id="first">
				<div class="geo">
				<p>����������</p>
				</div>
			</div>	
			<p class="gap1"></p>
			
			<div id="second">
				<div class="list">
				<p>��Ʈ �� ����Ʈ</p>
				</div>
			</div>		
			<p class="gap1"></p>
			
			<div id="third">
				<div class="comment">
				<p>���</p>
				</div>
			</div>
		</section>
</div>