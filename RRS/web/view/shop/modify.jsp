<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<div id="center">
<h1>Shop Update Page</h1>
<form enctype="multipart/form-data" action="shopupdateimpl.mc" method="post">
<br>
���� �̸� : <input type="text" name="shopname" value="${dbshop.shopname }"><br>
���� ��ȣ : <input name="shopphonenumber" value="${dbshop.shopphonenumber }"><br>
���� �ּ� : <input name="address" value="${dbshop.address }"><br>
<!-- ����<select name="locate" id="locate">
           <option value="gangnam">������</option>
           <option value="songpa">���ı�</option>
           <option value="jung">�߱�</option>
        </select><br>  --> 
���� ���� : <input name="info" value="${dbshop.info }"><br>

IMG: ${dbshop.img1 }<br>
NEWIMG: <input type="file" name="mf">
<input type="hidden" name="shopid" value="${dbshop.shopid }">
<input type="hidden" name="userid" value="${dbshop.userid }">
<input type="hidden" name="img1" value="${dbshop.img1 }">
<input type="hidden" name="img2" value="${dbshop.img2 }">
<input type="hidden" name="img3" value="${dbshop.img3 }">
<input type="submit" value="UPDATE">
</form>

</div>