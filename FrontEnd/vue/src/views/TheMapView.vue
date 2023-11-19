<script setup>
import { ref, onMounted } from "vue";
import { listAttraction, getSidoFromSidoTable, getgugunDtoFromSidoTable } from "@/api/map";

import AttractionKakaoMap from "@/components/common/AttractionKakaoMap.vue";
import VSelect from "@/components/common/VSelect.vue";

// const serviceKey = import.meta.env.VITE_OPEN_API_SERVICE_KEY;
const { VITE_OPEN_API_SERVICE_KEY } = import.meta.env;

const sidoList = ref([]);
const gugunList = ref([{ text: "구군선택", value: "" }]);
const attractionInfoList = ref([]);
const selectAttraction = ref({});

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
});

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
            console.log(data);
            // attractionInfoList.value = data.items[0].item;
            attractionInfoList.value = data;
            // data.forEach((singleElement) => {
            //     singleElement.contentId
            // });
        },
        (err) => {
            console.log(err);
        }
    );
};

const viewAttractionInfoList = (attraction) => {
    selectAttraction.value = attraction;
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
</template>

<style>
mark.purple {
    background: linear-gradient(to top, #c354ff 20%, transparent 30%);
}
</style>
