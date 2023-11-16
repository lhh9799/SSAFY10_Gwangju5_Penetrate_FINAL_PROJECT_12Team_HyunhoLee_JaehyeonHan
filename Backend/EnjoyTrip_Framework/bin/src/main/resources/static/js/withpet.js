
        // 로딩하면 펫 입장 가능한 리스트를 호출
        let areaUrl =
          "https://apis.data.go.kr/B551011/KorService1/detailPetTour1?serviceKey=" +
          serviceKey +
          "&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json";
        var tripList=``;
        var placeInfo=``;
        const colCnt=10;
        let nowCnt=0;
        let positions=[];
        let nowPageNum=1;
        let markerList=[];
        fetch(areaUrl, { method: "GET" })
          .then((response) => response.json())
          .then((data) => makePetList(data));

          function nextPage(pageNum){
            nowPageNum=pageNum;
            let nextUrl =
          "https://apis.data.go.kr/B551011/KorService1/detailPetTour1?serviceKey=" +
          serviceKey +
          "&numOfRows=10&pageNo="+
          pageNum+
          "&MobileOS=ETC&MobileApp=AppTest&_type=json";
          console.log(nextUrl);
          tripList=``;
          placeInfo=``;
          nowCnt=0;
          positions=[];
          for (let index = 0; index < markerList.length; index++) {
            markerList[index].setMap(null);
            
          }
            fetch(nextUrl, { method: "GET" })
          .then((response) => response.json())
          .then((data) => makePetList(data));
          }

        function makePetList(data) {
          let areas = data.response.body.items.item;
          areas.forEach((area) => {
            

            let petUrl = "https://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey="
            +serviceKey+
            "&MobileOS=ETC&MobileApp=AppTest&_type=json&contentId="+
            area.contentid+
            "&defaultYN=Y&firstImageYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&numOfRows=1&pageNo=1";
          let petInfo = area.etcAcmpyInfo;
            fetch(petUrl, { method: "GET" })
          .then((response) => response.json())
          .then((data1) => loadPlaceDate(data1,petInfo));

          });

        }

        
        function loadPlaceDate(data1,petInfo){
            let nowPlace = data1.response.body.items.item;
            let tmp=``;
            let tmp2=``;
            let tmp3=``;
            let contentTypeWord="관광지";
            nowPlace.forEach((nowP) => {
              // 관광코드 변환
              console.log(nowP.contenttypeid);
              switch(nowP.contenttypeid){
                case '14': contentTypeWord="문화시설";
                  break;
                case '15': contentTypeWord="행사";
                  break;
                case '25': contentTypeWord="여행코스";
                  break;
                case '28': contentTypeWord="레포츠";
                  break;
                case '32': contentTypeWord="숙박";
                  break;
                case '39': contentTypeWord="음식점";
                  break;
              }


            tmp =`
              <tr onclick="moveCenter(${nowP.mapy}, ${nowP.mapx});">
                <td>${nowP.title}</td>
                <td>${nowP.addr1}${nowP.addr2}</td>
                <td>${contentTypeWord}</td>
                <td><button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#Modal${nowCnt}"">상세정보</button></td>
                <td><button id="" class="btn btn-outline-success" type="button">추가</button></td>
                </tr>
            `;
            tmp2=`
            <div class="modal" id="Modal${nowCnt}">
              <div class="modal-dialog">
                <div class="modal-content">

                  <!-- Modal Header -->
                  <div class="modal-header">
                    <h4 class="modal-title">${nowP.title}</h4>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                  </div>

                  <!-- Modal body -->
                  <div class="modal-body">
                    <div>    
                          <img class="img-fluid" src=${nowP.firstimage} >
                          <div class="bg-light p-3">
                          <h1>${nowP.title}</h1>
                          <h4 class="p-1">${nowP.addr1}</h4>
                          </div>
                          <hr>
                          <h5 class="p-1">${petInfo}</h5>
                          <hr>
                          <p class="p-1">${nowP.overview}</p>
                          <hr>
                          <p class="p-1">${nowP.homepage}</p>
                        </div>      
                  </div>

                  <!-- Modal footer -->
                  <div class="modal-footer">
                    <button type="button" class="btn btn-danger" data-bs-dismiss="modal">닫기</button>
                  </div>
                </div>
                </div>
              </div>
            `;
             
            //console.log(petInfo);
            let markerInfo = {
              title: nowP.title,
              addr: nowP.addr1+nowP.addr2,
              tel: nowP.tel,
              latlng: new kakao.maps.LatLng(nowP.mapy, nowP.mapx),
            };
            positions.push(markerInfo);


            });

             
            tmp3=`
              <button type="button" class="btn btn-warning" onclick="nextPage(${nowPageNum-1});">이전 페이지</button>
              <button type="button" class="btn btn-warning" onclick="nextPage(${nowPageNum+1});">다음 페이지</button>
              `;

            tripList+=tmp;
            placeInfo+=tmp2;
            nowCnt++;
            //console.log(nowCnt);

            if(colCnt==nowCnt){
            document.getElementById("trip-list").innerHTML = tripList;
            document.getElementById("modalSpace").innerHTML = placeInfo;
            document.getElementById("pageButtonSpace").innerHTML = tmp3;
            //console.log(positions[4]);
            displayMarker();
            }
        }
  
        // 검색 버튼을 누르면..
        // 지역, 유형, 검색어 얻기.
        // 위 데이터를 가지고 공공데이터에 요청.
        // 받은 데이터를 이용하여 화면 구성.
        // document.getElementById("btn-search").addEventListener("click", () => {
        //   let baseUrl = `https://apis.data.go.kr/B551011/KorService1/`;
        //   // let searchUrl = `https://apis.data.go.kr/B551011/KorService1/searchKeyword1?serviceKey=${serviceKey}&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A`;
        //   // let searchUrl = `https://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey=${serviceKey}&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A`;
  
        //   let queryString = `serviceKey=${serviceKey}&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A`;
        //   let areaCode = document.getElementById("search-area").value;
        //   let contentTypeId = document.getElementById("search-content-id").value;
        //   let keyword = document.getElementById("search-keyword").value;
  
        //   if (parseInt(areaCode)) queryString += `&areaCode=${areaCode}`;
        //   if (parseInt(contentTypeId)) queryString += `&contentTypeId=${contentTypeId}`;
        //   // if (!keyword) {
        //   //   alert("검색어 입력 필수!!!");
        //   //   return;
        //   // } else searchUrl += `&keyword=${keyword}`;
        //   let service = ``;
        //   if (keyword) {
        //     service = `searchKeyword1`;
        //     queryString += `&keyword=${keyword}`;
        //   } else {
        //     service = `areaBasedList1`;
        //   }
        //   let searchUrl = baseUrl + service + "?" + queryString;
  
        //   fetch(searchUrl)
        //     .then((response) => response.json())
        //     .then((data) => makeList(data));
        // });
  
        // var positions; // marker 배열.
        // function makeList(data) {
        //   //console.log(data);
        //   document.querySelector("table").setAttribute("style", "display: ;");
        //   let trips = data.response.body.items.item;
        //   let tripList = ``;
        //   positions = [];
          

        //   trips.forEach((area) => {
        //     let findPetID = area.contentid;
        //     tripList += `
        //       <tr onclick="moveCenter(${area.mapy}, ${area.mapx});">
        //         <td><img src="${area.firstimage}" width="100px"></td>
        //         <td>${area.title}</td>
        //         <td>${area.addr1} ${area.addr2}</td>
        //         <td>${area.contentid}</td>
        //         <td>${area.tel}</td>
        //       </tr>
        //     `;
  
        //     let markerInfo = {
        //       title: area.title,
        //       addr: area.addr1+area.addr2,
        //       tel: area.tel,
        //       latlng: new kakao.maps.LatLng(area.mapy, area.mapx),
        //     };
        //     positions.push(markerInfo);
        //   });
        //   document.getElementById("trip-list").innerHTML = tripList;
        //   displayMarker();
        // }
  
        // 카카오지도
        var mapContainer = document.getElementById("map"), // 지도를 표시할 div
          mapOption = {
            center: new kakao.maps.LatLng(37.500613, 127.036431), // 지도의 중심좌표
            level: 5, // 지도의 확대 레벨
          };
  
        // 지도를 표시할 div와  지도 옵션으로  지도를 생성합니다
        var map = new kakao.maps.Map(mapContainer, mapOption);
  
        function displayMarker() {
          // 마커 이미지의 이미지 주소입니다
          var imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png";
  
          for (var i = 0; i < positions.length; i++) {
            // 마커 이미지의 이미지 크기 입니다
            var imageSize = new kakao.maps.Size(24, 35);
  
            // 마커 이미지를 생성합니다
            var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
  
            // 마커를 생성합니다
            var marker = new kakao.maps.Marker({
              map: map, // 마커를 표시할 지도
              position: positions[i].latlng, // 마커를 표시할 위치
              title: positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
              image: markerImage, // 마커 이미지
            });



            markerList[i]=marker;
            var infowindow = new kakao.maps.InfoWindow({
                content: positions[i].title // 인포윈도우에 표시할 내용
            });
            kakao.maps.event.addListener(marker, 'mouseover', makeOverListener(map, marker, infowindow));
            kakao.maps.event.addListener(marker, 'mouseout', makeOutListener(infowindow));
          }

          
  
          // 첫번째 검색 정보를 이용하여 지도 중심을 이동 시킵니다
          map.setCenter(positions[0].latlng);
        }

        // 인포윈도우를 표시하는 클로저를 만드는 함수입니다 
        function makeOverListener(map, marker, infowindow) {
            return function() {
                infowindow.open(map, marker);
            };
        }

        // 인포윈도우를 닫는 클로저를 만드는 함수입니다 
        function makeOutListener(infowindow) {
            return function() {
                infowindow.close();
            };
        }            

  
        function moveCenter(lat, lng) {
          map.setCenter(new kakao.maps.LatLng(lat, lng));
        }