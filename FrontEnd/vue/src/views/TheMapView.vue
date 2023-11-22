<script setup>
import { ref, onMounted, watch } from "vue";
import { listAttraction, getSidoFromSidoTable, getgugunDtoFromSidoTable } from "@/api/map";

import AttractionKakaoMap from "@/components/common/AttractionKakaoMap.vue";
import TheItineraryView from "@/components/itinerary/TheItineraryView.vue";
import VSelect from "@/components/common/VSelect.vue";

import { attractionType, sidoType, defaultMapLocation } from "@/util/attraction-type";

// const serviceKey = import.meta.env.VITE_OPEN_API_SERVICE_KEY;
const { VITE_OPEN_API_SERVICE_KEY } = import.meta.env;

const sidoList = ref([]);
const gugunList = ref([{ text: "구군선택", value: "" }]);
var entireAttractionInfoList = ref([]);             //VSelect에서 선택한 지역의 전체 관광지 정보
var attractionInfoList = ref([]);                   //관광지 유형 조건에 맞는 관광지 정보
const selectAttraction = ref({});                   //테이블에서 선택한 관광지 1개의 정보
const attractionTypeMap = new Map();                //관광지 유형 코드로 문자열을 얻어오기 위한 Map
const sidoTypeMap = new Map();                      //시/도 코드로 해당 지역 문자열을 얻어오기 위한 Map
const mapSelectedAttractionType = ref([]);          //(테이블의 행) checkbox의 선택된 관광지 유형들을 저장하는 배열 (예: { key: 12, value: '관광지', }) -> 지도 출력 #@/util/attraction-type.js의 sampleSelectedItineraries)
const selectedItineraries = ref([]);                //(여행 일정) checkbox의 선택된 관광지 유형들을 저장하는 배열 (예: { key: 12, value: '관광지', }) -> 사이드바에 출력 #@/util/attraction-type.js의 sampleSelectedItineraries)

const param = ref({
    // serviceKey: VITE_OPEN_API_SERVICE_KEY,
    pageNo: 1,
    numOfRows: 20,
    sidoCode: 0,
    gugunCode: 0,
});

onMounted(() => {
    getSidoList();
    initDataMap();
});

//Map 자료구조에 값 대입
const initDataMap = () => {
    //attractionTypeMap라는 Map 자료구조에 관광지 유형 쌍 대입 (예: { key: 12, value: '관광지', })
    attractionType.forEach((item) => {
        attractionTypeMap.set(item.key, item.value);
    });

    //시/도 코드와 해당 지역 문자열 쌍 대입 (예: { key: 1, value: '서울' })
    sidoType.forEach((item) => {
        sidoTypeMap.set(item.key, item.value);
    })
};

const getSidoList = () => {
    getSidoFromSidoTable(
        ({ data }) => {
            let options = [];
            options.push({ text: "시도선택", value: "" });
            data.forEach((sido) => {
            options.push({ text: sido.sidoName, value: sido.sidoCode });
        });
        sidoList.value = options;
        },
        (err) => {
            console.log(err);
        }
    );
};

const onChangeSido = (val) => {
    param.value.sidoCode = val;

    getgugunDtoFromSidoTable(
        { sido: val },
        ({ data }) => {
            let options = [];
            options.push({ text: "구군선택", value: "" });
            data.forEach((gugun) => {
                options.push({ text: gugun.gugunName, value: gugun.gugunCode });
            });
            gugunList.value = options;
        },
        (err) => {
            console.log(err);
        }
    );
};

const onChangeGugun = (val) => {
    //관광지 정보 쿼리를 위한 객체에 선택한 구/군 코드 저장
    param.value.gugunCode = val;

    //지역 별 1회 실행 (선택한 지역의 전체 관광지 조회)
    getAttractionInfoList();

    //조건에 맞는 관광지 로드
    // queryAttractions();
    selectAllAttractionType();
};

//선택한 지역의 전체 관광지 조회
const getAttractionInfoList = () => {
    listAttraction(
        param.value,
        ({ data }) => {
            entireAttractionInfoList.value = data;
            attractionInfoList.value = data;
        },
        (err) => {
            console.log(err);
        }
    );
};

const viewAttractionInfoList = (attraction) => {
    selectAttraction.value = attraction;
};

