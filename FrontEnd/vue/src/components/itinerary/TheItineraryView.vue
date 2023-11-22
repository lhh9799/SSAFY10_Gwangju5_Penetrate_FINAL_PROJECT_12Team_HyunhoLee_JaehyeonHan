<template>
  <div class="sidebar">
    <div class="col-6">
      <div class="alert alert-info" role="alert" style="flex: 1; height: 1000px;">
        <div class="head">
          <!-- <strong>여행 일정</strong> -->
          <button type="button" id="save-button" class="btn btn-outline-primary" @click="save">저장하기</button>
        </div>
        <p>여행 일정을 계획해보세요</p>

        <h1>커스텀 탭</h1>
        <div class='day-tabs-div'>
            <button class='my-tab-style' :value='index' v-for="index in userTravelDays" :key="index" @click='onTravelDayTabClick'>
              {{ index }}
            </button>
        </div>

        <div class='user-itinerary-content-div'>
          <div v-if='displayUserItinerary.length > 0'>
            <!-- <draggable :list="props.selectedItineraries" item-key="name" class="list-group" ghost-class="ghost" @start="dragging = true" @end="dragging = false" > -->
            <draggable :list="displayUserItinerary" item-key="name" class="list-group" ghost-class="ghost" @start="dragging = true" @end="dragging = false" >
        
          <template #item="{ element }">
            <li class="list-group-item">
              {{ element.place }}
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
import { ref, defineProps, onMounted } from 'vue';
import draggable from "vuedraggable";
import {Tabs, Tab} from 'vue3-tabs-component';
import { registerPlan, getPlan } from "@/api/user";

const props = defineProps({ selectedItineraries: Array });
const memberStoreData = JSON.parse(localStorage.getItem("memberStore"));
const userId = memberStoreData.userInfo.userId;
var userItinerary = ref([]);
var displayUserItinerary = ref([]); //클릭한 버튼에 맞는 일자의 여행 계획 (예: 1일차, 2일차 등)
// var userItinerary = ref({});
var userTravelDays = ref(1);        //로그인한 사용자의 최대 여행 일수

const onTravelDayTabClick = ($event) => {
  // console.log($event);
  // console.log($event.currentTarget);
  console.log($event.currentTarget.value);
  let pressedButtonValue = $event.currentTarget.value;  //사용자가 선택한 여행 일 (예: 1일차, 2일차 등)
  console.log('pressedButtonValue: ' + pressedButtonValue);

  //기존 여행 계획 초기화
  displayUserItinerary.value = [];

  //클릭한 버튼에 맞는 일자의 여행 계획을 배열에 추가
  userItinerary.value.forEach((item) => {
    if(item.day == pressedButtonValue) {
      displayUserItinerary.value.push(item);
    }
  });

  console.log('displayUserItinerary.value');
  console.log(displayUserItinerary.value);
};

const save = () => {
  console.log('save 버튼 클릭됨');
}

//DB에 저장된 여행 계획과 메뉴에서 선택한 여행 계획을 합치는 함수
const unifyItineraries = () => {
  props.selectedItineraries.forEach((item) => {
    displayUserItinerary.value.push(item);
  });
};

onMounted(() => {
  console.log('userId');
  console.log(userId);

  getPlan(
    userId,
    ({data}) => {
      userItinerary.value.maxDays = 1;
      data.forEach(element => {
        //로그인한 사용자의 최대 여행 일수 계산
        userTravelDays.value = Math.max(userTravelDays.value, element.day);
      });

      userItinerary.value = data;
    },
    (error) => {
      console.log('getPlan - error');
    },
  );

  //DB에 저장된 여행 계획과 메뉴에서 선택한 여행 계획을 합치는 함수
  unifyItineraries();
});
</script>

<style scoped>
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
  /* margin-left: 35%; */
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
  /* max-width: 100%; */
  display: block;
}

.user-itinerary-content-div {
  border: 3px solid aquamarine;
}
</style>