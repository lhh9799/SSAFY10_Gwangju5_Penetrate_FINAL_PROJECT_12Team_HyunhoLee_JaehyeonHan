//attraction type code (AttractionInfoDto)
//DB: ssafyvue TABLE: attraction_info
export const attractionType = [
  { key: 12, value: '관광지', },
  { key: 14, value: '문화시설', },
  { key: 15, value: '축제공연행사', },
  { key: 25, value: '여행코스', },
  { key: 28, value: '레포츠', },
  { key: 32, value: '숙박', },
  { key: 38, value: '쇼핑', },
  { key: 39, value: '음식점', },
];

//DB: ssafyvue TABLE: sido
export const sidoType = [
  { key: 1, value: '서울' },
  { key: 2, value: '인천' },
  { key: 3, value: '대전' },
  { key: 4, value: '대구' },
  { key: 5, value: '광주' },
  { key: 6, value: '부산' },
  { key: 7, value: '울산' },
  { key: 8, value: '세종특별자치시' },
  { key: 31, value: '경기도' },
  { key: 32, value: '강원도' },
  { key: 33, value: '충청북도' },
  { key: 34, value: '충청남도' },
  { key: 35, value: '경상북도' },
  { key: 36, value: '경상남도' },
  { key: 37, value: '전라북도' },
  { key: 38, value: '전라남도' },
  { key: 39, value: '제주도' },
];

export const defaultMapLocation = {
  "contentId": 0,
  "contentTypeId": 12,
  "title": "카카오 스페이스닷원",
  "addr1": "제주특별자치도 제주시 첨단로 242",
  "addr2": "",
  "zipcode": "63309",
  "tel": "",
  "firstImage": "https://api.cdn.visitjeju.net/photomng/imgpath/201812/19/a1056453-2576-4cca-bf62-d2649b5c2077.JPG",
  "firstImage2": "",
  "readcount": 0,
  "sidoCode": 39,
  "gugunCode": 0,
  "latitude": 33.450701,
  "longitude": 126.570667,
  "mlevel": "0"
}