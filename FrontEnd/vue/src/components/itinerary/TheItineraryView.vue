<template>
  <div class="sidebar">
    <div class="col-6">
      <div class="alert alert-info" role="alert" style="flex: 1; height: 1000px;">
        <div class="head">
          <button type="button" id="save-button" class="btn btn-outline-primary" @click="save">저장하기</button>
        </div>
        <h4>여행 일정을 계획해보세요</h4>
        <h6>드래그로 순서를 변경할 수 있습니다.</h6> <br />
        <div class='day-tabs-div'>
            <button class='my-tab-style' :value='index' v-for="index in userTravelDays + 1" :key="index" @click='onTravelDayTabClick'>
              {{ index }}일차
            </button>
        </div>

        <div class='user-itinerary-content-div'>
          <div v-if='displayUserItinerary.length > 0'>
            <draggable :list="displayUserItinerary" item-key="name" class="list-group" ghost-class="ghost" @start="dragging = true" @end="dragging = false" >
        
          <template #item="{ element }">
            <li class="list-group-item">
              {{ element.title }}
            </li>
          </template>
          </draggable>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watchEffect, defineProps, onMounted } from 'vue';
import draggable from "vuedraggable";
// import {Tabs, Tab} from 'vue3-tabs-component';
import { registPlan, getPlan } from "@/api/user";
import { httpStatusCode } from "@/util/http-status";

const emit = defineEmits(["clearProps"]);
const props = defineProps({ selectedItineraries: Array });
const memberStoreData = JSON.parse(localStorage.getItem("memberStore"));
const userId = memberStoreData.userInfo.userId;
var userItinerary = ref([]);
var displayUserItinerary = ref([]);               //클릭한 버튼에 맞는 일자의 여행 계획 (예: 1일차, 2일차 등)
var userTravelDays = ref(1);                      //로그인한 사용자의 최대 여행 일수
var targetDay = ref(1);                           //사용자가 선택한 조회할 여행 일자 (예: 1일차, 2일차 등)

const onTravelDayTabClick = ($event) => {
  targetDay.value = $event.currentTarget.value;   //사용자가 선택한 여행 일 (예: 1일차, 2일차 등)
  
  loadData();
};

const loadData = () => {
  //기존 여행 계획 초기화
  displayUserItinerary.value = [];

  //DB 여행 계획 중 선택한 일자의 여행 계획을 배열에 추가
  userItinerary.value.forEach((item) => {
    if(item.day == targetDay.value) {
      displayUserItinerary.value.push(item);
    }
  });

  //왼쪽 패널에서 선택한 여행지 추가
  props.selectedItineraries.forEach((item) => {
    item.userId = userId;     //여행 계획 DB에 저장하기 위해 ID 설정 (PK 제약)
    item.day = targetDay;     //여행 계획 DB에 저장하기 위해 일자 설정
    item.addr = item.addr1;   //여행 계획 DB 저장 위해 주소 저장
    displayUserItinerary.value.push(item);

    //로그인한 사용자의 최대 여행 일수 계산
    userTravelDays.value = Math.max(userTravelDays.value, item.day);

    emit("clearProps"); //체크박스 선택된 값 삭제
  });
};

const save = () => {
  let order = 1;
  displayUserItinerary.value.forEach((item) => {
    item.sequence = order;
    order += 1;
  });

  registPlan(
    displayUserItinerary.value,
      (response) => {
        if(response.status == httpStatusCode.OK) {
          alert('정상 저장되었습니다.');
        }
      },
      (error) => console.error(error)
  );
}

onMounted(() => {
  getPlan(
    userId,
    ({data}) => {
      userItinerary.value.maxDays = 1;

      data.forEach(element => {
        //로그인한 사용자의 최대 여행 일수 계산
        userTravelDays.value = Math.max(userTravelDays.value, element.day);
      });

      userItinerary.value = data;

      loadData();
    },
    (error) => {
      console.log('getPlan - error');
    },
  );
});

watchEffect(() => {
  loadData();
}, { flush: 'post' }
);
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500&display=swap');

* {
    font-family: 'Noto Sans KR', sans-serif;
}

.sidebar {
  display: flex;
  flex-direction: column;
  width: 200%;
}

.col-6 {
  flex: 1;
}

.head {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  align-items: center;
  vertical-align: middle;
  align-content: center;
  justify-content: space-evenly;
}

#save-button{
  margin-top: auto;
  margin-bottom: 5%;
  vertical-align: middle;
  display: flex;
  align-self: flex-end;
}

.day-tabs-div {
  display: flex;
  flex-direction: row;
}

.my-tab-style {
  border: 1px solid black;
  border-radius: 5px;
  width: 100%;
  display: block;
  margin-bottom: 10px;
}

.user-itinerary-content-div {
  border: 3px solid black;
}
</style>