//선택한 지역의 관광지 중 선택한 체크박스에 부합하는 관광지 리스트 출력
//TODO: 체크박스에 부합하는 관광지가 없는 경우 지도 출력되지 않는 문제 있음 (null 체크) -> 지도 원래 위치 넣기!
const queryAttractions = () => {
    //관광지 유형 모두 체크 해제된 상태 -> 선택된 지역의 모든 관광지 출력
    if (attractionInfoList.value.length == 0) {
        // attractionInfoList.value = entireAttractionInfoList;
        attractionInfoList.value = defaultMapLocation;

        return;
    }

    var filteredAttractionInfoList = [];

    entireAttractionInfoList.value.forEach((item) => {
        mapSelectedAttractionType.value.forEach((checked) => {
            if (item.contentTypeId == checked) {
                filteredAttractionInfoList.push(item);
            }
        });
    });

    attractionInfoList.value = filteredAttractionInfoList;
};

const onChangeCheckbox = () => {
    //전체 관광지 리스트가 비어있거나 조건에 맞는 관광지(해당 지역의 관광지 유형 (예: 서울 강서구 여행코스))가 없는 경우 알림창 출력 후 리턴
    if(entireAttractionInfoList.value.length == 0 || attractionInfoList.value.length == 0) {
        if(param.value.sidoCode == 0) {
            alert('시/도를 선택해주시기 바랍니다.');
            mapSelectedAttractionType.value = [];

            return;
        }

        else if(param.value.gugunCode == 0) {
            alert('구/군을 선택해주시기 바랍니다.');
            mapSelectedAttractionType.value = [];

            return;
        }

        attractionInfoList.value = defaultMapLocation;
        alert('해당 지역에 관광지 정보가 없습니다.');

        return;
    }

    if (mapSelectedAttractionType.value.length == 0) {
        // attractionInfoList.value = JSON.parse(JSON.stringify(entireAttractionInfoList.value));
        attractionInfoList.value = defaultMapLocation;

        return;
    }

    //조건에 맞는 관광지 출력
    queryAttractions();
};

const selectAllAttractionType = () => {
    if(param.value.sidoCode == 0) {
        alert('시/도를 선택해주시기 바랍니다.');

        return;

    } else if(param.value.gugunCode == 0) {
        alert('구/군을 선택해주시기 바랍니다.');

        return;
    }

    //checkbox 초기화 - 모두 선택
    mapSelectedAttractionType.value = [];
    attractionType.forEach((item) => {
        mapSelectedAttractionType.value.push(item.key);
    });

    //조건에 맞는 관광지 로드 (전체)
    attractionInfoList.value = JSON.parse(JSON.stringify(entireAttractionInfoList.value));
};

const deselectAllAttractionType = () => {
    //checkbox 초기화 - 모두 선택 해제
    mapSelectedAttractionType.value = [];

    //조건에 맞는 관광지 로드 (0개 관광지 로드)
    attractionInfoList.value = [];
    attractionInfoList.value.push(defaultMapLocation);
};

const onChangeItineraryCheckbox = (attractionInfo) => {
    if (attractionInfo.isSelected) {
        //객체 복사 필요할 듯!
        selectedItineraries.value.push(JSON.parse(JSON.stringify(attractionInfo)));
    } else {
        const index = selectedItineraries.value.findIndex(
            (item) => item.contentType === attractionInfo.contentType
        );

        if (index !== -1) {
            selectedItineraries.value.splice(index, 1);
        }
    }

    // console.log('반응형 변수 변경');
    // console.log(selectedItineraries.value);
};

//삭제 가능
// watch(
//     () => selectedItineraries.value,
//     (value) => {
//         console.log('value');
//         console.log(value);
//     }
// );
</script>

