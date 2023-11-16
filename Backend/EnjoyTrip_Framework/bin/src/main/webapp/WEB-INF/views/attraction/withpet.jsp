<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%@ include file="include/head.jsp"%>
<head>
<%@ include file="include/nav.jsp"%>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<!-- 모달 입력용 공간 -->
    <div id="modalSpace"></div>
    <!-- 펫 입장 가능한 곳 리스트 시작 -->
    <div class="container-lg">
        <!-- kakao map start -->
        <div id="map" class="mt-3" style="width: 100%; height: 400px"></div>
        <!-- kakao map end -->
        <div class="row">
        <table class="table table-striped">
            <thead>
            <tr>
                <th>관광지명</th>
                <th>주소</th>
                <th width="80px">분류</th>
                <th width="110px"></th>
                <th width="75px"></th>
            </tr>
            </thead>
            <tbody id="trip-list"></tbody>
        </table>

        <!-- 자세히 보기 누르면 각종 정보보기 모달로 처리할 예정-->
        </div>
      </div>
      <div id="pageButtonSpace" class="col text-center">
    </div>
    <!-- 펫 입장 가능한 곳 리스트 끝-->
    <script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=d819f7ebec33822a522caf7fed96316a&libraries=services,clusterer,drawing"></script>
    <script src="js/key.js"></script> 
    <script src="js/withpet.js"></script>  
	<%@ include file="include/footer.jsp"%>
</body>
</html>