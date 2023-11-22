<template>
  <div>
    <b-tabs content-class="mt-3">
      <b-tab title="First" active><p>I'm the first tab</p></b-tab>
      <b-tab title="Second"><p>I'm the second tab</p></b-tab>
      <b-tab title="Disabled" disabled><p>I'm a disabled tab!</p></b-tab>
    </b-tabs>
  </div>


  <div class="sidebar">
    <div class="col-6">
      <div class="alert alert-info" role="alert" style="flex: 1; height: 1000px;">
        <div class="head">
          <!-- <strong>여행 일정</strong> -->
          <button type="button" id="save-button" class="btn btn-outline-primary" @click="save">저장하기</button>
        </div>
        <p>여행 일정을 계획해보세요</p>
        <template v-for='day in days'>
          <div>
            <p>{{ day }}일차</p>
          </div>
        </template>

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

</style>

<script setup>
import { ref, defineProps } from 'vue';
import draggable from "vuedraggable";
// import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'
// Import Bootstrap and BootstrapVue CSS files (order is important)
// import 'bootstrap/dist/css/bootstrap.css'
// import 'bootstrap-vue/dist/bootstrap-vue.css'

// Make BootstrapVue available throughout your project
// Vue.use(BootstrapVue)
// Optionally install the BootstrapVue icon components plugin
// Vue.use(IconsPlugin)

const props = defineProps({ selectedItineraries: Array });
const day = ref(1);
const days = ref(3);

const tab = ref(['1', '2', '3']);

const save = () => {
  console.log('save 버튼 클릭됨');
}

</script>