<template>
    <!-- <div class="container text-center mt-3"> -->
    <div class="text-center" style='width: 90%; margin: auto;'>
    <!-- <div> -->
        <div class="alert alert-success" role="alert">여행지 정보</div>
        <div class='content'>
            <div class='left-area'>
                <div class="row mb-2">
                    <div class="col d-flex flex-row-reverse">
                        <VSelect :selectOption="sidoList" @onKeySelect="onChangeSido" />
                    </div>
                    <div class="col">
                        <VSelect :selectOption="gugunList" @onKeySelect="onChangeGugun" />
                    </div>
                </div>
                <div class="col d-flex flex-row-reverse">
                </div>

                <!-- 관광지 유형 옵션 (checkbox) -->
                <!-- 12:관광지, 14:문화시설, 15:축제공연행사, 25:여행코스, 28:레포츠, 32:숙박, 38:쇼핑, 39:음식점 -->
                <div id='attraction-options-div' >
                    <template v-for='item in attractionType'>
                        <input type='checkbox' :value='item.key' :id='item.value' class='attraction-options' @change='onChangeCheckbox' v-model='mapSelectedAttractionType'>
                        <label :for='item.value' class='attraction-options'>{{ item.value }}</label>
                    </template>

                    <!-- 전체 선택, 전채 선택 해제 버튼 -->
                    <!-- <div class='checkbox-buttons'> -->
                        <button class="btn btn-outline-success" type="button" id='deselect-all' @click='selectAllAttractionType'>전체 선택</button>
                        <button class="btn btn-outline-primary" type="button" id='deselect-all' @click='deselectAllAttractionType'>전체 선택 해제</button>
                    <!-- </div> -->
                </div>

                <div>
                    <AttractionKakaoMap :attractionInfoList='attractionInfoList' :selectAttraction='selectAttraction' />
                    <table class="table table-hover">
                        <thead>
                            <tr class="text-center">
                                <th scope="col">이미지</th>
                                <th scope="col">관광지 유형</th>
                                <th scope="col">관광지 명</th>
                                <th scope="col">시/도</th>
                                <th scope="col">주소</th>
                                <th scope="col">일정에 추가</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr class="text-center" v-for="attractionInfo in attractionInfoList" :key="attractionInfo.contentType" @click="viewAttractionInfoList(attractionInfo)">
                            <!-- <tr class="text-center" v-for="attractionInfo in attractionInfoList" :key="attractionInfo.contentType"> -->
                                <th><img :src='attractionInfo.firstImage' style='height: 50px;'></th>
                                <td>{{ attractionTypeMap.get(attractionInfo.contentTypeId) }}</td>
                                <td>{{ attractionInfo.title }}</td>
                                <td>{{ sidoTypeMap.get(attractionInfo.sidoCode) }}</td>
                                <td>{{ attractionInfo.addr1 }}</td>
                                <!-- 아래는 수정 중 (완료 X) -->
                                <!-- <td><input type='checkbox' :value='attractionInfo.contentId' :id='"selected_" + attractionInfo.contentId' @click='onChangeItineraryCheckbox(attractionInfo, $event)' class='attraction-options'></td> -->
                                <!-- <td><input type='checkbox' :value='attractionInfo.contentId' :id='"selected_" + attractionInfo.contentId' @click='onChangeItineraryCheckbox(attractionInfo, $event)' v-model='attractionInfo.isSelected' class='attraction-options'></td> -->
                                <td><input type='checkbox' :value='attractionInfo.contentId' :id='"selected_" + attractionInfo.contentId' @change='onChangeItineraryCheckbox(attractionInfo)' v-model='attractionInfo.isSelected' class='attraction-options'></td>

                                <!-- <td><input type='checkbox' :value='attractionInfo.contentId' :id='"selected_" + attractionInfo.contentId' @click='onChangeItineraryCheckbox(attractionInfo)' v-model='selectedItineraries' class='attraction-options'></td> -->
                                <!-- <td><input type='checkbox' :value='attractionInfo.contentId' :id='"selected_" + attractionInfo.contentId' v-model='selectedItineraries' @change='onChangeItineraryCheckbox()' class='attraction-options'></td> -->
                                <!-- <td><input type='checkbox' :value='attractionInfo.contentId' :id='"selected_" + attractionInfo.contentId' v-model='selectedItineraries' class='attraction-options'></td> -->
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class='right-area'>
                <TheItineraryView :selectedItineraries='selectedItineraries'></TheItineraryView>
            </div>
        </div>
    </div>
</template>

<style>
label {
    display: flex;
    flex-basis: content;
    vertical-align: middle;
}

mark.purple {
    background: linear-gradient(to top, #c354ff 20%, transparent 30%);
}

#attraction-options-div {
    display: flex;
    flex-wrap: wrap;
    vertical-align: middle;
    justify-content: center;
    align-items: center;
    margin-bottom: 10px;
    margin-right: 10%;
}

.attraction-options {
    zoom: 1.2;
    padding-right: 3%;
}

.checkbox-buttons {
    display: flex;
    margin-top: 1%;
    margin-bottom: 1%;
}

.content {
    display: flex;
    /* flex-wrap: wrap; */
}

.left-area {
    flex: 3;
}

.right-area {
    flex: 1;
    padding-left: 5%;
    height: 1000px;
}

button {
    margin-right: 1%;
}
</style>
