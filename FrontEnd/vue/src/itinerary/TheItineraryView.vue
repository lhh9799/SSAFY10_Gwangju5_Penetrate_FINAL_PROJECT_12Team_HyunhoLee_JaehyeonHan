<template>
  <div class="sidebar">
    <div class="col-6">
      <div class="alert alert-info" role="alert" style="flex: 1; height: 1000px;">
        <div class="head">
          <strong>여행 일정</strong>
          <button type="button" id="save-button" class="btn btn-outline-primary" @click="save">저장하기</button>
        </div>
        <ul>
          <li v-for="location in props.selectedItineraries" :key="location.contentId">
            {{ location.title }}
          </li>
          <!-- <myDraggableNext></myDraggableNext> -->
          <aaa></aaa>
        </ul>
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
}

#save-button{
  margin-top: auto;
  margin-left: 35%;
  vertical-align: middle;
}

</style>

<script setup>
import { defineProps } from 'vue';
// import { VueDraggableNext } from 'vue-draggable-next'
// import defineComponent from '@/components/itinerary/TheVueDraggableNextView.vue'
import aaa from '@/components/itinerary/TheVueDraggableNextView.vue'

const props = defineProps({ selectedItineraries: Array });

const save = () => {
  console.log('save 버튼 클릭됨');
}

aaa.data = ['hi', 'hello', 'bonjour'];

</script>
