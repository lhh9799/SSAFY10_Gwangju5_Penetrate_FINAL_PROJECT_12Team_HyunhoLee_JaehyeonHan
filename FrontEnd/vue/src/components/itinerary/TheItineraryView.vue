<template>
  <div class="sidebar">
    <div class="col-6">
      <div class="alert alert-info" role="alert" style="flex: 1; height: 1000px;">
        <div class="head">
          <!-- <strong>여행 일정</strong> -->
          <button type="button" id="save-button" class="btn btn-outline-primary" @click="save">저장하기</button>
          <button type="button" id="dummy-button" class="btn btn-outline-danger" @click="console.log(props.selectedItineraries)">더미 버튼</button>
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
// import { ref, watch, defineProps, onMounted } from 'vue';
import { ref, toRefs, watch, watchEffect, defineProps, onMounted } from 'vue';
import draggable from "vuedraggable";
import {Tabs, Tab} from 'vue3-tabs-component';
import { registPlan, getPlan } from "@/api/user";
import { httpStatusCode } from "@/util/http-status";

const props = defineProps({ selectedItineraries: Array });
const memberStoreData = JSON.parse(localStorage.getItem("memberStore"));
const userId = memberStoreData.userInfo.userId;
var userItinerary = ref([]);
var displayUserItinerary = ref([]); //클릭한 버튼에 맞는 일자의 여행 계획 (예: 1일차, 2일차 등)
// var userItinerary = ref({});
var userTravelDays = ref(1);        //로그인한 사용자의 최대 여행 일수
var targetDay = ref(1);             //사용자가 선택한 조회할 여행 일자 (예: 1일차, 2일차 등)

const onTravelDayTabClick = ($event) => {
  // console.log($event);
  // console.log($event.currentTarget);
  // console.log($event.currentTarget.value);
  // targetDay = $event.currentTarget.value;  //사용자가 선택한 여행 일 (예: 1일차, 2일차 등)
  targetDay.value = $event.currentTarget.value;  //사용자가 선택한 여행 일 (예: 1일차, 2일차 등)
  // console.log('pressedButtonValue: ' + pressedButtonValue);
  
  loadData();
};

const loadData = () => {
  //기존 여행 계획 초기화
  displayUserItinerary.value = [];

  // console.log('targetDay');
  // console.log(targetDay);

  //DB 여행 계획 중 선택한 일자의 여행 계획을 배열에 추가
  userItinerary.value.forEach((item) => {
    if(item.day == targetDay.value) {
      displayUserItinerary.value.push(item);
    } else {
    }
  });

  //왼쪽 패널에서 선택한 여행지 추가
  props.selectedItineraries.forEach((item) => {
    item.userId = userId;     //여행 계획 DB에 저장하기 위해 ID 설정 (PK 제약)
    item.day = targetDay;     //여행 계획 DB에 저장하기 위해 일자 설정
    item.addr = item.addr1;   //여행 계획 DB 저장 위해 주소 저장
    displayUserItinerary.value.push(item);
  });

  // console.log('displayUserItinerary.value');
  // console.log(displayUserItinerary.value);
};

const save = () => {
  console.log('save 버튼 클릭됨');
  console.log('displayUserItinerary.value');
  console.log(displayUserItinerary.value);

  let order = 1;
  displayUserItinerary.value.forEach((item) => {
    item.sequence = order;
    order += 1;
  });

  registPlan(
    displayUserItinerary.value,
      (response) => {
        console.log('response');
        console.log(response);
        console.log('response.status');
        console.log(response.status);

        if(response.status == httpStatusCode.OK) {
          alert('정상 저장되었습니다.');
        }
      },
      (error) => console.error(error)
  );
}

//DB에 저장된 여행 계획과 메뉴에서 선택한 여행 계획을 합치는 함수
const unifyItineraries = () => {
  // console.log('호출은 함');
  // console.log(props.selectedItineraries);

  //초기화
  displayUserItinerary.value = [];

  //왼쪽 패널에서 선택한 여행지 추가
  props.selectedItineraries.forEach((item) => {
    if(item.day == targetDay) {
      displayUserItinerary.value.push(item);
    }
  });

  //DB의 여행지 추가
  userItinerary.value.forEach((item) => {
    displayUserItinerary.value.push(item);
  });
};

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

      console.log('userItinerary.value');
      console.log(userItinerary.value);

      loadData();
    },
    (error) => {
      console.log('getPlan - error');
    },
  );

  //1일차 여행 계획 정보 로드
  // console.log('호출 전');
  // loadData();
  // console.log('호출 후');
  // setTimeout(() => {
  //   console.log('타이머 호출');

  //   loadData(1);
  //   console.log('displayUserItinerary.value');
  //   console.log(displayUserItinerary.value);
  // }, 5000);

  // //DB에 저장된 여행 계획과 메뉴에서 선택한 여행 계획을 합치는 함수
  // unifyItineraries();
});

watchEffect(() => {
  // 이제 selectedItineraries를 사용할 수 있습니다.
  // console.log(props.selectedItineraries);
  // unifyItineraries();

  loadData();

  // 추가로 다른 로직을 여기에 작성할 수 있습니다.

}, { flush: 'post' });


// watch(
  // () => toRefs(props.selectedItineraries),
  // () => props.selectedItineraries,
//   // (newValue) => {
//   //   console.log('새로 추가된 듯');
//   //   console.log(newValue);
//   // },
//   // (oldValue) => {
//   //   console.log('새로 삭제된 듯');
//   //   console.log(oldValue);
//   // },
//   (newValue, oldValue) => {
//     console.log('newValue');
//     console.log(newValue);
//     console.log('oldValue');
//     console.log(oldValue);
//     unifyItineraries();
//   },
  // {immediate: true},
  // console.log('props.selectedItineraries'),
  // console.log(props.selectedItineraries),
// );
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