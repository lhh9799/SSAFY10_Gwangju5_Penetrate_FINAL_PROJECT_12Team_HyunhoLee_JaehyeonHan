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
        <!-- <template v-for=''> -->
        <template>
          <div class='my-tab-style'>
            안녕
          </div>
        </template>

        <h1>hi</h1>
        <div>
            <tabs>
                <tab name="First tab">
                    First tab content
                </tab>
                <tab name="Second tab">
                    Second tab content
                </tab>
                <tab name="Third tab">
                    Third tab content
                </tab>
            </tabs>
        </div>

        <!-- <template v-for='day in days'>
          <div>
            <p>{{ day }}일차</p>
          </div>
        </template> -->

        <draggable :list="props.selectedItineraries" item-key="name" class="list-group" ghost-class="ghost" @start="dragging = true" @end="dragging = false" >
        
        <template #item="{ element }">
          <li class="list-group-item">
            {{ element.title }}
          </li>
        </template>
        </draggable>
        
      </div>
    </div>
  </div>
</template>

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

.my-tab-style {
  border: 1px solid black;
  border-radius: 5px;
}

</style>

<script setup>
import { ref, defineProps, onMounted } from 'vue';
import draggable from "vuedraggable";
import {Tabs, Tab} from 'vue3-tabs-component';
import { useMemberStore } from "@/stores/member";
import { registerPlan, getPlan } from "@/api/user";

const props = defineProps({ selectedItineraries: Array });
const { getUserInfo } = useMemberStore();
const memberStoreData = JSON.parse(localStorage.getItem("memberStore"));
const day = ref(1);
const days = ref(3);
const userId = memberStoreData.userInfo.userId;
var userItinerary = ref([]);
// var userItinerary = ref({});
var userTravelDays = ref(1);

const save = () => {
  console.log('save 버튼 클릭됨');
}

onMounted(() => {
  console.log('userId');
  console.log(userId);

  getPlan(
    userId,
    ({data}) => {
      console.log('getPlan - data');
      console.log(data);
      userItinerary.value.maxDays = 1;
      data.forEach(element => {
        console.log('element');
        console.log(element);
        console.log(element.day);

        userTravelDays.value = Math.max(userTravelDays.value, element.day);
      });

      userItinerary.value = data;
      console.log('userTravelDays.value');
      console.log(userTravelDays.value);
    },
    (error) => {
      console.log('getPlan - error');
    },
  );
});

</script>
