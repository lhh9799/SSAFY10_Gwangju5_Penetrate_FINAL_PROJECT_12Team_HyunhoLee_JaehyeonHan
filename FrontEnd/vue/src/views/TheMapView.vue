<script setup>
import { ref, onMounted } from "vue";
import { listAttraction, getSidoFromSidoTable, getgugunDtoFromSidoTable } from "@/api/map";

import AttractionKakaoMap from "@/components/common/AttractionKakaoMap.vue";
import VSelect from "@/components/common/VSelect.vue";

import { attractionType } from "@/util/attraction-type";

// const serviceKey = import.meta.env.VITE_OPEN_API_SERVICE_KEY;
const { VITE_OPEN_API_SERVICE_KEY } = import.meta.env;

const sidoList = ref([]);
const gugunList = ref([{ text: "구군선택", value: "" }]);
var attractionInfoList = ref([]);
const selectAttraction = ref({});
const attractionTypeMap = new Map();
const selectedAttractionType = ref([]); //checkbox의 선택된 항목들을 저장하는 배열 (예: { key: 12, value: '관광지', })

const param = ref({
    // serviceKey: VITE_OPEN_API_SERVICE_KEY,
    pageNo: 1,
    numOfRows: 20,
    sidoCode: 0,
    gugunCode: 0,
});

onMounted(() => {
    // getChargingStations();
    getSidoList();

    getAttractionTypeMap();
});

//attractionTypeMap라는 Map 자료구조에 관광지 유형 쌍 대입 (예: { key: 12, value: '관광지', })
const getAttractionTypeMap = () => {
    attractionType.forEach((item) => {
        attractionTypeMap.set(item.key, item.value);
    });
};

const getSidoList = () => {
    getSidoFromSidoTable(
        ({ data }) => {
            console.log('getSidoList data');
            console.log(data);

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
    console.log('onChangeSido:' + val);
    param.value.sidoCode = val;

    getgugunDtoFromSidoTable(
        { sido: val },
        ({ data }) => {
            console.log('onChangeSido data');
            console.log(data);

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
    console.log('onChangeGugun:' + val);
    param.value.gugunCode = val;

    param.value.zscode = val;
    getAttractionInfoList();
};

const getAttractionInfoList = () => {
    console.log('param.value');
    console.log(param.value);

    listAttraction(
        param.value,
        ({ data }) => {
            console.log('getAttractionInfoList data');
            attractionInfoList.value = data;
            console.log(attractionInfoList.value);
        },
        (err) => {
            console.log(err);
        }
    );
};

const viewAttractionInfoList = (attraction) => {
    selectAttraction.value = attraction;
};

const onChangeCheckbox = () => {
    var filteredAttractionInfoList = [];

    if (attractionInfoList.value.length == 0) {
        //여행지 정보가 없으므로 리턴 (attractionInfoList.value.length == 0)
        return;
    }

    attractionInfoList.value.forEach((item) => {
        selectedAttractionType.value.forEach((checked) => {

            if (item.contentTypeId == checked) {
                filteredAttractionInfoList.push(item);
            }
        });
    });

    attractionInfoList.value = filteredAttractionInfoList;
};
</script>

<template>
    <div class="container text-center mt-3">
        <div class="alert alert-success" role="alert">여행지 정보</div>
        <div class="row mb-2">
            <div class="col d-flex flex-row-reverse">
                <VSelect :selectOption="sidoList" @onKeySelect="onChangeSido" />
            </div>
            <div class="col">
                <VSelect :selectOption="gugunList" @onKeySelect="onChangeGugun" />
            </div>
        </div>

        <!-- 관광지 유형 옵션 (checkbox) -->
        <!-- 12:관광지, 14:문화시설, 15:축제공연행사, 25:여행코스, 28:레포츠, 32:숙박, 38:쇼핑, 39:음식점 -->
        <div id='attraction-options-div' >
            <template v-for='item in attractionType'>
                <input type='checkbox' :value='item.key' :id='item.value' class='attraction-options' @change='onChangeCheckbox' v-model='selectedAttractionType'>
                <label :for='item.value' class='attraction-options'>{{ item.value }}</label>
            </template>
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
                        <th scope="col">구/군</th>
                        <th scope="col">주소</th>
                    </tr>
                </thead>
                <tbody>
                    <tr class="text-center" v-for="attractionInfo in attractionInfoList" :key="attractionInfo.contentId" @click="viewAttractionInfoList(attractionInfo)">
                        <th><img :src='attractionInfo.firstImage' style='height: 50px;'></th>
                        <td>{{ attractionInfo.contentTypeId }}</td>
                        <td>{{ attractionInfo.title }}</td>
                        <td>{{ attractionInfo.sidoCode }}</td>
                        <td>{{ attractionInfo.gugunCode }}</td>
                        <td>{{ attractionInfo.addr1 }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<style>
mark.purple {
    background: linear-gradient(to top, #c354ff 20%, transparent 30%);
}

.attraction-options-div {
    display: flex;
    flex-wrap: wrap;
}

.attraction-options {
    /* width: 500px; */
    zoom: 1.5;
    padding-right: 3%;
}
</style>
